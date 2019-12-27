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
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class CorePopulator implements UiElementPopulator<UiElement> {
    @Override
    public void populate(XmlReader.Element xmlElement, UiElement uiElement) {
        uiElement.setDebugEnabled(xmlElement.getBooleanAttribute("debug", false));

        if (xmlElement.hasAttribute("x")) {
            uiElement.setX(xmlElement.getFloatAttribute("x"));
        }

        if (xmlElement.hasAttribute("y")) {
            uiElement.setY(xmlElement.getFloatAttribute("y"));
        }

        if (xmlElement.hasAttribute("width")) {
            uiElement.setWidth(xmlElement.getFloatAttribute("width"));
        }

        if (xmlElement.hasAttribute("height")) {
            uiElement.setHeight(xmlElement.getFloatAttribute("height"));
        }

        if (xmlElement.hasAttribute("z-index")) {
            uiElement.setZIndex(xmlElement.getIntAttribute("z-index"));
        }

        if (xmlElement.hasAttribute("style")) {
            uiElement.setStyleId(xmlElement.getAttribute("style"));
        }

        String providedValue = xmlElement.getAttribute("visibility", "VISIBLE");
        try {
            uiElement.setVisibility(Visibility.valueOf(providedValue.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidVisibilityException(xmlElement, providedValue);
        }
    }
}
