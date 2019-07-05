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
package org.mini2Dx.core.serialization.collection;

import org.mini2Dx.gdx.utils.IntArray;
import org.mini2Dx.gdx.utils.IntSet;

/**
 * Utility class used during JSON/XML serialization
 */
public class IntSetSerializedCollection extends SerializedCollection<IntSet> {
	private final IntArray intArray = new IntArray();

	public IntSetSerializedCollection(IntSet collection) {
		super(collection);
		final IntSet.IntSetIterator intSetIterator = collection.iterator();
		while(intSetIterator.hasNext) {
			intArray.add(intSetIterator.next());
		}
	}

	@Override
	public Object get(int index) {
		return intArray.get(index);
	}

	@Override
	public int getLength() {
		return collection.size;
	}

	@Override
	public void dispose() {
		intArray.clear();
	}
}
