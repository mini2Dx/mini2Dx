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
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * User acceptance testing of {@link SpriteCache} APIs
 */
public class SpriteCacheUAT extends BasicGameScreen {
	public static final String TEXTURE_PACK = "texture-region-uat";

	private final FileHandleResolver fileHandleResolver;

	private SpriteCache spriteCache;
	private int cacheID;
	private int cacheID2;
	private int cacheID3;


	public SpriteCacheUAT(FileHandleResolver fileHandleResolver) {
		this.fileHandleResolver = fileHandleResolver;
		spriteCache = Mdx.graphics.newSpriteCache();
	}

	@Override
	public void initialise(GameContainer gc) {
		TextureAtlas textureAtlas = Mdx.graphics.newTextureAtlas(fileHandleResolver.resolve(TEXTURE_PACK + ".atlas"));
		TextureRegion cardDiamondsA = textureAtlas.findRegion("cardDiamondsA");
		TextureRegion cardDiamondsJ = textureAtlas.findRegion("cardDiamondsJ");
		TextureRegion cardDiamondsK = textureAtlas.findRegion("cardDiamondsK");
		TextureRegion cardDiamondsQ = textureAtlas.findRegion("cardDiamondsQ");

		//TODO: remove this
		if (spriteCache == null){ //temp fix to run the UAT on libgdx because the spritecache isn't implemented there.
			return;
		}

		spriteCache.beginCache();
		for (int x = 0; x < 4; x++){
			for (int y = 0; y < 4; y++){
				spriteCache.add(cardDiamondsA, 50 + 40 * x * 4, 50 + 50 * y);
				spriteCache.add(cardDiamondsJ, 50 + 40 * x * 4 + 40, 50 + 50 * y);
				spriteCache.add(cardDiamondsK, 50 + 40 * x * 4 + 40 * 2, 50 + 50 * y);
				spriteCache.add(cardDiamondsQ, 50 + 40 * x * 4 + 40 * 3, 50 + 50 * y);
			}
		}
		cacheID = spriteCache.endCache();

		spriteCache.beginCache();
		spriteCache.setColor(Colors.BLUE());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50, 250 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40, 250 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 2, 250 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 3, 250 + 50 * y);
		}
		spriteCache.setColor(Colors.RED());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 4, 250 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 4 + 40, 250 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 4 + 40 * 2, 250 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 4 + 40 * 3, 250 + 50 * y);
		}
		spriteCache.setColor(Colors.GREEN());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 2 * 4, 250 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 2 * 4 + 40, 250 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 2 * 4 + 40 * 2, 250 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 2 * 4 + 40 * 3, 250 + 50 * y);
		}
		spriteCache.setColor(Colors.VIOLET());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 3 * 4, 250 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 3 * 4 + 40, 250 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 3 * 4 + 40 * 2, 250 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 3 * 4 + 40 * 3, 250 + 50 * y);
		}
		cacheID2 = spriteCache.endCache();

		spriteCache.beginCache();
		spriteCache.setColor(Colors.BLUE());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.RED());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 4 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.GREEN());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 2 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 2 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 2 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 2 * 4 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.VIOLET());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsA, 50 + 40 * 3 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 3 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 3 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 3 * 4 + 40 * 3, 450 + 50 * y);
		}
		cacheID3 = spriteCache.endCache();

		spriteCache.beginCache(cacheID3);
		spriteCache.setColor(Colors.VIOLET());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsQ, 50, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsA, 50 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.GREEN());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsQ, 50 + 40 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsA, 50 + 40 * 4 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.RED());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsQ, 50 + 40 * 2 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 2 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 2 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsQ, 50 + 40 * 2 * 4 + 40 * 3, 450 + 50 * y);
		}
		spriteCache.setColor(Colors.BLUE());
		for (int y = 0; y < 4; y++) {
			spriteCache.add(cardDiamondsQ, 50 + 40 * 3 * 4, 450 + 50 * y);
			spriteCache.add(cardDiamondsK, 50 + 40 * 3 * 4 + 40, 450 + 50 * y);
			spriteCache.add(cardDiamondsJ, 50 + 40 * 3 * 4 + 40 * 2, 450 + 50 * y);
			spriteCache.add(cardDiamondsA, 50 + 40 * 3 * 4 + 40 * 3, 450 + 50 * y);
		}
		if (spriteCache.endCache() != cacheID3){
			throw new RuntimeException("ID should be the same in case of cache redefinition");
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.drawSpriteCache(spriteCache, cacheID);
		g.drawSpriteCache(spriteCache, cacheID2);
		g.drawSpriteCache(spriteCache, cacheID3);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(SpriteCacheUAT.class);
	}
}
