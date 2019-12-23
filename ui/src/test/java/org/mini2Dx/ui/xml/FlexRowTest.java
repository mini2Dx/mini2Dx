package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.FlexRow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlexRowTest extends AbstractUiElementXmlTest<FlexRow> {
    @Test
    public void flex_row_with_overflow_clipped() {
        String xml = "<flex-row overflow-clipped=\"true\"/>";

        assertTrue(loadFileWithContainer(xml).isOverflowClipped());
    }

    @Test
    public void flex_row_with_flex_layout() {
        String xml = "<flex-row flex-layout=\"text\"/>";

        assertEquals("text", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void flex_row_with_flex_direction() {
        String xml = "<flex-row flex-direction=\"COLUMN_REVERSE\"/>";

        assertEquals("flex-column-reverse:xs-12c", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<flex-row xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "          id=\"test\"" +
                "          visibility=\"NO_RENDER\"" +
                "          width=\"200\"" +
                "          height=\"100\"" +
                "          x=\"10\"" +
                "          y=\"20\"" +
                "          style-id=\"test-style\"" +
                "          z-index=\"100\"" +
                "          flex-direction=\"CENTER\"" +
                "          flex-layout=\"flex-row:xs-12c\"" +
                "          overflow-clipped=\"true\">" +
                "" +
                "    <text-button id=\"x\" text=\"hello\" />" +
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
                "</flex-row>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "flex-row";
    }

    @Override
    protected void assertDefaultValues(FlexRow element) {
        assertEquals("flex-column:xs-12c", element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
    }

}