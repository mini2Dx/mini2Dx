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
package org.mini2Dx.ui.xml.spi;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.layout.FlexDirection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mini2Dx.ui.layout.FlexDirection.COLUMN;
import static org.mini2Dx.ui.layout.FlexDirection.values;
import static org.mini2Dx.ui.xml.spi.XmlAttributeMapper.mapToFlexDirection;

public class XmlAttributeMapperTest {
    private XmlReader.Element tag;

    @Before
    public void setUp() {
        tag = new XmlReader.Element("tag-name", null);
    }

    @Test
    public void mapToFlexDirection_unsupported_flex_direction() {
        tag.setAttribute("flex-direction", "does-not-exist");

        try {
            mapToFlexDirection(tag);
            fail();
        } catch (InvalidFlexDirectionException e) {
            assertTrue(e.getMessage().startsWith("tag-name has an invalid value: does-not-exist"));
        }
    }

    @Test
    public void mapToFlexDirection_flex_directions_case_insensitive() {
        tag.setAttribute("flex-direction", "ColUmn");

        assertEquals(COLUMN, mapToFlexDirection(tag));
    }

    @Test
    public void mapToFlexDirection_all_directions() {
        for (FlexDirection value : values()) {
            tag.setAttribute("flex-direction", value.name());
            assertEquals(value, mapToFlexDirection(tag));
        }
    }

}