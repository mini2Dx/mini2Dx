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
package org.mini2Dx.core.collections;

import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.IntSet;

public class FreeArrayTest {
	private final FreeArray<String> array = new FreeArray<>();

	@Test
	public void testAdd() {
		for(int i = 0; i < 100; i++) {
			final int index = array.add(String.valueOf(i));
			Assert.assertEquals(i, index);
			Assert.assertEquals(i + 1, array.totalItems);
		}
		Assert.assertEquals(100, array.totalItems);
		Assert.assertEquals(100, array.length);

		for(int i = 0; i < 100; i++) {
			Assert.assertNotNull(array.items[i]);
			Assert.assertEquals(String.valueOf(i), array.items[i].element);
		}
	}

	@Test
	public void testRemoveSequential() {
		for(int i = 0; i < 100; i++) {
			array.add(String.valueOf(i));
		}
		Assert.assertEquals(100, array.length);

		for(int i = 99; i >= 0; i--) {
			array.remove(i);
			Assert.assertEquals(i, array.nextFreeIndex);
		}
		Assert.assertEquals(100, array.length);
		Assert.assertEquals(0, array.totalItems);

		for(int i = 0; i < 100; i++) {
			Assert.assertNotNull(array.items[i]);
			Assert.assertNull(array.items[i].element);
		}
	}

	@Test
	public void testRemoveRandom() {
		for(int i = 0; i < 100; i++) {
			array.add(String.valueOf(i));
		}

		final IntSet removedElements = new IntSet();
		while(array.totalItems > 0) {
			final int index = MathUtils.random(0, 99);
			if(!removedElements.add(index)) {
				continue;
			}
			array.remove(index);
			Assert.assertEquals(100, array.length);
			Assert.assertEquals(100 - removedElements.size, array.totalItems);
		}

		for(int i = 0; i < 100; i++) {
			Assert.assertNotNull(array.items[i]);
			Assert.assertNull(array.items[i].element);
		}
	}

	@Test
	public void testClear() {
		for(int i = 0; i < 100; i++) {
			array.add(String.valueOf(i));
		}
		array.clear();
		Assert.assertEquals(0, array.length);
		Assert.assertEquals(0, array.totalItems);
		Assert.assertEquals(-1, array.nextFreeIndex);
	}

	@Test
	public void testReAddMiddle() {
		Assert.assertEquals(0, array.add("0"));
		Assert.assertEquals(1, array.add("1"));
		Assert.assertEquals(2, array.add("2"));
		Assert.assertEquals(3, array.add("3"));

		Assert.assertNotNull(array.remove(1));
		Assert.assertEquals(1, array.nextFreeIndex);

		array.add("4");
		Assert.assertEquals("4", array.items[1].element);

		array.add("5");
		Assert.assertEquals("5", array.items[4].element);
	}
}
