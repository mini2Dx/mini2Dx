package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Slider;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class SliderPopulator implements UiElementPopulator<Slider> {
    @Override
    public void populate(XmlReader.Element xmlTag, Slider uiElement) {
        uiElement.setEnabled(xmlTag.getBooleanAttribute("enabled", true));
        uiElement.setValue(xmlTag.getFloatAttribute("value", 0.0f));
        uiElement.setValueStep(xmlTag.getFloatAttribute("value-step", 0.1f));
        uiElement.setChangedOnBeginEvent(xmlTag.getBooleanAttribute("changed-on-begin-event", false));
    }
}
