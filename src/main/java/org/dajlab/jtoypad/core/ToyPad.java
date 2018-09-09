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
 * Interface for toypad.
 * 
 * @author Erik Amzallag
 *
 */
public interface ToyPad {

	/**
	 * Open the connection with the toypad.
	 * 
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void open() throws ToyPadException;

	/**
	 * Close the connection with the toypad.
	 * 
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void close() throws ToyPadException;

	/**
	 * Turn off all the pads.
	 * 
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void turnOffPads() throws ToyPadException;

	/**
	 * Switch the pad.
	 * 
	 * @param pad
	 *            pad
	 * @param color
	 *            color
	 * @throws ToyPadException
	 *             in case of exception
	 */
	void switchPad(final PadEnum pad, final Color color) throws ToyPadException;

	/**
	 * Switch the three pads.
	 * 
	 * @param colorPadLeft
	 *            color for left pad
	 * @param colorPadCenter
	 *            color for center pad
	 * @param colorPadRight
	 *            color for right pad
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void switchPads(final Color colorPadLeft, final Color colorPadCenter, final Color colorPadRight)
			throws ToyPadException;

	/**
	 * Flash a pad.
	 * 
	 * @param pad
	 *            pad to flash
	 * @param flashColor
	 *            a flashcolor which describes the properties of the flash
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void flashPad(final PadEnum pad, final FlashColor flashColor) throws ToyPadException;

	/**
	 * Flash all pads.<br>
	 * If a flashColor is null, then the pad continues as previous.
	 * 
	 * @param flashColorLeft
	 *            flashColor for left pad
	 * @param flashColorCenter
	 *            flashColor for center pad
	 * @param flashColorRight
	 *            flashColor for right pad
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void flashPads(final FlashColor flashColorLeft, final FlashColor flashColorCenter, final FlashColor flashColorRight)
			throws ToyPadException;

	/**
	 * Fade a pad.
	 * 
	 * @param pad
	 *            pad
	 * @param fadeColor
	 *            a fadeColor which describes the properties of the fade
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void fadePad(final PadEnum pad, final FadeColor fadeColor) throws ToyPadException;

	/**
	 * Fade once from a color to another color, the fade lasts the given time.
	 * 
	 * @param pad
	 *            pad
	 * @param fromColor
	 *            color
	 * @param toColor
	 *            color
	 * @param time
	 *            time from 0 to 255.
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void fadePad(final PadEnum pad, final Color fromColor, final Color toColor, final int time) throws ToyPadException;

	/**
	 * Fade between random colors (Colors are selected by the hardware).
	 * 
	 * @param pad
	 *            pad
	 * @param pulseTime
	 *            time. If 0, then random time by the hardware
	 * @param pulseCount
	 *            count. Use FadeColor.FADE_FOR_EVER for infinite time.
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void fadePadRandom(final PadEnum pad, final int pulseTime, final int pulseCount) throws ToyPadException;

	/**
	 * Fade all pads.<br>
	 * If a fadeColor is null, then the pad continues as previous.
	 * 
	 * @param fadeColorLeft
	 *            fadeColor left
	 * @param fadeColorCenter
	 *            fadeColor center
	 * @param fadeColorRight
	 *            fadeColor right
	 * @throws ToyPadException
	 *             toypad exception
	 */
	void fadePads(final FadeColor fadeColorLeft, final FadeColor fadeColorCenter, final FadeColor fadeColorRight)
			throws ToyPadException;

	/**
	 * Return the id of the pad.
	 * 
	 * @return the id
	 */
	String getIdentifiant();

	/**
	 * Add a tag listener (useful only if startReader is called).
	 * 
	 * @param tagListener
	 *            tag listener
	 */
	void addTagListener(final TagListener tagListener);
}
