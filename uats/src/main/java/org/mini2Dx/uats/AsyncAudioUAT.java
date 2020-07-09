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
package org.mini2Dx.uats;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.audio.AsyncSoundResult;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.audio.SoundCompletionListener;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.uats.util.ScreenIds;
import org.mini2Dx.uats.util.UATSelectionScreen;

/**
 * UAT for {@link org.mini2Dx.core.Audio} API
 */
public class AsyncAudioUAT extends BasicGameScreen implements SoundCompletionListener {
	private static final String LOGGING_TAG = AsyncAudioUAT.class.getSimpleName();

	private final FileHandleResolver fileHandleResolver;

	private Sound sound;

	private float timer;
	private long expectedSoundId, completionSoundId;
	private long lastSoundCompletionEvent;

	public AsyncAudioUAT(FileHandleResolver fileHandleResolver) {
		this.fileHandleResolver = fileHandleResolver;
	}

	@Override
	public void initialise(GameContainer gc) {
		Mdx.audio.addSoundCompletionListener(this);
		try {
			AsyncSoundResult soundResult = Mdx.audio.newAsyncSound(fileHandleResolver.resolve("sounds/sound.ogg"));
			while (!soundResult.isFinished()){
				Thread.sleep(10);
			}
			sound = soundResult.getResult();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		timer -= delta;
		
		if(timer < 0f) {
			expectedSoundId = sound.play(1f, 0.8f, 0f);
			timer = 5f;
		}

		if (Mdx.input.justTouched()) {
			screenManager.enterGameScreen(UATSelectionScreen.SCREEN_ID, new FadeOutTransition(),
					new FadeInTransition());
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setBackgroundColor(Colors.WHITE());
		g.setColor(Colors.BLACK());
		g.drawString("Expecting: " + expectedSoundId + " Completed: " + completionSoundId, 32, 32);
		g.drawString("Last sound completion event at: " + lastSoundCompletionEvent, 32, 64);
	}

	@Override
	public int getId() {
		return ScreenIds.getScreenId(AsyncAudioUAT.class);
	}

	@Override
	public void onSoundCompleted(long soundId) {
		lastSoundCompletionEvent = System.currentTimeMillis();
		completionSoundId = soundId;
	}
}
