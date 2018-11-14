/*
 * Copyright 2018 Erik Amzallag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dajlab.jtoypad.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of Toypad.
 * 
 * @author Erik Amzallag
 *
 */
public class ToyPadImpl implements ToyPad, UsbPipeListener {

	/**
	 * Generic prefix.
	 */
	private final static byte PREFIX_GENERIC = 0x55;
	/**
	 * Prefix for event.
	 */
	private final static byte PREFIX_EVENT = 0x56;

	/**
	 * Init command.
	 */
	private final static byte[] TOYPAD_INIT = new byte[] { PREFIX_GENERIC, 0x0f, CommandEnum.INIT.getCommandId(), 0x01,
			0x28, 0x63, 0x29, 0x20, 0x4c, 0x45, 0x47, 0x4f, 0x20, 0x32, 0x30, 0x31, 0x34, (byte) 0xf7, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Endpoint for ouput.
	 */
	private final static byte ENDPOINT_OUT = 0x01;

	/**
	 * Endpoint for input.
	 */
	private final static byte ENDPOINT_IN = (byte) 0x81;

	/**
	 * Usb device.
	 */
	private UsbDevice usbDevice;
	/**
	 * Usb interface.
	 */
	private UsbInterface iface;

	/**
	 * Logger.
	 */
	private Logger logger = LogManager.getLogger(ToyPadImpl.class);

	/**
	 * Message counter. Start at 2 (1 is for the init command).
	 */
	private int messageCounter = 2;

	/**
	 * Tag listeners.
	 */
	private EventListenerList tagListeners;

	/**
	 * Map events.
	 */
	private Map<Integer, TagEvent> mapEvents = new HashMap<>();

	/**
	 * USB pipe in.
	 */
	private UsbPipe pipeIn;

	/**
	 * Cache for tags.<br>
	 * Key: UID<br>
	 * Value: Tag
	 */
	private Map<String, Tag> tagCache;

	/**
	 * Constructor.
	 * 
	 * @param usbDevice
	 *            usb device
	 * @throws ToyPadException
	 *             toypad exception
	 */
	public ToyPadImpl(final UsbDevice usbDevice) throws ToyPadException {

		this.usbDevice = usbDevice;
		tagCache = new HashMap<>();
		UsbConfiguration configuration = usbDevice.getActiveUsbConfiguration();
		iface = (UsbInterface) configuration.getUsbInterfaces().get(0);
		open();
		sendMessage(TOYPAD_INIT);
		// Got this response, seems to be useless :
		// 55 19 01 00 2F 02 01 02 02 04 02 F5 00 19 87 55 80 4D D7 AE AE
		// 1C E5 11 FF 20 1C E2 00 00 00 00

		// The SEED command seems to be useless to only read tags.
		// sendCommand(CommandEnum.SEED,
		// new byte[] { (byte) 0xaa, 0x6F, (byte) 0xC8, (byte) 0xCD, 0x21, 0x1E,
		// (byte)
		// 0xF8, (byte) 0xCE });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws ToyPadException {

		try {
			iface.claim(new UsbInterfacePolicy() {
				public boolean forceClaim(UsbInterface usbInterface) {
					return true;
				}
			});
			UsbEndpoint endpoint = (UsbEndpoint) iface.getUsbEndpoint(ENDPOINT_IN);
			pipeIn = endpoint.getUsbPipe();
			pipeIn.open();
			pipeIn.addUsbPipeListener(this);
			byte[] data = new byte[32];
			pipeIn.asyncSubmit(data);

		} catch (Exception ex) {
			try {
				iface.release();
			} catch (Exception e) {
			}
			logger.error("Error while claiming usb [{}]", ex.getMessage());
			throw new ToyPadException(JToypadConstants.ERR_OPEN_USB);
		} finally {

		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws ToyPadException {

		try {
			iface.release();
		} catch (Exception e) {
			logger.error("Error while release usb [{}]", e.getMessage());
			throw new ToyPadException(JToypadConstants.ERR_CLOSE_USB);
		}

	}

	/**
	 * Complete a command if payload is too short.
	 * 
	 * @param command
	 *            command
	 * @param payload
	 *            payload
	 * @return a complete 32 bits command
	 */
	private byte[] completeCommand(CommandEnum command, byte[] payload) {

		byte[] fullCommand = new byte[32];
		fullCommand[0] = PREFIX_GENERIC;
		fullCommand[1] = command.getPayloadSize();
		fullCommand[2] = command.getCommandId();
		fullCommand[3] = (byte) messageCounter;
		int i = 0;
		while (i < payload.length) {
			fullCommand[i + 4] = payload[i];
			i++;
		}
		fullCommand[i + 4] = calculateChecksum(fullCommand);
		messageCounter++;
		return fullCommand;
	}

	/**
	 * Calculate checksum for a command.
	 * 
	 * @param command
	 *            command
	 * @return the checksum
	 */
	private byte calculateChecksum(byte[] command) {

		byte checksum = 0;
		for (byte b : command) {
			checksum += b;
			checksum = (byte) (checksum % 256);
		}

		return checksum;
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            command
	 * @param payload
	 *            payload
	 * @return the message counter
	 * @throws ToyPadException
	 *             toypad exception
	 */
	private int sendCommand(CommandEnum command, byte[] payload) throws ToyPadException {

		logger.trace("Sending command [{}]", command.name());
		if (payload.length != command.getPayloadSize() - 2) {
			logger.error("Incorrect payload size : expected=[{}], actual=[{}]", command.getPayloadSize(),
					payload.length);
		}
		byte[] completedMessage = completeCommand(command, payload);
		sendMessage(completedMessage);

		// returning the message counter
		return completedMessage[3];
	}

	/**
	 * Send message. <br>
	 * This method is synchronized.
	 * 
	 * @param command
	 *            the full command
	 * @throws ToyPadException
	 *             toypad exception
	 */
	private synchronized void sendMessage(byte[] command) throws ToyPadException {

		byte[] message;
		if (command.length != 32) {
			logger.error("Incorrect lenght. Expected 32 bits, got [{}]", command.length);
			throw new ToyPadException(JToypadConstants.ERR_MESSAGE_LENGTH);
		} else {
			message = command;
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Sending {}", TagDecoder.byteToHex(message));
		}
		try {
			UsbEndpoint endpoint = (UsbEndpoint) iface.getUsbEndpoint(ENDPOINT_OUT);
			UsbPipe pipe = endpoint.getUsbPipe();
			pipe.open();
			pipe.syncSubmit(message);
			pipe.close();
		} catch (Exception ex) {
			logger.error("Error while sending message to toypad.");
			throw new ToyPadException(JToypadConstants.ERR_USB_SEND);
		} finally {

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnOffPads() throws ToyPadException {

		switchPad(PadEnum.ALL, Color.BLACK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchPad(final PadEnum pad, final Color color) throws ToyPadException {

		if (color != null) {
			sendCommand(CommandEnum.SWITCH_PAD, new byte[] { pad.getValue(), (byte) color.getRed(),
					(byte) color.getGreen(), (byte) color.getBlue() });
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchPads(final Color colorPadLeft, final Color colorPadCenter, final Color colorPadRight)
			throws ToyPadException {

		byte[] payload = new byte[12];
		if (colorPadCenter != null) {
			payload[0] = 1;
			payload[1] = (byte) colorPadCenter.getRed();
			payload[2] = (byte) colorPadCenter.getGreen();
			payload[3] = (byte) colorPadCenter.getBlue();
		}
		if (colorPadLeft != null) {
			payload[4] = 1;
			payload[5] = (byte) colorPadLeft.getRed();
			payload[6] = (byte) colorPadLeft.getGreen();
			payload[7] = (byte) colorPadLeft.getBlue();
		}
		if (colorPadRight != null) {
			payload[8] = 1;
			payload[9] = (byte) colorPadRight.getRed();
			payload[10] = (byte) colorPadRight.getGreen();
			payload[11] = (byte) colorPadRight.getBlue();
		}
		sendCommand(CommandEnum.SWITCH_PADS, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flashPad(final PadEnum pad, final FlashColor flashColor) throws ToyPadException {

		switchPad(pad, flashColor.getColor2());
		sendCommand(CommandEnum.FLASH_PAD,
				new byte[] { pad.getValue(), (byte) flashColor.getTimeForColor1(), (byte) flashColor.getTimeForColor2(),
						(byte) flashColor.getPulseCount(), (byte) flashColor.getColor1().getRed(),
						(byte) flashColor.getColor1().getGreen(), (byte) flashColor.getColor1().getBlue() });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flashPads(final FlashColor flashColorLeft, final FlashColor flashColorCenter,
			final FlashColor flashColorRight) throws ToyPadException {

		byte[] payload = new byte[21];

		Color color2Center = null;
		Color color2Left = null;
		Color color2Right = null;

		if (flashColorCenter != null) {
			payload[0] = 1;
			payload[1] = (byte) flashColorCenter.getTimeForColor1();
			payload[2] = (byte) flashColorCenter.getTimeForColor2();
			payload[3] = (byte) flashColorCenter.getPulseCount();
			payload[4] = (byte) flashColorCenter.getColor1().getRed();
			payload[5] = (byte) flashColorCenter.getColor1().getGreen();
			payload[6] = (byte) flashColorCenter.getColor1().getBlue();
			color2Center = flashColorCenter.getColor2();
		}
		if (flashColorLeft != null) {
			payload[7] = 1;
			payload[8] = (byte) flashColorLeft.getTimeForColor1();
			payload[9] = (byte) flashColorLeft.getTimeForColor2();
			payload[10] = (byte) flashColorLeft.getPulseCount();
			payload[11] = (byte) flashColorLeft.getColor1().getRed();
			payload[12] = (byte) flashColorLeft.getColor1().getGreen();
			payload[13] = (byte) flashColorLeft.getColor1().getBlue();
			color2Left = flashColorLeft.getColor2();
		}
		if (flashColorRight != null) {
			payload[14] = 1;
			payload[15] = (byte) flashColorRight.getTimeForColor1();
			payload[16] = (byte) flashColorRight.getTimeForColor2();
			payload[17] = (byte) flashColorRight.getPulseCount();
			payload[18] = (byte) flashColorRight.getColor1().getRed();
			payload[19] = (byte) flashColorRight.getColor1().getGreen();
			payload[20] = (byte) flashColorRight.getColor1().getBlue();
			color2Right = flashColorRight.getColor2();
		}
		switchPads(color2Left, color2Center, color2Right);
		sendCommand(CommandEnum.FLASH_PADS, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadePadRandom(final PadEnum pad, final int pulseTime, final int pulseCount) throws ToyPadException {

		sendCommand(CommandEnum.FADE_PAD_RANDOM, new byte[] { pad.getValue(), (byte) pulseTime, (byte) pulseCount });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadePad(final PadEnum pad, final FadeColor fadeColor) throws ToyPadException {

		switchPad(pad, fadeColor.getFromColor());
		sendCommand(CommandEnum.FADE_PAD,
				new byte[] { pad.getValue(), (byte) fadeColor.getPulseTime(), (byte) fadeColor.getPulseCount(),
						(byte) fadeColor.getToColor().getRed(), (byte) fadeColor.getToColor().getGreen(),
						(byte) fadeColor.getToColor().getBlue() });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadePad(final PadEnum pad, final Color fromColor, final Color toColor, final int time)
			throws ToyPadException {

		fadePad(pad, new FadeColor(fromColor, toColor, time, 1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadePads(final FadeColor fadeColorLeft, final FadeColor fadeColorCenter, final FadeColor fadeColorRight)
			throws ToyPadException {

		byte[] payload = new byte[18];
		Color fromColorCenter = null;
		Color fromColorLeft = null;
		Color fromColorRight = null;

		if (fadeColorCenter != null) {
			payload[0] = 1;
			payload[1] = (byte) fadeColorCenter.getPulseTime();
			payload[2] = (byte) fadeColorCenter.getPulseCount();
			payload[3] = (byte) fadeColorCenter.getToColor().getRed();
			payload[4] = (byte) fadeColorCenter.getToColor().getGreen();
			payload[5] = (byte) fadeColorCenter.getToColor().getBlue();
			fromColorCenter = fadeColorCenter.getFromColor();
		}
		if (fadeColorLeft != null) {
			payload[6] = 1;
			payload[7] = (byte) fadeColorLeft.getPulseTime();
			payload[8] = (byte) fadeColorLeft.getPulseCount();
			payload[9] = (byte) fadeColorLeft.getToColor().getRed();
			payload[10] = (byte) fadeColorLeft.getToColor().getGreen();
			payload[11] = (byte) fadeColorLeft.getToColor().getBlue();
			fromColorLeft = fadeColorLeft.getFromColor();
		}
		if (fadeColorRight != null) {
			payload[12] = 1;
			payload[13] = (byte) fadeColorRight.getPulseTime();
			payload[14] = (byte) fadeColorRight.getPulseCount();
			payload[15] = (byte) fadeColorRight.getToColor().getRed();
			payload[16] = (byte) fadeColorRight.getToColor().getGreen();
			payload[17] = (byte) fadeColorRight.getToColor().getBlue();
			fromColorRight = fadeColorRight.getFromColor();
		}
		switchPads(fromColorLeft, fromColorCenter, fromColorRight);
		sendCommand(CommandEnum.FADE_PADS, payload);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIdentifiant() {
		return usbDevice.toString();
	}

	/**
	 * Decode input data to tag
	 * 
	 * @param data
	 *            data
	 */
	private void decodeInput(final byte[] data) {

		if (logger.isTraceEnabled()) {
			logger.trace("Incoming {}", TagDecoder.byteToHex(data));
		}

		Tag tag = null;

		if (data[0] == PREFIX_EVENT) {
			// Check if it's an event message
			PadEnum pad = PadEnum.findPad(data[2]);
			ActionEnum action = ActionEnum.findAction(data[5]);
			if (pad != null && action != null) {
				TagEvent tagEvent = new TagEvent(this);
				tagEvent.setAction(action);
				tagEvent.setPad(pad);

				StringBuilder uid = new StringBuilder();
				for (int i = 6; i < 13; i++) {
					byte b = data[i];
					uid.append(String.format("%02X", b));
				}
				String sUid = uid.toString();
				tagEvent.getTag().setUid(sUid);
				logger.debug("Tag's UID = [{}]", tagEvent.getTag().getUid());

				byte idx = data[4];
				tagEvent.setIndex(idx);

				if (action == ActionEnum.ADDED) {
					logger.debug("Tag " + action.name().toLowerCase() + " to " + pad.name().toLowerCase() + " pad");
					if (tagCache.containsKey(sUid)) {
						tag = tagCache.get(sUid);
						tagEvent.setTag(tag);
						fireTagEvent(tagEvent);
					} else {
						try {
							int counter = 0;
							// Sending READ command
							byte[] readPl = new byte[] { idx, 0x24 };
							counter = sendCommand(CommandEnum.READ, readPl);
							mapEvents.put(new Integer(counter), tagEvent);
						} catch (ToyPadException e) {
							logger.warn("Fail to send READ command.");
						}
					}
				} else {
					logger.debug("Tag " + action.name().toLowerCase() + " from " + pad.name().toLowerCase() + " pad");
					fireTagEvent(tagEvent);
				}
			}
		} else if (data[0] == PREFIX_GENERIC) {
			// Awaited message
			if (data[1] == 0x12) {

				logger.debug("Response for READ command");
				int counter = data[2];

				byte isVehicle = data[13];
				if (isVehicle == 0x01) {
					// It's a vehicle
					byte[] idVehicle = new byte[2];
					idVehicle[0] = data[4];
					idVehicle[1] = data[5];

					tag = TagDecoder.decodeVehicle(idVehicle);
					TagEvent tagEvent = mapEvents.remove(new Integer(counter));
					if (tagEvent != null) {
						tag.setUid(tagEvent.getTag().getUid());
						tagCache.put(tag.getUid(), tag);
						tagEvent.getTag().setId(tag.getId());
						tagEvent.getTag().setName(tag.getName());
						fireTagEvent(tagEvent);
					}
				} else {
					// It's a character
					// Sending LST_MODEL command
					byte[] lst_modelPl = new byte[8];
					TagEvent tagEvent = mapEvents.remove(new Integer(counter));
					if (tagEvent != null) {
						lst_modelPl[0] = tagEvent.getIndex();
						try {
							counter = sendCommand(CommandEnum.LST_MODEL, TagDecoder.encode(lst_modelPl));
							mapEvents.put(new Integer(counter), tagEvent);
						} catch (ToyPadException e) {
							logger.warn("Fail to send LST_MODEL command.");
						}
					}
				}

			} else if (data[1] == 0x0a) {
				// Example : 55 0A 09 00 7D 9C 04 79 96 32 69 B1 E0 00 00 00 00
				// 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00

				logger.debug("Response for LST_MODEL command");
				int counter = data[2];

				TagEvent tagEvent = mapEvents.remove(new Integer(counter));
				if (tagEvent != null) {
					byte[] idcrypted = new byte[8];
					for (int i = 4; i < 12; i++) {
						idcrypted[i - 4] = data[i];
					}
					tag = TagDecoder.decodeCharacter(idcrypted);
					tag.setUid(tagEvent.getTag().getUid());
					tagCache.put(tag.getUid(), tag);
					tagEvent.getTag().setId(tag.getId());
					tagEvent.getTag().setName(tag.getName());
					fireTagEvent(tagEvent);
				}
			} else {
				logger.debug("Useless return.");
			}
		}
	}

	/**
	 * Fire tag event to tag listeners.
	 * 
	 * @param tagEvent
	 *            tag event
	 */
	private void fireTagEvent(final TagEvent tagEvent) {

		if (tagListeners != null) {
			for (TagListener listener : tagListeners.getListeners(TagListener.class)) {
				listener.newTagEvent(tagEvent);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTagListener(final TagListener tagListener) {

		if (tagListeners == null) {
			tagListeners = new EventListenerList();
		}
		tagListeners.add(TagListener.class, tagListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void errorEventOccurred(UsbPipeErrorEvent event) {

		logger.error(event.getUsbException().getMessage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dataEventOccurred(final UsbPipeDataEvent event) {

		byte[] data = new byte[32];
		try {
			pipeIn.asyncSubmit(data);
		} catch (Exception e) {
			logger.error("Error while reading data. {}", e.getMessage());
		}
		if (event.getData() != null && event.getData().length == 32) {
			decodeInput(event.getData());
		}
	}
}
