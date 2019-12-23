package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Slider;
import org.mini2Dx.ui.xml.UiElementFactory;

public class SliderFactory implements UiElementFactory<Slider> {
    @Override
    public Slider build(XmlReader.Element xmlTag) {
        return new Slider(xmlTag.getAttribute("id", null));
    }
}
