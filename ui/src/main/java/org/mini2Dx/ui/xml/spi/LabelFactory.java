package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.xml.UiElementFactory;

public class LabelFactory implements UiElementFactory<Label> {
    @Override
    public Label build(XmlReader.Element xmlTag) {
        return new Label(xmlTag.getAttribute("id", null));
    }
}
