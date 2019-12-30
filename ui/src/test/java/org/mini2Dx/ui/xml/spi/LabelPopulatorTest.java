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
package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Label;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mini2Dx.ui.layout.HorizontalAlignment.CENTER;
import static org.mini2Dx.ui.layout.HorizontalAlignment.LEFT;
import static org.mini2Dx.ui.layout.HorizontalAlignment.RIGHT;

public class LabelPopulatorTest extends AbstractPopulatorTest {

    private LabelPopulator populator;
    private Label label;

    @Before
    public void setUp() {
        populator = new LabelPopulator();
        label = new Label();
    }

    @Test
    public void responsive_is_provided() {
        xmlTag.setAttribute("responsive", "true");

        populator.populate(xmlTag, label);

        assertTrue(label.isResponsive());
    }

    @Test
    public void responsive_is_defaulted_to_false() {
        populator.populate(xmlTag, label);

        assertFalse(label.isResponsive());
    }

    @Test
    public void horizontal_alignment_case_does_not_matter() {
        xmlTag.setAttribute("horizontal-alignment", "center");

        populator.populate(xmlTag, label);

        assertEquals(CENTER, label.getHorizontalAlignment());
    }

    @Test
    public void horizontal_alignment_CENTER() {
        xmlTag.setAttribute("horizontal-alignment", "CENTER");

        populator.populate(xmlTag, label);

        assertEquals(CENTER, label.getHorizontalAlignment());
    }

    @Test
    public void horizontal_alignment_RIGHT() {
        xmlTag.setAttribute("horizontal-alignment", "RIGHT");

        populator.populate(xmlTag, label);

        assertEquals(RIGHT, label.getHorizontalAlignment());
    }

    @Test
    public void default_horizontal_alignment() {
        populator.populate(xmlTag, label);

        assertEquals(LEFT, label.getHorizontalAlignment());
    }

    @Test
    public void text_provided() {
        xmlTag.setAttribute("text", "hello");

        populator.populate(xmlTag, label);

        assertEquals("hello", label.getText());
    }

    @Test
    public void no_text_provided() {
        populator.populate(xmlTag, label);

        assertEquals("", label.getText());
    }
}