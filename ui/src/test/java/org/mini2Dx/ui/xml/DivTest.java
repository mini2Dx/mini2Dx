package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Div;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DivTest extends AbstractUiElementXmlTest<Div> {
    @Test
    public void div_with_overflow_clipped() {
        String xml = "<div overflow-clipped=\"true\"/>";

        assertTrue(loadFileWithContainer(xml).isOverflowClipped());
    }

    @Test
    public void div_with_flex_layout() {
        String xml = "<div flex-layout=\"text\"/>";

        assertEquals("text", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<div xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "     id=\"test\"" +
                "     visibility=\"NO_RENDER\"" +
                "     width=\"200\"" +
                "     height=\"100\"" +
                "     x=\"10\"" +
                "     y=\"20\"" +
                "     style-id=\"test-style\"" +
                "     z-index=\"100\"" +
                "     flex-layout=\"flex-row:xs-12c\"" +
                "     overflow-clipped=\"true\">" +
                "" +
                "    <text-button id=\"x\" text=\"hello\"/>" +
                "    <label text=\"blah\"/>" +
                "    <text-box id=\"x\"/>" +
                "    <container/>" +
                "    <flex-row/>" +
                "    <div/>" +
                "    <check-box id=\"x\"/>" +
                "    <progress-bar id=\"x\"/>" +
                "    <radio-button id=\"x\">" +
                "      <option>1</option>" +
                "      <option>2</option>" +
                "    </radio-button>" +
                "    <slider id=\"x\"/>" +
                "</div>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "div";
    }

    @Override
    protected void assertDefaultValues(Div element) {
        assertNull(element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
    }

}