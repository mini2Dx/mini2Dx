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
package org.mini2Dx.core.util;

import org.mini2Dx.gdx.utils.LongArray;

import java.util.Iterator;

public class LongArrayIterable implements Iterable {
	private final LongArray longArray;

	public LongArrayIterable(LongArray longArray) {
		super();
		this.longArray = longArray;
	}

	@Override
	public Iterator iterator() {
		return new Iterator() {
			int index;

			@Override
			public boolean hasNext() {
				return index < longArray.size;
			}

			@Override
			public Object next() {
				final long result = longArray.get(index);
				index++;
				return result;
			}

			@Override
			public void remove() {
				longArray.removeIndex(index);
				index--;
			}
		};
	}
}
