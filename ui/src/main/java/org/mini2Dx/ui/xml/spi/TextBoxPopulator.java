package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class TextBoxPopulator implements UiElementPopulator<TextBox> {
    @Override
    public void populate(XmlReader.Element xmlTag, TextBox uiElement) {
        uiElement.setValue(xmlTag.getAttribute("value", null));
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setPasswordField(xmlTag.getBoolean("password", false));
        uiElement.setFlexLayout(xmlTag.getAttribute("layout", "flex-column:xs-12c"));
    }
}
