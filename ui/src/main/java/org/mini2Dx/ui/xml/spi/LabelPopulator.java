package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class LabelPopulator implements UiElementPopulator<Label> {
    @Override
    public void populate(XmlReader.Element xmlTag, Label uiElement) {
        if (xmlTag.hasAttribute("text")) {
            uiElement.setText(xmlTag.getAttribute("text"));
        }

        uiElement.setResponsive(xmlTag.getBoolean("responsive", false));

        String providedHorizontalAlignment = xmlTag.get("horizontal-alignment", "LEFT").toUpperCase();
        uiElement.setHorizontalAlignment(HorizontalAlignment.valueOf(providedHorizontalAlignment));
    }
}
