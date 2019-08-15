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

public class ShortMapTest {
    private ShortMap<String> shortMap;

    @Before
    public void setup(){
        shortMap = new ShortMap<>();
    }

    @Test
    public void testAdd(){
        Assert.assertEquals(0, shortMap.size);
        shortMap.put((short) 123, "Example123");
        Assert.assertEquals(1, shortMap.size);
        shortMap.put((short) 34, "Example34");
        Assert.assertEquals(2, shortMap.size);
        shortMap.put((short) 0, "Example0");
        Assert.assertEquals(3, shortMap.size);
        Assert.assertEquals("Example123", shortMap.put((short) 123, "NewExample123"));
        Assert.assertEquals(3, shortMap.size);
    }

    @Test
    public void testGet(){
        testAdd();
        Assert.assertNull(shortMap.get((short) 100));
        Assert.assertEquals("Example0", shortMap.get((short) 0));
        Assert.assertEquals("NewExample123", shortMap.get((short) 123));
        Assert.assertEquals("Example34", shortMap.get((short) 34));
        Assert.assertEquals("Example34", shortMap.get((short) 34, "DefaultValue"));
        Assert.assertEquals("DefaultValue", shortMap.get((short) 56, "DefaultValue"));
        Assert.assertEquals(3, shortMap.size);
    }

    @Test
    public void testRemove(){
        testAdd();
        Assert.assertNull(shortMap.remove((short) 100));
        Assert.assertEquals("NewExample123", shortMap.remove((short) 123));
        Assert.assertEquals(2, shortMap.size);
        Assert.assertEquals("Example34", shortMap.remove((short) 34));
        Assert.assertEquals(1, shortMap.size);
        Assert.assertEquals("Example0", shortMap.remove((short) 0));
        Assert.assertEquals(0, shortMap.size);
    }

    @Test
    public void testClear(){
        testAdd();
        shortMap.clear();
        Assert.assertEquals(0, shortMap.size);
    }

    @Test
    public void testContainsKey(){
        Assert.assertFalse(shortMap.containsKey((short) 0));
        Assert.assertFalse(shortMap.containsKey((short) 34));
        Assert.assertFalse(shortMap.containsKey((short) 123));
        Assert.assertFalse(shortMap.containsKey((short) 200));
        testAdd();
        Assert.assertTrue(shortMap.containsKey((short) 0));
        Assert.assertTrue(shortMap.containsKey((short) 34));
        Assert.assertTrue(shortMap.containsKey((short) 123));
        Assert.assertFalse(shortMap.containsKey((short) 200));
        shortMap.clear();
        Assert.assertFalse(shortMap.containsKey((short) 0));
        Assert.assertFalse(shortMap.containsKey((short) 34));
        Assert.assertFalse(shortMap.containsKey((short) 123));
        Assert.assertFalse(shortMap.containsKey((short) 200));
    }

    @Test
    public void testContainsValue(){
        Assert.assertFalse(shortMap.containsValue("Example0", false));
        Assert.assertFalse(shortMap.containsValue("Example34", false));
        Assert.assertFalse(shortMap.containsValue("NewExample123", false));
        Assert.assertFalse(shortMap.containsValue("ThisValueShouldNotBePresent", false));
        testAdd();
        Assert.assertTrue(shortMap.containsValue("Example0", false));
        Assert.assertTrue(shortMap.containsValue("Example34", false));
        Assert.assertTrue(shortMap.containsValue("NewExample123", false));
        Assert.assertFalse(shortMap.containsValue("ThisValueShouldNotBePresent", false));
        shortMap.clear();
        Assert.assertFalse(shortMap.containsKey((short) 0));
        Assert.assertFalse(shortMap.containsKey((short) 34));
        Assert.assertFalse(shortMap.containsKey((short) 123));
        Assert.assertFalse(shortMap.containsKey((short) 200));
    }

    @Test
    public void testFindKey(){
        Assert.assertEquals(256, shortMap.findKey("Example0", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("Example34", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("NewExample123", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("ThisValueShouldNotBePresent", false, (short) 256));
        testAdd();
        Assert.assertEquals(0, shortMap.findKey("Example0", false, (short) 256));
        Assert.assertEquals(34, shortMap.findKey("Example34", false, (short) 256));
        Assert.assertEquals(123, shortMap.findKey("NewExample123", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("ThisValueShouldNotBePresent", false, (short) 256));
        shortMap.clear();
        Assert.assertEquals(256, shortMap.findKey("Example0", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("Example34", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("NewExample123", false, (short) 256));
        Assert.assertEquals(256, shortMap.findKey("ThisValueShouldNotBePresent", false, (short) 256));
    }
}
