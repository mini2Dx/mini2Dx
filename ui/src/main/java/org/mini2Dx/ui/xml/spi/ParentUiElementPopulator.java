package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class ParentUiElementPopulator implements UiElementPopulator<ParentUiElement> {
    @Override
    public void populate(XmlReader.Element xmlTag, ParentUiElement uiElement) {
        if (xmlTag.hasAttribute("layout")) {
            uiElement.setFlexLayout(xmlTag.getAttribute("layout"));
        }

        uiElement.setOverflowClipped(xmlTag.getBoolean("overflow-clipped", false));
    }
}
