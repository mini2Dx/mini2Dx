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

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.DesktopMini2DxGame;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Mini2DxConfig;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import org.mini2Dx.uats.util.UATApplication;

/**
 * Main entry point for desktop UAT app
 */
public class Lwjgl3UATApplication {
    public static void main(String[] args) {
        Lwjgl3Mini2DxConfig cfg = new Lwjgl3Mini2DxConfig("org.mini2Dx.uats");
        cfg.setTitle("mini2Dx - User Acceptance Tests");
        cfg.setWindowedMode(800, 720);

        //final Graphics.Monitor primaryMonitor = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
        //cfg.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode(primaryMonitor));

        new DesktopMini2DxGame(new UATApplication(), cfg);
    }
}
