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
import org.mini2Dx.ui.element.Checkbox;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckboxTest extends AbstractUiElementXmlTest<Checkbox> {

    @Test
    public void check_box_checked_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" checked=\"true\" />");

        assertTrue(element.isChecked());
    }

    @Test
    public void check_box_responsive_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" responsive=\"true\" />");

        assertTrue(element.isResponsive());
    }

    @Test
    public void check_box_enabled_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" enabled=\"false\" />");

        assertFalse(element.isEnabled());
    }

    @Test
    public void check_box_id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <check-box />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <check-box" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            checked=\"true\"" +
                "            responsive=\"true\"" +
                "            enabled=\"false\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "check-box";
    }

    @Override
    protected void assertDefaultValues(Checkbox element) {
        assertTrue(element.isEnabled());
        assertFalse(element.isChecked());
        assertFalse(element.isResponsive());
    }

}