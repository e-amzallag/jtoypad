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

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

/**
 * Extension for the colorpicker.
 * 
 * @author Erik Amzallag
 *
 */
public class ColorButton extends ColorPicker {

	/**
	 * Constructor.
	 */
	public ColorButton() {

		super(Color.BLACK);
		setPrefSize(50, 25);
		// Add the custom colors
		for (org.dajlab.jtoypad.core.Color color : org.dajlab.jtoypad.core.Color.ALL_COLORS) {
			getCustomColors()
					.add(new Color(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1));
		}
	}
}
