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
package org.mini2Dx.ui.listener;

import org.mini2Dx.ui.element.ScrollBox;

/**
 * Common interface for listening to {@link ScrollBox} scrolling
 */
public interface ScrollListener {

	/**
	 * Called when scrolling occurs
	 * 
	 * @param source
	 *            The {@link ScrollBox} that was scrolled
	 * @param scrollThumbPosition
	 *            The position of the top of the scroll thumb (value between 0.0
	 *            and 1.0)
	 */
	public void onScroll(ScrollBox source, float scrollThumbPosition);
}
