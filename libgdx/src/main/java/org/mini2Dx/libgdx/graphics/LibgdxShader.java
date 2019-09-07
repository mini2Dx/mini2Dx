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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.mini2Dx.core.graphics.Shader;
import org.mini2Dx.core.graphics.ShaderType;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.gdx.math.Matrix4;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.math.Vector3;

public class LibgdxShader implements Shader {
	private static int AUTO_BIND_ID = 1;

	public final ShaderProgram shaderProgram;
	private final com.badlogic.gdx.math.Matrix4 tmpMatrix4 = new com.badlogic.gdx.math.Matrix4();

	public LibgdxShader(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	@Override
	public void begin() {
		shaderProgram.begin();
	}

	@Override
	public void end() {
		shaderProgram.end();
	}

	@Override
	public boolean hasParameter(String name) {
		return shaderProgram.hasAttribute(name) || shaderProgram.hasUniform(name);
	}

	@Override
	public void setParameter(String name, Texture texture) {
		setParameter(name, AUTO_BIND_ID, texture);
		AUTO_BIND_ID = AUTO_BIND_ID + 1 >= GL20.GL_MAX_TEXTURE_UNITS ? 1 : AUTO_BIND_ID++;
	}

	@Override
	public void setParameter(String name, int bindId, Texture texture) {
		LibgdxTexture libgdxTexture = (LibgdxTexture) texture;
		libgdxTexture.bind(bindId);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		shaderProgram.setUniformi(name, bindId);
	}

	@Override
	public void setParameterf(String name, float value) {
		shaderProgram.setUniformf(name, value);
	}

	@Override
	public void setParameterf(String name, float value1, float value2) {
		shaderProgram.setUniformf(name, value1, value2);
	}

	@Override
	public void setParameterf(String name, float value1, float value2, float value3) {
		shaderProgram.setUniformf(name, value1, value2, value3);
	}

	@Override
	public void setParameterf(String name, float value1, float value2, float value3, float value4) {
		if(shaderProgram.hasAttribute(name)) {
			shaderProgram.setAttributef(name, value1, value2, value3, value4);
		} else {
			shaderProgram.setUniformf(name, value1, value2, value3, value4);
		}
	}

	@Override
	public void setParameterf(String name, Vector2 vec) {
		shaderProgram.setUniformf(name, vec.x, vec.y);
	}

	@Override
	public void setParameterf(String name, Vector3 vec) {
		shaderProgram.setUniformf(name, vec.x, vec.y, vec.z);
	}

	@Override
	public void setParameteri(String name, int value) {
		shaderProgram.setUniformi(name, value);
	}

	@Override
	public void setParameterMatrix(String name, Matrix4 matrix) {
		tmpMatrix4.set(matrix.getValues());
		shaderProgram.setUniformMatrix(name, tmpMatrix4);
	}

	@Override
	public void setParameterMatrix(String name, Matrix4 matrix, boolean transpose) {
		tmpMatrix4.set(matrix.getValues());
		shaderProgram.setUniformMatrix(name, tmpMatrix4, transpose);
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
