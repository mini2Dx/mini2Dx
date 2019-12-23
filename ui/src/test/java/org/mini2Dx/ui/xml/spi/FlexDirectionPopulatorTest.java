package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.FlexRow;
import org.mini2Dx.ui.layout.FlexDirection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FlexDirectionPopulatorTest {
    private FlexDirectionPopulator populator;
    private XmlReader.Element tag;
    private FlexRow uiElement;

    @Before
    public void setUp() {
        populator = new FlexDirectionPopulator();
        tag = new XmlReader.Element("tag-name", null);
        uiElement = new FlexRow();
    }

    @Test
    public void unsupported_flex_direction() {
        tag.setAttribute("flex-direction", "does-not-exist");

        try {
            populator.populate(tag, uiElement);
            fail();
        } catch (InvalidFlexDirectionException e) {
            assertTrue(e.getMessage().startsWith("tag-name has an invalid value: does-not-exist"));
        }
    }

    @Test
    public void flex_directions_case_insensitive() {
        assertDirection("column", "flex-column:xs-12c");
        assertDirection("ColUmn", "flex-column:xs-12c");
    }

    @Test
    public void flex_directions() {
        assertDirection(FlexDirection.COLUMN.name(), "flex-column:xs-12c");
        assertDirection(FlexDirection.COLUMN_REVERSE.name(), "flex-column-reverse:xs-12c");
        assertDirection(FlexDirection.CENTER.name(), "flex-center:xs-12c");
        assertDirection(FlexDirection.ROW.name(), "flex-row:xs-12c");
        assertDirection(FlexDirection.ROW_REVERSE.name(), "flex-row-reverse:xs-12c");
    }

    private void assertDirection(String tagValue, String expectedResult) {
        tag.setAttribute("flex-direction", tagValue);

        populator.populate(tag, uiElement);

        assertEquals(expectedResult, uiElement.getFlexLayout());
    }
}