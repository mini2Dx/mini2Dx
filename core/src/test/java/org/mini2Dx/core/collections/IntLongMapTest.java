/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntLongMapTest {
	private IntLongMap map;

	@Before
	public void setup(){
		map = new IntLongMap();
	}

	@Test
	public void testAdd(){
		Assert.assertEquals(0, map.size);
		map.put(123, Long.MIN_VALUE);
		Assert.assertEquals(1, map.size);
		map.put(34, Long.MIN_VALUE + 1);
		Assert.assertEquals(2, map.size);
		map.put(0, Long.MIN_VALUE + 2);
		Assert.assertEquals(3, map.size);
		map.put(123, Long.MIN_VALUE + 3);
		Assert.assertEquals(3, map.size);
	}

	@Test
	public void testGet(){
		testAdd();
		Assert.assertEquals(1, map.get(100, 1));
		Assert.assertEquals(Long.MIN_VALUE + 2, map.get(0, -1));
		Assert.assertEquals(Long.MIN_VALUE + 3, map.get(123, -1));
		Assert.assertEquals(Long.MIN_VALUE + 1, map.get(34, -1));
		Assert.assertEquals(Long.MIN_VALUE + 1, map.get(34, -1));
		Assert.assertEquals(-1, map.get(56, -1));
		Assert.assertEquals(3, map.size);
	}

	@Test
	public void testRemove(){
		testAdd();
		Assert.assertEquals(-1, map.remove(100, -1));
		Assert.assertEquals(Long.MIN_VALUE + 3, map.remove(123, -1));
		Assert.assertEquals(2, map.size);
		Assert.assertEquals(Long.MIN_VALUE + 1, map.remove(34, -1));
		Assert.assertEquals(1, map.size);
		Assert.assertEquals(Long.MIN_VALUE + 2, map.remove(0, -1));
		Assert.assertEquals(0, map.size);
		Assert.assertEquals(-1, map.remove(0, -1));
	}

	@Test
	public void testClear(){
		testAdd();
		map.clear();
		Assert.assertEquals(0, map.size);
		Assert.assertEquals(-1, map.remove(0, -1));
	}

	@Test
	public void testContainsKey(){
		Assert.assertFalse(map.containsKey(0));
		Assert.assertFalse(map.containsKey(34));
		Assert.assertFalse(map.containsKey(123));
		Assert.assertFalse(map.containsKey(127));
		testAdd();
		Assert.assertTrue(map.containsKey(0));
		Assert.assertTrue(map.containsKey(34));
		Assert.assertTrue(map.containsKey(123));
		Assert.assertFalse(map.containsKey(127));
		map.clear();
		Assert.assertFalse(map.containsKey(0));
		Assert.assertFalse(map.containsKey(34));
		Assert.assertFalse(map.containsKey(123));
		Assert.assertFalse(map.containsKey(127));
	}

	@Test
	public void testContainsValue(){
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 1));
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 2));
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 3));
		Assert.assertFalse(map.containsValue(Long.MAX_VALUE));
		testAdd();
		Assert.assertTrue(map.containsValue(Long.MIN_VALUE + 1));
		Assert.assertTrue(map.containsValue(Long.MIN_VALUE + 2));
		Assert.assertTrue(map.containsValue(Long.MIN_VALUE + 3));
		Assert.assertFalse(map.containsValue(Long.MAX_VALUE));
		map.clear();
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 1));
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 2));
		Assert.assertFalse(map.containsValue(Long.MIN_VALUE + 3));
		Assert.assertFalse(map.containsValue(Long.MAX_VALUE));
	}

	@Test
	public void testFindKey(){
		Assert.assertEquals(127, map.findKey(0, 127));
		Assert.assertEquals(127, map.findKey(34, 127));
		Assert.assertEquals(127, map.findKey(123, 127));
		Assert.assertEquals(127, map.findKey(Integer.MAX_VALUE, 127));
		testAdd();
		Assert.assertEquals(0, map.findKey(Long.MIN_VALUE + 2, 127));
		Assert.assertEquals(34, map.findKey(Long.MIN_VALUE + 1, 127));
		Assert.assertEquals(123, map.findKey(Long.MIN_VALUE + 3, 127));
		Assert.assertEquals(127, map.findKey(Integer.MAX_VALUE,  127));
		map.clear();
		Assert.assertEquals(127, map.findKey(0, 127));
		Assert.assertEquals(127, map.findKey(34, 127));
		Assert.assertEquals(127, map.findKey(123, 127));
		Assert.assertEquals(127, map.findKey(Integer.MAX_VALUE, 127));
	}
}
