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
import org.mini2Dx.ui.element.TabView;
import org.mini2Dx.ui.xml.UiElementPopulator;

public class TabViewPopulator implements UiElementPopulator<TabView> {
    @Override
    public boolean populate(XmlReader.Element xmlTag, TabView uiElement) {
        uiElement.setOverflowClipped(xmlTag.getBooleanAttribute("overflow-clipped", false));
        uiElement.setFlexLayout(xmlTag.getAttribute("layout", "flex-column:xs-12c"));
        uiElement.setTabButtonLayout(xmlTag.getAttribute("tab-button-layout", "flex-column:xs-3c sm-4c md-2c lg-2c"));
        return false;
    }
}
