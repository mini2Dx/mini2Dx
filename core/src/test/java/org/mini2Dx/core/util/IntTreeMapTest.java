/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
