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
 * Common interface for {@link UiElement} width rules
 */
public interface SizeRule {
	/**
	 * Determines the preferred size of a {@link UiElement}
	 * 
	 * @param layoutState The current {@link LayoutState}
	 * @return The {@link UiElement} preferred size
	 */
	public float getSize(LayoutState layoutState);
	
	/**
	 * Returns if the size rule is an 'auto' rule
	 * @return True if set to 'auto'
	 */
	public boolean isAutoSize();
}
