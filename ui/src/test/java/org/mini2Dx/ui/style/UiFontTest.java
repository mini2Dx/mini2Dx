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
package org.mini2Dx.ui.style;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.JsonSerializer;
import org.mini2Dx.core.serialization.XmlSerializer;

import java.util.Scanner;

public class UiFontTest {

	@Before
	public void setup() {
		Mdx.reflect = new JvmReflection();
		Mdx.locks = new JvmLocks();
		Mdx.xml = new XmlSerializer();
		Mdx.json = new JsonSerializer();
	}

	@Test
	public void testMonospaceFontXmlSerialization() throws SerializationException {
		final MonospaceGameFont.FontParameters result = Mdx.xml.fromXml(
				readFromResource("/monospace-font.xml"), MonospaceGameFont.FontParameters.class);
		Assert.assertEquals("_generated/textures/mod.atlas", result.textureAtlasPath);
		Assert.assertEquals("font/cutie", result.texturePath);
		Assert.assertEquals(6, result.frameWidth);
		Assert.assertEquals(16, result.frameHeight);
		Assert.assertEquals(0, result.framePaddingTop);
		Assert.assertEquals(2, result.framePaddingBottom);
		Assert.assertEquals(0, result.framePaddingLeft);
		Assert.assertEquals(1, result.framePaddingRight);
		Assert.assertEquals(5, result.characterWidth);
		Assert.assertEquals(16, result.lineHeight);
		Assert.assertEquals(1, result.spacing);
		Assert.assertEquals(95, result.overrideCharacterIndices.size);
	}

	@Test
	public void testMonospaceFontJsonSerialization() throws SerializationException {
		final MonospaceGameFont.FontParameters result = Mdx.json.fromJson(
				readFromResource("/monospace-font.json"), MonospaceGameFont.FontParameters.class);
		Assert.assertEquals("_generated/textures/mod.atlas", result.textureAtlasPath);
		Assert.assertEquals("font/cutie", result.texturePath);
		Assert.assertEquals(6, result.frameWidth);
		Assert.assertEquals(16, result.frameHeight);
		Assert.assertEquals(0, result.framePaddingTop);
		Assert.assertEquals(2, result.framePaddingBottom);
		Assert.assertEquals(0, result.framePaddingLeft);
		Assert.assertEquals(1, result.framePaddingRight);
		Assert.assertEquals(5, result.characterWidth);
		Assert.assertEquals(16, result.lineHeight);
		Assert.assertEquals(1, result.spacing);
		Assert.assertEquals(95, result.overrideCharacterIndices.size);
	}

	private String readFromResource(String path) {
		final Scanner scanner = new Scanner(UiFont.class.getResourceAsStream(path));
		final StringBuilder result = new StringBuilder();

		while(scanner.hasNext()) {
			result.append(scanner.next());
		}
		scanner.close();
		return result.toString();
	}
}
