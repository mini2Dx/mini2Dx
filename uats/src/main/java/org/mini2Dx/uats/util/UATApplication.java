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
package org.mini2Dx.uats.util;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FallbackFileHandleResolver;
import org.mini2Dx.core.files.InternalFileHandleResolver;
import org.mini2Dx.core.files.LocalFileHandleResolver;
import org.mini2Dx.core.game.ScreenBasedGame;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledMapLoader;
import org.mini2Dx.uats.*;
import org.mini2Dx.ui.NavigationMode;
import org.mini2Dx.ui.UiThemeLoader;
import org.mini2Dx.ui.style.UiTheme;

import java.io.IOException;

/**
 *
 * @author Thomas Cashman
 */
public class UATApplication extends ScreenBasedGame {
	private static final String LOGGING_TAG = UATApplication.class.getSimpleName();

	public static final NavigationMode NAVIGATION_MODE = NavigationMode.BUTTON_OR_POINTER;
	public static boolean USE_AOT_DATA = false;

	private AssetManager assetManager;

	@Override
	public void initialise() {
		TiledMap.FAST_RENDER_EMPTY_LAYERS = true;

		if(USE_AOT_DATA) {
			try {
				AotSerializationData.restoreFrom(Mdx.files.internal("aot-data.txt"));
			} catch (IOException e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			}
		}

		FallbackFileHandleResolver fallbackFileHandleResolver = new FallbackFileHandleResolver(new InternalFileHandleResolver(),
				new LocalFileHandleResolver());
		assetManager = new AssetManager(fallbackFileHandleResolver);

		assetManager.setAssetLoader(UiTheme.class, new UiThemeLoader(fallbackFileHandleResolver));
		assetManager.setAssetLoader(TiledMap.class, new TiledMapLoader());

		assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);
		while(!assetManager.update()) {}

		addScreen(new LoadingScreen(assetManager));
		addScreen(new UATSelectionScreen(assetManager, fallbackFileHandleResolver));
		addScreen(new BlendingUAT(assetManager));
		addScreen(new ClippingUAT(assetManager));
		addScreen(new GeometryUAT());
		addScreen(new GraphicsUAT(assetManager, fallbackFileHandleResolver));
		addScreen(new TextureRegionUAT(fallbackFileHandleResolver));
		addScreen(new SpriteUAT(fallbackFileHandleResolver));
		addScreen(new ViewportUAT(fallbackFileHandleResolver));
		addScreen(new MonospaceFontUAT(assetManager));
		addScreen(new AudioUAT(fallbackFileHandleResolver));
		addScreen(new AsyncAudioUAT(fallbackFileHandleResolver));
		addScreen(new OrthogonalTiledMapNoCachingUAT(assetManager));
		addScreen(new IsometricTiledMapUAT(assetManager));
		addScreen(new HexagonalTiledMapUAT(assetManager));
		addScreen(new TextureAtlasLoaderUAT(assetManager));
//		addScreen(new ParticleEffectsUAT(fallbackFileHandleResolver));
		addScreen(new GamePadUAT());
		addScreen(new GamePadMapping());
		addScreen(new XmlUiUAT(assetManager, fallbackFileHandleResolver));
		addScreen(new FlexUiUAT(assetManager, fallbackFileHandleResolver));
		addScreen(new PixelUiUAT(assetManager, fallbackFileHandleResolver));
		addScreen(new PlatformUtilsUAT());
		addScreen(new ShaderUAT());
		addScreen(new ShapeClippingUAT());
		addScreen(new TilingDrawableUAT(fallbackFileHandleResolver));
		addScreen(new SpriteCacheUAT(fallbackFileHandleResolver));
		addScreen(new UiSerializationUAT(assetManager));
	}

	@Override
	public int getInitialScreenId() {
		return 0;
	}
}
