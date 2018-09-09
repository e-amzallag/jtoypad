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

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Toypads manager.
 * 
 * @author Erik Amzallag
 *
 */
public class ToyPadManager {

	/**
	 * Vendor ID (usb).
	 */
	private static short VENDOR_ID = 0x0e6f;
	/**
	 * Product id (usb).
	 */
	private static short PRODUCT_ID = 0x0241;

	/**
	 * List of connected toypads.
	 */
	private List<ToyPad> toyPads;

	/**
	 * Logger.
	 */
	private Logger logger = LogManager.getLogger(ToyPadManager.class);

	/**
	 * Constructor.
	 */
	public ToyPadManager() {
		toyPads = new ArrayList<ToyPad>();
	}

	/**
	 * Connect the toypads.
	 * 
	 * @throws ToyPadException
	 *             toypad exception
	 */
	public void connect() throws ToyPadException {

		List<UsbDevice> devices = findDevices(VENDOR_ID, PRODUCT_ID);
		if (devices != null) {
			for (UsbDevice usbDevice : devices) {
				ToyPad toyPad;
				try {
					toyPad = new ToyPadImpl(usbDevice);
					toyPads.add(toyPad);
				} catch (ToyPadException e) {
					logger.error("Error while connecting to [{}]", usbDevice.toString());
				}
			}
		}
	}

	/**
	 * Disconnect the toypads.
	 */
	public void disconnect() {
		for (ToyPad toyPad : toyPads) {
			try {
				toyPad.close();
			} catch (ToyPadException e) {
				logger.error("Error while disconnecting [{}]", toyPad.getIdentifiant());
			}
		}
	}

	/**
	 * Find devices.
	 * 
	 * @param vendorId
	 *            vendor id.
	 * @param productId
	 *            product id.
	 * @return list of devices, null if not usbroot
	 * @throws ToyPadException
	 *             toypad exception
	 */
	private List<UsbDevice> findDevices(short vendorId, short productId) throws ToyPadException {

		List<UsbDevice> list = new ArrayList<>(0);
		UsbHub hub = (UsbHub) getUsbRootHoob();
		if (hub != null) {
			list = findDevices(hub, vendorId, productId);
		}
		logger.debug("Number of toypads detected : [{}]", list.size());
		return list;
	}

	/**
	 * Find the devices recursively among the usb hubs.
	 * 
	 * @param hub
	 *            hub
	 * @param vendorId
	 *            vendor id
	 * @param productId
	 *            product id
	 * @return list of devices
	 */
	private List<UsbDevice> findDevices(UsbHub hub, short vendorId, short productId) {
		List<UsbDevice> devices = new ArrayList<UsbDevice>();

		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
			if (desc.idVendor() == vendorId && desc.idProduct() == productId)
				devices.add(device);
			if (device.isUsbHub()) {
				List<UsbDevice> sousDevices = findDevices((UsbHub) device, vendorId, productId);
				if (sousDevices != null)
					devices.addAll(sousDevices);
			}
		}

		return devices;
	}

	/**
	 * Return the root usb hub.
	 * 
	 * @return a hub
	 * @throws ToyPadException
	 *             toypad exception
	 */
	private UsbDevice getUsbRootHoob() throws ToyPadException {

		try {
			final UsbServices services = UsbHostManager.getUsbServices();
			return services.getRootUsbHub();
		} catch (SecurityException | UsbException e) {
			logger.error("Error while accessing usb");
			throw new ToyPadException(JToypadConstants.ERR_OPEN_USB);
		}
	}

	/**
	 * @return the toyPads
	 */
	public final List<ToyPad> getToyPads() {
		return toyPads;
	}

}
