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
import org.mini2Dx.ui.element.AnimatedImage;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class AnimatedImagePopulator implements UiElementPopulator<AnimatedImage> {
    @Override
    public boolean populate(XmlReader.Element xmlTag, AnimatedImage uiElement) {
        uiElement.setResponsive(xmlTag.getBooleanAttribute("responsive", false));
        uiElement.setFlipX(xmlTag.getBooleanAttribute("flip-x", false));
        uiElement.setFlipY(xmlTag.getBooleanAttribute("flip-y", false));
        uiElement.setAtlas(xmlTag.getAttribute("atlas", null));

        String[] textures = new String[xmlTag.getChildCount()];
        float[] durations = new float[xmlTag.getChildCount()];

        for (int i = 0; i < xmlTag.getChildCount(); i++) {
            textures[i] = xmlTag.getChild(i).getText();
            durations[i] = xmlTag.getChild(i).getFloatAttribute("duration");
        }

        uiElement.setTexturePaths(textures, durations);
        return false;
    }
}
