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
package org.mini2Dx.ui.layout;

import org.mini2Dx.ui.element.UiElement;

/**
 * An implementation of {@link OffsetRule} that changes the offset of the current
 * {@link UiElement} based on the size of the parent {@link UiElement}. The
 * parent element is split into 12 columns and the {@link ResponsiveOffsetRule}
 * sets how many columns to offset by.
 */
public class ResponsiveOffsetRule implements OffsetRule {
	private final int columns;
	
	/**
	 * Constructor
	 * @param columns The offset in columns
	 */
	public ResponsiveOffsetRule(int columns) {
		this.columns = columns;
	}

	@Override
	public float getOffset(LayoutState layoutState) {
		return layoutState.getColumnWidth(columns);
	}
}
