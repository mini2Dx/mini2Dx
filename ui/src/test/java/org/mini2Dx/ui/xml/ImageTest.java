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

import org.junit.Test;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.element.Image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ImageTest extends AbstractUiElementXmlTest<Image> {

    @Test
    public void flip_y_provided() {
        String xml = "<image texture-path=\"blah\" flip-y=\"true\" />";

        Image element = loadFile(xml);

        assertTrue(element.isFlipY());
    }

    @Test
    public void flip_x_provided() {
        String xml = "<image texture-path=\"blah\" flip-x=\"true\" />";

        Image element = loadFile(xml);

        assertTrue(element.isFlipX());
    }

    @Test
    public void responsive_provided() {
        String xml = "<image texture-path=\"blah\" responsive=\"true\" />";

        Image element = loadFile(xml);

        assertTrue(element.isResponsive());
    }

    @Test
    public void atlas_provided() {
        String xml = "<image texture-path=\"blah\" atlas=\"foo\" />";

        Image element = loadFile(xml);

        assertEquals("foo", element.getAtlas());
    }

    @Test
    public void texture_path_provided() {
        String xml = "<image texture-path=\"blah\" />";

        Image element = loadFile(xml);

        assertEquals("blah", element.getTexturePath());
    }

    @Test
    public void texture_path_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <image/>" +
                "</container>";

        assertAttributeRequired("texture-path", xml);
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<image xmlns=\"https://github.com/mini2Dx/mini2Dx\"" +
                "          id=\"test\"" +
                "          visibility=\"NO_RENDER\"" +
                "          width=\"200\"" +
                "          height=\"100\"" +
                "          x=\"10\"" +
                "          y=\"20\"" +
                "          style=\"test-style\"" +
                "          z-index=\"100\"" +
                "          texture-path=\"blah\"" +
                "          atlas=\"blah\"" +
                "          flip-x=\"true\"" +
                "          flip-y=\"true\"" +
                "          responsive=\"true\"" +
                "/>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "image";
    }

    @Override
    protected void assertDefaultValues(Image element) {
        assertFalse(element.isResponsive());
        assertFalse(element.isFlipX());
        assertFalse(element.isFlipY());
        assertNull(element.getAtlas());
        assertEquals("blah", element.getTexturePath());
    }

    @Override
    protected ObjectMap<String, String> requiredAttributesForDefaultTestCases() {
        ObjectMap<String, String> map = new ObjectMap<>();
        map.put("texture-path", "blah");
        return map;
    }
}