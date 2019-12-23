package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Container;
import org.mini2Dx.ui.xml.UiElementFactory;

public class ContainerFactory implements UiElementFactory<Container> {
    @Override
    public Container build(XmlReader.Element xmlTag) {
        return new Container(xmlTag.getAttribute("id", null));
    }
}
