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

public class ByteTreeMapTest {

	@Test
	public void testPutRemove() {
		final byte key = 2;

		final ByteTreeMap<String> byteTreeMap = new ByteTreeMap<String>();
		Assert.assertEquals(false, byteTreeMap.containsKey(key));

		final String result1 = byteTreeMap.put(key, "Example 1");
		Assert.assertEquals(true, byteTreeMap.containsKey(key));
		Assert.assertNull(result1);

		final String result2 = byteTreeMap.put(key, "Example 2");
		Assert.assertEquals(true, byteTreeMap.containsKey(key));
		Assert.assertEquals("Example 1", result2);

		byteTreeMap.remove(key);
		Assert.assertEquals(false, byteTreeMap.containsKey(key));
	}

	@Test
	public void testIterator() {
		final TreeMap<Byte, String> treeMap = new TreeMap<Byte, String>();
		final ByteTreeMap<String> byteTreeMap = new ByteTreeMap<String>();

		put(treeMap, byteTreeMap, (byte) 2, "Example 2");
		put(treeMap, byteTreeMap, (byte) -1, "Example -1");
		put(treeMap, byteTreeMap, (byte) 0, "Example 0");
		put(treeMap, byteTreeMap, (byte) 9, "Example 9");
		put(treeMap, byteTreeMap, (byte) 3, "Example 3");
		put(treeMap, byteTreeMap, (byte) 3, "Example 3");
		put(treeMap, byteTreeMap, (byte) -7, "Example -7");

		for(int i = 0; i < 4; i++) {
			ByteMap.Keys ascKeys = byteTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(byte key : treeMap.keySet()) {
				final byte result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			ByteMap.Keys descKeys = byteTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(byte key : treeMap.descendingKeySet()) {
				final byte result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		remove(treeMap, byteTreeMap, (byte) 2);
		remove(treeMap, byteTreeMap, (byte) -1);

		for(int i = 0; i < 4; i++) {
			ByteMap.Keys ascKeys = byteTreeMap.ascendingKeys();
			ascKeys.reset();
			Assert.assertEquals(true, ascKeys.hasNext);
			for(byte key : treeMap.keySet()) {
				final byte result = ascKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, ascKeys.hasNext);
		}
		for(int i = 0; i < 4; i++) {
			ByteMap.Keys descKeys = byteTreeMap.descendingKeys();
			descKeys.reset();
			Assert.assertEquals(true, descKeys.hasNext);
			for(byte key : treeMap.descendingKeySet()) {
				final byte result = descKeys.next();
				Assert.assertEquals(key, result);
			}
			Assert.assertEquals(false, descKeys.hasNext);
		}

		clear(treeMap, byteTreeMap);

		ByteMap.Keys ascKeys = byteTreeMap.ascendingKeys();
		ascKeys.reset();
		Assert.assertEquals(false, ascKeys.hasNext);

		ByteMap.Keys descKeys = byteTreeMap.descendingKeys();
		descKeys.reset();
		Assert.assertEquals(false, descKeys.hasNext);
	}

	private void put(TreeMap<Byte, String> treeMap, ByteTreeMap<String> byteTreeMap, byte key, String value) {
		treeMap.put(key, value);
		byteTreeMap.put(key, value);
	}

	private void remove(TreeMap<Byte, String> treeMap, ByteTreeMap<String> byteTreeMap, byte key) {
		treeMap.remove(key);
		byteTreeMap.remove(key);
	}

	private void clear(TreeMap<Byte, String> treeMap, ByteTreeMap<String> byteTreeMap) {
		treeMap.clear();
		byteTreeMap.clear();
	}
}
