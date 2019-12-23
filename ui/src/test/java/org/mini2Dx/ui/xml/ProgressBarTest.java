package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.ProgressBar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProgressBarTest extends AbstractUiElementXmlTest<ProgressBar> {

    @Test
    public void set_flex_layout() {
        ProgressBar element = loadFile("<progress-bar layout=\"blah\" />");

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void set_min_and_max() {
        ProgressBar element = loadFile("<progress-bar min=\"1\" max=\"100\" />");

        assertEquals(1, element.getMin(), 0.01);
        assertEquals(100, element.getMax(), 0.01);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <progress-bar" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            min=\"1\"" +
                "            max=\"1000\"" +
                "            layout=\"does-not-matter\"" +
                "    />" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <progress-bar/>" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "progress-bar";
    }

    @Override
    protected void assertDefaultValues(ProgressBar element) {
        assertNull(element.getFlexLayout());
        assertEquals(0, element.getValue(), 0.01);
        assertEquals(0, element.getMin(), 0.01);
        assertEquals(1, element.getMax(), 0.01);
    }

}