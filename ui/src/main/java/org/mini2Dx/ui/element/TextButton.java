/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
