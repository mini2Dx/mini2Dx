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
package org.mini2Dx.core.serialization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.RequiredFieldException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.dummy.*;
import org.mini2Dx.gdx.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonSerializationTest {
	private JsonSerializer serializer;

	private TestParentObject parentObject;



	@Before
	public void setUp() {
		AotSerializationData.clear();

		Mdx.reflect = new JvmReflection();
		Mdx.locks = new JvmLocks();
		serializer = new JsonSerializer();

		parentObject = new TestParentObject();
		parentObject.setSuperField("super super");
		parentObject.setBooleanValue(true);
		parentObject.setByteValue((byte) 1);
		parentObject.setFloatValue(2.5f);
		parentObject.setIgnoredValue(1);
		parentObject.setIntValue(255);
		parentObject.setEnumValue(TestEnum.OPTION_B);

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

		parentObject.setGdxIntArray(new IntArray());
		parentObject.getGdxIntArray().add(1);
		parentObject.getGdxIntArray().add(77);

		parentObject.setGdxIntSet(new IntSet());
		parentObject.getGdxIntSet().add(99);
		parentObject.getGdxIntSet().add(101);

		parentObject.setGdxIntIntMap(new IntIntMap());
		parentObject.getGdxIntIntMap().put(44, 55);
		parentObject.getGdxIntIntMap().put(66, 77);
	}

	@Test
	public void testJsonSerialization() throws SerializationException {
		testJsonSerialization(false);
	}

	@Test
	public void testJsonSerializationWithPrettyPrint() throws SerializationException {
		testJsonSerialization(true);
	}

	@Test(expected= RequiredFieldException.class)
	public void testJsonSerializationWithMissingRequiredField() throws SerializationException {
		String json = serializer.toJson(parentObject);
		json = json.replace("intValue", "fintValue");
		serializer.fromJson(json, TestParentObject.class);
	}

	@Test
	public void testJsonSerializationWithAotData() throws SerializationException {
		AotSerializationData.registerClass(TestParentObject.class);

		testJsonSerialization(false);
	}

	private void testJsonSerialization(boolean prettyPrint) throws SerializationException {
		String json = serializer.toJson(parentObject, prettyPrint);
		Assert.assertEquals(true, json.length() > 2);
		System.out.println(json);

		TestParentObject result = serializer.fromJson(json, TestParentObject.class);
		Assert.assertTrue(result.isPostDeserializeCalled());
		Assert.assertTrue(result.getChildObject().isPostDeserializeCalled());

		Assert.assertEquals(parentObject.getSuperField(), result.getSuperField());
		Assert.assertEquals(parentObject.getEnumValue(), result.getEnumValue());
		Assert.assertEquals(parentObject.isBooleanValue(), result.isBooleanValue());
		Assert.assertEquals(parentObject.getByteValue(), result.getByteValue());
		Assert.assertEquals(parentObject.getFloatValue(), result.getFloatValue(), 0f);
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

		Assert.assertEquals(parentObject.getGdxIntArray().size, result.getGdxIntArray().size);
		for(int i = 0; i < parentObject.getGdxIntArray().size; i++) {
			Assert.assertEquals(parentObject.getGdxIntArray().get(i), result.getGdxIntArray().get(i));
		}

		Assert.assertEquals(parentObject.getGdxIntSet().size, result.getGdxIntSet().size);
		Assert.assertEquals(parentObject.getGdxIntSet(), result.getGdxIntSet());

		Assert.assertEquals(parentObject.getGdxIntIntMap().size, result.getGdxIntIntMap().size);
		Assert.assertEquals(parentObject.getGdxIntIntMap(), result.getGdxIntIntMap());
	}
}
