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
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.xml.UiElementPopulator;

import static org.mini2Dx.ui.xml.spi.XmlAttributeMapper.mapToFlexDirection;

public class RadioButtonPopulator implements UiElementPopulator<RadioButton> {
    @Override
    public boolean populate(XmlReader.Element xmlTag, RadioButton uiElement) {
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setResponsive(xmlTag.getBoolean("responsive", false));

        if (xmlTag.hasAttribute("flex-direction")) {
            uiElement.setFlexDirection(mapToFlexDirection(xmlTag));
        }

        for (int i = 0; i < xmlTag.getChildCount(); i++) {
            XmlReader.Element child = xmlTag.getChild(i);
            uiElement.addOption(child.getText());
        }

        uiElement.setSelectedOptionIndex(xmlTag.getIntAttribute("default-selected-index", 0));
        return true;
    }
}
