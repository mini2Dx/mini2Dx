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
package org.mini2Dx.ui;

import org.mini2Dx.ui.element.Container;

/**
 * A simple UiModel interface for accessing the UiElements defined in the XML
 * using the corresponding ui-annotations {@link org.mini2Dx.ui.annotation.UiElement}
 */
public interface UiModel {
    Container getContainer();

    void initialize(final UiContainer uiContainer);

    void update(final float delta);
}
