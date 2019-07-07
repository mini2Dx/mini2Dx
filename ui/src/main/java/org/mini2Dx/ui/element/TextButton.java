/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.element;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.layout.HorizontalAlignment;

/**
 * Utility implementation of {@link Button} that contains a {@link Label}
 */
public class TextButton extends Button {
	protected Label label;
	
	/**
	 * Constructor. Generates a unique ID for this {@link TextButton}
	 */
	public TextButton() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param id The unique ID for this {@link TextButton}
	 */
	public TextButton(@ConstructorArg(clazz=String.class, name = "id") String id) {
		this(id, 0f, 0f, 40f, 20f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public TextButton(@ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public TextButton(@ConstructorArg(clazz = String.class, name = "id") String id,
					   @ConstructorArg(clazz = Float.class, name = "x") float x,
					   @ConstructorArg(clazz = Float.class, name = "y") float y,
					   @ConstructorArg(clazz = Float.class, name = "width") float width,
					   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}
	
	private void checkInitialised() {
		if(label != null) {
			return;
		}
		for(int i = 0; i < children.size; i++) {
			if(children.get(i) instanceof Label) {
				label = (Label) children.get(i);
				return;
			}
		}
		
		label = new Label(getId() + "-backingLabel");
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setResponsive(true);
		label.setVisibility(Visibility.VISIBLE);
		add(label);
	}
	
	/**
	 * Returns the text of this {@link TextButton}
	 * @return An empty {@link String} by default
	 */
	public String getText() {
		checkInitialised();
		return label.getText();
	}

	/**
	 * Sets the text of this {@link TextButton}
	 * @param text A non-null {@link String}
	 */
	public void setText(String text) {
		checkInitialised();
		label.setText(text);
	}

	/**
	 * Returns the {@link HorizontalAlignment} of the button's text
	 * @return {@link HorizontalAlignment#CENTER} by default
	 */
	public HorizontalAlignment getTextAlignment() {
		checkInitialised();
		return label.getHorizontalAlignment();
	}

	/**
	 * Sets the {@link HorizontalAlignment} of the button's text
	 * @param textAlignment The text alignment
	 */
	public void setTextAlignment(HorizontalAlignment textAlignment) {
		checkInitialised();
		label.setHorizontalAlignment(textAlignment);
	}

	/**
	 * Returns the backing label for the button
	 * @return
	 */
	public Label getLabel() {
		checkInitialised();
		return label;
	}
}
