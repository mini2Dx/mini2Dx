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
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.viewport.*;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * UAT for testing {@link Viewport} implementations
 */
public class ViewportUAT extends BasicGameScreen {
	public static final String TEXTURE_PACK = "texture-region-uat";
	private static final int VIEWPORT_WIDTH = 320;
	private static final int VIEWPORT_HEIGHT = 240;

	private final FileHandleResolver fileHandleResolver;

	private final Viewport[] viewports = new Viewport[5];

	private Texture texture;
	private int viewportIndex;

	public ViewportUAT(FileHandleResolver fileHandleResolver) {
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		texture = Mdx.graphics.newTexture(fileHandleResolver.resolve(TEXTURE_PACK + ".png"));

		viewports[0] = new FillViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		viewports[1] = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		viewports[2] = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		viewports[3] = new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		viewports[4] = new ScreenViewport();
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (Mdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			viewportIndex = viewportIndex == viewports.length - 1 ? 0 : viewportIndex + 1;
		} else if (Mdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			for(int i = 0; i < viewports.length; i++) {
				if(viewports[i] instanceof ScalingViewport) {
					final ScalingViewport scalingViewport = ((ScalingViewport) viewports[i]);
					scalingViewport.setPowerOfTwo(!scalingViewport.isPowerOfTwo());
				}
			}
		} else if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {

	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.BLACK());

		final Viewport viewport = viewports[viewportIndex];
		viewport.apply(g);
		g.setColor(Colors.BLUE());
		g.fillRect(-VIEWPORT_WIDTH, -VIEWPORT_HEIGHT, VIEWPORT_WIDTH * 3f, VIEWPORT_HEIGHT * 3f);
		g.drawTexture(texture, 0f, 8f);

		g.setColor(Colors.RED());
		g.drawRect(0f, 0f, viewport.getWidth(), viewport.getHeight());

		switch(viewportIndex) {
		case 1:
			g.drawString("Fit Viewport (" +
					viewport.getX() + "," + viewport.getY() + "," +
					viewport.getWidth() + "," + viewport.getHeight() + ") vs Window (" +
					g.getWindowWidth() + "," + g.getWindowHeight() + ")", 4f, 4f);
			break;
		case 2:
			g.drawString("Stretch Viewport (" +
					viewport.getX() + "," + viewport.getY() + "," +
					viewport.getWidth() + "," + viewport.getHeight() + ") vs Window (" +
					g.getWindowWidth() + "," + g.getWindowHeight() + ")", 4f, 4f);
			break;
		case 3:
			g.drawString("Extend Viewport (" +
					viewport.getX() + "," + viewport.getY() + "," +
					viewport.getWidth() + "," + viewport.getHeight() + ") vs Window (" +
					g.getWindowWidth() + "," + g.getWindowHeight() + ")", 4f, 4f);
			break;
		case 4:
			g.drawString("Screen Viewport (" +
					viewport.getX() + "," + viewport.getY() + "," +
					viewport.getWidth() + "," + viewport.getHeight() + ") vs Window (" +
					g.getWindowWidth() + "," + g.getWindowHeight() + ")", 4f, 4f);
			break;
		default:
		case 0:
			g.drawString("Fill Viewport (" +
					viewport.getX() + "," + viewport.getY() + "," +
					viewport.getWidth() + "," + viewport.getHeight() + ") vs Window (" +
					g.getWindowWidth() + "," + g.getWindowHeight() + ")", 4f, 4f);
			break;
		}
		viewport.unapply(g);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(ViewportUAT.class);
	}
}
