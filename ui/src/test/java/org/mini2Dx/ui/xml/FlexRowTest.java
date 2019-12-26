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
import org.mini2Dx.ui.element.FlexRow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlexRowTest extends AbstractUiElementXmlTest<FlexRow> {
    @Test
    public void flex_row_with_overflow_clipped() {
        String xml = "<flex-row overflow-clipped=\"true\"/>";

        assertTrue(loadFileWithContainer(xml).isOverflowClipped());
    }

    @Test
    public void flex_row_with_flex_layout() {
        String xml = "<flex-row layout=\"text\"/>";

        assertEquals("text", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void flex_row_with_flex_direction() {
        String xml = "<flex-row flex-direction=\"COLUMN_REVERSE\"/>";

        assertEquals("flex-column-reverse:xs-12c", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<flex-row xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "          id=\"test\"" +
                "          visibility=\"NO_RENDER\"" +
                "          width=\"200\"" +
                "          height=\"100\"" +
                "          x=\"10\"" +
                "          y=\"20\"" +
                "          style=\"test-style\"" +
                "          z-index=\"100\"" +
                "          flex-direction=\"CENTER\"" +
                "          layout=\"flex-row:xs-12c\"" +
                "          overflow-clipped=\"true\">" +
                "" +
                "    <text-button id=\"x\" text=\"hello\" />" +
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
                "</flex-row>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "flex-row";
    }

    @Override
    protected void assertDefaultValues(FlexRow element) {
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
    }

}