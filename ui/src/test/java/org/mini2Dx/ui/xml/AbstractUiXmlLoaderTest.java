/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
