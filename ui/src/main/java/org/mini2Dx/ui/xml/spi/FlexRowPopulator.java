package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.FlexRow;
import org.mini2Dx.ui.xml.UiElementPopulator;

import static org.mini2Dx.ui.xml.spi.XmlAttributeMapper.mapToFlexDirection;

public class FlexRowPopulator implements UiElementPopulator<FlexRow> {
    @Override
    public void populate(XmlReader.Element xmlTag, FlexRow uiElement) {
        if (xmlTag.hasAttribute("flex-direction")) {
            uiElement.setFlexDirection(mapToFlexDirection(xmlTag));
        }
    }
}
