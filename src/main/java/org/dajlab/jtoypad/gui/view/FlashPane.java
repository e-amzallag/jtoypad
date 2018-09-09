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

import org.dajlab.jtoypad.gui.model.PadModel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

/**
 * Pane with flash commands.<br>
 * Display one colorpicker and one spinner.
 * 
 * @author Erik Amzallag
 *
 */
public class FlashPane extends AbstractToypadGridPan {

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            model
	 * @param on
	 *            true if for the "on" flash, false for the "off" flash
	 */
	public FlashPane(final PadModel model, final boolean on) {
		super();

		// Button
		ColorButton colorButton = new ColorButton();
		colorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Color color = colorButton.getValue();
				if (on) {
					model.setColorFlashOn(color);
				} else {
					model.setColorFlashOff(color);
				}
			}
		});
		if (on) {
			colorButton.valueProperty().bindBidirectional(model.colorFlashOnProperty());
		} else {
			colorButton.valueProperty().bindBidirectional(model.colorFlashOffProperty());
		}

		add(colorButton, 0, 0);

		// Spinner
		TimeSpinner timeSpinner = new TimeSpinner();
		if (on) {
			timeSpinner.timeProperty().bindBidirectional(model.timeFlashOnProperty());
		} else {
			timeSpinner.timeProperty().bindBidirectional(model.timeFlashOffProperty());
		}
		add(timeSpinner, 1, 0);
	}
}
