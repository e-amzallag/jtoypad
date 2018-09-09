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
 * Describe fade effect.
 * 
 * @author Erik Amzallag
 *
 */
public class FadeColor {

	/**
	 * Fade for ever.
	 */
	public final static int FADE_FOR_EVER = 255;

	/**
	 * Color 1.
	 */
	private Color toColor;
	/**
	 * Color 2.
	 */
	private Color fromColor;
	/**
	 * Time for pulse.
	 */
	private int pulseTime;
	/**
	 * Number of pulses.
	 */
	private int pulseCount;

	/**
	 * Constructor.
	 * 
	 * @param toColor
	 *            fade to this color
	 * @param pulseTime
	 *            pulse time
	 * @param pulseCount
	 *            pulse count. Use FADE_FOR_EVER for infinite pulse.
	 */
	public FadeColor(final Color toColor, final int pulseTime, final int pulseCount) {
		super();
		this.toColor = toColor;
		if (toColor == null) {
			this.toColor = Color.BLACK;
		}
		this.pulseTime = pulseTime;
		this.pulseCount = pulseCount;
	}

	/**
	 * Constructor.
	 * 
	 * @param fromColor
	 *            fade from this color
	 * @param toColor
	 *            fade to this color
	 * @param pulseTime
	 *            pulse time
	 * @param pulseCount
	 *            pulse count. Use FADE_FOR_EVER for infinite pulse.
	 */
	public FadeColor(final Color fromColor, final Color toColor, final int pulseTime, final int pulseCount) {
		super();
		this.toColor = toColor;
		if (toColor == null) {
			this.toColor = Color.BLACK;
		}
		this.fromColor = fromColor;
		this.pulseTime = pulseTime;
		this.pulseCount = pulseCount;
	}

	/**
	 * @return the color
	 */
	public final Color getToColor() {
		return toColor;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public final void setToColor(final Color color) {
		this.toColor = color;
	}

	/**
	 * @return the color2
	 */
	public final Color getFromColor() {
		return fromColor;
	}

	/**
	 * @param color2
	 *            the color2 to set
	 */
	public final void setFromColor(final Color color2) {
		this.fromColor = color2;
	}

	/**
	 * @return the pulseTime
	 */
	public final int getPulseTime() {
		return pulseTime;
	}

	/**
	 * @param pulseTime
	 *            the pulseTime to set
	 */
	public final void setPulseTime(final int pulseTime) {
		this.pulseTime = pulseTime;
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
	public final void setPulseCount(final int pulseCount) {
		this.pulseCount = pulseCount;
	}

}
