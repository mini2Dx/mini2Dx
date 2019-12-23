package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.TextBox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextBoxTest extends AbstractUiElementXmlTest<TextBox> {

    @Test
    public void text_box_password_field_provided() {
        TextBox element = loadFile("<text-box id=\"x\" password=\"true\" />");

        assertTrue(element.isPasswordField());
    }

    @Test
    public void text_box_enabled_provided() {
        TextBox element = loadFile("<text-box id=\"x\" enabled=\"false\" />");

        assertFalse(element.isEnabled());
    }

    @Test
    public void text_box_flex_layout_provided() {
        TextBox element = loadFile("<text-box id=\"x\" flex-layout=\"test\"/>");

        assertEquals("test", element.getFlexLayout());
    }

    @Test
    public void text_box_text_provided() {
        TextBox element = loadFile("<text-box id=\"x\" value=\"hello\"/>");

        assertEquals("hello", element.getValue());
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-box />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <text-box id=\"test\"" +
                "              z-index=\"100\"" +
                "              style-id=\"test-style\"" +
                "              y=\"100\"" +
                "              x=\"200\"" +
                "              width=\"2\"" +
                "              height=\"1\"" +
                "              visibility=\"HIDDEN\"" +
                "              password=\"true\"" +
                "              value=\"hello\"" +
                "              enabled=\"false\"" +
                "              flex-layout=\"blah\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "text-box";
    }

    @Override
    protected void assertDefaultValues(TextBox element) {
        assertFalse(element.isPasswordField());
        assertTrue(element.isEnabled());
        assertEquals("", element.getValue());
    }

}