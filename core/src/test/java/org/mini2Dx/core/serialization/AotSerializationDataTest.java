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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.dummy.TestChildObject;
import org.mini2Dx.core.serialization.dummy.TestComplexConstructorArgObject;
import org.mini2Dx.core.serialization.dummy.TestParentObject;

import java.io.StringReader;
import java.io.StringWriter;

public class AotSerializationDataTest {

	@Before
	public void setUp() {
		AotSerializationData.clear();
		Mdx.reflect = new JvmReflection();
	}

	@Test
	public void testSaveToRestoreFrom() throws Exception {
		AotSerializationData.registerClass(TestParentObject.class);
		AotSerializationData.registerClass(TestChildObject.class);

		StringWriter stringWriter = new StringWriter();
		AotSerializationData.saveTo(stringWriter);

		AotSerializedClassData classData = AotSerializationData.getClassData(TestParentObject.class);
		Assert.assertTrue(classData.getTotalFields() > 0);
		Assert.assertTrue(classData.getPostDeserializeMethodName() != null);

		String data = stringWriter.toString();
		System.out.println(data);
		Assert.assertTrue(data.length() > 0);

		AotSerializationData.clear();
		AotSerializationData.restoreFrom(new StringReader(data));

		AotSerializedClassData result = AotSerializationData.getClassData(TestParentObject.class);
		Assert.assertTrue(result.getTotalFields() > 0);
		Assert.assertTrue(classData.getPostDeserializeMethodName() != null);
		Assert.assertEquals(classData, result);
	}

	@Test
	public void testSaveToRestoreFromWithConstructorArgs() throws Exception {
		AotSerializationData.registerClass(TestComplexConstructorArgObject.class);

		StringWriter stringWriter = new StringWriter();
		AotSerializationData.saveTo(stringWriter);

		AotSerializedClassData classData = AotSerializationData.getClassData(TestComplexConstructorArgObject.class);
		Assert.assertEquals(0, classData.getTotalFields());
		Assert.assertEquals(3, classData.getTotalConstructors());

		String data = stringWriter.toString();
		System.out.println(data);
		Assert.assertTrue(data.length() > 0);

		AotSerializationData.clear();
		AotSerializationData.restoreFrom(new StringReader(data));

		AotSerializedClassData result = AotSerializationData.getClassData(TestComplexConstructorArgObject.class);
		Assert.assertEquals(0, classData.getTotalFields());
		Assert.assertEquals(3, classData.getTotalConstructors());
		Assert.assertEquals(classData, result);

		Assert.assertTrue(result.getTotalConstructors() > 0);
		for(int i = 0; i < result.getTotalConstructors(); i++) {
			switch(result.getConstructorData(i).getTotalArgs() ) {
			case 1:
				Assert.assertEquals("id", result.getConstructorData(i).getConstructorArgName(0));
				Assert.assertEquals(Integer.class, result.getConstructorData(i).getConstructorArgType(0));
				break;
			case 3:
				Assert.assertEquals("id", result.getConstructorData(i).getConstructorArgName(0));
				Assert.assertEquals(Integer.class, result.getConstructorData(i).getConstructorArgType(0));
				Assert.assertEquals("x", result.getConstructorData(i).getConstructorArgName(1));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(1));
				Assert.assertEquals("y", result.getConstructorData(i).getConstructorArgName(2));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(2));
				break;
			case 5:
				Assert.assertEquals("id", result.getConstructorData(i).getConstructorArgName(0));
				Assert.assertEquals(Integer.class, result.getConstructorData(i).getConstructorArgType(0));
				Assert.assertEquals("x", result.getConstructorData(i).getConstructorArgName(1));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(1));
				Assert.assertEquals("y", result.getConstructorData(i).getConstructorArgName(2));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(2));
				Assert.assertEquals("width", result.getConstructorData(i).getConstructorArgName(3));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(3));
				Assert.assertEquals("height", result.getConstructorData(i).getConstructorArgName(4));
				Assert.assertEquals(Float.class, result.getConstructorData(i).getConstructorArgType(4));
				break;
			default:
				Assert.fail("Incorrect number of constructor args");
				break;
			}
		}
	}
}
