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
 * Enumeration for pads.
 * 
 * @author Erik Amzallag
 */
public enum PadEnum {
	/**
	 * Left pad.
	 */
	LEFT((byte) 2),
	/**
	 * Center pad.
	 */
	CENTER((byte) 1),
	/**
	 * Right pad.
	 */
	RIGHT((byte) 3),
	/**
	 * All pads.
	 */
	ALL((byte) 0);

	/**
	 * Pad address.
	 */
	private byte pad;

	/**
	 * Private constructor.
	 * 
	 * @param pad
	 *            pad
	 */
	private PadEnum(final byte pad) {
		this.pad = pad;
	}

	/**
	 * @return the pad
	 */
	public final byte getValue() {
		return pad;
	}

	/**
	 * Find a pad from its address.
	 * 
	 * @param pad
	 *            pad address
	 * @return a pad if found, null otherwise
	 */
	public final static PadEnum findPad(final byte pad) {

		PadEnum padEnum = null;
		for (PadEnum padenum : PadEnum.values()) {
			if (pad == padenum.pad) {
				padEnum = padenum;
				break;
			}
		}
		return padEnum;
	}
}
