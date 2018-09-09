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
package org.dajlab.jtoypad.gui.view;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

/**
 * Time spinner.
 * 
 * @author Erik Amzallag
 *
 */
public class TimeSpinner extends Spinner<Integer> {

	/**
	 * Add a binding IntegerProperty to the default valueFactory of the spinner.
	 */
	final private IntegerProperty valueObjectProperty = new SimpleIntegerProperty(this, "time", 0);

	/**
	 * Constructor.
	 */
	public TimeSpinner() {

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Integer.MAX_VALUE, 10, 1);
		setValueFactory(valueFactory);
		setPrefSize(80, 25);
		getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		setEditable(true);
		getValueFactory().valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable obs) {

				Integer tin = getValueFactory().valueProperty().get();
				if (tin == null) {
					valueObjectProperty.set(10);
				} else {
					valueObjectProperty.set(tin.intValue());
				}
			}
		});
		valueObjectProperty.addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				getValueFactory().valueProperty().setValue(valueObjectProperty.get());

			}
		});

		// Redefine setOnAction from super() to prevent NumberFormatException
		getEditor().setOnAction(action -> {
			String text = getEditor().getText();
			if (StringUtils.isNumeric(text)) {
				if (getValueFactory() != null) {
					StringConverter<Integer> converter = getValueFactory().getConverter();
					if (converter != null) {

						Integer value = converter.fromString(text);
						getValueFactory().setValue(value);
					}
				}
			}
		});
	}

	/**
	 * {@inheritDoc} Redefines the method to prevent a NumberFormatException
	 * when incrementing an alpha value.
	 */
	public void increment(int steps) {
		SpinnerValueFactory<Integer> valueFactory = getValueFactory();
		if (valueFactory == null) {
			throw new IllegalStateException("Can't increment Spinner with a null SpinnerValueFactory");
		}
		commitEditorText();
		valueFactory.increment(steps);
	}

	/**
	 * {@inheritDoc} Redefines the method to prevent a NumberFormatException
	 * when decrementing an alpha value.
	 */
	public void decrement(int steps) {
		SpinnerValueFactory<Integer> valueFactory = getValueFactory();
		if (valueFactory == null) {
			throw new IllegalStateException("Can't decrement Spinner with a null SpinnerValueFactory");
		}
		commitEditorText();
		valueFactory.decrement(steps);
	}

	/**
	 * {@inheritDoc} Redefines the method to prevent a NumberFormatException
	 * when incrementing an alpha value.
	 */
	private void commitEditorText() {
		if (!isEditable())
			return;
		String text = getEditor().getText();
		if (StringUtils.isNumeric(text)) {
			SpinnerValueFactory<Integer> valueFactory = getValueFactory();
			if (valueFactory != null) {
				StringConverter<Integer> converter = valueFactory.getConverter();
				if (converter != null) {
					Integer value = converter.fromString(text);
					valueFactory.setValue(value);
				}
			}
		}
	}

	public IntegerProperty timeProperty() {
		return this.valueObjectProperty;
	}

	public Integer getTime() {
		return this.valueObjectProperty.getValue();
	}

	public void setTime(Integer value) {
		this.valueObjectProperty.set(value);
	}

}
