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
package org.mini2Dx.ui.xml;

import org.mini2Dx.gdx.utils.ObjectMap;

import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;

public class TestXmlUiBuilder {
    private static final String NAMESPACE = " xmlns=\"https://github.com/mini2Dx/mini2Dx\"";
    private final String tagName;
    private ObjectMap<String, String> attributes = new ObjectMap<>();
    private Supplier<String> childTagSupplier = () -> "";

    public TestXmlUiBuilder(String tagName) {
        this.tagName = tagName;
    }

    public TestXmlUiBuilder withAttribute(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public TestXmlUiBuilder withChildTagSupplier(Supplier<String> supplier) {
        assertNotNull(supplier);
        childTagSupplier = supplier;
        return this;
    }

    public TestXmlUiBuilder withAllComponentTagsAsChildren() {
        return withChildTagSupplier(() -> {
            return "    <text-button id=\"x\" text=\"hello\"/>" +
                    "    <label text=\"blah\"/>" +
                    "    <text-box id=\"x\"/>" +
                    "    <container/>" +
                    "    <flex-row/>" +
                    "    <div/>" +
                    "    <check-box id=\"x\"/>" +
                    "    <progress-bar id=\"x\"/>" +
                    "    <radio-button id=\"x\">" +
                    "      <option>1</option>" +
                    "      <option>2</option>" +
                    "    </radio-button>" +
                    "    <slider id=\"x\"/>" +
                    "    <select id=\"x\">" +
                    "      <option>1</option>" +
                    "      <option>2</option>" +
                    "    </select>" +
                    "    <image texture-path=\"x\"/>" +
                    "    <scroll-box />" +
                    "    <animated-image>" +
                    "      <texture duration=\"1\">foo</texture>" +
                    "      <texture duration=\"2\">bar</texture>" +
                    "    </animated-image>" +
                    "    <image-button id=\"1\">" +
                    "      <normal-texture>blah</normal-texture>" +
                    "    </image-button>" +
                    "    <tab-view>" +
                    "    </tab-view>" +
                    "    <button id=\"x\" />";

        });
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("<")
                .append(tagName)
                .append(NAMESPACE);

        for (ObjectMap.Entry<String, String> entry : attributes) {
            builder.append(" ")
                    .append(entry.key)
                    .append("=")
                    .append("\"")
                    .append(entry.value)
                    .append("\"");
        }
        builder.append(">");

        builder.append(childTagSupplier.get());
        builder.append("</").append(tagName).append(">");
        return builder.toString();
    }
}
