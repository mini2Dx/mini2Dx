package org.mini2Dx.ui.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InMemoryFileHandler extends AbstractFileHandle {
    private byte[] data;

    public InMemoryFileHandler(String contents) {
        this(contents.getBytes(UTF_8));
    }

    public InMemoryFileHandler(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(data);
    }
}
