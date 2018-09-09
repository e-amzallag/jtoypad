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

/**
 * JToyPad colors. <br>
 * Redefine the basic colors for a better visual render.
 * 
 * @author Erik Amzallag
 *
 */
public class Color {

	/**
	 * Red.
	 */
	private int red;
	/**
	 * Green.
	 */
	private int green;
	/**
	 * Blue.
	 */
	private int blue;

	/**
	 * Black.
	 */
	public final static Color BLACK = new Color(0, 0, 0);
	/**
	 * Red.
	 */
	public final static Color RED = new Color(255, 0, 0);
	/**
	 * Green.
	 */
	public final static Color GREEN = new Color(0, 255, 0);
	/**
	 * Blue.
	 */
	public final static Color BLUE = new Color(0, 0, 255);
	/**
	 * Magenta.
	 */
	public final static Color MAGENTA = new Color(255, 0, 255);
	/**
	 * Yellow.
	 */
	public final static Color YELLOW = new Color(255, 255, 0);
	/**
	 * Cyan.
	 */
	public final static Color CYAN = new Color(0, 255, 255);
	/**
	 * White.
	 */
	public final static Color WHITE = new Color(255, 255, 255);
	/**
	 * Pink.
	 */
	public final static Color PINK = new Color(255, 20, 147);
	/**
	 * Orange.
	 */
	public final static Color ORANGE = new Color(255, 69, 0);
	/**
	 * Purple.
	 */
	public final static Color PURPLE = new Color(128, 0, 128);
	/**
	 * Grey.
	 */
	public final static Color GREY = new Color(128, 128, 128);
	/**
	 * Dark grey.
	 */
	public final static Color DARK_GREY = new Color(30, 30, 30);
	/**
	 * Light grey.
	 */
	public final static Color LIGHT_GREY = new Color(211, 211, 211);

	/**
	 * Table with all colors.
	 */
	public final static Color[] ALL_COLORS = new Color[] { BLACK, RED, GREEN, BLUE, MAGENTA, YELLOW, CYAN, WHITE, PINK,
			ORANGE, PURPLE, GREY, DARK_GREY, LIGHT_GREY };

	/**
	 * Constructor.
	 * 
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 */
	public Color(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * @return the red
	 */
	public final int getRed() {
		return red;
	}

	/**
	 * @param red
	 *            the red to set
	 */
	public final void setRed(int red) {
		this.red = red;
	}

	/**
	 * @return the green
	 */
	public final int getGreen() {
		return green;
	}

	/**
	 * @param green
	 *            the green to set
	 */
	public final void setGreen(int green) {
		this.green = green;
	}

	/**
	 * @return the blue
	 */
	public final int getBlue() {
		return blue;
	}

	/**
	 * @param blue
	 *            the blue to set
	 */
	public final void setBlue(int blue) {
		this.blue = blue;
	}

}
