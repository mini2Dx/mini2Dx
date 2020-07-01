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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.XmlSerializer;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.ui.animation.TypingTextAnimation;
import org.mini2Dx.ui.layout.HorizontalAlignment;

/**
 * Unit and integration tests for {@link Label}
 */
public class LabelTest {
	@Before
	public void setUp() {
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.reflect = new JvmReflection();
		Mdx.locks = new JvmLocks();
		Mdx.xml = new XmlSerializer();
	}

	@Test
	public void testSerialization() {
		Label label = new Label("label-1");
		label.setText("Example text");
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setResponsive(true);
		label.setTextAnimation(new TypingTextAnimation(32f));

		try {
			String xml = Mdx.xml.toXml(label);
			System.out.println(xml);
			Label result = Mdx.xml.fromXml(xml, Label.class);
			Assert.assertEquals(label.getId(), result.getId());
			Assert.assertEquals(label.getHorizontalAlignment(), result.getHorizontalAlignment());
			Assert.assertEquals(label.getText(), result.getText());
			Assert.assertEquals(label.isResponsive(), result.isResponsive());
			Assert.assertEquals(((TypingTextAnimation) label.getTextAnimation()).getCharactersPerSecond(),
					((TypingTextAnimation) result.getTextAnimation()).getCharactersPerSecond());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
