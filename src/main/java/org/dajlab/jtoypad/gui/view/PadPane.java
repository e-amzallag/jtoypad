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
package org.dajlab.jtoypad.gui.view;

import javax.swing.event.EventListenerList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dajlab.core.CommandListener;
import org.dajlab.gui.MessagesUtil;
import org.dajlab.jtoypad.core.Tag;
import org.dajlab.jtoypad.gui.model.CommandEnum;
import org.dajlab.jtoypad.gui.model.PadModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Pane for a pad.
 * 
 * @author Erik Amzallag
 *
 */
public class PadPane extends TitledPane {

	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(PadPane.class);

	/**
	 * Command listeners.
	 */
	private EventListenerList commandListeners;

	/**
	 * Constructor.
	 * 
	 * @param padModel
	 *            pad model
	 */
	public PadPane(final PadModel padModel) {

		PadModel model = padModel;
		commandListeners = new EventListenerList();

		// Pad title
		setText(MessagesUtil.getString("jtoypad.pad." + model.getPad().toString().toLowerCase()));
		setCollapsible(false);

		VBox layout = new VBox();

		// Toggle Group
		ToggleGroupValue<CommandEnum> radioGroup = new ToggleGroupValue<>();
		model.activeCommandProperty().bindBidirectional(radioGroup.valueProperty());
		model.activeCommandProperty().addListener(new ChangeListener<CommandEnum>() {

			@Override
			public void changed(ObservableValue<? extends CommandEnum> observable, CommandEnum oldValue,
					CommandEnum newValue) {
				logger.debug("Event [COMMAND] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue.name());
				CommandToyPadEvent event = new CommandToyPadEvent(model, null);
				fireEvent(event);
			}
		});

		// Off - no control
		RadioButton offButton = new RadioButton(MessagesUtil.getString(CommandEnum.OFF.getMessage()));
		offButton.setUserData(CommandEnum.OFF);
		offButton.setToggleGroup(radioGroup);
		offButton.setSelected(true);
		layout.getChildren().add(offButton);

		// On - one color picker
		RadioButton onButton = new RadioButton(MessagesUtil.getString(CommandEnum.ON.getMessage()));
		onButton.setUserData(CommandEnum.ON);
		onButton.setToggleGroup(radioGroup);
		layout.getChildren().add(onButton);
		layout.getChildren().add(new OnPane(model));

		model.colorOnProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				logger.debug("Event [COLOR ON] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.ON);
				fireEvent(event);
			}
		});

		// Flash
		RadioButton flashButton = new RadioButton(MessagesUtil.getString(CommandEnum.FLASH.getMessage()));
		flashButton.setUserData(CommandEnum.FLASH);
		flashButton.setToggleGroup(radioGroup);
		layout.getChildren().add(flashButton);

		// Flash On part
		FlashPane onPane = new FlashPane(model, true);
		model.colorFlashOnProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				logger.debug("Event [COLOR FLASH ON] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FLASH);
				fireEvent(event);
			}

		});

		model.timeFlashOnProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				logger.debug("Event [TIME FLASH ON] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FLASH);
				fireEvent(event);

			}
		});
		layout.getChildren().add(onPane);

		// Flash Off part
		FlashPane offPane = new FlashPane(model, false);
		model.colorFlashOffProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				logger.debug("Event [COLOR FLASH OFF] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FLASH);
				fireEvent(event);
			}
		});

		model.timeFlashOffProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				logger.debug("Event [TIME FLASH OFF] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FLASH);
				fireEvent(event);
			}
		});
		layout.getChildren().add(offPane);

		// Fade
		RadioButton fadeButton = new RadioButton(MessagesUtil.getString(CommandEnum.FADE.getMessage()));
		fadeButton.setUserData(CommandEnum.FADE);
		fadeButton.setToggleGroup(radioGroup);
		layout.getChildren().add(fadeButton);
		layout.getChildren().add(new FadePane(model));

		model.color1FadeProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				logger.debug("Event [COLOR FADE 1] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FADE);
				fireEvent(event);
			}

		});

		model.color2FadeProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				logger.debug("Event [COLOR FADE 2] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FADE);
				fireEvent(event);
			}

		});

		model.timeFadeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				logger.debug("Event [TIME FADE] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.FADE);
				fireEvent(event);

			}
		});

		// Random fade
		RadioButton fadeRandomButton = new RadioButton(MessagesUtil.getString(CommandEnum.RANDOM_FADE.getMessage()));
		fadeRandomButton.setUserData(CommandEnum.RANDOM_FADE);
		fadeRandomButton.setToggleGroup(radioGroup);
		layout.getChildren().add(fadeRandomButton);
		layout.getChildren().add(new RandomFadePane(model));

		model.timeRandomFadeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				logger.debug("Event [TIME RANDOM FADE] for toypad [{}] [{}] : [{}]", model.getToyPadNumber(),
						model.getPad().toString(), newValue);
				CommandToyPadEvent event = new CommandToyPadEvent(model, CommandEnum.RANDOM_FADE);
				fireEvent(event);

			}
		});

		// Tags list - no user input for this
		ListView<Tag> tagList = new ListView<>();
		tagList.setItems(padModel.getTagsList());
		tagList.setPrefHeight(80);
		tagList.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>() {

			@Override
			public ListCell<Tag> call(ListView<Tag> param) {
				ListCell<Tag> cell = new ListCell<Tag>() {

					@Override
					protected void updateItem(Tag item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							if (item.getName() != null) {
								setText(item.getName());
							} else {
								setText("Unknown");
							}
						}
					}
				};
				return cell;
			}
		});
		layout.getChildren().add(tagList);

		setContent(layout);

	}

	/**
	 * Add a command listener.
	 * 
	 * @param listener
	 *            listener
	 */
	public void addCommandListeners(final CommandListener listener) {
		commandListeners.add(CommandListener.class, listener);
	}

	/**
	 * Fire a toypad command to command listeners.
	 * 
	 * @param event
	 *            event
	 */
	private void fireEvent(final CommandToyPadEvent event) {
		if (commandListeners != null) {
			for (CommandListener listener : commandListeners.getListeners(CommandListener.class)) {
				listener.newCommand(event);
			}
		}
	}

}
