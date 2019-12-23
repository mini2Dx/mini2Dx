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