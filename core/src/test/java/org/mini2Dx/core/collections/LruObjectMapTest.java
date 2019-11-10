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

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link LruObjectMap}
 */
public class LruObjectMapTest {
	private static final int MAX_CAPACITY = 64;

	private final LruObjectMap<String, String> map = new LruObjectMap<String, String>(16, MAX_CAPACITY);

	@After
	public void teardown() {
		map.clear();
	}

	@Test
	public void testImmediatePurge() {
		for(int i = 0; i < MAX_CAPACITY; i++) {
			map.put("key" + i, "value" + i);
		}

		Assert.assertEquals(MAX_CAPACITY, map.size);

		for(int i = MAX_CAPACITY; i < MAX_CAPACITY * 2; i++) {
			map.put("key" + i, "value" + i);
			Assert.assertEquals(MAX_CAPACITY, map.size);
		}
	}

	@Test
	public void testLruPurge() {
		for(int i = 0; i < MAX_CAPACITY; i++) {
			map.put("key" + i, "value" + i);
		}

		for(int i = 0; i < MAX_CAPACITY; i++) {
			for(int j = 0; j <= i; j++) {
				map.get("key" + i);
			}
		}

		Assert.assertEquals(MAX_CAPACITY, map.size);
		Assert.assertTrue(map.containsKey("key0"));

		map.put("key" + MAX_CAPACITY, "value" + MAX_CAPACITY);

		Assert.assertEquals(MAX_CAPACITY, map.size);
		Assert.assertFalse(map.containsKey("key0"));
	}
}
