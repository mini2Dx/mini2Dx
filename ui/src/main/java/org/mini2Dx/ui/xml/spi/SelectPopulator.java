package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class SelectPopulator implements UiElementPopulator<Select> {
    @Override
    public void populate(XmlReader.Element xmlTag, Select uiElement) {
        uiElement.setEnabled(xmlTag.getBooleanAttribute("enabled", true));
        uiElement.setFlexLayout(xmlTag.getAttribute("flex-layout", null));
        uiElement.setLeftButtonText(xmlTag.getAttribute("left-button-text", null));
        uiElement.setRightButtonText(xmlTag.getAttribute("right-button-text", null));

        for (int i = 0; i < xmlTag.getChildCount(); i++) {
            XmlReader.Element child = xmlTag.getChild(i);
            String value = child.getAttribute("value", child.getText());
            uiElement.addOption(child.getText(), value);
        }
    }
}
