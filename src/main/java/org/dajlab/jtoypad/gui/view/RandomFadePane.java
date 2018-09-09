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

/**
 * Pane for the random fade pane.<br>
 * Display one spinner.
 * 
 * @author Erik Amzallag
 *
 */
public class RandomFadePane extends AbstractToypadGridPan {

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            model
	 */
	public RandomFadePane(PadModel model) {

		super();

		// Spinner
		TimeSpinner timeSpinner = new TimeSpinner();
		timeSpinner.timeProperty().bindBidirectional(model.timeRandomFadeProperty());
		timeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				model.setTimeRandomFade(newValue.intValue());
			}
		});
		add(timeSpinner, 1, 0);
	}
}
