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
package org.dajlab.jtoypad.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.dajlab.core.CommandEvent;
import org.dajlab.core.CommandListener;
import org.dajlab.gui.AbstractDajlabTab;
import org.dajlab.gui.MessageTab;
import org.dajlab.gui.MessagesUtil;
import org.dajlab.gui.extension.DajlabControllerExtensionInterface;
import org.dajlab.gui.extension.TabExtensionInterface;
import org.dajlab.jtoypad.core.Color;
import org.dajlab.jtoypad.core.FadeColor;
import org.dajlab.jtoypad.core.FlashColor;
import org.dajlab.jtoypad.core.JToypadConstants;
import org.dajlab.jtoypad.core.PadEnum;
import org.dajlab.jtoypad.core.TagEvent;
import org.dajlab.jtoypad.core.TagListener;
import org.dajlab.jtoypad.core.ToyPad;
import org.dajlab.jtoypad.core.ToyPadException;
import org.dajlab.jtoypad.core.ToyPadManager;
import org.dajlab.jtoypad.gui.model.CommandEnum;
import org.dajlab.jtoypad.gui.model.JToyPadModel;
import org.dajlab.jtoypad.gui.model.PadModel;
import org.dajlab.jtoypad.gui.model.ToyPadModel;
import org.dajlab.jtoypad.gui.view.CommandToyPadEvent;
import org.dajlab.jtoypad.gui.view.ToyPadTab;

import javafx.application.Platform;

/**
 * JToyPad controller.
 * 
 * @author Erik Amzallag
 *
 */
public class JToyPadController implements DajlabControllerExtensionInterface<JToyPadModel>, TabExtensionInterface,
		CommandListener, TagListener {

	/**
	 * ToyPad manager.
	 */
	private ToyPadManager toyPadManager;
	/**
	 * Toypads map.
	 */
	private Map<String, ToyPad> toyPadsMap;

	/**
	 * Main model.
	 */
	private JToyPadModel mainModel = new JToyPadModel();

	/**
	 * List of tabs.
	 */
	private List<AbstractDajlabTab> tabs;

	/**
	 * Prefix for localization.
	 */
	private static final String MESSAGES_PREFIX = "jtoypad";

	/**
	 * Error code.
	 */
	private String errorCode = null;

	/**
	 * Constructor.
	 * 
	 */
	public JToyPadController() {

		toyPadManager = new ToyPadManager();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() {

		try {
			toyPadManager.connect();
		} catch (ToyPadException e) {
			errorCode = e.getMessage();
		}
		List<ToyPad> toyPads = toyPadManager.getToyPads();
		toyPadsMap = new HashMap<>(toyPads.size());
		int i = 0;
		for (ToyPad toyPad : toyPads) {
			// Toypads map
			toyPadsMap.put(toyPad.getIdentifiant(), toyPad);

			// ToypadModel map
			String title = "Toypad " + i;
			ToyPadModel tpm = new ToyPadModel(i, title, toyPad.getIdentifiant());
			mainModel.getToypads().put(Integer.toString(i), tpm);

			toyPad.addTagListener(this);
			i++;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() {

		// Turn off all the pads.
		if (CollectionUtils.isNotEmpty(toyPadsMap.values())) {
			for (ToyPad toyPad : toyPadsMap.values()) {
				try {
					toyPad.turnOffPads();
				} catch (ToyPadException e) {
					// N/A
				}
			}
		}
		toyPadManager.disconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AbstractDajlabTab> getTabs() {

		// Create tabs with model.
		if (tabs == null) {
			if (CollectionUtils.isNotEmpty(mainModel.getToypads().values())) {

				tabs = new ArrayList<>(mainModel.getToypads().size());
				for (ToyPadModel toyPadModel : mainModel.getToypads().values()) {
					ToyPadTab tab = new ToyPadTab(toyPadModel, this);
					tabs.add(tab);
				}
			} else {
				tabs = new ArrayList<>(1);
				MessageTab messTab = null;
				if (errorCode != null) {
					messTab = new MessageTab(MessagesUtil.getString(JToypadConstants.ERR_NO_DEVICES),
							MessagesUtil.getString(errorCode));
				} else {
					messTab = new MessageTab("JToypad", JToypadConstants.ICON_JTOYPAD,
							MessagesUtil.getString(JToypadConstants.ERR_NO_DEVICES));
				}

				tabs.add(messTab);
			}
		}
		return tabs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newCommand(final CommandEvent ev) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (ev instanceof CommandToyPadEvent) {
					// Listen only for commands from ToyPad
					CommandToyPadEvent event = (CommandToyPadEvent) ev;
					String toyPadModelNumber = Integer.toString(event.getPadModel().getToyPadNumber());
					String usbId = mainModel.getToypads().get(toyPadModelNumber).getUsbId();
					ToyPad toyPad = toyPadsMap.get(usbId);
					PadModel padModel = event.getPadModel();
					if (event.getCommand() == null || padModel.getActiveCommand() == event.getCommand()) {
						PadEnum pad = padModel.getPad();
						try {
							CommandEnum command = padModel.getActiveCommand();
							switch (command) {
							case OFF:
								toyPad.switchPad(pad, Color.BLACK);
								break;
							case ON:
								toyPad.switchPad(pad, convertColor(padModel.getColorOn()));
								break;
							case FLASH:
								toyPad.flashPad(pad,
										new FlashColor(convertColor(padModel.getColorFlashOn()),
												padModel.getTimeFlashOn(), convertColor(padModel.getColorFlashOff()),
												padModel.getTimeFlashOff(), FlashColor.FLASH_FOR_EVER));
								break;
							case FADE:
								toyPad.fadePad(pad,
										new FadeColor(convertColor(padModel.getColor1Fade()),
												convertColor(padModel.getColor2Fade()), padModel.getTimeFade(),
												FadeColor.FADE_FOR_EVER));
								break;
							case RANDOM_FADE:
								toyPad.fadePadRandom(pad, padModel.getTimeRandomFade(), FadeColor.FADE_FOR_EVER);
								break;
							default:
								toyPad.switchPad(pad, Color.BLACK);
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateModel(JToyPadModel model) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (model != null) {

					// Simple algo "all or nothing".
					// TODO improve the algo
					Set<String> connectedUsbAdresses = toyPadsMap.keySet();
					Set<String> loadedUsbAdresses = new HashSet<>();
					for (ToyPadModel tpm : model.getToypads().values()) {
						loadedUsbAdresses.add(tpm.getUsbId());
					}

					if (CollectionUtils.isEqualCollection(connectedUsbAdresses, loadedUsbAdresses)) {
						// The usb adresses are the same, we can map datas
						for (String idtpmodel : model.getToypads().keySet()) {
							ToyPadModel modelToUpdate = mainModel.getToypads().get(idtpmodel);
							ToyPadModel newModel = model.getToypads().get(idtpmodel);
							updateToyPadModel(modelToUpdate, newModel);
						}
					} else {
						// TODO raise alert.
					}

				}
			}
		});
	}

	/**
	 * Update toy pad model.
	 * 
	 * @param currentModel
	 *            current model
	 * @param newModel
	 *            new model
	 */
	private void updateToyPadModel(ToyPadModel currentModel, ToyPadModel newModel) {

		currentModel.setTitle(newModel.getTitle());
		for (PadEnum pad : PadEnum.values()) {
			currentModel.getPadModel(pad).setColorOn(newModel.getPadModel(pad).getColorOn());

			currentModel.getPadModel(pad).setColor1Fade(newModel.getPadModel(pad).getColor1Fade());
			currentModel.getPadModel(pad).setColor2Fade(newModel.getPadModel(pad).getColor2Fade());
			currentModel.getPadModel(pad).setTimeFade(newModel.getPadModel(pad).getTimeFade());

			currentModel.getPadModel(pad).setColorFlashOn(newModel.getPadModel(pad).getColorFlashOn());
			currentModel.getPadModel(pad).setColorFlashOff(newModel.getPadModel(pad).getColorFlashOff());
			currentModel.getPadModel(pad).setTimeFlashOn(newModel.getPadModel(pad).getTimeFlashOn());
			currentModel.getPadModel(pad).setTimeFlashOff(newModel.getPadModel(pad).getTimeFlashOff());

			currentModel.getPadModel(pad).setTimeRandomFade(newModel.getPadModel(pad).getTimeRandomFade());
			currentModel.getPadModel(pad).setActiveCommand(newModel.getPadModel(pad).getActiveCommand());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JToyPadModel getModel() {

		return mainModel;
	}

	/**
	 * Convert JavaFX Color to JToyPad Color.
	 * 
	 * @param color
	 *            JavaFX color
	 * @return JToyPad color
	 */
	public static Color convertColor(final javafx.scene.paint.Color color) {

		Color col = new Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
		return col;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newTagEvent(final TagEvent event) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ToyPad tp = event.getSource();

				// Identify the pad and update the model
				String idToyPadModel = null;
				for (ToyPadModel tpm : mainModel.getToypads().values()) {
					if (tpm.getUsbId().equals(tp.getIdentifiant())) {
						idToyPadModel = Integer.toString(tpm.getNumber());
					}
				}
				PadModel padModel = mainModel.getToypads().get(idToyPadModel).getPadModel(event.getPad());
				switch (event.getAction()) {
				case ADDED:
					padModel.getTagsList().add(event.getTag());
					break;
				case REMOVED:
					padModel.getTagsList().remove(event.getTag());
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalization() {

		return MESSAGES_PREFIX;
	}

	/**
	 * @return the toyPadsMap
	 */
	public final Map<String, ToyPad> getToyPadsMap() {
		return toyPadsMap;
	}

}
