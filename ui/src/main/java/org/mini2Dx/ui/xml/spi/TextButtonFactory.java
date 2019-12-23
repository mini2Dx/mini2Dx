package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.TextButton;

public class TextButtonFactory implements UiXmlLoader.UiElementFactory<TextButton> {
    @Override
    public TextButton build(XmlReader.Element xmlTag) {
        return new TextButton(xmlTag.getAttribute("id", null));
    }
}
