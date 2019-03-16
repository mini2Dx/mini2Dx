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
package org.mini2Dx.core.graphics;

import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.gdx.utils.Disposable;

import java.io.IOException;
import java.io.Writer;

public interface ParticleEffect extends Disposable {

	public void load(FileHandle effectFile, FileHandle imagesDir);

	public void load(FileHandle effectFile, TextureAtlas atlas);

	public void load(FileHandle effectFile, TextureAtlas atlas, String atlasPrefix);

	public void update(GameContainer gc, float delta);

	public void interpolate(GameContainer gc, float alpha);

	public void scaleEffect(float scaleFactor);

	public void start();

	public void reset();

	public void allowCompletion();

	public boolean isComplete();

	public void setDuration(int duration);

	public void setPosition(float x, float y);

	public void setFlip(boolean flipX, boolean flipY);

	public void flipY();

	public void setEmittersCleanUpBlendFunction(boolean cleanUpBlendFunction);
}
