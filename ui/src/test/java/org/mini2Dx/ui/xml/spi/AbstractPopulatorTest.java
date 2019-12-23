package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;

public abstract class AbstractPopulatorTest {
    protected XmlReader.Element xmlTag;

    @Before
    public void init() {
        Mdx.graphics = new LibgdxGraphicsUtils();

        xmlTag = new XmlReader.Element("tag-name", null);
    }
}
