package org.mini2Dx.ui.xml;

import org.jmock.Expectations;
import org.junit.Test;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.ui.element.Container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class UiXmlLoaderTest extends AbstractUiXmlLoaderTest {

    @Test
    public void unknown_tag() {
        String xml = "<does-not-exist/>";

        try {
            loadFile(xml);
            fail();
        } catch (MdxException e) {
            String error = e.getCause().getMessage();
            assertEquals("Ui xml tag (does-not-exist) is currently not supported. Did you make a typing error?", error);
        }
    }

    @Test
    public void nested_containers() {
        String xml =
                "<container id=\"1\">" +
                "  <container id=\"2\">" +
                "    <container id=\"3\"/>" +
                "  </container>" +
                "</container>";

        Container container = loadFile(xml);
        assertEquals("1", container.getId());
        assertEquals(1, container.getTotalChildren());

        container = (Container) container.get(0);
        assertEquals("2", container.getId());
        assertEquals(1, container.getTotalChildren());

        container = (Container) container.get(0);
        assertEquals("3", container.getId());
        assertEquals(0, container.getTotalChildren());
    }

    @Test
    public void fail_to_parse_file_contents() {
        String badFileFormat = "{\"hello\": \"world\"}";
        mockery.checking(new Expectations() {
            {
                oneOf(fileHandleResolver).resolve(filename);
                will(returnValue(new InMemoryFileHandler(badFileFormat)));
            }
        });

        try {
            loader.load(filename);
            fail();
        } catch (Exception e) {
            assertEquals("Failed to load UI file: " + filename, e.getMessage());
            assertNotNull(e.getCause());
        }
    }

    @Test
    public void fail_to_resolve_file() {
        RuntimeException realError = new RuntimeException("BOOM");
        mockery.checking(new Expectations() {
            {
                oneOf(fileHandleResolver).resolve(filename);
                will(throwException(realError));
            }
        });

        try {
            loader.load(filename);
            fail();
        } catch (Exception e) {
            assertEquals("Failed to load UI file: " + filename, e.getMessage());
            assertSame(e.getCause(), realError);
        }
    }


}