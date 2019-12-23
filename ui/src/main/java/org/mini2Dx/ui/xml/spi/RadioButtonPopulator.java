package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.xml.UiElementPopulator;

import static org.mini2Dx.ui.xml.spi.XmlAttributeMapper.mapToFlexDirection;

public class RadioButtonPopulator implements UiElementPopulator<RadioButton> {
    @Override
    public void populate(XmlReader.Element xmlTag, RadioButton uiElement) {
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setResponsive(xmlTag.getBoolean("responsive", false));

        if (xmlTag.hasAttribute("flex-direction")) {
            uiElement.setFlexDirection(mapToFlexDirection(xmlTag));
        }

        for (int i = 0; i < xmlTag.getChildCount(); i++) {
            XmlReader.Element child = xmlTag.getChild(i);
            uiElement.addOption(child.getText());
        }

        uiElement.setSelectedOptionIndex(xmlTag.getIntAttribute("default-selected-index", 0));
    }
}
