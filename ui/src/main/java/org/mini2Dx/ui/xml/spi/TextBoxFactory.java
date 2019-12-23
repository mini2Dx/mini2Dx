package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.TextBox;

public class TextBoxFactory implements UiXmlLoader.UiElementFactory<TextBox> {
    @Override
    public TextBox build(XmlReader.Element xmlTag) {
        return new TextBox(xmlTag.getAttribute("id", null));
    }
}
