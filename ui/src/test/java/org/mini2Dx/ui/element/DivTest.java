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
package org.mini2Dx.ui.element;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.XmlSerializer;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;

/**
 * Unit and integration tests for {@link Div}
 */
public class DivTest {

	@Before
	public void setUp() {
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.reflect = new JvmReflection();
		Mdx.locks = new JvmLocks();
		Mdx.xml = new XmlSerializer();
	}

	@Test
	public void testSerialization() {
		Div div = new Div("div-1");
		div.set(10f, 100f, 1000f, 10000f);
		div.setZIndex(22);
		div.setOverflowClipped(true);
		div.add(new Label());
		div.add(new TextButton("button-1"));
		
		try {
			String xml = Mdx.xml.toXml(div);
			System.out.println(xml);
			Div result = Mdx.xml.fromXml(xml, Div.class);

			Assert.assertEquals(div.getId(), result.getId());
			Assert.assertEquals(div.getZIndex(), result.getZIndex());
			Assert.assertEquals(10f, result.getX(), 0.01f);
			Assert.assertEquals(100f, result.getY(), 0.01f);
			Assert.assertEquals(1000f, result.getWidth(), 0.01f);
			Assert.assertEquals(10000f, result.getHeight(), 0.01f);
			Assert.assertEquals(div.isOverflowClipped(), result.isOverflowClipped());
			Assert.assertEquals(div.children.size, result.children.size);
			for(int i = 0; i < div.children.size; i++) {
				Assert.assertEquals(div.children.get(i).getId(), result.children.get(i).getId());
			}
		} catch (SerializationException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testSerializationAsChild() {
		FlexRow parent = new FlexRow();

		Div div = new Div("div-1");
		div.set(10f, 100f, 1000f, 10000f);
		div.setZIndex(22);
		div.setOverflowClipped(true);
		div.add(new Label());
		div.add(new TextButton("button-1"));

		parent.add(div);

//		try {
//			String xml = Mdx.xml.toXml(parent);
//			System.out.println(xml);
//
//			FlexRow rowResult = Mdx.xml.fromXml(xml, FlexRow.class);
//			Div result = (Div) rowResult.get(0);
//
//			Assert.assertEquals(div.getId(), result.getId());
//			Assert.assertEquals(div.getZIndex(), result.getZIndex());
//			Assert.assertEquals(10f, result.getX(), 0.01f);
//			Assert.assertEquals(100f, result.getY(), 0.01f);
//			Assert.assertEquals(1000f, result.getWidth(), 0.01f);
//			Assert.assertEquals(10000f, result.getHeight(), 0.01f);
//			Assert.assertEquals(div.isOverflowClipped(), result.isOverflowClipped());
//			Assert.assertEquals(div.children.size, result.children.size);
//			for(int i = 0; i < div.children.size; i++) {
//				Assert.assertEquals(div.children.get(i).getId(), result.children.get(i).getId());
//			}
//		} catch (SerializationException e) {
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
	}
}
