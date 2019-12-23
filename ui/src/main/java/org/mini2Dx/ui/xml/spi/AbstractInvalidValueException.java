package org.mini2Dx.ui.xml.spi;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.gdx.xml.XmlReader;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AbstractInvalidValueException extends MdxException {
    public AbstractInvalidValueException(XmlReader.Element tag, String invalidValue, Object[] availableValues) {
        super(buildMessage(tag, invalidValue, availableValues));
    }


    private static String buildMessage(XmlReader.Element tag, String invalidValue, Object[] values) {
        String availableValues = Arrays.stream(values)
                .map((v) -> "  - " + v.toString())
                .sorted()
                .collect(Collectors.joining("\n"));

        String prefix = tag.hasAttribute("id") ?
                tag.getName() + " with id (" + tag.getAttribute("id") + ")"
                : tag.getName();

        StringBuilder builder = new StringBuilder()
                .append(prefix)
                .append(" has an invalid value: ")
                .append(invalidValue)
                .append("\n\n")
                .append("Available Values:")
                .append("\n")
                .append(availableValues)
                .append("\n");

        return builder.toString();
    }
}
