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
import org.mini2Dx.ui.element.Button;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ButtonTest extends AbstractParentUiElementXmlTest<Button> {

    @Test
    public void with_enabled_provided() {
        String xml = "<button id=\"x\" enabled=\"false\" />";

        Button element = loadFile(xml);

        assertFalse(element.isEnabled());
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<button xmlns=\"https://github.com/mini2Dx/mini2Dx\"/>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<button xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "     id=\"test\"" +
                "     visibility=\"NO_RENDER\"" +
                "     width=\"200\"" +
                "     height=\"100\"" +
                "     x=\"10\"" +
                "     y=\"20\"" +
                "     style=\"test-style\"" +
                "     z-index=\"100\"" +
                "     layout=\"flex-row:xs-12c\"" +
                "     overflow-clipped=\"true\"" +
                "     enabled=\"false\"" +
                "/>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "button";
    }

    @Override
    protected void assertDefaultValues(Button element) {
        assertNull(element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
        assertTrue(element.isEnabled());
    }

    @Override
    protected ObjectMap<String, String> requiredAttributesForDefaultTestCases() {
        ObjectMap<String, String> attribs = new ObjectMap<>();
        attribs.put("id", "1234");
        return attribs;
    }
}