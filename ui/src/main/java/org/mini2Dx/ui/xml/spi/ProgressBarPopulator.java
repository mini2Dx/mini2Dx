package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.xml.UiXmlLoader;

public class ProgressBarPopulator implements UiXmlLoader.UiElementPopulator<ProgressBar> {
    @Override
    public void populate(XmlReader.Element xmlTag, ProgressBar uiElement) {
        uiElement.setFlexLayout(xmlTag.getAttribute("flex-layout", null));
        uiElement.setMax(xmlTag.getFloatAttribute("max", 1.0f));
        uiElement.setMin(xmlTag.getFloatAttribute("min", 0.0f));
    }
}
