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
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

public class ShaderUAT extends BasicGameScreen {
	private static final String SHADER_PATH = "shader";
	private static final float COLOR_CHANGE_INTERVAL = 2f;

	private final Color[] COLORS = new Color[] {
			Colors.RED(),
			Colors.BLUE(),
			Colors.GREEN()
	};

	private Pixmap colorFill;
	private Texture backgroundTexture, overlayTexture;
	private Shader shader;

	private float colorTimer = 0f;
	private int colorIndex = 0;

	@Override
	public void initialise(GameContainer gc) {
		shader = Mdx.graphics.newShader(SHADER_PATH);
		backgroundTexture = Mdx.graphics.newTexture(Mdx.files.internal("unsealed.png"));

		colorFill = Mdx.graphics.newPixmap(backgroundTexture.getWidth(), backgroundTexture.getHeight(), PixmapFormat.RGBA8888);
		overlayTexture = Mdx.graphics.newTexture(colorFill);

		updateOverlayTexture();

		shader.begin();
		shader.setParameter("u_texture", 0, backgroundTexture);
		shader.setParameter("u_overlay", 1, overlayTexture);
		shader.end();
	}

	private void updateOverlayTexture() {
		colorFill.setColor(COLORS[colorIndex]);
		colorFill.fill();
		overlayTexture.draw(colorFill, 0, 0);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		colorTimer += delta;

		if(colorTimer >= COLOR_CHANGE_INTERVAL) {
			colorTimer %= COLOR_CHANGE_INTERVAL;
			colorIndex = colorIndex + 1 >= COLORS.length ? 0 : colorIndex + 1;

			updateOverlayTexture();
		}

		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		final Shader previousShader = g.getShader();
		g.setShader(shader);

		g.drawTexture(backgroundTexture, 0, 0);

		g.setShader(previousShader);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(ShaderUAT.class);
	}
}
