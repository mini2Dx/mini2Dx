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
import org.mini2Dx.ui.element.TextBox;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class TextBoxPopulator implements UiElementPopulator<TextBox> {
    @Override
    public boolean populate(XmlReader.Element xmlTag, TextBox uiElement) {
        uiElement.setValue(xmlTag.getAttribute("value", null));
        uiElement.setEnabled(xmlTag.getBoolean("enabled", true));
        uiElement.setPasswordField(xmlTag.getBoolean("password", false));
        uiElement.setCharacterLimit(xmlTag.getInt("characterLimit", -1));
        uiElement.setFlexLayout(xmlTag.getAttribute("layout", "flex-column:xs-12c"));
        return false;
    }
}
