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

import org.junit.Test;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.element.UiElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mini2Dx.ui.element.Visibility.VISIBLE;

public abstract class AbstractUiElementXmlTest<T extends UiElement> extends AbstractUiXmlLoaderTest {
    private UiXmlValidator uiXmlValidator = new UiXmlValidator();

    protected abstract String getUiElementTagName();

    protected abstract void assertDefaultValues(T element);

    protected ObjectMap<String, String> requiredAttributesForDefaultTestCases() {
        return new ObjectMap<>(0);
    }

    @Test
    public void defaults() {
        String xml = newBuilder().build();

        T element = loadFile(xml);

        assertNotNull(element.getId());
        assertEquals(VISIBLE, element.getVisibility());
        assertDefaultValues(element);
    }

    @Test
    public void id_provided() {
        String xml = newBuilder()
                .withAttribute("id", "10")
                .build();

        UiElement element = loadFile(xml);

        assertEquals("10", element.getId());
    }

    @Test
    public void with_a_style_id() {
        String xml = newBuilder()
                .withAttribute("style", "blah")
                .build();

        UiElement element = loadFile(xml);

        assertEquals("blah", element.getStyleId());
    }

    @Test
    public void with_a_z_index() {
        String xml = newBuilder()
                .withAttribute("z-index", "1")
                .build();

        UiElement element = loadFile(xml);

        assertEquals(1, element.getZIndex());
    }

    @Test
    public void with_width_and_height() {
        String xml = newBuilder()
                .withAttribute("width", "1")
                .withAttribute("height", "2")
                .build();

        UiElement element = loadFile(xml);

        assertEquals(1.0, element.getWidth(), 0.0);
        assertEquals(2.0, element.getHeight(), 0.0);
    }

    @Test
    public void with_x_and_y() {
        String xml = newBuilder()
                .withAttribute("x", "1")
                .withAttribute("y", "2")
                .build();

        UiElement element = loadFile(xml);

        assertEquals(1.0, element.getX(), 0.0);
        assertEquals(2.0, element.getY(), 0.0);
    }

    protected TestXmlUiBuilder newBuilder() {
        TestXmlUiBuilder builder = new TestXmlUiBuilder(getUiElementTagName());
        for (ObjectMap.Entry<String, String> entry : requiredAttributesForDefaultTestCases()) {
            builder.withAttribute(entry.key, entry.value);
        }
        return builder;
    }

    protected void assertAttributeRequired(String expectedAttributeName, String actualXml) {
        assertXmlIsInvalid(actualXml,
                "Attribute '" + expectedAttributeName
                        + "' must appear on element '" + getUiElementTagName() + "'");
    }

    protected void assertXmlIsValid(String xml) {
        uiXmlValidator.validateXmlContents(xml);
    }

    protected void assertXmlIsInvalid(String xml, String expectedMessage) {
        Exception exception = uiXmlValidator.invalidXml(xml);
        assertFalse("It looks like you forgot to include the XML schema in your XML",
                exception.getMessage().contains("Cannot find the declaration of element"));
        assertTrue("Actual message: [" + exception.getMessage() + "]", exception.getMessage().contains(expectedMessage));
    }
}
