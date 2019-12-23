package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Checkbox;

public class CheckBoxFactory implements UiXmlLoader.UiElementFactory<Checkbox> {
    @Override
    public Checkbox build(XmlReader.Element xmlTag) {
        return new Checkbox(xmlTag.getAttribute("id", null));
    }
}
