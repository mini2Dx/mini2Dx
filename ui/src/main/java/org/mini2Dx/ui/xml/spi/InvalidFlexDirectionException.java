package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.layout.FlexDirection;

public class InvalidFlexDirectionException extends AbstractInvalidValueException {
    public InvalidFlexDirectionException(XmlReader.Element tag, String invalidValue) {
        super(tag, invalidValue, FlexDirection.values());
    }
}
