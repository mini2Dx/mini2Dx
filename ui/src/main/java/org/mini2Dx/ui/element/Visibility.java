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

/**
 * The visibility mode for a {@link UiElement}
 */
public enum Visibility {
	/**
	 * Element will be included in layout and rendering
	 */
	VISIBLE,
	/**
	 * Element will be excluded from layout and rendering
	 */
	HIDDEN,
	/**
	 * Element will be included in layout but not rendered
	 */
	NO_RENDER
}
