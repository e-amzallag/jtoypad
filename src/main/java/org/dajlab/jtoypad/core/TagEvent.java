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
 * Tag event.
 * 
 * @author Erik Amzallag
 *
 */
public class TagEvent {

	/**
	 * Toypad source.
	 */
	private ToyPad source;

	/**
	 * Pad source.
	 */
	private PadEnum pad;

	/**
	 * Action (added or removed).
	 */
	private ActionEnum action;

	/**
	 * Tag.
	 */
	private Tag tag = new Tag();

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            source
	 */
	public TagEvent(final ToyPad source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public final ToyPad getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public final void setSource(final ToyPad source) {
		this.source = source;
	}

	/**
	 * @return the pad
	 */
	public final PadEnum getPad() {
		return pad;
	}

	/**
	 * @param pad
	 *            the pad to set
	 */
	public final void setPad(final PadEnum pad) {
		this.pad = pad;
	}

	/**
	 * @return the action
	 */
	public final ActionEnum getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public final void setAction(final ActionEnum action) {
		this.action = action;
	}

	/**
	 * @return the tag
	 */
	public final Tag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public final void setTag(final Tag tag) {
		this.tag = tag;
	}

}
