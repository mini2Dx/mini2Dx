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

import java.util.TreeMap;

public class ShortTreeMapTest {

	@Test
	public void testPutRemove() {
		final short key = 2;

		final ShortTreeMap<String> shortTreeMap = new ShortTreeMap<String>();
		Assert.assertEquals(false, shortTreeMap.containsKey(key));

		final String result1 = shortTreeMap.put(key, "Example 1");
		Assert.assertEquals(true, shortTreeMap.containsKey(key));
		Assert.assertNull(result1);

		final String result2 = shortTreeMap.put(key, "Example 2");
		Assert.assertEquals(true, shortTreeMap.containsKey(key));
		Assert.assertEquals("Example 1", result2);

		shortTreeMap.remove(key);
		Assert.assertEquals(false, shortTreeMap.containsKey(key));
	}

	@Test
	public void testIterator() {
		final TreeMap<Short, String> treeMap = new TreeMap<Short, String>();
		final ShortTreeMap<String> shortTreeMap = new ShortTreeMap<String>();

		put(treeMap, shortTreeMap, (short) 2, "Example 2");
		put(treeMap, shortTreeMap, (short) -1, "Example -1");
		put(treeMap, shortTreeMap, (short) 0, "Example 0");
		put(treeMap, shortTreeMap, (short) 9, "Example 9");
		put(treeMap, shortTreeMap, (short) 3, "Example 3");
		put(treeMap, shortTreeMap, (short) 3, "Example 3");
		put(treeMap, shortTreeMap, (short) -7, "Example -7");

		for(int i = 0; i < 4; i++) {
			ShortMap.Keys ascKeys = shortTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(short key : treeMap.keySet()) {
				final short result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			ShortMap.Keys descKeys = shortTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(short key : treeMap.descendingKeySet()) {
				final short result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		remove(treeMap, shortTreeMap, (short) 2);
		remove(treeMap, shortTreeMap, (short) -1);

		for(int i = 0; i < 4; i++) {
			ShortMap.Keys ascKeys = shortTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(short key : treeMap.keySet()) {
				final short result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			ShortMap.Keys descKeys = shortTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(short key : treeMap.descendingKeySet()) {
				final short result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		clear(treeMap, shortTreeMap);

		ShortMap.Keys ascKeys = shortTreeMap.ascendingKeys();
		ascKeys.reset();
		Assert.assertEquals(false, ascKeys.hasNext);

		ShortMap.Keys descKeys = shortTreeMap.descendingKeys();
		descKeys.reset();
		Assert.assertEquals(false, descKeys.hasNext);
	}

	private void put(TreeMap<Short, String> treeMap, ShortTreeMap<String> shortTreeMap, short key, String value) {
		treeMap.put(key, value);
		shortTreeMap.put(key, value);
	}

	private void remove(TreeMap<Short, String> treeMap, ShortTreeMap<String> shortTreeMap, short key) {
		treeMap.remove(key);
		shortTreeMap.remove(key);
	}

	private void clear(TreeMap<Short, String> treeMap, ShortTreeMap<String> shortTreeMap) {
		treeMap.clear();
		shortTreeMap.clear();
	}
}
