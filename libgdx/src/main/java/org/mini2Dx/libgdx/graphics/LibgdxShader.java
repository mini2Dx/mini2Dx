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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.mini2Dx.core.graphics.Shader;
import org.mini2Dx.core.graphics.ShaderType;

public class LibgdxShader implements Shader {
	public final ShaderProgram shaderProgram;

	public LibgdxShader(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	@Override
	public String getLog() {
		return shaderProgram.getLog();
	}

	@Override
	public boolean isCompiled() {
		return shaderProgram.isCompiled();
	}

	@Override
	public ShaderType getShaderType() {
		return ShaderType.GLSL;
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
	}
}
