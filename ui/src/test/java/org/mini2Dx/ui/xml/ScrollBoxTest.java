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
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.Visibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ScrollBoxTest extends AbstractParentUiElementXmlTest<ScrollBox> {

    @Test
    public void scroll_track_visibility_provided() {
        String xml = newBuilder()
                .withAttribute("scroll-track-visibility", "HIDDEN")
                .build();

        ScrollBox element = loadFile(xml);

        assertEquals(Visibility.HIDDEN, element.getScrollTrackVisibility());
    }

    @Test
    public void max_height_provided() {
        String xml = newBuilder()
                .withAttribute("max-height", "1")
                .build();

        ScrollBox element = loadFile(xml);

        assertEquals(1f, element.getMaxHeight(), 0.0001);
    }

    @Test
    public void min_height_provided() {
        String xml = newBuilder()
                .withAttribute("min-height", "1")
                .build();

        ScrollBox element = loadFile(xml);

        assertEquals(1f, element.getMinHeight(), 0.0001);
    }

    @Test
    public void scroll_factor_provided() {
        String xml = newBuilder()
                .withAttribute("scroll-factor", "1")
                .build();

        ScrollBox element = loadFile(xml);

        assertEquals(1f, element.getScrollFactor(), 0.0001);
    }

    @Test
    public void scroll_factor_can_not_be_alpha() {
        String xml = newBuilder()
                .withAttribute("scroll-factor", "abc")
                .build();

        assertXmlIsInvalid(xml, "'abc' is not a valid value for 'float'");
    }

    @Test
    public void min_height_can_not_be_alpha() {
        String xml = newBuilder()
                .withAttribute("min-height", "xxx")
                .build();

        assertXmlIsInvalid(xml, "'xxx' is not a valid value for 'float'");
    }

    @Test
    public void max_height_can_not_be_alpha() {
        String xml = newBuilder()
                .withAttribute("max-height", "xyz")
                .build();

        assertXmlIsInvalid(xml, "'xyz' is not a valid value for 'float'");
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<scroll-box xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "           id=\"test\"" +
                "           z-index=\"100\"" +
                "           style=\"test-style\"" +
                "           y=\"100\"" +
                "           x=\"200\"" +
                "           width=\"2\"" +
                "           height=\"1\"" +
                "           visibility=\"HIDDEN\"" +
                "           layout=\"flex-row:xs-12c\"" +
                "           overflow-clipped=\"true\"" +
                "           scroll-factor=\"0.1\"" +
                "           min-height=\"0.1\"" +
                "           max-height=\"0.1\"" +
                "           scroll-track-visibility=\"HIDDEN\"" +
                "/>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "scroll-box";
    }

    @Override
    protected void assertDefaultValues(ScrollBox element) {
        assertNull(element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
        assertEquals(Visibility.VISIBLE, element.getScrollTrackVisibility());
        assertEquals(Float.MAX_VALUE, element.getMaxHeight(), 0.00001);
        assertEquals(Float.MIN_VALUE, element.getMinHeight(), 0.00001);
        assertEquals(0.005f, element.getScrollFactor(), 0.00001);
    }

}