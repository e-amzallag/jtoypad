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
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

/**
 * Pane for the on pane.<br>
 * Display one colorpicker.
 * 
 * @author Erik Amzallag
 *
 */
public class OnPane extends AbstractToypadGridPan {

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            model
	 */
	public OnPane(PadModel model) {
		super();
		setAlignment(Pos.CENTER);
		ColorButton colorButton = new ColorButton();
		colorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Color color = colorButton.getValue();
				model.setColorOn(color);
			}
		});
		colorButton.valueProperty().bindBidirectional(model.colorOnProperty());
		add(colorButton, 0, 0);
	}
}
