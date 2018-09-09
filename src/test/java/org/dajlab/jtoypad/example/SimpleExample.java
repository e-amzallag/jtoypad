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
package org.dajlab.jtoypad.example;

import org.apache.commons.collections4.CollectionUtils;
import org.dajlab.jtoypad.core.ActionEnum;
import org.dajlab.jtoypad.core.Color;
import org.dajlab.jtoypad.core.FadeColor;
import org.dajlab.jtoypad.core.PadEnum;
import org.dajlab.jtoypad.core.TagEvent;
import org.dajlab.jtoypad.core.TagListener;
import org.dajlab.jtoypad.core.ToyPad;
import org.dajlab.jtoypad.core.ToyPadImpl;
import org.dajlab.jtoypad.core.ToyPadManager;

/**
 * This example illustrates how to use the API to send commands to a toypad and
 * read tags.
 * 
 * @author Erik Amzallag
 *
 */
public class SimpleExample implements TagListener {

	/**
	 * Toypad.
	 */
	private ToyPad toypad;

	/**
	 * Main
	 * 
	 * @param args
	 *            args
	 */
	public static void main(final String[] args) {

		new SimpleExample();

	}

	/**
	 * Constructor.
	 */
	public SimpleExample() {

		// Create the toypad manager
		ToyPadManager toypadManager = new ToyPadManager();
		try {
			// Connect the toypads.
			toypadManager.connect();
			if (CollectionUtils.isNotEmpty(toypadManager.getToyPads())) {
				// get the first one
				toypad = (ToyPadImpl) toypadManager.getToyPads().get(0);
				// add a tag listener as we want to read tags
				toypad.addTagListener(this);
				// Turn on randomly the center pad
				toypad.fadePadRandom(PadEnum.CENTER, 5, FadeColor.FADE_FOR_EVER);

				System.out.println("Put and remove some LEGO Dimensions tags on the toypad and see what happens");
				// sleep for 60s
				Thread.sleep(60000);
				System.out.println("End of the test");
				// turn off the pads
				toypad.turnOffPads();
			}
			// disconnect the toypads
			toypadManager.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newTagEvent(final TagEvent event) {

		try {
			if (ActionEnum.ADDED == event.getAction()) {
				// Tag has been added, let's turn on the pad with a color
				// depending of the character
				if ("Batman".equals(event.getTag().getName())) {
					toypad.switchPad(event.getPad(), Color.BLUE);
				} else if ("Gandalf".equals(event.getTag().getName())) {
					toypad.switchPad(event.getPad(), Color.WHITE);
				} else if ("Wyldstyle".equals(event.getTag().getName())) {
					toypad.switchPad(event.getPad(), Color.MAGENTA);
				} else if (event.getTag().getId() >= 4 && event.getTag().getId() <= 30) {
					toypad.switchPad(event.getPad(), Color.ORANGE);
				} else if (event.getTag().getId() > 30) {
					toypad.switchPad(event.getPad(), Color.GREEN);
				}
			} else {
				// When a tag is removed, just turn off the pad
				toypad.switchPad(event.getPad(), Color.BLACK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
