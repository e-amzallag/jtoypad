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
 * Describe flash effect.
 * 
 * @author Erik Amzallag
 */
public class FlashColor {

	/**
	 * Flash for ever.
	 */
	public final static int FLASH_FOR_EVER = 255;

	/**
	 * Color 1.
	 */
	private Color color1;
	/**
	 * Time for color 1.
	 */
	private int timeForColor1;
	/**
	 * Color 2.
	 */
	private Color color2;
	/**
	 * Time for color 2.
	 */
	private int timeForColor2;
	/**
	 * Number of pulses.
	 */
	private int pulseCount;

	/**
	 * Flash between color1 and color 2, each color for its duration, repeated
	 * pulseCount times.<br>
	 * If color1 is null, then it's replaced by black (off).<br>
	 * If color2 is null, then the previous value of the color of the pad is
	 * used.
	 * 
	 * @param color1
	 *            color 1
	 * @param timeForColor1
	 *            duration for color 1
	 * @param color2
	 *            color 2
	 * @param timeForColor2
	 *            duration for color 2
	 * @param pulseCount
	 *            number of pulses. Use FLASH_FOR_EVER for infinite pulse.
	 */
	public FlashColor(final Color color1, final int timeForColor1, final Color color2, final int timeForColor2,
			final int pulseCount) {

		super();
		this.color1 = color1;
		if (color1 == null) {
			this.color1 = Color.BLACK;
		}
		this.color2 = color2;
		this.timeForColor1 = timeForColor1;
		this.timeForColor2 = timeForColor2;
		this.pulseCount = pulseCount;
	}

	/**
	 * Flash between color1 and the previous color of the pad, each color for
	 * its duration, repeated pulseCount times.<br>
	 * If color1 is null, then it's replaced by black (off).<br>
	 * 
	 * @param color1
	 *            color 1
	 * @param timeForColor1
	 *            duration for color 1
	 * @param timeForColor2
	 *            duration for color 2
	 * @param pulseCount
	 *            number of pulses. Use FLASH_FOR_EVER for infinite pulse.
	 */
	public FlashColor(final Color color1, final int timeForColor1, final int timeForColor2, final int pulseCount) {

		super();
		this.color1 = color1;
		if (color1 == null) {
			this.color1 = Color.BLACK;
		}
		this.timeForColor1 = timeForColor1;
		this.timeForColor2 = timeForColor2;
		this.pulseCount = pulseCount;
	}

	/**
	 * @return the color
	 */
	public final Color getColor1() {
		return color1;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public final void setColor1(Color color) {
		this.color1 = color;
	}

	/**
	 * @return the color2
	 */
	public final Color getColor2() {
		return color2;
	}

	/**
	 * @param color2
	 *            the color2 to set
	 */
	public final void setColor2(Color color2) {
		this.color2 = color2;
	}

	/**
	 * @return the onLength
	 */
	public final int getTimeForColor1() {
		return timeForColor1;
	}

	/**
	 * @param onLength
	 *            the onLength to set
	 */
	public final void setTimeForColor1(int onLength) {
		this.timeForColor1 = onLength;
	}

	/**
	 * @return the offLength
	 */
	public final int getTimeForColor2() {
		return timeForColor2;
	}

	/**
	 * @param offLength
	 *            the offLength to set
	 */
	public final void setTimeForColor2(int offLength) {
		this.timeForColor2 = offLength;
	}

	/**
	 * @return the pulseCount
	 */
	public final int getPulseCount() {
		return pulseCount;
	}

	/**
	 * @param pulseCount
	 *            the pulseCount to set
	 */
	public final void setPulseCount(int pulseCount) {
		this.pulseCount = pulseCount;
	}

}
