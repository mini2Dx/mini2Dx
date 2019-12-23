package org.mini2Dx.ui.xml;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.UiElement;

public abstract class AbstractUiXmlLoaderTest {
    protected final Mockery mockery = new Mockery();
    protected final String filename = "test-file.xml";
    protected FileHandleResolver fileHandleResolver;
    protected UiXmlLoader loader;

    @Before
    public void setUpLoader() {
        Mdx.graphics = new LibgdxGraphicsUtils();
        fileHandleResolver = mockery.mock(FileHandleResolver.class);

        loader = new UiXmlLoader(fileHandleResolver);
    }

    protected <T extends ParentUiElement> T loadFileWithContainer(String xml) {
        return (T) loadFile(xml);
    }

    protected <T extends UiElement> T loadFile(String xml) {
        final String realXml = "<?xml version=\"1.0\"?>\n" + xml;

        mockery.checking(new Expectations() {
            {
                oneOf(fileHandleResolver).resolve(filename);
                will(returnValue(new InMemoryFileHandler(realXml)));
            }
        });
        return (T) loader.load(filename);
    }
}
