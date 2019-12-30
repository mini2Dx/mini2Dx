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
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.ImageButton;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ImageButtonTest extends AbstractUiElementXmlTest<ImageButton> {

    @Test
    public void with_disabled_texture_flipped_y() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <disabled-texture flip-y=\"true\">blah</disabled-texture>" +
                "</image-button>");

        assertTrue(element.getDisabledImage().isFlipY());
        assertFalse(element.getDisabledImage().isFlipX());
    }

    @Test
    public void with_disabled_texture_flipped_x() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <disabled-texture flip-x=\"true\">blah</disabled-texture>" +
                "</image-button>");

        assertTrue(element.getDisabledImage().isFlipX());
        assertFalse(element.getDisabledImage().isFlipY());
    }

    @Test
    public void with_disabled_texture() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <disabled-texture>blah</disabled-texture>" +
                "</image-button>");

        assertEquals("blah", element.getDisabledTexturePath());
    }

    @Test
    public void with_action_texture_flipped_y() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <action-texture flip-y=\"true\">blah</action-texture>" +
                "</image-button>");

        assertTrue(element.getActionImage().isFlipY());
        assertFalse(element.getActionImage().isFlipX());
    }

    @Test
    public void with_action_texture_flipped_x() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <action-texture flip-x=\"true\">blah</action-texture>" +
                "</image-button>");

        assertTrue(element.getActionImage().isFlipX());
        assertFalse(element.getActionImage().isFlipY());
    }

    @Test
    public void with_action_texture() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <action-texture>blah</action-texture>" +
                "</image-button>");

        assertEquals("blah", element.getActionTexturePath());
    }

    @Test
    public void with_hover_texture_flipped_y() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <hover-texture flip-y=\"true\">blah</hover-texture>" +
                "</image-button>");

        assertTrue(element.getHoverImage().isFlipY());
        assertFalse(element.getHoverImage().isFlipX());
    }

    @Test
    public void with_hover_texture_flipped_x() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <hover-texture flip-x=\"true\">blah</hover-texture>" +
                "</image-button>");

        assertTrue(element.getHoverImage().isFlipX());
        assertFalse(element.getHoverImage().isFlipY());
    }

    @Test
    public void with_hover_texture() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <hover-texture>blah</hover-texture>" +
                "</image-button>");

        assertEquals("blah", element.getHoverTexturePath());
    }

    @Test
    public void with_normal_texture_flipped_y() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <normal-texture flip-y=\"true\">blah</normal-texture>" +
                "</image-button>");

        assertTrue(element.getNormalImage().isFlipY());
        assertFalse(element.getNormalImage().isFlipX());
    }

    @Test
    public void with_normal_texture_flipped_x() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <normal-texture flip-x=\"true\">blah</normal-texture>" +
                "</image-button>");

        assertTrue(element.getNormalImage().isFlipX());
        assertFalse(element.getNormalImage().isFlipY());
    }

    @Test
    public void with_normal_texture() {
        ImageButton element = loadFile("" +
                "<image-button responsive=\"true\">" +
                "   <normal-texture>blah</normal-texture>" +
                "</image-button>");

        assertEquals("blah", element.getNormalTexturePath());
    }

    @Test
    public void with_responsive() {
        ImageButton element = loadFile("<image-button responsive=\"true\"><normal-texture/></image-button>");

        assertTrue(element.isResponsive());
        assertAllChildren(element, true, Image::isResponsive);
    }

    @Test
    public void with_atlas() {
        ImageButton element = loadFile("<image-button atlas=\"test\"><normal-texture/></image-button>");

        assertEquals("test", element.getAtlas());
        assertAllChildren(element, "test", Image::getAtlas);
    }

    @Test
    public void with_disabled() {
        ImageButton element = loadFile("<image-button enabled=\"false\"><normal-texture/></image-button>");

        assertFalse(element.isEnabled());
    }

    @Test
    public void with_flex_layout() {
        ImageButton element = loadFile("<image-button layout=\"blah\"><normal-texture/></image-button>");

        assertEquals("blah", element.getFlexLayout());
    }

    @Test
    public void id_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <image-button><normal-texture/></image-button>" +
                "</container>";

        assertAttributeRequired("id", xml);
    }

    @Test
    public void normal_texture_is_required() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <image-button id=\"x\"></image-button>" +
                "</container>";

        assertXmlIsInvalid(xml, "The content of element 'image-button' is not complete.");
    }

    @Test
    public void invalid_texture_provided() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <image-button id=\"x\">" +
                "      <unknown-texture/>" +
                "    </image-button>" +
                "</container>";

        assertXmlIsInvalid(xml, "Invalid content was found starting with element 'unknown-texture'");
    }

    @Test
    public void all_values() {
        String xml = "<?xml version=\"1.0\"?>" +
                "<container xmlns=\"https://github.com/mini2Dx/mini2Dx\">" +
                "    <image-button id=\"test\"" +
                "                 z-index=\"100\"" +
                "                 style=\"test-style\"" +
                "                 y=\"100\"" +
                "                 x=\"200\"" +
                "                 width=\"2\"" +
                "                 height=\"1\"" +
                "                 visibility=\"HIDDEN\"" +
                "                 enabled=\"false\"" +
                "                 layout=\"x\"" +
                "                 atlas=\"x\"" +
                "                 responsive=\"true\"" +
                "    >" +
                "       <normal-texture flip-x=\"true\" flip-y=\"true\">blah</normal-texture>" +
                "       <hover-texture flip-x=\"true\" flip-y=\"true\">blah</hover-texture>" +
                "       <action-texture flip-x=\"true\" flip-y=\"true\">blah</action-texture>" +
                "       <disabled-texture flip-x=\"true\" flip-y=\"true\">blah</disabled-texture>" +
                "   </image-button>" +
                "</container>";

        assertXmlIsValid(xml);
    }

    @Override
    protected String getUiElementTagName() {
        return "image-button";
    }

    @Override
    protected void assertDefaultValues(ImageButton element) {
        assertTrue(element.isEnabled());
        assertNull(element.getFlexLayout());
        assertFalse(element.isResponsive());
        assertAllChildren(element, false, Image::isResponsive);
        assertAllChildren(element, false, Image::isFlipX);
        assertAllChildren(element, false, Image::isFlipY);
        assertAllChildren(element, null, Image::getAtlas);
        assertAllChildren(element, null, Image::getTexturePath);
    }

    private void assertAllChildren(ImageButton element, Object expectedValue, Function<Image, Object> supplier) {
        assertEquals(4, element.getTotalChildren());
        for (int i = 0; i < element.getTotalChildren(); i++) {
            assertEquals(expectedValue, supplier.apply((Image) element.getChild(i)));
        }
    }
}