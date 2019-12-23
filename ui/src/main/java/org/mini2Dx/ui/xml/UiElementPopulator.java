package org.mini2Dx.ui.xml;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.UiElement;

public interface UiElementPopulator<T extends UiElement> {
    /**
     * Populate properties on the UiElement
     *
     * @param xmlTag    - the tag to pull values off of
     * @param uiElement - the UiElement to be populated
     */
    void populate(XmlReader.Element xmlTag, T uiElement);
}