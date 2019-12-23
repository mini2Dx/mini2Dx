package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Label;

public class LabelFactory implements UiXmlLoader.UiElementFactory<Label> {
    @Override
    public Label build(XmlReader.Element xmlTag) {
        return new Label(xmlTag.getAttribute("id", null));
    }
}
