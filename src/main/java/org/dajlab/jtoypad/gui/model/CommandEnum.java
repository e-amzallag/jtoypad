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
package org.dajlab.jtoypad.gui.model;

/**
 * Command enum.
 * 
 * @author Erik Amzallag
 *
 */
public enum CommandEnum {

	/**
	 * Off.
	 */
	OFF("jtoypad.command.off"),
	/**
	 * On.
	 */
	ON("jtoypad.command.on"),
	/**
	 * Flash.
	 */
	FLASH("jtoypad.command.flash"),
	/**
	 * Fade.
	 */
	FADE("jtoypad.command.fade"),
	/**
	 * Random fade.
	 */
	RANDOM_FADE("jtoypad.command.random_fade");

	/**
	 * Message.
	 */
	private String message;

	/**
	 * Private constructor.
	 * 
	 * @param message
	 *            message key
	 */
	private CommandEnum(String message) {

		this.message = message;
	}

	/**
	 * @return the title
	 */
	public final String getMessage() {
		return message;
	}

}
