package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.xml.UiElementFactory;

public class RadioButtonFactory implements UiElementFactory<RadioButton> {
    @Override
    public RadioButton build(XmlReader.Element xmlTag) {
        return new RadioButton(xmlTag.getAttribute("id", null));
    }
}
