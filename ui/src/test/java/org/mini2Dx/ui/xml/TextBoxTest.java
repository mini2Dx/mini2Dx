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
import org.mini2Dx.ui.element.TextBox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextBoxTest extends AbstractUiElementXmlTest<TextBox> {

    @Test
    public void text_box_password_field_provided() {
        TextBox element = loadFile("<text-box id=\"x\" password=\"true\" />");

        assertTrue(element.isPasswordField());
    }

    @Test
    public void text_box_character_limit_field_provided() {
        TextBox element = loadFile("<text-box id=\"x\" characterLimit=\"3\" />");

        assertEquals(element.getCharacterLimit(), 3);
    }

    @Test
    public void text_box_enabled_provided() {
        TextBox element = loadFile("<text-box id=\"x\" enabled=\"false\" />");

        assertFalse(element.isEnabled());
    }

    @Test
    public void text_box_flex_layout_provided() {
        TextBox element = loadFile("<text-box id=\"x\" layout=\"test\"/>");

        assertEquals("test", element.getFlexLayout());
    }

    @Test
    public void text_box_text_provided() {
        TextBox element = loadFile("<text-box id=\"x\" value=\"hello\"/>");

        assertEquals("hello", element.getValue());
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-box />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-box id=\"test\"" +
                "              z-index=\"100\"" +
                "              style=\"test-style\"" +
                "              y=\"100\"" +
                "              x=\"200\"" +
                "              width=\"2\"" +
                "              height=\"1\"" +
                "              visibility=\"HIDDEN\"" +
                "              password=\"true\"" +
                "              characterLimit=\"10\"" +
                "              value=\"hello\"" +
                "              enabled=\"false\"" +
                "              layout=\"blah\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "text-box";
    }

    @Override
    protected void assertDefaultValues(TextBox element) {
        assertFalse(element.isPasswordField());
        assertTrue(element.isEnabled());
        assertEquals("", element.getValue());
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
    }

}