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
package org.mini2Dx.core.serialization;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.serialization.dummy.TestAbstractImplObject;
import org.mini2Dx.core.serialization.dummy.TestChildObject;
import org.mini2Dx.core.serialization.dummy.TestConstuctorArgObject;
import org.mini2Dx.core.serialization.dummy.TestInterface;
import org.mini2Dx.core.serialization.dummy.TestInterfaceImpl;
import org.mini2Dx.core.serialization.dummy.TestParentObject;
import org.mini2Dx.core.util.Os;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;

import junit.framework.Assert;

/**
 * User acceptance tests for {@link JsonSerializer}
 */
public class JsonSerializerTest {
	private JsonSerializer serializer;
	
	private TestParentObject parentObject;
	
	@Before
	public void setUp() {
		serializer = new JsonSerializer();
		
		parentObject = new TestParentObject();
		parentObject.setSuperField("super super");
		parentObject.setBooleanValue(true);
		parentObject.setByteValue((byte) 1);
		parentObject.setFloatValue(2.5f);
		parentObject.setIgnoredValue(1);
		parentObject.setIntValue(255);
		parentObject.setEnumValue(Os.UNKNOWN);
		
		parentObject.setListValues(new ArrayList<String>());
		parentObject.getListValues().add("itemA");
		parentObject.getListValues().add("itemB");
		
		parentObject.setLongValue(Long.MAX_VALUE);
		parentObject.setMapValues(new HashMap<String, Integer>());
		parentObject.getMapValues().put("key", 77);
		
		parentObject.setShortValue((short) 655);
		parentObject.setStringValue("hello");
		parentObject.setStringArrayValue(new String[] { "item1", "item2" });
		parentObject.setIntArrayValue(new int[] { 1, 2, 3 });
		
		parentObject.setChildObject(new TestChildObject(34));
		parentObject.setChildObjectArray(new TestChildObject[3]);
		parentObject.getChildObjectArray()[0] = new TestChildObject(51);
		parentObject.getChildObjectArray()[1] = new TestChildObject(57);
		
		parentObject.setChildren(new ArrayList<TestChildObject>());
		parentObject.getChildren().add(new TestChildObject(35));
		parentObject.getChildren().add(new TestChildObject(36));
		
		parentObject.setMapObjectValues(new HashMap<String, TestChildObject>());
		parentObject.getMapObjectValues().put("key1", new TestChildObject(100));
		parentObject.getMapObjectValues().put("key2", new TestChildObject(101));
		
		parentObject.setArgObject(new TestConstuctorArgObject("cargValue"));
		parentObject.setInterfaceObject(new TestInterfaceImpl("id-1"));
		parentObject.setInterfaceObjectList(new ArrayList<TestInterface>());
		parentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-3"));
		parentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-4"));
		
		parentObject.getFinalStringList().add("fstr1");
		parentObject.getFinalStringList().add("fstr2");
		
		parentObject.getFinalStringArray()[0] = "fstr3";
		parentObject.getFinalStringArray()[1] = "fstr4";
		parentObject.getFinalStringArray()[2] = "fstr5";
		
		parentObject.getFinalStringMap().put("fkey1", "fstr6");
		parentObject.getFinalStringMap().put("fkey2", "fstr7");
		
		parentObject.setAbstractObject(new TestAbstractImplObject());
		parentObject.getAbstractObject().setValue(91);
		
		parentObject.setGdxObjectMap(new ObjectMap<String, String>());
		parentObject.getGdxObjectMap().put("testGdxKey", "testGdxValue");
		
		parentObject.setGdxArray(new Array<String>());
		parentObject.getGdxArray().add("testGdxArrayValue");
	}
	
	@Test
	public void testJsonSerialization() throws SerializationException {
		testJsonSerialization(false);
	}
	
	@Test
	public void testJsonSerializationWithPrettyPrint() throws SerializationException {
		testJsonSerialization(true);
	}
	
	@Test(expected=RequiredFieldException.class)
	public void testJsonSerializationWithMissingRequiredField() throws SerializationException {
		String json = serializer.toJson(parentObject);
		json = json.replace("intValue", "fintValue");
		serializer.fromJson(json, TestParentObject.class);
	}
	
	private void testJsonSerialization(boolean prettyPrint) throws SerializationException {
		String json = serializer.toJson(parentObject, prettyPrint);
		Assert.assertEquals(true, json.length() > 2);
		System.out.println(json);
		
		TestParentObject result = serializer.fromJson(json, TestParentObject.class);
		Assert.assertEquals(parentObject.getSuperField(), result.getSuperField());
		Assert.assertEquals(parentObject.getEnumValue(), result.getEnumValue());
		Assert.assertEquals(parentObject.isBooleanValue(), result.isBooleanValue());
		Assert.assertEquals(parentObject.getByteValue(), result.getByteValue());
		Assert.assertEquals(parentObject.getFloatValue(), result.getFloatValue());
		Assert.assertEquals(parentObject.getIntValue(), result.getIntValue());
		Assert.assertEquals(parentObject.getIntArrayValue().length, result.getIntArrayValue().length);
		for(int i = 0; i < parentObject.getIntArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getIntArrayValue()[i], result.getIntArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getLongValue(), result.getLongValue());
		Assert.assertEquals(parentObject.getShortValue(), result.getShortValue());
		Assert.assertEquals(parentObject.getStringValue(), result.getStringValue());
		Assert.assertEquals(parentObject.getStringArrayValue().length, result.getStringArrayValue().length);
		for(int i = 0; i < parentObject.getStringArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getStringArrayValue()[i], result.getStringArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getListValues().size(), result.getListValues().size());
		Assert.assertEquals(parentObject.getListValues(), result.getListValues());
		Assert.assertEquals(parentObject.getMapValues().size(), result.getMapValues().size());
		for(String key : parentObject.getMapValues().keySet()) {
			Assert.assertEquals(true, result.getMapValues().containsKey(key));
			Assert.assertEquals(parentObject.getMapValues().get(key), result.getMapValues().get(key));
		}
		
		Assert.assertEquals(parentObject.getChildObject().getIntValue(), result.getChildObject().getIntValue());
		Assert.assertEquals(parentObject.getChildObjectArray().length, result.getChildObjectArray().length);
		for(int i = 0; i < parentObject.getChildObjectArray().length; i++) {
			Assert.assertEquals(parentObject.getChildObjectArray()[i], result.getChildObjectArray()[i]);
		}
		
		Assert.assertEquals(parentObject.getChildren().size(), result.getChildren().size());
		for(int i = 0; i < parentObject.getChildren().size(); i++) {
			Assert.assertEquals(parentObject.getChildren().get(i).getIntValue(), result.getChildren().get(i).getIntValue());
		}
		Assert.assertEquals(parentObject.getArgObject(), result.getArgObject());
		
		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
		Assert.assertEquals(parentObject.getInterfaceObject(), result.getInterfaceObject());
		Assert.assertEquals(parentObject.getInterfaceObjectList().size(), result.getInterfaceObjectList().size());
		for(int i = 0; i < parentObject.getInterfaceObjectList().size(); i++) {
			Assert.assertEquals(parentObject.getInterfaceObjectList().get(i), result.getInterfaceObjectList().get(i));
		}
		
		Assert.assertEquals(parentObject.getFinalStringList().size(), result.getFinalStringList().size());
		for(int i = 0; i < parentObject.getFinalStringList().size(); i++) {
			Assert.assertEquals(parentObject.getFinalStringList().get(i), result.getFinalStringList().get(i));
		}
		Assert.assertEquals(parentObject.getFinalStringMap().size(), result.getFinalStringMap().size());
		for(String key : parentObject.getFinalStringMap().keySet()) {
			Assert.assertEquals(parentObject.getFinalStringMap().get(key), result.getFinalStringMap().get(key));
		}
		Assert.assertEquals(parentObject.getFinalStringArray().length, result.getFinalStringArray().length);
		for(int i = 0; i < parentObject.getFinalStringArray().length; i++) {
			Assert.assertEquals(parentObject.getFinalStringArray()[i], result.getFinalStringArray()[i]);
		}
		Assert.assertEquals(parentObject.getAbstractObject().getValue(), result.getAbstractObject().getValue());
	
		Assert.assertEquals(parentObject.getGdxObjectMap().size, result.getGdxObjectMap().size);
		ObjectMap.Entries<String, String> entries = parentObject.getGdxObjectMap().entries();
		while(entries.hasNext()) {
			ObjectMap.Entry<String, String> entry = entries.next();
			Assert.assertEquals(entry.value, result.getGdxObjectMap().get(entry.key));
		}
		
		Assert.assertEquals(parentObject.getGdxArray().size, result.getGdxArray().size);
		for(int i = 0; i < parentObject.getGdxArray().size; i++) {
			Assert.assertEquals(parentObject.getGdxArray().get(i), result.getGdxArray().get(i));
		}
	}
}
