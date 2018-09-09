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
 * Enumeration with the different supported commands of the protocol.
 * 
 * @author Erik Amzallag
 */
public enum CommandEnum {

	/**
	 * Switch pad.
	 */
	SWITCH_PAD(0xc0, 0x06),
	/**
	 * Get color.
	 */
	GET_COL(0xc1, 0x03),
	/**
	 * Fade pad.
	 */
	FADE_PAD(0xc2, 0x08),
	/**
	 * Flash pad.
	 */
	FLASH_PAD(0xc3, 0x09),
	/**
	 * Random fade pad.
	 */
	FADE_PAD_RANDOM(0xc4, 0x05),
	/**
	 * Undetermined fad command.
	 */
	FADE_PAD_TOBEDETERMINED(0xc5, 0x05),
	/**
	 * Fad all pads.
	 */
	FADE_PADS(0xc6, 0x14),
	/**
	 * Flash all pads.
	 */
	FLASH_PADS(0xc7, 0x17),
	/**
	 * Switch all pads.
	 */
	SWITCH_PADS(0xc8, 0x0e),
	/**
	 * Read tag info.
	 */
	READ(0xd2, 0x04),
	/**
	 * List models.
	 */
	LST_MODEL(0xd4, 0x0a),
	/**
	 * Init message.
	 */
	INIT(0xb0, 0x0f),
	/**
	 * Kind of identify command.
	 */
	SEED(0xb1, 0x0a),
	/**
	 * Challenge the toypad.
	 */
	CHAL(0xb3, 0x0a);

	/**
	 * Command id.
	 */
	private int commandId;
	/**
	 * Payload size for the command.
	 */
	private int payloadSize;

	/**
	 * Private constructor.
	 * 
	 * @param commandId
	 *            command id
	 * @param payloadSize
	 *            payload size
	 */
	private CommandEnum(final int commandId, final int payloadSize) {
		this.commandId = commandId;
		this.payloadSize = payloadSize;
	}

	/**
	 * @return the commandId
	 */
	public final byte getCommandId() {
		return (byte) commandId;
	}

	/**
	 * @return the payloadSize
	 */
	public final byte getPayloadSize() {
		return (byte) payloadSize;
	}

}
