/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.desktop.playerdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.DependencyInjection;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.JsonSerializer;
import org.mini2Dx.core.serialization.XmlSerializer;
import org.mini2Dx.gdx.utils.*;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.desktop.DesktopComponentScanner;
import org.mini2Dx.libgdx.desktop.DesktopPlayerData;
import org.mini2Dx.libgdx.game.GameWrapper;
import org.mini2Dx.core.serialization.dummy.*;

import java.io.File;
import java.util.*;

/**
 * Unit tests for {@link DesktopPlayerData}
 */
public class DesktopPlayerDataTest {
    private static final String TEST_IDENTIFIER = "org.mini2Dx.testgame";
    private static final String XML_FILENAME = "test.xml";
    private static final String JSON_FILENAME = "test.json";
    private static final Random RANDOM = new Random(1209561832);

    private static final String MAP_KEY = "test";
    
    private DesktopPlayerData desktopData;
    private TestParentObject expectedParentObject;
    private TestChildObject expectedChildObject;
    
    @Before
    public void setUp() {
        Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
        Mdx.reflect = new JvmReflection();
        Mdx.platform = GameWrapper.getPlatform();
        Mdx.di = new DependencyInjection(new DesktopComponentScanner());
        Mdx.locks = new JvmLocks();
        Mdx.xml = new XmlSerializer();
        Mdx.json = new JsonSerializer();
        desktopData = new DesktopPlayerData(TEST_IDENTIFIER);
        
        createTestObjects();
    }
    
    @After
    public void teardown() throws Exception {
        desktopData.wipe();
    }

    @Test
    public void testMultipleWrites() throws Exception {
        desktopData.writeXml(expectedParentObject, XML_FILENAME);
        Assert.assertEquals(true, new File(desktopData.getSaveDirectoryForGame(TEST_IDENTIFIER), XML_FILENAME).exists());

        expectedParentObject.setIntValue(RANDOM.nextInt());
        desktopData.writeXml(expectedParentObject, XML_FILENAME);
        Assert.assertEquals(true, new File(desktopData.getSaveDirectoryForGame(TEST_IDENTIFIER), XML_FILENAME).exists());

        TestParentObject result = desktopData.readXml(TestParentObject.class, XML_FILENAME);
        assertObjectIsAsExpected(result);
    }
    
    @Test
    public void testXml() throws Exception {
        desktopData.writeXml(expectedParentObject, XML_FILENAME);
        Assert.assertEquals(true, new File(desktopData.getSaveDirectoryForGame(TEST_IDENTIFIER), XML_FILENAME).exists());
        
        TestParentObject result = desktopData.readXml(TestParentObject.class, XML_FILENAME);
        assertObjectIsAsExpected(result);
    }
    
    @Test(expected=Exception.class)
    public void testReadXmlFromNonExistingFile() throws Exception {
        desktopData.readXml(TestParentObject.class, "blah-" + XML_FILENAME);
    }
    
    @Test
    public void testJson() throws Exception {
        desktopData.writeJson(expectedParentObject, JSON_FILENAME);
        
        TestParentObject result = desktopData.readJson(TestParentObject.class, JSON_FILENAME);
        assertObjectIsAsExpected(result);
    }
    
    @Test(expected=Exception.class)
    public void testReadJsonFromNonExistingFile() throws Exception {
        desktopData.readJson(TestParentObject.class, "blah-" + JSON_FILENAME);
    }
    
    @Test
    public void testFileExists() throws Exception {
        desktopData.writeXml(expectedParentObject, XML_FILENAME);
        Assert.assertEquals(true, desktopData.hasFile(XML_FILENAME));
    }
    
    @Test
    public void testDirectoryExists() throws Exception {
        
    }
    
    private void assertObjectIsAsExpected(TestParentObject result) {
        Assert.assertEquals(expectedParentObject.isBooleanValue(), result.isBooleanValue());
        Assert.assertEquals(expectedParentObject.getFloatValue(), result.getFloatValue());
        Assert.assertNotSame(expectedParentObject.getIgnoredValue(), result.getIgnoredValue());
        Assert.assertEquals(expectedParentObject.getIntValue(), result.getIntValue());
        Assert.assertEquals(expectedParentObject.getLongValue(), result.getLongValue());
        Assert.assertEquals(expectedParentObject.getShortValue(), result.getShortValue());
        Assert.assertEquals(expectedParentObject.getChildObject(), result.getChildObject());
        Assert.assertEquals(expectedParentObject.getListValues(), result.getListValues());
        Assert.assertEquals(true, result.getMapValues().containsKey(MAP_KEY));
        Assert.assertEquals(expectedParentObject.getMapValues().get(MAP_KEY), result.getMapValues().get(MAP_KEY));
    }
    
    private void createTestObjects() {
        expectedParentObject = new TestParentObject();
        expectedParentObject.setSuperField("super duper");
        expectedParentObject.setBooleanValue(true);
        expectedParentObject.setIgnoredValue(RANDOM.nextInt() + 1);
        expectedParentObject.setIntValue(RANDOM.nextInt() + 1);
        expectedParentObject.setLongValue(RANDOM.nextLong() + 1);
        expectedParentObject.setShortValue((short) 77);
        expectedParentObject.setStringValue(String.valueOf(RANDOM.nextInt()));
        expectedParentObject.setFloatValue(RANDOM.nextFloat());
        expectedParentObject.setEnumValue(TestEnum.OPTION_B);
        
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(RANDOM.nextInt()));
        expectedParentObject.setListValues(list);
        
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(MAP_KEY, RANDOM.nextInt());
        expectedParentObject.setMapValues(map);
        
        expectedChildObject = new TestChildObject();
        expectedChildObject.setIntValue(RANDOM.nextInt() + 1);
        
        expectedParentObject.setChildObject(expectedChildObject);
        expectedParentObject.setChildObjectArray(new TestChildObject[3]);
        expectedParentObject.getChildObjectArray()[0] = new TestChildObject(51);
        expectedParentObject.getChildObjectArray()[1] = new TestChildObject(57);
        
        expectedParentObject.setChildren(new ArrayList<TestChildObject>());
        expectedParentObject.setIntArrayValue(new int [] { 0, 1, 2 });
        expectedParentObject.setStringArrayValue(new String [] { "item1", "item2" });
        
        expectedParentObject.setMapObjectValues(new HashMap<String, TestChildObject>());
        expectedParentObject.getMapObjectValues().put("key1", new TestChildObject(100));
        expectedParentObject.getMapObjectValues().put("key2", new TestChildObject(101));
        
        expectedParentObject.setArgObject(new TestConstuctorArgObject("cargValue"));
        expectedParentObject.setInterfaceObject(new TestInterfaceImpl("id-5"));
        expectedParentObject.setInterfaceObjectList(new ArrayList<TestInterface>());
        expectedParentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-3"));
        expectedParentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-4"));
        
        expectedParentObject.getFinalStringList().add("fstr1");
        expectedParentObject.getFinalStringList().add("fstr2");
		
        expectedParentObject.getFinalStringArray()[0] = "fstr3";
        expectedParentObject.getFinalStringArray()[1] = "fstr4";
        expectedParentObject.getFinalStringArray()[2] = "fstr5";
		
        expectedParentObject.getFinalStringMap().put("fkey1", "fstr6");
        expectedParentObject.getFinalStringMap().put("fkey2", "fstr7");
        
        expectedParentObject.setAbstractObject(new TestAbstractImplObject());
        expectedParentObject.getAbstractObject().setValue(91);
        
        expectedParentObject.setGdxObjectMap(new ObjectMap<String, String>());
        expectedParentObject.getGdxObjectMap().put("testGdxKey", "testGdxValue");
        
        expectedParentObject.setGdxArray(new Array<String>());
        expectedParentObject.getGdxArray().add("testGdxArrayValue");

        expectedParentObject.setGdxIntArray(new IntArray());
        expectedParentObject.getGdxIntArray().add(1);
        expectedParentObject.getGdxIntArray().add(77);

        expectedParentObject.setGdxIntSet(new IntSet());
        expectedParentObject.getGdxIntSet().add(99);
        expectedParentObject.getGdxIntSet().add(101);

        expectedParentObject.setGdxIntIntMap(new IntIntMap());
        expectedParentObject.getGdxIntIntMap().put(44, 55);
        expectedParentObject.getGdxIntIntMap().put(66, 77);
    }
}
