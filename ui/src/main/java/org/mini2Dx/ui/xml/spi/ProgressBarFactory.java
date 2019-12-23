package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.xml.UiXmlLoader;

public class ProgressBarFactory implements UiXmlLoader.UiElementFactory<ProgressBar> {
    @Override
    public ProgressBar build(XmlReader.Element xmlTag) {
        return new ProgressBar(xmlTag.getAttribute("id", null));
    }
}
