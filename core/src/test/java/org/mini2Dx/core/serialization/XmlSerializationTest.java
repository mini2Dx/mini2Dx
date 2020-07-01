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
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.dummy.*;
import org.mini2Dx.gdx.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class XmlSerializationTest {
	private XmlSerializer xmlSerializer;
	private TestParentObject parentObject;

	@Before
	public void setUp() {
		Mdx.reflect = new JvmReflection();
		AotSerializationData.clear();
		Mdx.locks = new JvmLocks();
		xmlSerializer = new XmlSerializer();
		parentObject = createTestParentObject();
	}

	@Test
	public void testXmlSerialization() throws SerializationException {
		String xml = xmlSerializer.toXml(parentObject);
		Assert.assertEquals(true, xml.length() > 2);
		System.out.println(xml);

		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
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

		Assert.assertEquals(parentObject.getMapObjectValues().size(), result.getMapObjectValues().size());
		for(String key : parentObject.getMapObjectValues().keySet()) {
			Assert.assertEquals(true, result.getMapObjectValues().containsKey(key));
			Assert.assertEquals(parentObject.getMapObjectValues().get(key), result.getMapObjectValues().get(key));
		}

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

	@Test
	public void testXmlSerializationWithNullField() throws SerializationException {
		parentObject.getFinalStringMap().put("test", null);
		String xml = xmlSerializer.toXml(parentObject);
		xmlSerializer.fromXml(xml, TestParentObject.class);
	}

	@Test
	public void testXmlSerializationWithAotData() throws SerializationException {
		String xml = xmlSerializer.toXml(parentObject);
		Assert.assertEquals(true, xml.length() > 2);
		System.out.println(xml);
		xmlSerializer.fromXml(xml, TestParentObject.class);
	}

	@Test
	public void testPrettyXmlDeserialization() throws SerializationException {
		String xml = "<?xml version=\"1.0\"?>\n";
		xml += "<data>\n";
		xml += "    <intValue>255</intValue>\n";
		xml += "    <booleanValue>true</booleanValue>\n";
		xml += "    <byteValue>1</byteValue>\n";
		xml += "    <shortValue>655</shortValue>\n";
		xml += "    <longValue>9223372036854775807</longValue>\n";
		xml += "    <floatValue>2.5</floatValue>\n";
		xml += "    <intArrayValue length=\"3\">\n";
		xml += "        <value>1</value>\n";
		xml += "        <value>2</value>\n";
		xml += "        <value>3</value>\n";
		xml += "    </intArrayValue>\n";
		xml += "    <stringArrayValue length=\"2\">\n";
		xml += "        <value>item1</value>\n";
		xml += "        <value>item2</value>\n";
		xml += "    </stringArrayValue>\n";
		xml += "    <stringValue>hello</stringValue>\n";
		xml += "    <enumValue>OPTION_B</enumValue>\n";
		xml += "    <mapValues>\n";
		xml += "        <entry>\n";
		xml += "            <key>key</key>\n";
		xml += "            <value>77</value>\n";
		xml += "        </entry>\n";
		xml += "    </mapValues>\n";
		xml += "    <listValues>\n";
		xml += "        <value>itemA</value>\n";
		xml += "        <value>itemB</value>\n";
		xml += "    </listValues>\n";
		xml += "    <childObject>\n";
		xml += "        <intValue>34</intValue>\n";
		xml += "    </childObject>\n";
		xml += "    <childObjectArray length=\"3\">\n";
		xml += "        <value>\n";
		xml += "            <intValue>51</intValue>\n";
		xml += "        </value>\n";
		xml += "        <value>\n";
		xml += "            <intValue>57</intValue>\n";
		xml += "        </value>\n";
		xml += "    </childObjectArray>\n";
		xml += "    <optionalChildObject/>\n";
		xml += "    <children>\n";
		xml += "        <value>\n";
		xml += "            <intValue>35</intValue>\n";
		xml += "        </value>\n";
		xml += "        <value>\n";
		xml += "            <intValue>36</intValue>\n";
		xml += "        </value>\n";
		xml += "    </children>\n";
		xml += "    <superField>super super</superField>\n";
		xml += "    <argObject argValue=\"cargValue\">\n";
		xml += "    </argObject>\n";
		xml += "    <interfaceObject id=\"id-5\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "    </interfaceObject>\n";
		xml += "    <interfaceObjectList>\n";
		xml += "        <value id=\"id-3\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "        </value>\n";
		xml += "        <value id=\"id-4\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "        </value>\n";
		xml += "    </interfaceObjectList>\n";
		xml += "    <finalStringList>\n";
		xml += "        <value>fstr1</value>\n";
		xml += "        <value>fstr2</value>\n";
		xml += "    </finalStringList>\n";
		xml += "    <finalStringArray length=\"3\">\n";
		xml += "        <value>fstr3</value>\n";
		xml += "        <value>fstr4</value>\n";
		xml += "        <value>fstr5</value>\n";
		xml += "    </finalStringArray>\n";
		xml += "    <finalStringMap>\n";
		xml += "        <entry>\n";
		xml += "            <key>fkey1</key>\n";
		xml += "            <value>fstr6</value>\n";
		xml += "        </entry>\n";
		xml += "        <entry>\n";
		xml += "            <key>fkey2</key>\n";
		xml += "            <value>fstr7</value>\n";
		xml += "        </entry>\n";
		xml += "    </finalStringMap>\n";
		xml += "    <abstractObject class=\"org.mini2Dx.core.serialization.dummy.TestAbstractImplObject\">\n";
		xml += "        <value>91</value>\n";
		xml += "    </abstractObject>\n";
		xml += "    <gdxObjectMap>\n";
		xml += "        <entry>\n";
		xml += "            <key>testGdxKey</key>\n";
		xml += "            <value>testGdxValue</value>\n";
		xml += "        </entry>\n";
		xml += "    </gdxObjectMap>\n";
		xml += "    <gdxArray>\n";
		xml += "        <value>testGdxArrayValue</value>\n";
		xml += "    </gdxArray>\n";
		xml += "    <gdxIntArray>\n";
		xml += "        <value>1</value>\n";
		xml += "        <value>77</value>\n";
		xml += "    </gdxIntArray>\n";
		xml += "    <gdxIntSet>\n";
		xml += "        <value>99</value>\n";
		xml += "        <value>101</value>\n";
		xml += "    </gdxIntSet>\n";
		xml += "    <gdxIntIntMap>\n";
		xml += "        <entry>\n";
		xml += "            <key>44</key>\n";
		xml += "            <value>55</value>\n";
		xml += "        </entry>\n";
		xml += "        <entry>\n";
		xml += "            <key>66</key>\n";
		xml += "            <value>77</value>\n";
		xml += "        </entry>\n";
		xml += "    </gdxIntIntMap>\n";
		xml += "</data>";

		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
		Assert.assertEquals(parentObject.getByteValue(), result.getByteValue());
		Assert.assertEquals(parentObject.getFloatValue(), result.getFloatValue(), 0f);
		Assert.assertEquals(parentObject.getIntValue(), result.getIntValue());
		Assert.assertEquals(parentObject.isBooleanValue(), result.isBooleanValue());
		Assert.assertEquals(parentObject.getEnumValue(), result.getEnumValue());

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

		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
		Assert.assertEquals(parentObject.getInterfaceObject(), result.getInterfaceObject());
		Assert.assertEquals(parentObject.getInterfaceObjectList().size(), result.getInterfaceObjectList().size());
		for(int i = 0; i < parentObject.getInterfaceObjectList().size(); i++) {
			Assert.assertEquals(parentObject.getInterfaceObjectList().get(i), result.getInterfaceObjectList().get(i));
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

		Assert.assertEquals(parentObject.getSuperField(), result.getSuperField());
	}

	@Test
	public void testComplexConstructorMatching() throws SerializationException {
		final TestComplexConstructorArgObject expected1 = new TestComplexConstructorArgObject();
		String xml = xmlSerializer.toXml(expected1);
		System.out.println(xml);
		TestComplexConstructorArgObject result1 = xmlSerializer.fromXml(xml, TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected1, result1);
		result1 = xmlSerializer.fromXml("<?xml version=\"1.0\" ?><data></data>", TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected1, result1);

		final TestComplexConstructorArgObject expected2 = new TestComplexConstructorArgObject(99);
		xml = xmlSerializer.toXml(expected2);
		TestComplexConstructorArgObject result2 = xmlSerializer.fromXml(xml, TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected2, result2);
		result2 = xmlSerializer.fromXml("<?xml version=\"1.0\" ?><data id=\"99\"></data>", TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected2, result2);

		final TestComplexConstructorArgObject expected3 = new TestComplexConstructorArgObject(101, 1, 2, 3, 4);
		xml = xmlSerializer.toXml(expected3);
		TestComplexConstructorArgObject result3 = xmlSerializer.fromXml(xml, TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected3, result3);
		result3 = xmlSerializer.fromXml("<?xml version=\"1.0\" ?><data id=\"101\" x=\"1\" y=\"2\" width=\"3\" height=\"4\"></data>", TestComplexConstructorArgObject.class);
		Assert.assertEquals(expected3, result3);

		final TestComplexConstructorParentObject expected4 = new TestComplexConstructorParentObject();
		expected4.getChildren().add(expected3);
		xml = xmlSerializer.toXml(expected4);
		System.out.println(xml);
		TestComplexConstructorParentObject result4 = xmlSerializer.fromXml(xml, TestComplexConstructorParentObject.class);
		Assert.assertEquals(expected4, result4);
		result4 = xmlSerializer.fromXml("<?xml version=\"1.0\" ?><data><children length=\"1\"><value class=\"org.mini2Dx.core.serialization.dummy.TestComplexConstructorArgObject\" id=\"101\" width=\"3\" height=\"4\" x=\"1\" y=\"2\"></value></children></data>", TestComplexConstructorParentObject.class);
		Assert.assertEquals(expected4, result4);
	}

	protected TestParentObject createTestParentObject() {
		TestParentObject parentObject = new TestParentObject();
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
		parentObject.setInterfaceObject(new TestInterfaceImpl("id-5"));
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
		return parentObject;
	}
}
