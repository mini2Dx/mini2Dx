/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.uats;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.ParticleEffect;
import org.mini2Dx.core.graphics.ParticleEffectPool;
import org.mini2Dx.core.graphics.PooledParticleEffect;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * A {@link GameScreen} that allows visual user acceptance testing of {@link ParticleEffect}s
 */
public class ParticleEffectsUAT extends BasicGameScreen {
	private ParticleEffect prototype;
	private ParticleEffectPool pool;
	private Array<PooledParticleEffect> effects;
	
	private float timer = -1f;

	@Override
	public void initialise(GameContainer gc) {		
		prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("explosion.p"), Gdx.files.internal("particles"));
		prototype.setPosition(gc.getWidth() / 2f, gc.getHeight() / 2f);
		prototype.start();
		
		pool = new ParticleEffectPool(prototype, 0, 70);
		effects = new Array<PooledParticleEffect>();
	}

	@Override
	public void update(GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, float delta) {
		timer -= delta;
		if(timer < 0f) {
			PooledParticleEffect effect = pool.obtain();
			float x = MathUtils.random() * gc.getWidth();
			float y = MathUtils.random() * gc.getHeight();
			effect.setPosition(x, y);
			System.out.println("Spawning effect at " + x + "," + y);
			effects.add(effect);
			timer = 1f;
		}
		
		for(PooledParticleEffect effect : effects) {
			effect.update(gc, delta);
			if(effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.free();
			}
		}
		
		if(Gdx.input.justTouched()) {
            screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(), new FadeInTransition());
        }
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		for(PooledParticleEffect effect : effects) {
			effect.interpolate(gc, alpha);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Color.BLACK);
		for(PooledParticleEffect effect : effects) {
			g.drawParticleEffect(effect);
		}
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(ParticleEffectsUAT.class);
	}
}
