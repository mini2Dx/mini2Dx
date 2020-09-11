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
package org.mini2Dx.libgdx.game;

import com.badlogic.gdx.Application;

/**
 * Extends {@link com.badlogic.gdx.ApplicationListener} to add update and interpolate methods.
 *
 * An <code>ApplicationListener</code> is called when the {@link Application} is created, resumed, rendering, paused or destroyed.
 * All methods are called in a thread that has the OpenGL context current. You can thus safely create and manipulate graphics
 * resources.
 */
public interface ApplicationListener extends com.badlogic.gdx.ApplicationListener {
	/** Called before the {@link Application} should update itself. */
	public void preUpdate(float delta);

	/** Called before the {@link Application} should update its physics. */
	public void preUpdatePhysics(float delta);

	/** Called when the {@link Application} should update itself. */
	public void update (float delta);

	/** Called when the {@link Application} should update its physics. */
	public void updatePhysics(float delta);

	/** Called when the {@link Application} should interpolate itself. */
	public void interpolate (float alpha);
}
