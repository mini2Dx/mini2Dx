package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ParentUiElement;

public class ParentUiElementPopulator implements UiXmlLoader.UiElementPopulator<ParentUiElement> {
    @Override
    public void populate(XmlReader.Element xmlTag, ParentUiElement uiElement) {
        if (xmlTag.hasAttribute("flex-layout")) {
            uiElement.setFlexLayout(xmlTag.getAttribute("flex-layout"));
        }

        uiElement.setOverflowClipped(xmlTag.getBoolean("overflow-clipped", false));
    }
}
