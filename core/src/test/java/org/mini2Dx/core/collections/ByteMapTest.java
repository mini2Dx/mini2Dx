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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ByteMapTest {
    private ByteMap<String> byteMap;

    @Before
    public void setup(){
        byteMap = new ByteMap<>();
    }

    @Test
    public void testAdd(){
        Assert.assertEquals(0, byteMap.size);
        byteMap.put((byte) 123, "Example123");
        Assert.assertEquals(1, byteMap.size);
        byteMap.put((byte) 34, "Example34");
        Assert.assertEquals(2, byteMap.size);
        byteMap.put((byte) 0, "Example0");
        Assert.assertEquals(3, byteMap.size);
        Assert.assertEquals("Example123", byteMap.put((byte) 123, "NewExample123"));
        Assert.assertEquals(3, byteMap.size);
    }

    @Test
    public void testGet(){
        testAdd();
        Assert.assertNull(byteMap.get((byte) 100));
        Assert.assertEquals("Example0", byteMap.get((byte) 0));
        Assert.assertEquals("NewExample123", byteMap.get((byte) 123));
        Assert.assertEquals("Example34", byteMap.get((byte) 34));
        Assert.assertEquals("Example34", byteMap.get((byte) 34, "DefaultValue"));
        Assert.assertEquals("DefaultValue", byteMap.get((byte) 56, "DefaultValue"));
        Assert.assertEquals(3, byteMap.size);
    }

    @Test
    public void testRemove(){
        testAdd();
        Assert.assertNull(byteMap.remove((byte) 100));
        Assert.assertEquals("NewExample123", byteMap.remove((byte) 123));
        Assert.assertEquals(2, byteMap.size);
        Assert.assertEquals("Example34", byteMap.remove((byte) 34));
        Assert.assertEquals(1, byteMap.size);
        Assert.assertEquals("Example0", byteMap.remove((byte) 0));
        Assert.assertEquals(0, byteMap.size);
    }

    @Test
    public void testClear(){
        testAdd();
        byteMap.clear();
        Assert.assertEquals(0, byteMap.size);
    }

    @Test
    public void testContainsKey(){
        Assert.assertFalse(byteMap.containsKey((byte) 0));
        Assert.assertFalse(byteMap.containsKey((byte) 34));
        Assert.assertFalse(byteMap.containsKey((byte) 123));
        Assert.assertFalse(byteMap.containsKey((byte) 127));
        testAdd();
        Assert.assertTrue(byteMap.containsKey((byte) 0));
        Assert.assertTrue(byteMap.containsKey((byte) 34));
        Assert.assertTrue(byteMap.containsKey((byte) 123));
        Assert.assertFalse(byteMap.containsKey((byte) 127));
        byteMap.clear();
        Assert.assertFalse(byteMap.containsKey((byte) 0));
        Assert.assertFalse(byteMap.containsKey((byte) 34));
        Assert.assertFalse(byteMap.containsKey((byte) 123));
        Assert.assertFalse(byteMap.containsKey((byte) 127));
    }

    @Test
    public void testContainsValue(){
        Assert.assertFalse(byteMap.containsValue("Example0", false));
        Assert.assertFalse(byteMap.containsValue("Example34", false));
        Assert.assertFalse(byteMap.containsValue("NewExample123", false));
        Assert.assertFalse(byteMap.containsValue("ThisValueShouldNotBePresent", false));
        testAdd();
        Assert.assertTrue(byteMap.containsValue("Example0", false));
        Assert.assertTrue(byteMap.containsValue("Example34", false));
        Assert.assertTrue(byteMap.containsValue("NewExample123", false));
        Assert.assertFalse(byteMap.containsValue("ThisValueShouldNotBePresent", false));
        byteMap.clear();
        Assert.assertFalse(byteMap.containsValue("Example0", false));
        Assert.assertFalse(byteMap.containsValue("Example34", false));
        Assert.assertFalse(byteMap.containsValue("NewExample123", false));
        Assert.assertFalse(byteMap.containsValue("ThisValueShouldNotBePresent", false));
    }

    @Test
    public void testFindKey(){
        Assert.assertEquals(127, byteMap.findKey("Example0", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("Example34", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("NewExample123", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("ThisValueShouldNotBePresent", false, (byte) 127));
        testAdd();
        Assert.assertEquals(0, byteMap.findKey("Example0", false, (byte) 127));
        Assert.assertEquals(34, byteMap.findKey("Example34", false, (byte) 127));
        Assert.assertEquals(123, byteMap.findKey("NewExample123", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("ThisValueShouldNotBePresent", false, (byte) 127));
        byteMap.clear();
        Assert.assertEquals(127, byteMap.findKey("Example0", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("Example34", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("NewExample123", false, (byte) 127));
        Assert.assertEquals(127, byteMap.findKey("ThisValueShouldNotBePresent", false, (byte) 127));
    }
}
