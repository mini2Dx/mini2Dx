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
