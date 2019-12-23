package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class ProgressBarPopulator implements UiElementPopulator<ProgressBar> {
    @Override
    public void populate(XmlReader.Element xmlTag, ProgressBar uiElement) {
        uiElement.setFlexLayout(xmlTag.getAttribute("layout", null));
        uiElement.setMax(xmlTag.getFloatAttribute("max", 1.0f));
        uiElement.setMin(xmlTag.getFloatAttribute("min", 0.0f));
    }
}
