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
import org.dajlab.jtoypad.core.Tag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 * Pad model.
 * 
 * @author Erik Amzallag
 *
 */
public class PadModel {

	/**
	 * Toypad number.
	 */
	private IntegerProperty toyPadNumber = new SimpleIntegerProperty();
	/**
	 * Pad.
	 */
	private PadEnum pad;

	/**
	 * Activ command.
	 */
	private ObjectProperty<CommandEnum> activeCommand = new SimpleObjectProperty<CommandEnum>(this, "activeCommand",
			CommandEnum.OFF);

	/**
	 * Selected color for On.
	 */
	private ObjectProperty<Color> colorOn = new SimpleObjectProperty<Color>(this, "colorOn", Color.BLACK);

	/**
	 * Selected color for flash on.
	 */
	private ObjectProperty<Color> colorFlashOn = new SimpleObjectProperty<Color>(this, "colorFlashOn", Color.BLACK);
	/**
	 * Selected color for flash off.
	 */
	private ObjectProperty<Color> colorFlashOff = new SimpleObjectProperty<Color>(this, "colorFlashOff", Color.BLACK);
	/**
	 * Time flash on.
	 */
	private IntegerProperty timeFlashOn = new SimpleIntegerProperty(this, "timeFlashOn", 5);
	/**
	 * Time flash off.
	 */
	private IntegerProperty timeFlashOff = new SimpleIntegerProperty(this, "timeFlashOff", 5);

	/**
	 * Selected color 1 for fade.
	 */
	private ObjectProperty<Color> color1Fade = new SimpleObjectProperty<Color>(this, "color1Fade", Color.BLACK);
	/**
	 * Selected color 2 for fade.
	 */
	private ObjectProperty<Color> color2Fade = new SimpleObjectProperty<Color>(this, "color2Fade", Color.BLACK);
	/**
	 * Time fade.
	 */
	private IntegerProperty timeFade = new SimpleIntegerProperty(this, "timeFade", 5);

	/**
	 * Time random fade.
	 */
	private IntegerProperty timeRandomFade = new SimpleIntegerProperty(this, "timeRandomFade", 5);

	/**
	 * List of tags associated to this pad.
	 */
	private ObservableList<Tag> tagsList = FXCollections.observableArrayList();

	/**
	 * Private constructor.
	 */
	private PadModel() {

	}

	/**
	 * Constructor.
	 * 
	 * @param pad
	 *            pad
	 */
	public PadModel(final PadEnum pad) {
		this.pad = pad;
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

	public final ObjectProperty<CommandEnum> activeCommandProperty() {
		return this.activeCommand;
	}

	public final CommandEnum getActiveCommand() {
		return this.activeCommandProperty().get();
	}

	public final void setActiveCommand(final CommandEnum activeCommand) {
		this.activeCommandProperty().set(activeCommand);
	}

	public final IntegerProperty timeFlashOnProperty() {
		return this.timeFlashOn;
	}

	public final int getTimeFlashOn() {
		return this.timeFlashOnProperty().get();
	}

	public final void setTimeFlashOn(final int timeFlashOn) {
		this.timeFlashOnProperty().set(timeFlashOn);
	}

	public final IntegerProperty timeFlashOffProperty() {
		return this.timeFlashOff;
	}

	public final int getTimeFlashOff() {
		return this.timeFlashOffProperty().get();
	}

	public final void setTimeFlashOff(final int timeFlashOff) {
		this.timeFlashOffProperty().set(timeFlashOff);
	}

	public final IntegerProperty timeFadeProperty() {
		return this.timeFade;
	}

	public final int getTimeFade() {
		return this.timeFadeProperty().get();
	}

	public final void setTimeFade(final int timeFade) {
		this.timeFadeProperty().set(timeFade);
	}

	public final IntegerProperty timeRandomFadeProperty() {
		return this.timeRandomFade;
	}

	public final int getTimeRandomFade() {
		return this.timeRandomFadeProperty().get();
	}

	public final void setTimeRandomFade(final int timeRandomFade) {
		this.timeRandomFadeProperty().set(timeRandomFade);
	}

	public final ObjectProperty<Color> colorOnProperty() {
		return this.colorOn;
	}

	public final Color getColorOn() {
		return this.colorOnProperty().get();
	}

	public final void setColorOn(final Color colorOn) {
		this.colorOnProperty().set(colorOn);
	}

	/**
	 * @return the tags
	 */
	public ObservableList<Tag> getTagsList() {
		return tagsList;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTagsList(ObservableList<Tag> tags) {
		this.tagsList = tags;
	}

	public final ObjectProperty<Color> colorFlashOnProperty() {
		return this.colorFlashOn;
	}

	public final Color getColorFlashOn() {
		return this.colorFlashOnProperty().get();
	}

	public final void setColorFlashOn(final Color colorFlashOn) {
		this.colorFlashOnProperty().set(colorFlashOn);
	}

	public final ObjectProperty<Color> colorFlashOffProperty() {
		return this.colorFlashOff;
	}

	public final Color getColorFlashOff() {
		return this.colorFlashOffProperty().get();
	}

	public final void setColorFlashOff(final Color colorFlashOff) {
		this.colorFlashOffProperty().set(colorFlashOff);
	}

	public final ObjectProperty<Color> color1FadeProperty() {
		return this.color1Fade;
	}

	public final Color getColor1Fade() {
		return this.color1FadeProperty().get();
	}

	public final void setColor1Fade(final Color color1Fade) {
		this.color1FadeProperty().set(color1Fade);
	}

	public final ObjectProperty<Color> color2FadeProperty() {
		return this.color2Fade;
	}

	public final Color getColor2Fade() {
		return this.color2FadeProperty().get();
	}

	public final void setColor2Fade(final Color color2Fade) {
		this.color2FadeProperty().set(color2Fade);
	}

	public final IntegerProperty toyPadNumberProperty() {
		return this.toyPadNumber;
	}

	public final int getToyPadNumber() {
		return this.toyPadNumberProperty().get();
	}

	public final void setToyPadNumber(final int toyPadNumber) {
		this.toyPadNumberProperty().set(toyPadNumber);
	}

}
