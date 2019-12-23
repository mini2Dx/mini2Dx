package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.xml.UiElementFactory;

public class SelectFactory implements UiElementFactory<Select> {
    @Override
    public Select build(XmlReader.Element xmlTag) {
        return new Select(xmlTag.getAttribute("id", null));
    }
}
