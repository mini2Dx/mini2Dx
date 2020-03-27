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
package org.mini2Dx.uats.desktop;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import org.mini2Dx.libgdx.desktop.Lwjgl2Mini2DxConfig;
import org.mini2Dx.uats.util.UATApplication;

/**
 * Main entry point for desktop UAT app
 */
public class Lwjgl2UATApplication {
    public static void main(String[] args) {
        Lwjgl2Mini2DxConfig cfg = new Lwjgl2Mini2DxConfig("org.mini2Dx.uats");
        cfg.title = "mini2Dx - User Acceptance Tests";
        cfg.width = 800;
        cfg.height = 720;
        cfg.vSyncEnabled = true;
        new DesktopMini2DxGame(new UATApplication(), cfg);
    }
}
