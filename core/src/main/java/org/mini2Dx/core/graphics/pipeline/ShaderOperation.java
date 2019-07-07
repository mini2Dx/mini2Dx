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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Shader;

/**
 * Applies/unapplies a {@link Shader}
 */
public class ShaderOperation implements RenderOperation {
	private static final String LOGGING_TAG = ShaderOperation.class.getSimpleName();

	protected final Shader shader;
	private Shader previousShader;
	
	public ShaderOperation(String shaderPath) {
		this(Mdx.graphics.newShader(shaderPath));
	}
	
	public ShaderOperation(Shader shader) {
		this.shader = shader;
		if(!shader.isCompiled()) {
			Mdx.log.error(LOGGING_TAG, shader.getLog());
		}
	}
	
	@Override
	public void update(GameContainer gc, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}
	
	@Override
	public void apply(GameContainer gc, Graphics g) {
		previousShader = g.getShader();
		g.setShader(shader);
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
		g.setShader(previousShader);
	}
}
