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
package org.mini2Dx.core.collections;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.gdx.utils.LongMap;

import java.util.TreeMap;

public class LongTreeMapTest {

	@Test
	public void testPutRemove() {
		final int key = 2;

		final LongTreeMap<String> longTreeMap = new LongTreeMap<String>();
		Assert.assertEquals(false, longTreeMap.containsKey(key));

		final String result1 = longTreeMap.put(key, "Example 1");
		Assert.assertEquals(true, longTreeMap.containsKey(key));
		Assert.assertNull(result1);

		final String result2 = longTreeMap.put(key, "Example 2");
		Assert.assertEquals(true, longTreeMap.containsKey(key));
		Assert.assertEquals("Example 1", result2);

		longTreeMap.remove(key);
		Assert.assertEquals(false, longTreeMap.containsKey(key));
	}

	@Test
	public void testIterator() {
		final TreeMap<Long, String> treeMap = new TreeMap<Long, String>();
		final LongTreeMap<String> longTreeMap = new LongTreeMap<String>();

		put(treeMap, longTreeMap, 2, "Example 2");
		put(treeMap, longTreeMap, -1, "Example -1");
		put(treeMap, longTreeMap, 0, "Example 0");
		put(treeMap, longTreeMap, 9, "Example 9");
		put(treeMap, longTreeMap, 3, "Example 3");
		put(treeMap, longTreeMap, 3, "Example 3");
		put(treeMap, longTreeMap, -7, "Example -7");

		for(int i = 0; i < 4; i++) {
			LongMap.Keys ascKeys = longTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(long key : treeMap.keySet()) {
				final long result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			LongMap.Keys descKeys = longTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(long key : treeMap.descendingKeySet()) {
				final long result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		remove(treeMap, longTreeMap, 2);
		remove(treeMap, longTreeMap, -1);

		for(int i = 0; i < 4; i++) {
			LongMap.Keys ascKeys = longTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(long key : treeMap.keySet()) {
				final long result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			LongMap.Keys descKeys = longTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(long key : treeMap.descendingKeySet()) {
				final long result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		clear(treeMap, longTreeMap);

		LongMap.Keys ascKeys = longTreeMap.ascendingKeys();
		ascKeys.reset();
		Assert.assertEquals(false, ascKeys.hasNext);

		LongMap.Keys descKeys = longTreeMap.descendingKeys();
		descKeys.reset();
		Assert.assertEquals(false, descKeys.hasNext);
	}

	private void put(TreeMap<Long, String> treeMap, LongTreeMap<String> longTreeMap, long key, String value) {
		treeMap.put(key, value);
		longTreeMap.put(key, value);
	}

	private void remove(TreeMap<Long, String> treeMap, LongTreeMap<String> longTreeMap, long key) {
		treeMap.remove(key);
		longTreeMap.remove(key);
	}

	private void clear(TreeMap<Long, String> treeMap, LongTreeMap<String> longTreeMap) {
		treeMap.clear();
		longTreeMap.clear();
	}
}
