package org.mini2Dx.ui.xml;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.UiElement;

public interface UiElementFactory<T extends UiElement> {
    /**
     * Simply instantiate an instance of the UiElement
     * <p>
     * This is intended to help handle scenarios where there is NO default constructor
     * for an UiElement
     *
     * @param xmlTag - the tag that triggered this element to be created
     * @return a new instance of the UiElement
     */
    T build(XmlReader.Element xmlTag);
}