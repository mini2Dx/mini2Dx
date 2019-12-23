package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.Container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ContainerTest extends AbstractUiElementXmlTest<Container> {

    @Test
    public void container_with_overflow_clipped() {
        String xml = "<container overflow-clipped=\"true\"/>";

        assertTrue(loadFileWithContainer(xml).isOverflowClipped());
    }

    @Test
    public void container_with_flex_layout() {
        String xml = "<container layout=\"text\"/>";

        assertEquals("text", loadFileWithContainer(xml).getFlexLayout());
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "           id=\"test\"" +
                "           z-index=\"100\"" +
                "           style=\"test-style\"" +
                "           y=\"100\"" +
                "           x=\"200\"" +
                "           width=\"2\"" +
                "           height=\"1\"" +
                "           visibility=\"HIDDEN\"" +
                "           layout=\"flex-row:xs-12c\"" +
                "           overflow-clipped=\"true\">" +
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
                "    <select id=\"x\">" +
                "      <option>1</option>" +
                "      <option>2</option>" +
                "    </select>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "container";
    }

    @Override
    protected void assertDefaultValues(Container element) {
        assertNull(element.getFlexLayout());
        assertFalse(element.isOverflowClipped());
    }

}