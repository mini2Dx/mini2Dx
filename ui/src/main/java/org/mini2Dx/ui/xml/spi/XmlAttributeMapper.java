package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.layout.FlexDirection;

public class XmlAttributeMapper {
    public static FlexDirection mapToFlexDirection(XmlReader.Element element) {
        String value = element.getAttribute("flex-direction");

        try {
            return FlexDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidFlexDirectionException(element, value);
        }
    }
}
