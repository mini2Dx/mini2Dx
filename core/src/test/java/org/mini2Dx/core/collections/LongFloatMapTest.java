/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LongFloatMapTest {
	private LongFloatMap map;

	@Before
	public void setup(){
		map = new LongFloatMap();
	}

	@Test
	public void testAdd(){
		Assert.assertEquals(0, map.size);
		map.put(Long.MIN_VALUE, 100);
		Assert.assertEquals(1, map.size);
		map.put(Long.MIN_VALUE + 1, 101);
		Assert.assertEquals(2, map.size);
		map.put(Long.MIN_VALUE + 2, 102);
		Assert.assertEquals(3, map.size);
		map.put(Long.MIN_VALUE, 103);
		Assert.assertEquals(3, map.size);
	}

	@Test
	public void testGet(){
		testAdd();
		Assert.assertEquals(1, map.get(100, 1), 0f);
		Assert.assertEquals(103, map.get(Long.MIN_VALUE, -1), 0f);
		Assert.assertEquals(101, map.get(Long.MIN_VALUE + 1, -1), 0f);
		Assert.assertEquals(102, map.get(Long.MIN_VALUE + 2, -1), 0f);
		Assert.assertEquals(102, map.get(Long.MIN_VALUE + 2, -1), 0f);
		Assert.assertEquals(-1, map.get(Long.MAX_VALUE, -1), 0f);
		Assert.assertEquals(3, map.size);
	}

	@Test
	public void testRemove(){
		testAdd();
		Assert.assertEquals(-1, map.remove(100, -1), 0f);
		Assert.assertEquals(103, map.remove(Long.MIN_VALUE, -1), 0f);
		Assert.assertEquals(2, map.size);
		Assert.assertEquals(101, map.remove(Long.MIN_VALUE + 1, -1), 0f);
		Assert.assertEquals(1, map.size);
		Assert.assertEquals(102, map.remove(Long.MIN_VALUE + 2, -1), 0f);
		Assert.assertEquals(0, map.size);
		Assert.assertEquals(-1, map.remove(0, -1), 0f);
	}

	@Test
	public void testClear(){
		testAdd();
		map.clear();
		Assert.assertEquals(0, map.size);
		Assert.assertEquals(-1, map.remove(0, -1), 0f);
	}

	@Test
	public void testContainsKey(){
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE));
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE + 1));
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE + 2));
		Assert.assertFalse(map.containsKey(127));
		testAdd();
		Assert.assertTrue(map.containsKey(Long.MIN_VALUE));
		Assert.assertTrue(map.containsKey(Long.MIN_VALUE + 1));
		Assert.assertTrue(map.containsKey(Long.MIN_VALUE + 2));
		Assert.assertFalse(map.containsKey(127));
		map.clear();
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE));
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE + 1));
		Assert.assertFalse(map.containsKey(Long.MIN_VALUE + 2));
		Assert.assertFalse(map.containsKey(127));
	}

	@Test
	public void testContainsValue(){
		Assert.assertFalse(map.containsValue(101));
		Assert.assertFalse(map.containsValue(102));
		Assert.assertFalse(map.containsValue(103));
		Assert.assertFalse(map.containsValue(Integer.MAX_VALUE));
		testAdd();
		Assert.assertTrue(map.containsValue(101));
		Assert.assertTrue(map.containsValue(102));
		Assert.assertTrue(map.containsValue(103));
		Assert.assertFalse(map.containsValue(Integer.MAX_VALUE));
		map.clear();
		Assert.assertFalse(map.containsValue(101));
		Assert.assertFalse(map.containsValue(102));
		Assert.assertFalse(map.containsValue(103));
		Assert.assertFalse(map.containsValue(Integer.MAX_VALUE));
	}

	@Test
	public void testFindKey(){
		Assert.assertEquals(-1, map.findKey(101, -1));
		Assert.assertEquals(-1, map.findKey(102, -1));
		Assert.assertEquals(-1, map.findKey(103, -1));
		Assert.assertEquals(-1, map.findKey(Integer.MAX_VALUE, -1));
		testAdd();
		Assert.assertEquals(Long.MIN_VALUE + 1, map.findKey(101, -1));
		Assert.assertEquals(Long.MIN_VALUE + 2, map.findKey(102, -1));
		Assert.assertEquals(Long.MIN_VALUE, map.findKey(103, -1));
		Assert.assertEquals(-1, map.findKey(Integer.MAX_VALUE,  -1));
		map.clear();
		Assert.assertEquals(-1, map.findKey(101, -1));
		Assert.assertEquals(-1, map.findKey(102, -1));
		Assert.assertEquals(-1, map.findKey(103, -1));
		Assert.assertEquals(-1, map.findKey(Integer.MAX_VALUE, -1));
	}
}
