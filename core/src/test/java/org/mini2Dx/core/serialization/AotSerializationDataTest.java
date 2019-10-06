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
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.dummy.TestChildObject;
import org.mini2Dx.core.serialization.dummy.TestParentObject;

import java.io.StringReader;
import java.io.StringWriter;

public class AotSerializationDataTest {

	@Test
	public void testSaveToRestoreFrom() throws Exception {
		Mdx.reflect = new JvmReflection();

		AotSerializationData.registerClass(TestParentObject.class);
		AotSerializationData.registerClass(TestChildObject.class);

		StringWriter stringWriter = new StringWriter();
		AotSerializationData.saveTo(stringWriter);

		AotSerializedClassData classData = AotSerializationData.getClassData(TestParentObject.class);
		Assert.assertTrue(classData.getTotalFields() > 0);

		String data = stringWriter.toString();
		System.out.println(data);
		Assert.assertTrue(data.length() > 0);

		AotSerializationData.clear();
		AotSerializationData.restoreFrom(new StringReader(data));

		AotSerializedClassData result = AotSerializationData.getClassData(TestParentObject.class);
		Assert.assertTrue(result.getTotalFields() > 0);
		Assert.assertEquals(classData, result);
	}
}
