package org.mini2Dx.ui.xml;

import org.junit.Test;
import org.mini2Dx.ui.element.ParentUiElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
public abstract class AbstractParentUiElementXmlTest<T extends ParentUiElement> extends AbstractUiElementXmlTest<T> {

    @Test
    public void supports_all_components_as_child_tags() {
        String xml = newBuilder()
                .withAllComponentTagsAsChildren()
                .build();

        assertXmlIsValid(xml);
    }

    @Test
    public void with_overflow_clipped() {
        String xml = newBuilder()
                .withAttribute("overflow-clipped", "true")
                .build();

        assertTrue(loadFileWithContainer(xml).isOverflowClipped());
    }

    @Test
    public void with_flex_layout() {
        String xml = newBuilder()
                .withAttribute("layout", "text")
                .build();

        assertEquals("text", loadFileWithContainer(xml).getFlexLayout());
    }

}
