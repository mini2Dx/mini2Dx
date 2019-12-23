package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.layout.HorizontalAlignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mini2Dx.ui.layout.HorizontalAlignment.LEFT;

public class LabelTest extends AbstractUiElementXmlTest<Label> {

    @Test
    public void label_responsive() {
        Label element = loadFile("<label responsive=\"true\" text=\"hello\"/>");

        assertTrue(element.isResponsive());
    }

    @Test
    public void label_horizontal_alignment() {
        Label element = loadFile("<label horizontal-alignment=\"CENTER\" text=\"hello\"/>");

        assertEquals(HorizontalAlignment.CENTER, element.getHorizontalAlignment());
    }

    @Test
    public void label_text_provided() {
        Label element = loadFile("<label text=\"hello\"/>");

        assertEquals("hello", element.getText());
    }

    @Test
    public void label_text_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <label />" +
                "</container>";

        assertAttributeRequired("text", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <label id=\"test\"" +
                "           z-index=\"100\"" +
                "           style-id=\"test-style\"" +
                "           y=\"100\"" +
                "           x=\"200\"" +
                "           width=\"2\"" +
                "           height=\"1\"" +
                "           visibility=\"HIDDEN\"" +
                "           text=\"hello\"" +
                "           responsive=\"true\"" +
                "           horizontal-alignment=\"LEFT\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "label";
    }

    @Override
    protected void assertDefaultValues(Label element) {
        assertFalse(element.isResponsive());
        assertEquals("", element.getText());
        assertEquals(LEFT, element.getHorizontalAlignment());
    }

}