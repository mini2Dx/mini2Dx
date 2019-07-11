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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit and integration tests for {@link FlexRow}
 */
@Ignore("Travis CI suddenly complains about this test")
public class FlexRowTest {
	@Before
	public void setUp() {
		//Mdx.xml = new DesktopXmlSerializer();
	}

	@Test
	public void testSerialization() {
		FlexRow flexRow = new FlexRow("flexRow-1");
		flexRow.setZIndex(27);
		flexRow.add(new Label());
		flexRow.add(new TextButton("button-2"));
		
//		try {
//			String xml = Mdx.xml.toXml(flexRow);
//			System.out.println(xml);
//			FlexRow result = Mdx.xml.fromXml(xml, FlexRow.class);
//
//			Assert.assertEquals(flexRow.getId(), result.getId());
//			Assert.assertEquals(flexRow.getZIndex(), result.getZIndex());
//			Assert.assertEquals(flexRow.children.size, result.children.size);
//			for(int i = 0; i < flexRow.children.size; i++) {
//				Assert.assertEquals(flexRow.children.get(i).getId(), result.children.get(i).getId());
//			}
//		} catch (SerializationException e) {
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
	}
}
