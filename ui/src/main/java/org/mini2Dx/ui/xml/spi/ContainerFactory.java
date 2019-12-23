package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Container;

public class ContainerFactory implements UiXmlLoader.UiElementFactory<Container> {
    @Override
    public Container build(XmlReader.Element xmlTag) {
        return new Container(xmlTag.getAttribute("id", null));
    }
}
