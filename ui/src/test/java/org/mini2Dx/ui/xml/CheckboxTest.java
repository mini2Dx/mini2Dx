package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Checkbox;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckboxTest extends AbstractUiElementXmlTest<Checkbox> {

    @Test
    public void check_box_checked_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" checked=\"true\" />");

        assertTrue(element.isChecked());
    }

    @Test
    public void check_box_responsive_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" responsive=\"true\" />");

        assertTrue(element.isResponsive());
    }

    @Test
    public void check_box_enabled_provided() {
        Checkbox element = loadFile("<check-box id=\"x\" enabled=\"false\" />");

        assertFalse(element.isEnabled());
    }

    @Test
    public void check_box_id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <check-box />" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <check-box" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style-id=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            checked=\"true\"" +
                "            responsive=\"true\"" +
                "            enabled=\"false\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "check-box";
    }

    @Override
    protected void assertDefaultValues(Checkbox element) {
        assertTrue(element.isEnabled());
        assertFalse(element.isChecked());
        assertFalse(element.isResponsive());
    }

}