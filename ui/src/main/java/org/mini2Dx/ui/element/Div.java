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
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.DivRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A division or section containing {@link UiElement}s
 */
public class Div extends ParentUiElement {

	/**
	 * Constructor. Generates a unique ID for this {@link Div}
	 */
	public Div() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link Div}
	 */
	public Div(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 300f, 300f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public Div(@ConstructorArg(clazz = Float.class, name = "x") float x,
			   @ConstructorArg(clazz = Float.class, name = "y") float y,
			   @ConstructorArg(clazz = Float.class, name = "width") float width,
			   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public Div(@ConstructorArg(clazz = String.class, name = "id") String id,
					@ConstructorArg(clazz = Float.class, name = "x") float x,
					@ConstructorArg(clazz = Float.class, name = "y") float y,
					@ConstructorArg(clazz = Float.class, name = "width") float width,
					@ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new DivRenderNode(parent, this);
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getColumnStyleRule(styleId, ScreenSize.XS);
	}
	
	/**
	 * Creates a {@link Visibility#VISIBLE} {@link Div} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param elements The {@link UiElement}s to add to the {@link Div}
	 * @return A new {@link Div} containing the {@link UiElement}s
	 */
	public static Div withElements(UiElement... elements) {
		return withElements(null, elements);
	}

	/**
	 * Creates a {@link Visibility#VISIBLE} {@link Div} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param columnId The unique ID of the {@link Div}
	 * @param elements The {@link UiElement}s to add to the {@link Div}
	 * @return A new {@link Div} containing the {@link UiElement}s
	 */
	public static Div withElements(String columnId, UiElement... elements) {
		Div result = new Div(columnId);

		float maxX = 0f;
		float maxY = 0f;

		for (int i = 0; i < elements.length; i++) {
			result.add(elements[i]);
			maxX = Math.max(maxX, elements[i].getX() + elements[i].getWidth());
			maxY = Math.max(maxY, elements[i].getY() + elements[i].getHeight());
		}
		result.setContentWidth(maxX);
		result.setContentHeight(maxY);
		result.setVisibility(Visibility.VISIBLE);
		return result;
	}
}
