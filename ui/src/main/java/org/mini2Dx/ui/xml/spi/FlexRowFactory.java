package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.FlexRow;

public class FlexRowFactory implements UiXmlLoader.UiElementFactory<FlexRow> {
    @Override
    public FlexRow build(XmlReader.Element xmlTag) {
        return new FlexRow(xmlTag.getAttribute("id", null));
    }
}
