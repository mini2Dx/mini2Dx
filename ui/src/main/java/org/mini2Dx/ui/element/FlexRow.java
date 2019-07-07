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
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.render.DivRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 * Utility {@link Div} class pre-set with flex layout "flex-column:xs-12c"
 */
public class FlexRow extends Div {
	/**
	 * Constructor. Generates a unique ID for this {@link FlexRow}
	 */
	public FlexRow() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param id The unique ID for this {@link FlexRow}
	 */
	public FlexRow(@ConstructorArg(clazz=String.class, name = "id") String id) {
		super(id);
		setFlexLayout("flex-column:xs-12c");
	}

	/**
	 * Sets the {@link FlexDirection} of this {@link FlexRow}
	 * @param flexDirection The flex direction
	 */
	public void setFlexDirection(FlexDirection flexDirection) {
		if(flexDirection == null) {
			return;
		}
		switch(flexDirection) {
		case COLUMN:
			setFlexLayout("flex-column:xs-12c");
			break;
		case COLUMN_REVERSE:
			setFlexLayout("flex-column-reverse:xs-12c");
			break;
		case ROW:
			setFlexLayout("flex-row:xs-12c");
			break;
		case ROW_REVERSE:
			setFlexLayout("flex-row-reverse:xs-12c");
			break;
		case CENTER:
			setFlexLayout("flex-center:xs-12c");
			break;
		}
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new DivRenderNode(parent, this);
	}
	
	/**
	 * Creates a {@link Visibility#VISIBLE} {@link FlexRow} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param elements The {@link UiElement}s to add to the {@link FlexRow}
	 * @return A new {@link FlexRow} containing the {@link UiElement}s
	 */
	public static FlexRow withElements(UiElement ...elements) {
		return withElements(null, elements);
	}
	
	/**
	 * Creates a {@link Visibility#VISIBLE} {@link FlexRow} containing the
	 * specified {@link UiElement}s
	 * 
	 * @param rowId The unique ID of the {@link FlexRow}
	 * @param elements The {@link UiElement}s to add to the {@link FlexRow}
	 * @return A new {@link FlexRow} containing the {@link UiElement}s
	 */
	public static FlexRow withElements(String rowId, UiElement ...elements) {
		FlexRow result = new FlexRow(rowId);
		for(int i = 0; i < elements.length; i++) {
			result.add(elements[i]);
		}
		result.setVisibility(Visibility.VISIBLE);
		return result;
	}
}
