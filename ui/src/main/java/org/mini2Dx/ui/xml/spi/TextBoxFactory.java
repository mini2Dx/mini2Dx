package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.xml.UiElementFactory;

public class TextBoxFactory implements UiElementFactory<TextBox> {
    @Override
    public TextBox build(XmlReader.Element xmlTag) {
        return new TextBox(xmlTag.getAttribute("id", null));
    }
}
