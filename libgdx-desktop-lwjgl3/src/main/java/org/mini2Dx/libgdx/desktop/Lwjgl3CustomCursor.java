/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.libgdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Mini2DxGraphics;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.libgdx.graphics.LibgdxCustomCursor;

import static org.lwjgl.glfw.GLFW.*;

public class Lwjgl3CustomCursor extends LibgdxCustomCursor {

    /**
     * Constructor
     *
     * @param upPixmap   The image to use in the mouse button up state
     * @param downPixmap The image to use in the mouse button down state
     * @param xHotspot   The x location of the hotspot pixel within the cursor image (origin top-left corner)
     * @param yHotspot   The y location of the hotspot pixel within the cursor image (origin top-left corner)
     */
    public Lwjgl3CustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) {
        super(upPixmap, downPixmap, xHotspot, yHotspot);
    }

    @Override
    protected void updateCursorVisibility() {
        glfwSetInputMode(((Lwjgl3Mini2DxGraphics) Gdx.graphics).getWindow().getWindowHandle(), GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
    }
}
