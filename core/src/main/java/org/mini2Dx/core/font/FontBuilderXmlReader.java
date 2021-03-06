/*******************************************************************************
 * Copyright 2021 See AUTHORS file
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
package org.mini2Dx.core.font;

import org.mini2Dx.core.collections.CharMap;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.xml.XmlReader;

import java.io.IOException;
import java.io.Reader;

public class FontBuilderXmlReader {
	private static final String ENCODING = "UTF-8";

	private static final String ELEMENT_FONT = "Font";
	private static final String ELEMENT_CHAR = "Char";

	private static final String ATTRIBUTE_SIZE = "size";
	private static final String ATTRIBUTE_FAMILY = "family";
	private static final String ATTRIBUTE_WIDTH = "width";
	private static final String ATTRIBUTE_HEIGHT = "height";
	private static final String ATTRIBUTE_STYLE = "style";
	private static final String ATTRIBUTE_CODE = "code";
	private static final String ATTRIBUTE_OFFSET = "offset";
	private static final String ATTRIBUTE_RECT = "rect";

	private static final String DEFAULT_RECT = "0 0 1 1";

	public void read(FileHandle xmlFileHandle, FontBuilderGameFont.FontProperties fontProperties,
	                            CharMap<FontBuilderGameFont.FontBuilderChar> charMap) throws IOException {
		final XmlReader xmlReader = new XmlReader();
		final Reader ioReader = xmlFileHandle.reader(ENCODING);
		XmlReader.Element rootElement = xmlReader.parse(ioReader);
		ioReader.close();

		if(!rootElement.getName().equals(ELEMENT_FONT)) {
			rootElement = rootElement.getChildByName(ELEMENT_FONT);
		}

		fontProperties.size = rootElement.getIntAttribute(ATTRIBUTE_SIZE, 0);
		fontProperties.family = rootElement.getAttribute(ATTRIBUTE_FAMILY, "");
		fontProperties.height = rootElement.getIntAttribute(ATTRIBUTE_HEIGHT, 0);
		fontProperties.style = rootElement.getAttribute(ATTRIBUTE_STYLE, "");

		final Array<XmlReader.Element> charElements = rootElement.getChildrenByName(ELEMENT_CHAR);
		for(XmlReader.Element charElement : charElements) {
			if(!charElement.hasAttribute(ATTRIBUTE_CODE)) {
				continue;
			}
			final FontBuilderGameFont.FontBuilderChar fontChar = new FontBuilderGameFont.FontBuilderChar();
			final char code = charElement.getAttribute(ATTRIBUTE_CODE).charAt(0);
			final String rect = charElement.getAttribute(ATTRIBUTE_RECT, DEFAULT_RECT);

			fontChar.code = code;
			fontChar.width = charElement.getIntAttribute(ATTRIBUTE_WIDTH, 0);

			final String [] rectComponents = rect.split(" ");
			fontChar.rectX = Integer.parseInt(rectComponents[0]);
			fontChar.rectY = Integer.parseInt(rectComponents[1]);
			fontChar.rectWidth = Integer.parseInt(rectComponents[2]);
			fontChar.rectHeight = Integer.parseInt(rectComponents[3]);

			if(charElement.hasAttribute(ATTRIBUTE_OFFSET)) {
				final String offset = charElement.getAttribute(ATTRIBUTE_OFFSET);
				final String [] offsetComponents = offset.split(" ");
				fontChar.offsetX = Integer.parseInt(offsetComponents[0]);
				fontChar.offsetY = Integer.parseInt(offsetComponents[1]);
			}

			charMap.put(code, fontChar);
		}
	}
}
