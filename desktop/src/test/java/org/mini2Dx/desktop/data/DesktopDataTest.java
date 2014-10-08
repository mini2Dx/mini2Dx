/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.desktop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.M2Dx;
import org.mini2Dx.core.data.dummy.TestChildObject;
import org.mini2Dx.core.data.dummy.TestParentObject;

import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

/**
 *
 * @author Thomas Cashman
 */
public class DesktopDataTest {
    private static final String TEST_IDENTIFIER = "org.mini2Dx.testgame";
    private static final String XML_FILENAME = "test.xml";
    private static final String JSON_FILENAME = "test.json";
    
    private static final String MAP_KEY = "test";
    
    private DesktopData desktopData;
    private TestParentObject expectedParentObject;
    private TestChildObject expectedChildObject;
    
    @Before
    public void setUp() {
        M2Dx.files = new LwjglFiles();
        desktopData = new DesktopData(TEST_IDENTIFIER);
        
        createTestObjects();
    }
    
    @After
    public void teardown() throws Exception {
        desktopData.wipe();
    }
    
    @Test
    public void testXml() throws Exception {
        desktopData.writeXml(expectedParentObject, XML_FILENAME);
        
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
        Random random = new Random();
        
        expectedParentObject = new TestParentObject();
        expectedParentObject.setBooleanValue(true);
        expectedParentObject.setIgnoredValue(random.nextInt() + 1);
        expectedParentObject.setIntValue(random.nextInt() + 1);
        expectedParentObject.setLongValue(random.nextLong() + 1);
        expectedParentObject.setShortValue((short) 77);
        expectedParentObject.setStringValue(String.valueOf(random.nextInt()));
        expectedParentObject.setFloatValue(random.nextFloat());
        
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(random.nextInt()));
        expectedParentObject.setListValues(list);
        
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(MAP_KEY, random.nextInt());
        expectedParentObject.setMapValues(map);
        
        expectedChildObject = new TestChildObject();
        expectedChildObject.setIntValue(random.nextInt() + 1);
        expectedParentObject.setChildObject(expectedChildObject);
    }
}
