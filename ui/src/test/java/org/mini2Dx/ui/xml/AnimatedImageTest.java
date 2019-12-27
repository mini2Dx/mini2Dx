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
import org.mini2Dx.ui.element.AnimatedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnimatedImageTest extends AbstractUiElementXmlTest<AnimatedImage> {

    @Test
    public void with_atlas() {
        AnimatedImage element = loadFile("<animated-image atlas=\"blah\" />");

        assertEquals("blah", element.getAtlas());
    }

    @Test
    public void with_flip_y() {
        AnimatedImage element = loadFile("<animated-image flip-y=\"true\" />");

        assertTrue(element.isFlipY());
    }

    @Test
    public void with_flip_x() {
        AnimatedImage element = loadFile("<animated-image flip-x=\"true\" />");

        assertTrue(element.isFlipX());
    }

    @Test
    public void with_responsive_provided() {
        AnimatedImage element = loadFile("<animated-image responsive=\"true\" />");

        assertTrue(element.isResponsive());
    }

    @Test
    public void with_whitespace_in_textures() {
        String xml = "<?xml version=\"1.0\"?>" +
                "    <animated-image>" +
                "       <texture duration=\"100\">    blah</texture>" +
                "       <texture duration=\"200\">\tblah 2</texture>" +
                "    </animated-image>";

        AnimatedImage element = loadFile(xml);

        assertEquals("blah", element.getTexturePaths()[0]);
        assertEquals("blah 2", element.getTexturePaths()[1]);
    }

    @Test
    public void with_textures() {
        String xml = "<?xml version=\"1.0\"?>" +
                "    <animated-image>" +
                "       <texture duration=\"100\">blah</texture>" +
                "       <texture duration=\"200\">blah 2</texture>" +
                "    </animated-image>";

        AnimatedImage element = loadFile(xml);

        assertEquals("blah", element.getTexturePaths()[0]);
        assertEquals("blah 2", element.getTexturePaths()[1]);
        assertEquals(2, element.getTexturePaths().length);

        assertEquals(100, element.getFrameDurations()[0], 0.0001);
        assertEquals(200, element.getFrameDurations()[1], 0.0001);
        assertEquals(2, element.getFrameDurations().length);
    }

    @Test
    public void texture_duration_can_not_be_alpha() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <animated-image>" +
                "       <texture duration=\"aaa\"/>" +
                "       <texture duration=\"bbb\"/>" +
                "    </animated-image>" +
                "</container>";

        assertXmlIsInvalid(xml, "'aaa' is not a valid value for 'float'");
    }

    @Test
    public void texture_requires_a_duration() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <animated-image>" +
                "       <texture/>" +
                "       <texture/>" +
                "    </animated-image>" +
                "</container>";

        assertXmlIsInvalid(xml, "Attribute 'duration' must appear on element 'texture'");
    }

    @Test
    public void require_at_least_two_textures() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <animated-image/>" +
                "</container>";

        assertXmlIsInvalid(xml, "The content of element 'animated-image' is not complete");
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <animated-image" +
                "            id=\"test\"" +
                "            z-index=\"100\"" +
                "            style=\"test-style\"" +
                "            y=\"100\"" +
                "            x=\"200\"" +
                "            width=\"2\"" +
                "            height=\"1\"" +
                "            visibility=\"HIDDEN\"" +
                "            responsive=\"true\"" +
                "            flip-x=\"true\"" +
                "            flip-y=\"true\"" +
                "            atlas=\"foo\">" +
                "       <texture duration=\"100\">blah</texture>" +
                "       <texture duration=\"100\">blah</texture>" +
                "    </animated-image>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "animated-image";
    }

    @Override
    protected void assertDefaultValues(AnimatedImage element) {
        assertFalse(element.isResponsive());
        assertFalse(element.isFlipX());
        assertFalse(element.isFlipY());
        assertNull(element.getAtlas());
    }

}