package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.layout.FlexDirection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mini2Dx.ui.layout.FlexDirection.COLUMN;
import static org.mini2Dx.ui.layout.FlexDirection.values;
import static org.mini2Dx.ui.xml.spi.XmlAttributeMapper.mapToFlexDirection;

public class XmlAttributeMapperTest {
    private XmlReader.Element tag;

    @Before
    public void setUp() {
        tag = new XmlReader.Element("tag-name", null);
    }

    @Test
    public void mapToFlexDirection_unsupported_flex_direction() {
        tag.setAttribute("flex-direction", "does-not-exist");

        try {
            mapToFlexDirection(tag);
            fail();
        } catch (InvalidFlexDirectionException e) {
            assertTrue(e.getMessage().startsWith("tag-name has an invalid value: does-not-exist"));
        }
    }

    @Test
    public void mapToFlexDirection_flex_directions_case_insensitive() {
        tag.setAttribute("flex-direction", "ColUmn");

        assertEquals(COLUMN, mapToFlexDirection(tag));
    }

    @Test
    public void mapToFlexDirection_all_directions() {
        for (FlexDirection value : values()) {
            tag.setAttribute("flex-direction", value.name());
            assertEquals(value, mapToFlexDirection(tag));
        }
    }

}