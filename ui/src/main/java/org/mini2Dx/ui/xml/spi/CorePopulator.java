package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.ui.xml.UiXmlLoader;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;

public class CorePopulator implements UiXmlLoader.UiElementPopulator<UiElement> {
    @Override
    public void populate(XmlReader.Element xmlElement, UiElement uiElement) {
        if (xmlElement.hasAttribute("x")) {
            uiElement.setX(xmlElement.getFloatAttribute("x"));
        }

        if (xmlElement.hasAttribute("y")) {
            uiElement.setY(xmlElement.getFloatAttribute("y"));
        }

        if (xmlElement.hasAttribute("width")) {
            uiElement.setWidth(xmlElement.getFloatAttribute("width"));
        }

        if (xmlElement.hasAttribute("height")) {
            uiElement.setHeight(xmlElement.getFloatAttribute("height"));
        }

        if (xmlElement.hasAttribute("z-index")) {
            uiElement.setZIndex(xmlElement.getIntAttribute("z-index"));
        }

        if (xmlElement.hasAttribute("style-id")) {
            uiElement.setStyleId(xmlElement.getAttribute("style-id"));
        }

        if (xmlElement.hasAttribute("flex-layout")) {
            uiElement.setStyleId(xmlElement.getAttribute("flex-layout"));
        }

        String providedValue = xmlElement.getAttribute("visibility", "VISIBLE");
        try {
            uiElement.setVisibility(Visibility.valueOf(providedValue.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidVisibilityException(xmlElement, providedValue);
        }
    }
}
