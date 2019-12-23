package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.TextButton;

public class TextButtonPopulator implements UiXmlLoader.UiElementPopulator<TextButton> {
    @Override
    public void populate(XmlReader.Element xmlTag, TextButton uiElement) {
        uiElement.setText(xmlTag.getAttribute("text", ""));
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setFlexLayout(xmlTag.getAttribute("flex-layout", "flex-column:xs-12c"));
    }
}
