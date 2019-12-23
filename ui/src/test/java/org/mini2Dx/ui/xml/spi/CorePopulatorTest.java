package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.element.Visibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mini2Dx.ui.element.Visibility.HIDDEN;
import static org.mini2Dx.ui.element.Visibility.VISIBLE;
import static org.mini2Dx.ui.element.Visibility.values;

public class CorePopulatorTest extends AbstractPopulatorTest {

    private CorePopulator corePopulator;
    private Container container;

    @Before
    public void setUp() {
        corePopulator = new CorePopulator();
        container = new Container();
    }

    @Test
    public void unknown_visibility() {
        xmlTag.setAttribute("visibility", "does-not-exist");

        try {
            corePopulator.populate(xmlTag, container);
            fail();
        } catch (InvalidVisibilityException e) {
            assertTrue(e.getMessage().startsWith("tag-name has an invalid value: does-not-exist"));
        }
    }

    @Test
    public void visibility_defaulted_to_visible() {
        testVisibilityValue(null, VISIBLE);
    }

    @Test
    public void visibility_case_is_not_important() {
        testVisibilityValue("hidden", HIDDEN);
    }

    @Test
    public void visibility_for_all_values() {
        for (Visibility value : values()) {
            testVisibilityValue(value.name(), value);
        }
    }

    private void testVisibilityValue(String xmlValue, Visibility expectedValue) {
        xmlTag.setAttribute("visibility", xmlValue);

        corePopulator.populate(xmlTag, container);

        assertEquals(expectedValue, container.getVisibility());
    }
}