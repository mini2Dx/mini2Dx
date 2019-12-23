package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.TextButton;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextButtonTest extends AbstractUiElementXmlTest<TextButton> {

    @Test
    public void text_button_disabled() {
        TextButton element = loadFile("<text-button enabled=\"false\"/>");

        assertFalse(element.isEnabled());
    }

    @Test
    public void text_button_with_flex_layout() {
        TextButton element = loadFile("<text-button flex-layout=\"blah\"/>");

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void text_button_text_provided() {
        TextButton element = loadFile("<text-button text=\"hello\"/>");

        assertEquals("hello", element.getText());
    }

    @Test
    public void text_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-button id=\"hello\"/>" +
                "</container>";

        assertAttributeRequired("text", xml);
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-button text=\"hello\" />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-button id=\"test\"" +
                "                 z-index=\"100\"" +
                "                 style-id=\"test-style\"" +
                "                 y=\"100\"" +
                "                 x=\"200\"" +
                "                 width=\"2\"" +
                "                 height=\"1\"" +
                "                 visibility=\"HIDDEN\"" +
                "                 text=\"hello\"" +
                "                 enabled=\"false\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "text-button";
    }

    @Override
    protected void assertDefaultValues(TextButton element) {
        assertTrue(element.isEnabled());
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertEquals("", element.getText());
    }

}