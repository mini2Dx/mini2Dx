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
    private final String tagName;
    private ObjectMap<String, String> attributes = new ObjectMap<>();
    private Supplier<String> childTagSupplier = () -> "";
    private String prefix;

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
            return "    <" + getTagPrefix() + "text-button id=\"x\" text=\"hello\"/>" +
                    "    <" + getTagPrefix() + "label text=\"blah\"/>" +
                    "    <" + getTagPrefix() + "text-box id=\"x\"/>" +
                    "    <" + getTagPrefix() + "container/>" +
                    "    <" + getTagPrefix() + "flex-row/>" +
                    "    <" + getTagPrefix() + "div/>" +
                    "    <" + getTagPrefix() + "check-box id=\"x\"/>" +
                    "    <" + getTagPrefix() + "progress-bar id=\"x\"/>" +
                    "    <" + getTagPrefix() + "radio-button id=\"x\">" +
                    "      <" + getTagPrefix() + "option>1</" + getTagPrefix() + "option>" +
                    "      <" + getTagPrefix() + "option>2</" + getTagPrefix() + "option>" +
                    "    </" + getTagPrefix() + "radio-button>" +
                    "    <" + getTagPrefix() + "slider id=\"x\"/>" +
                    "    <" + getTagPrefix() + "select id=\"x\">" +
                    "      <" + getTagPrefix() + "option>1</" + getTagPrefix() + "option>" +
                    "      <" + getTagPrefix() + "option>2</" + getTagPrefix() + "option>" +
                    "    </" + getTagPrefix() + "select>" +
                    "    <" + getTagPrefix() + "image texture-path=\"x\"/>" +
                    "    <" + getTagPrefix() + "scroll-box />" +
                    "    <" + getTagPrefix() + "animated-image>" +
                    "      <" + getTagPrefix() + "texture duration=\"1\">foo</" + getTagPrefix() + "texture>" +
                    "      <" + getTagPrefix() + "texture duration=\"2\">bar</" + getTagPrefix() + "texture>" +
                    "    </" + getTagPrefix() + "animated-image>" +
                    "    <" + getTagPrefix() + "image-button id=\"1\">" +
                    "      <" + getTagPrefix() + "normal-texture>blah</" + getTagPrefix() + "normal-texture>" +
                    "    </" + getTagPrefix() + "image-button>" +
                    "    <" + getTagPrefix() + "tab-view>" +
                    "    </" + getTagPrefix() + "tab-view>" +
                    "    <" + getTagPrefix() + "button id=\"x\" />";

        });
    }

    public TestXmlUiBuilder withNamespacePrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("<")
                .append(getTagPrefix())
                .append(tagName)
                .append(" xmlns");

        if (applyPrefix()) {
            builder.append(":").append(prefix);
        }

        builder.append("=\"https://github.com/mini2Dx/mini2Dx\"");

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
        builder.append("</").append(getTagPrefix()).append(tagName).append(">");
        return builder.toString();
    }

    private String getTagPrefix() {
        return applyPrefix() ? prefix + ":" : "";
    }

    private boolean applyPrefix() {
        return prefix != null && prefix.trim().length() > 0;
    }
}
