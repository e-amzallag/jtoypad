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

import org.dajlab.core.CommandListener;
import org.dajlab.gui.AbstractDajlabTab;
import org.dajlab.jtoypad.gui.model.ToyPadModel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * Tab for a toypad.<br>
 * A tab is composed by four pad panes.
 * 
 * @author Erik Amzallag
 *
 */
public class ToyPadTab extends AbstractDajlabTab {

	/**
	 * Toypad model.
	 */
	private ToyPadModel model;

	/**
	 * Constructor.
	 * 
	 * @param toyPadModel
	 *            toypad model
	 * @param listener
	 *            command listener
	 */
	public ToyPadTab(ToyPadModel toyPadModel, CommandListener listener) {

		model = toyPadModel;
		textProperty().bind(model.titleProperty());
		Tooltip tooltip = new Tooltip();
		tooltip.textProperty().bind(model.usbIdProperty());

		setTooltip(tooltip);
		setClosable(false);
		enableRenaming("/toypad32.png");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		PadPane paneL = new PadPane(model.getLeftPadModel());
		paneL.addCommandListeners(listener);
		grid.add(paneL, 0, 0);
		PadPane paneC = new PadPane(model.getCenterPadModel());
		paneC.addCommandListeners(listener);
		grid.add(paneC, 1, 0);
		PadPane paneR = new PadPane(model.getRightPadModel());
		paneR.addCommandListeners(listener);
		grid.add(paneR, 2, 0);
		PadPane paneA = new PadPane(model.getAllPadModel());
		paneA.addCommandListeners(listener);
		grid.add(paneA, 3, 0);

		setContent(grid);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTitle(String title) {
		// As the tab title property is bound to the toypad model, we have just
		// to update the model
		model.setTitle(title);
	}

}
