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
package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.gdx.xml.XmlReader;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mini2Dx.ui.xml.XmlTagUtil.getTagNameWithoutPrefix;

public class AbstractInvalidValueException extends MdxException {
    public AbstractInvalidValueException(XmlReader.Element tag, String invalidValue, Object[] availableValues) {
        super(buildMessage(tag, invalidValue, availableValues));
    }


    private static String buildMessage(XmlReader.Element tag, String invalidValue, Object[] values) {
        String availableValues = Arrays.stream(values)
                .map((v) -> "  - " + v.toString())
                .sorted()
                .collect(Collectors.joining("\n"));

        String prefix = tag.hasAttribute("id") ?
                getTagNameWithoutPrefix(tag) + " with id (" + tag.getAttribute("id") + ")"
                : getTagNameWithoutPrefix(tag);

        StringBuilder builder = new StringBuilder()
                .append(prefix)
                .append(" has an invalid value: ")
                .append(invalidValue)
                .append("\n\n")
                .append("Available Values:")
                .append("\n")
                .append(availableValues)
                .append("\n");

        return builder.toString();
    }
}
