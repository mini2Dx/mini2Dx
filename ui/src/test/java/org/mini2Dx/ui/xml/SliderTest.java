package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Slider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SliderTest extends AbstractUiElementXmlTest<Slider> {

    @Test
    public void changed_on_begin_event_provided() {
        String xml = "<slider changed-on-begin-event=\"true\" />";

        Slider element = loadFile(xml);

        assertTrue(element.isChangedOnBeginEvent());
    }

    @Test
    public void value_step_provided() {
        String xml = "<slider value-step=\"0.25\" />";

        Slider element = loadFile(xml);

        assertEquals(0.25f, element.getValueStep(), 0.01f);
    }

    @Test
    public void value_provided() {
        String xml = "<slider value=\"0.5\" />";

        Slider element = loadFile(xml);

        assertEquals(0.5f, element.getValue(), 0.01f);
    }

    @Test
    public void enabled_provided() {
        String xml = "<slider enabled=\"false\" />";

        Slider element = loadFile(xml);

        assertFalse(element.isEnabled());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <slider" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            enabled=\"false\"" +
                "            value=\"0.25\"" +
                "            value-step=\"0.0001\"" +
                "            changed-on-begin-event=\"false\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <slider/>" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void value_can_not_be_less_than_zero() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <slider id=\"\" value=\"-1\"/>" +
                "</container>";

        assertXmlIsInvalid(xml, "Value '-1' is not facet-valid with respect to minInclusive '0.0E1'");
    }

    @Test
    public void value_can_not_be_greater_than_one() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <slider id=\"\" value=\"1.1\"/>" +
                "</container>";

        assertXmlIsInvalid(xml, "Value '1.1' is not facet-valid with respect to maxInclusive '1.0E0'");
    }

    @Override
    protected String getUiElementTagName() {
        return "slider";
    }

    @Override
    protected void assertDefaultValues(Slider element) {
        assertEquals(0, element.getValue(), 0.01);
        assertEquals(0.1f, element.getValueStep(), 0.01);
        assertFalse(element.isChangedOnBeginEvent());
        assertTrue(element.isEnabled());
    }

}