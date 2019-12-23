package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.core.exception.MdxException;

public class UnknownUiTagException extends MdxException {
    public UnknownUiTagException(String tag) {
        super("Ui xml tag (" + tag + ") is currently not supported. Did you make a typing error?");
    }
}
