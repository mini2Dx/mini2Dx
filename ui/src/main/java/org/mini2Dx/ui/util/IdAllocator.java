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
package org.mini2Dx.ui.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.mini2Dx.ui.element.UiElement;

/**
 * Utility class for generating {@link UiElement} identifiers for none is
 * provided to the element
 */
public class IdAllocator {
	private static final String ID_SUFFIX = "::ui-element";
	private static final AtomicInteger NEXT_ID = new AtomicInteger();

	public static String getNextId() {
		return getNextId(ID_SUFFIX);
	}
	
	public static String getNextId(String suffix) {
		return NEXT_ID.incrementAndGet() + suffix;
	}
}
