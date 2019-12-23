package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Visibility;

public class InvalidVisibilityException extends AbstractInvalidValueException {
    public InvalidVisibilityException(XmlReader.Element tag, String invalidValue) {
        super(tag, invalidValue, Visibility.values());
    }
}
