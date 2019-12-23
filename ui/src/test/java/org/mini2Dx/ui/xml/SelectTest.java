package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.SelectOption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SelectTest extends AbstractUiElementXmlTest<Select<String>> {
    @Test
    public void right_button_text_is_provided() {
        String xml = "<select right-button-text=\"blah\" />";

        Select element = loadFile(xml);

        assertEquals("blah", element.getRightButtonText());
    }

    @Test
    public void left_button_text_is_provided() {
        String xml = "<select left-button-text=\"blah\" />";

        Select element = loadFile(xml);

        assertEquals("blah", element.getLeftButtonText());
    }

    @Test
    public void flex_layout_is_provided() {
        String xml = "<select layout=\"blah\" />";

        Select element = loadFile(xml);

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void enabled_is_provided() {
        String xml = "<select enabled=\"false\" />";

        Select element = loadFile(xml);

        assertFalse(element.isEnabled());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <select" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            enabled=\"false\"" +
                "            layout=\"blah\"" +
                "            left-button-text=\"foo\"" +
                "            right-button-text=\"bar\"" +
                "    >" +
                "       <option value=\"0\">text-0</option>" +
                "       <option value=\"1\">text-1</option>" +
                "       <option value=\"2\">text-2</option>" +
                "  </select>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Test
    public void options_provided() {
        String xml = "<?xml version=\"1.0\"?>" +
                "    <select id=\"test\">" +
                "       <option value=\"0\">text-0</option>" +
                "       <option value=\"1\">text-1</option>" +
                "       <option value=\"2\">text-2</option>" +
                "    </select>";

        Select element = loadFile(xml);

        for (int i = 0; i < 3; i++) {
            SelectOption option = element.getOption(i);

            assertEquals("text-" + i, option.getLabel());
            assertEquals(String.valueOf(i), option.getValue());
        }

        assertEquals("text-0", element.getSelectedLabel());
        assertEquals("0", element.getSelectedValue());
        assertEquals(3, element.getTotalOptions());
    }

    @Test
    public void options_provided_with_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "    <select id=\"test\">" +
                "       <option>text-0</option>" +
                "       <option>text-1</option>" +
                "       <option>text-2</option>" +
                "    </select>";

        Select element = loadFile(xml);

        for (int i = 0; i < 3; i++) {
            SelectOption option = element.getOption(i);

            assertEquals("text-" + i, option.getLabel());
            assertEquals("text-" + i, option.getValue());
        }
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <select/>" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "select";
    }

    @Override
    protected void assertDefaultValues(Select<String> element) {
        assertTrue(element.isEnabled());
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertNull(element.getLeftButtonText());
        assertNull(element.getRightButtonText());
    }

}