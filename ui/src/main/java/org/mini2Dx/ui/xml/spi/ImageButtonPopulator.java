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

import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.element.ImageButton;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class ImageButtonPopulator implements UiElementPopulator<ImageButton> {
    @Override
    public boolean populate(XmlReader.Element xmlTag, ImageButton uiElement) {
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setFlexLayout(xmlTag.getAttribute("layout", null));
        uiElement.setAtlas(xmlTag.getAttribute("atlas", null));
        uiElement.setResponsive(xmlTag.getBooleanAttribute("responsive", false));

        for (int i = 0; i < xmlTag.getChildCount(); i++) {
            XmlReader.Element child = xmlTag.getChild(i);
            Image image = determineImage(uiElement, child);
            image.setTexturePath(child.getText());
            image.setFlipY(child.getBooleanAttribute("flip-y", false));
            image.setFlipX(child.getBooleanAttribute("flip-x", false));
        }

        return true;
    }

    private Image determineImage(ImageButton uiElement, XmlReader.Element child) {
        Image image;

        switch (child.getName()) {
            case "normal-texture":
                image = uiElement.getNormalImage();
                break;
            case "action-texture":
                image = uiElement.getActionImage();
                break;
            case "disabled-texture":
                image = uiElement.getDisabledImage();
                break;
            case "hover-texture":
                image = uiElement.getHoverImage();
                break;
            default:
                throw new UnknownUiTagException(child.getName());
        }
        return image;
    }
}
