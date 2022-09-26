/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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
package org.mini2Dx.core.collision;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntIntMap;

public class QuadElements<T extends CollisionObject> extends Array<T> {
	public final IntIntMap objectIndices = new IntIntMap();

	public QuadElements() {
		super(false, 16, CollisionObject.class);
	}

	@Override
	public void add(T value) {
		super.add(value);
		objectIndices.put(value.getId(), size - 1);
	}

	@Override
	public void add(T value1, T value2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(T value1, T value2, T value3) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(T value1, T value2, T value3, T value4) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAll(T... array) {
		super.addAll(array);
	}

	@Override
	public void addAll(Array<? extends T> array) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAll(T[] array, int start, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAll(Array<? extends T> array, int start, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insert(int index, T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertRange(int index, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Array<? extends T> array, boolean identity) {
		return super.removeAll(array, identity);
	}

	@Override
	public T removeIndex(int index) {
		final T result = super.removeIndex(index);
		objectIndices.remove(result.getId(), -1);
		objectIndices.put(items[index].getId(), index);
		return result;
	}

	@Override
	public boolean removeValue(T value, boolean identity) {
		final int index = objectIndices.remove(value.getId(), -1);
		if(index < 0) {
			return false;
		}
		super.removeIndex(index);
		if(items[index] != null) {
			objectIndices.put(items[index].getId(), index);
		}
		return true;
	}

	@Override
	public void clear() {
		if(size == 0) {
			return;
		}
		super.clear();
		objectIndices.clear();
	}
}
