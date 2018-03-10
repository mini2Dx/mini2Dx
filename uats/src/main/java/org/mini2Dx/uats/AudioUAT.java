/**
 * Copyright (c) 2018 See AUTHORS file
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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.audio.Mini2DxAudio;
import org.mini2Dx.core.audio.SoundCompletionListener;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.uats.util.ScreenIds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

/**
 * UAT for {@link Mini2DxAudio} API
 */
public class AudioUAT extends BasicGameScreen implements SoundCompletionListener {
	private static final String LOGGING_TAG = AudioUAT.class.getSimpleName();
	
	private Sound sound;
	private Music music;
	
	private float timer;
	private long expectedSoundId, completionSoundId;
	private long lastSoundCompletionEvent;

	@Override
	public void initialise(GameContainer gc) {
		Mdx.audio.addSoundCompletionListener(this);
		sound = Gdx.audio.newSound(Gdx.files.internal("sound.ogg"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
		music.setLooping(true);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		timer -= delta;
		
		if(timer < 0f) {
			expectedSoundId = sound.play(1f, 0.8f, 0f);
			timer = 5f;
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawString("Expecting: " + expectedSoundId + " Completed: " + completionSoundId, 32, 32);
		g.drawString("Last sound completion event at: " + lastSoundCompletionEvent, 32, 64);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(AudioUAT.class);
	}
	
	@Override
	public void postTransitionIn(Transition transitionIn) {
		music.play();
	}
	
	@Override
	public void preTransitionOut(Transition transitionOut) {
		music.stop();
	}

	@Override
	public void onCompletion(long soundId) {
		lastSoundCompletionEvent = System.currentTimeMillis();
		completionSoundId = soundId;
	}
	
}
