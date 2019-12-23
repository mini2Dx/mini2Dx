package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Div;

public class DivFactory implements UiXmlLoader.UiElementFactory<Div> {
    @Override
    public Div build(XmlReader.Element xmlTag) {
        return new Div(xmlTag.getAttribute("id", null));
    }
}
