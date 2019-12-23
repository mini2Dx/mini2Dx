package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.FlexRow;
import org.mini2Dx.ui.layout.FlexDirection;

public class FlexDirectionPopulator implements UiXmlLoader.UiElementPopulator<FlexRow> {
    @Override
    public void populate(XmlReader.Element xmlTag, FlexRow uiElement) {
        if (xmlTag.hasAttribute("flex-direction")) {
            String providedValue = xmlTag.getAttribute("flex-direction");
            try {
                uiElement.setFlexDirection(FlexDirection.valueOf(providedValue.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidFlexDirectionException(xmlTag, providedValue);
            }
        }
    }
}
