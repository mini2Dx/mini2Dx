package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class CheckboxPopulator implements UiElementPopulator<Checkbox> {
    @Override
    public void populate(XmlReader.Element xmlTag, Checkbox uiElement) {
        uiElement.setChecked(xmlTag.getBoolean("checked", false));
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setResponsive(xmlTag.getBoolean("responsive", false));
    }
}
