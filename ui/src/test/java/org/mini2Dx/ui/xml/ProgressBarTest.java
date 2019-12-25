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
import org.mini2Dx.ui.element.ProgressBar;

import static org.junit.Assert.assertEquals;

public class ProgressBarTest extends AbstractUiElementXmlTest<ProgressBar> {

    @Test
    public void set_flex_layout() {
        ProgressBar element = loadFile("<progress-bar layout=\"blah\" />");

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void set_min_and_max() {
        ProgressBar element = loadFile("<progress-bar min=\"1\" max=\"100\" />");

        assertEquals(1, element.getMin(), 0.01);
        assertEquals(100, element.getMax(), 0.01);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <progress-bar" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            min=\"1\"" +
                "            max=\"1000\"" +
                "            layout=\"does-not-matter\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <progress-bar/>" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "progress-bar";
    }

    @Override
    protected void assertDefaultValues(ProgressBar element) {
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertEquals(0, element.getValue(), 0.01);
        assertEquals(0, element.getMin(), 0.01);
        assertEquals(1, element.getMax(), 0.01);
    }

}