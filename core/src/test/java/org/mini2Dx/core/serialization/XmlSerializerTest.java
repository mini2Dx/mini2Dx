/**
 * Copyright (c) 2015, mini2Dx Project
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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.serialization.dummy.TestChildObject;
import org.mini2Dx.core.serialization.dummy.TestParentObject;

/**
 * Utility class for {@link XmlSerializer} user acceptance tests
 */
public abstract class XmlSerializerTest {
	private XmlSerializer xmlSerializer;
	private TestParentObject parentObject;
	
	@Before
	public void setUp() {
		xmlSerializer = createXmlSerializer();
		parentObject = createTestParentObject();
	}
	
	public abstract XmlSerializer createXmlSerializer();
	
	@Test
	public void testXmlSerialization() throws SerializationException {
		String xml = xmlSerializer.toXml(parentObject);
		Assert.assertEquals(true, xml.length() > 2);
		System.out.println(xml);
		
		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
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
		Assert.assertEquals(parentObject.getChildren().size(), result.getChildren().size());
		for(int i = 0; i < parentObject.getChildren().size(); i++) {
			Assert.assertEquals(parentObject.getChildren().get(i).getIntValue(), result.getChildren().get(i).getIntValue());
		}
		
		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
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
		xml += "    <intArrayValue>\n";
		xml += "        <value>1</value>\n";
		xml += "        <value>2</value>\n";
		xml += "        <value>3</value>\n";
		xml += "    </intArrayValue>\n";
		xml += "    <stringArrayValue>\n";
		xml += "        <value>item1</value>\n";
		xml += "        <value>item2</value>\n";
		xml += "    </stringArrayValue>\n";
		xml += "    <stringValue>hello</stringValue>\n";
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
		xml += "    <optionalChildObject/>\n";
		xml += "    <children>\n";
		xml += "        <value>\n";
		xml += "            <intValue>35</intValue>\n";
		xml += "        </value>\n";
		xml += "        <value>\n";
		xml += "            <intValue>36</intValue>\n";
		xml += "        </value>\n";
		xml += "    </children>\n";
		xml += "</data>";
		
		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
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
		Assert.assertEquals(parentObject.getChildren().size(), result.getChildren().size());
		for(int i = 0; i < parentObject.getChildren().size(); i++) {
			Assert.assertEquals(parentObject.getChildren().get(i).getIntValue(), result.getChildren().get(i).getIntValue());
		}
		
		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
	}
	
	protected TestParentObject createTestParentObject() {
		TestParentObject parentObject = new TestParentObject();
		parentObject.setBooleanValue(true);
		parentObject.setByteValue((byte) 1);
		parentObject.setFloatValue(2.5f);
		parentObject.setIgnoredValue(1);
		parentObject.setIntValue(255);
		
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
		
		parentObject.setChildren(new ArrayList<TestChildObject>());
		parentObject.getChildren().add(new TestChildObject(35));
		parentObject.getChildren().add(new TestChildObject(36));
		return parentObject;
	}
}
