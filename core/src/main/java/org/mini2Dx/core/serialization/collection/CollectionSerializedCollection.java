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

import java.util.Collection;

/**
 * Utility class used during JSON/XML serialization
 */
public class CollectionSerializedCollection extends SerializedCollection<Collection> {
	private Object [] values;

	public CollectionSerializedCollection(Collection collection) {
		super(collection);
		values = collection.toArray();
	}

	@Override
	public Object get(int index) {
		return values[index];
	}

	@Override
	public int getLength() {
		return collection.size();
	}

	@Override
	public void dispose() {
		values = null;
	}
}
