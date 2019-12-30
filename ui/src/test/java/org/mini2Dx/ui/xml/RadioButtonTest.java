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
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.layout.FlexDirection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RadioButtonTest extends AbstractUiElementXmlTest<RadioButton> {

    @Test
    public void responsive_provided() {
        RadioButton element = loadFile("<radio-button id=\"x\" responsive=\"true\" />");

        assertTrue(element.isResponsive());
    }

    @Test
    public void enabled_provided() {
        RadioButton element = loadFile("<radio-button id=\"x\" enabled=\"false\" />");

        assertFalse(element.isEnabled());
    }

    @Test
    public void flex_direction_provided() {
        RadioButton element = loadFile("<radio-button id=\"x\" flex-direction=\"COLUMN\" />");

        assertEquals(FlexDirection.COLUMN, element.getFlexDirection());
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <radio-button />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void options_provided_with_default_selection() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<radio-button id=\"blah\" default-selected-index=\"2\">" +
                "  <option>0</option>" +
                "  <option>1</option>" +
                "  <option>2</option>" +
                "</radio-button>";

        RadioButton element = loadFile(xml);

        assertEquals("2", element.getSelectedOption());
        assertEquals(2, element.getSelectedOptionIndex());
    }

    @Test
    public void options_provided() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<radio-button id=\"blah\">" +
                "  <option>0</option>" +
                "  <option>1</option>" +
                "  <option>2</option>" +
                "</radio-button>";

        RadioButton element = loadFile(xml);

        for (int i = 0; i < 3; i++) {
            assertEquals(String.valueOf(i), element.getOption(i));
        }

        assertEquals("0", element.getSelectedOption());
        assertEquals(0, element.getSelectedOptionIndex());
    }

    @Test
    public void options_are_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <radio-button id=\"blah\">" +
                "    </radio-button>" +
                "</container>";

        assertXmlIsInvalid(xml, "The content of element 'radio-button' is not complete");
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <radio-button" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            responsive=\"true\"" +
                "            flex-direction=\"COLUMN\"" +
                "            enabled=\"false\"" +
                "            default-selected-index=\"0\"" +
                "    >" +
                "      <option></option>" +
                "      <option></option>" +
                "    </radio-button>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "radio-button";
    }

    @Override
    protected void assertDefaultValues(RadioButton element) {
        assertTrue(element.isEnabled());
        assertFalse(element.isResponsive());
        assertEquals(FlexDirection.ROW, element.getFlexDirection());
    }

}