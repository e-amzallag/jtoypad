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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

/**
 * Pane with fade commands.<br>
 * Display two colorpickers and one spinner.
 * 
 * @author Erik Amzallag
 *
 */
public class FadePane extends AbstractToypadGridPan {

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            model
	 */
	public FadePane(PadModel model) {
		super();

		// Button 1
		ColorButton color1Button = new ColorButton();
		color1Button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Color color = color1Button.getValue();
				model.setColor1Fade(color);
			}
		});
		color1Button.valueProperty().bindBidirectional(model.color1FadeProperty());
		add(color1Button, 0, 0);

		// Button 2
		ColorButton color2Button = new ColorButton();
		color2Button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Color color = color2Button.getValue();
				model.setColor2Fade(color);
			}
		});
		color2Button.valueProperty().bindBidirectional(model.color2FadeProperty());
		add(color2Button, 1, 0);

		// Spinner
		TimeSpinner timeSpinner = new TimeSpinner();
		timeSpinner.timeProperty().bindBidirectional(model.timeFadeProperty());
		timeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				model.setTimeFade(newValue.intValue());
			}
		});
		add(timeSpinner, 2, 0);
	}
}
