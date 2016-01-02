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
package org.mini2Dx.core.graphics;

import java.io.IOException;
import java.io.Writer;

import org.mini2Dx.core.engine.Updatable;
import org.mini2Dx.core.game.GameContainer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Wraps {@link com.badlogic.gdx.graphics.g2d.ParticleEffect} to match mini2Dx's
 * design
 */
public class ParticleEffect implements Updatable, Disposable {
	private com.badlogic.gdx.graphics.g2d.ParticleEffect particleEffect;

	public ParticleEffect() {
		particleEffect = new com.badlogic.gdx.graphics.g2d.ParticleEffect();
	}

	public ParticleEffect(ParticleEffect effect) {
		particleEffect = new com.badlogic.gdx.graphics.g2d.ParticleEffect(
				effect.particleEffect);
		particleEffect.setFlip(false, true);
	}

	public void load(FileHandle effectFile, FileHandle imagesDir) {
		particleEffect.load(effectFile, imagesDir);
		particleEffect.setFlip(false, true);
	}

	public void load(FileHandle effectFile, TextureAtlas atlas) {
		particleEffect.load(effectFile, atlas);
		particleEffect.setFlip(false, true);
	}

	public void load(FileHandle effectFile, TextureAtlas atlas,
			String atlasPrefix) {
		particleEffect.load(effectFile, atlas, atlasPrefix);
		particleEffect.setFlip(false, true);
	}

	public void save(Writer output) throws IOException {
		particleEffect.save(output);
	}

	@Override
	public void update(GameContainer gc, float delta) {
		particleEffect.update(delta);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		particleEffect.update(GameContainer.MAXIMUM_DELTA * alpha);
	}

	void render(SpriteBatch spriteBatch) {
		particleEffect.draw(spriteBatch);
	}

	@Override
	public void dispose() {
		particleEffect.dispose();
		particleEffect = null;
	}

	public void scaleEffect(float scaleFactor) {
		particleEffect.scaleEffect(scaleFactor);
	}

	public void start() {
		particleEffect.start();
	}

	public void reset() {
		particleEffect.reset();
	}

	public void allowCompletion() {
		particleEffect.allowCompletion();
	}

	public boolean isComplete() {
		return particleEffect.isComplete();
	}

	public void setDuration(int duration) {
		particleEffect.setDuration(duration);
	}

	public void setPosition(float x, float y) {
		particleEffect.setPosition(x, y);
	}

	public void setFlip(boolean flipX, boolean flipY) {
		particleEffect.setFlip(flipX, flipY);
	}

	public void flipY() {
		particleEffect.flipY();
	}

	public Array<ParticleEmitter> getEmitters() {
		return particleEffect.getEmitters();
	}

	public void setEmittersCleanUpBlendFunction(boolean cleanUpBlendFunction) {
		particleEffect.setEmittersCleanUpBlendFunction(cleanUpBlendFunction);
	}
}
