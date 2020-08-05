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

import org.mini2Dx.core.GraphicsUtils;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.CustomCursor;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;

public class Lwjgl3GameWrapper extends DesktopGameWrapper {

	public Lwjgl3GameWrapper(GameContainer gc, String gameIdentifier) {
		super(gc, gameIdentifier);
	}

	@Override
	public void initialisePlatformUtils() {
		Mdx.platformUtils = new Lwjgl3PlatformUtils();
	}

	@Override
	public boolean isGameWindowReady() {
		return Lwjgl3PlatformUtils.GAME_THREAD_ID != -1L;
	}

	@Override
	protected GraphicsUtils createGraphicsUtils() {
		return new LibgdxGraphicsUtils(){
			@Override
			public CustomCursor newCustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) {
				return new Lwjgl3CustomCursor(upPixmap, downPixmap, xHotspot, yHotspot);
			}
		};
	}
}
