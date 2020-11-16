/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
 * Interface for toggle elements that have multiple options (e.g. left/right selectors)
 */
public interface MultiToggle extends Actionable {
	/**
	 * Returns the index of the currently selected option
	 *
	 * @return 0 by default
	 */
	int getSelectedIndex();

	/**
	 * Sets the currently selected option
	 *
	 * @param index
	 *            The index to set as selected
	 */
	void setSelectedIndex(int index);

	/**
	 * Returns the total amount of options
	 *
	 * @return 0 if no options have been added
	 */
	int getTotalOptions();

	/**
	 * Changes the current selection to the next available option
	 */
	void nextOption();

	/**
	 * Changes the current selection to the option before the currently selected
	 * option.
	 */
	void previousOption();
}
