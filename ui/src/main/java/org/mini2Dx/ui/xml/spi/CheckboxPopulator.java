package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Checkbox;

public class CheckboxPopulator implements UiXmlLoader.UiElementPopulator<Checkbox> {
    @Override
    public void populate(XmlReader.Element xmlTag, Checkbox uiElement) {
        uiElement.setChecked(xmlTag.getBoolean("checked", false));
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setResponsive(xmlTag.getBoolean("responsive", false));
    }
}
