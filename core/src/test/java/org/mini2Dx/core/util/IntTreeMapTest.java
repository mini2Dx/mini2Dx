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

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.gdx.utils.IntMap;

import java.util.TreeMap;

public class IntTreeMapTest {

	@Test
	public void testPutRemove() {
		final int key = 2;

		final IntTreeMap<String> intTreeMap = new IntTreeMap<String>();
		Assert.assertEquals(false, intTreeMap.containsKey(key));

		final String result1 = intTreeMap.put(key, "Example 1");
		Assert.assertEquals(true, intTreeMap.containsKey(key));
		Assert.assertNull(result1);

		final String result2 = intTreeMap.put(key, "Example 2");
		Assert.assertEquals(true, intTreeMap.containsKey(key));
		Assert.assertEquals("Example 1", result2);

		intTreeMap.remove(key);
		Assert.assertEquals(false, intTreeMap.containsKey(key));
	}

	@Test
	public void testIterator() {
		final TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
		final IntTreeMap<String> intTreeMap = new IntTreeMap<String>();

		put(treeMap, intTreeMap, 2, "Example 2");
		put(treeMap, intTreeMap, -1, "Example -1");
		put(treeMap, intTreeMap, 0, "Example 0");
		put(treeMap, intTreeMap, 9, "Example 9");
		put(treeMap, intTreeMap, 3, "Example 3");
		put(treeMap, intTreeMap, 3, "Example 3");
		put(treeMap, intTreeMap, -7, "Example -7");

		for(int i = 0; i < 4; i++) {
			IntMap.Keys ascKeys = intTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(int key : treeMap.keySet()) {
				final int result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			IntMap.Keys descKeys = intTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(int key : treeMap.descendingKeySet()) {
				final int result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		remove(treeMap, intTreeMap, 2);
		remove(treeMap, intTreeMap, -1);

		for(int i = 0; i < 4; i++) {
			IntMap.Keys ascKeys = intTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(int key : treeMap.keySet()) {
				final int result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			IntMap.Keys descKeys = intTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(int key : treeMap.descendingKeySet()) {
				final int result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		clear(treeMap, intTreeMap);

		IntMap.Keys ascKeys = intTreeMap.ascendingKeys();
		ascKeys.reset();
		Assert.assertEquals(false, ascKeys.hasNext);

		IntMap.Keys descKeys = intTreeMap.descendingKeys();
		descKeys.reset();
		Assert.assertEquals(false, descKeys.hasNext);
	}

	private void put(TreeMap<Integer, String> treeMap, IntTreeMap intTreeMap, int key, String value) {
		treeMap.put(key, value);
		intTreeMap.put(key, value);
	}

	private void remove(TreeMap<Integer, String> treeMap, IntTreeMap intTreeMap, int key) {
		treeMap.remove(key);
		intTreeMap.remove(key);
	}

	private void clear(TreeMap<Integer, String> treeMap, IntTreeMap intTreeMap) {
		treeMap.clear();
		intTreeMap.clear();
	}
}
