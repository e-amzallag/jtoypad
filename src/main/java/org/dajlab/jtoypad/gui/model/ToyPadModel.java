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

import org.dajlab.jtoypad.core.PadEnum;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Toypad model. A toypad is composed by four pads : left, center, right, and
 * all.
 * 
 * @author Erik Amzallag
 *
 */
public class ToyPadModel {

	/**
	 * Toypad number.
	 */
	private IntegerProperty number = new SimpleIntegerProperty();
	/**
	 * Tab title.
	 */
	private StringProperty title = new SimpleStringProperty();
	/**
	 * Usb address.
	 */
	private StringProperty usbId = new SimpleStringProperty();
	/**
	 * Left PadModel.
	 */
	private PadModel leftPadModel;
	/**
	 * Center PadModel.
	 */
	private PadModel centerPadModel;
	/**
	 * Right PadModel.
	 */
	private PadModel rightPadModel;
	/**
	 * All PadModel.
	 */
	private PadModel allPadModel;

	/**
	 * Constructor.
	 * 
	 * @param number
	 *            toypad number
	 * @param title
	 *            tab title
	 * @param usbId
	 *            usb address
	 */
	public ToyPadModel(final int number, final String title, final String usbId) {
		this.number.set(number);
		this.title.set(title);
		this.usbId.set(usbId);

		leftPadModel = new PadModel(PadEnum.LEFT);
		leftPadModel.toyPadNumberProperty().bindBidirectional(this.number);

		rightPadModel = new PadModel(PadEnum.RIGHT);
		rightPadModel.toyPadNumberProperty().bindBidirectional(this.number);

		centerPadModel = new PadModel(PadEnum.CENTER);
		centerPadModel.toyPadNumberProperty().bindBidirectional(this.number);

		allPadModel = new PadModel(PadEnum.ALL);
		allPadModel.toyPadNumberProperty().bindBidirectional(this.number);
	}

	/**
	 * @return the leftPadModel
	 */
	public PadModel getLeftPadModel() {
		return leftPadModel;
	}

	/**
	 * @param leftPadModel
	 *            the leftPadModel to set
	 */
	public void setLeftPadModel(PadModel leftPadModel) {
		this.leftPadModel = leftPadModel;
	}

	/**
	 * @return the centerPadModel
	 */
	public PadModel getCenterPadModel() {
		return centerPadModel;
	}

	/**
	 * @param centerPadModel
	 *            the centerPadModel to set
	 */
	public void setCenterPadModel(PadModel centerPadModel) {
		this.centerPadModel = centerPadModel;
	}

	/**
	 * @return the rightPadModel
	 */
	public PadModel getRightPadModel() {
		return rightPadModel;
	}

	/**
	 * @param rightPadModel
	 *            the rightPadModel to set
	 */
	public void setRightPadModel(PadModel rightPadModel) {
		this.rightPadModel = rightPadModel;
	}

	/**
	 * @return the allPadModel
	 */
	public PadModel getAllPadModel() {
		return allPadModel;
	}

	/**
	 * @param allPadModel
	 *            the allPadModel to set
	 */
	public void setAllPadModel(PadModel allPadModel) {
		this.allPadModel = allPadModel;
	}

	/**
	 * Get padModel by pad.
	 * 
	 * @param pad
	 *            pad
	 * @return the PadModel for this pad
	 */
	public PadModel getPadModel(final PadEnum pad) {
		PadModel mod = null;
		switch (pad) {
		case LEFT:
			mod = leftPadModel;
			break;
		case CENTER:
			mod = centerPadModel;
			break;
		case RIGHT:
			mod = rightPadModel;
			break;
		case ALL:
			mod = allPadModel;
			break;
		default:
			break;
		}
		return mod;
	}

	public final StringProperty titleProperty() {
		return this.title;
	}

	public final java.lang.String getTitle() {
		return this.titleProperty().get();
	}

	public final void setTitle(final java.lang.String title) {
		this.titleProperty().set(title);
	}

	public final IntegerProperty numberProperty() {
		return this.number;
	}

	public final int getNumber() {
		return this.numberProperty().get();
	}

	public final void setNumber(final int number) {
		this.numberProperty().set(number);
	}

	public final StringProperty usbIdProperty() {
		return this.usbId;
	}

	public final java.lang.String getUsbId() {
		return this.usbIdProperty().get();
	}

	public final void setUsbId(final java.lang.String usbId) {
		this.usbIdProperty().set(usbId);
	}

}
