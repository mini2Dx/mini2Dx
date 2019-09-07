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

import org.mini2Dx.gdx.math.Matrix4;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.math.Vector3;
import org.mini2Dx.gdx.utils.Disposable;

/**
 * Interface to platform-specific shader implementations
 */
public interface Shader extends Disposable {
	/**
	 * Binds the shader for parameter modification
	 */
	public void begin();

	/**
	 * Unbinds the shader for parameter modification
	 */
	public void end();

	/**
	 * Checks if the shader has the specified parameter (uniform, attribute, etc.).
	 * @param name The parameter name
	 * @return False if the uniform, attribute, etc. does not exist
	 */
	public boolean hasParameter(String name);

	/**
	 * Sets the specified texture parameter. Automatically allocates a bind ID for the texture on OpenGL platforms.
	 * Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param texture The texture to bind to the parameter
	 */
	public void setParameter(String name, Texture texture);

	/**
	 * Sets the specified texture parameter. Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param bindId The texture bind ID to use on OpenGL platforms
	 * @param texture The texture to bind to the parameter
	 */
	public void setParameter(String name, int bindId, Texture texture);

	/**
	 * Sets the specified float parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param value
	 */
	public void setParameterf(String name, float value);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param value1
	 * @param value2
	 */
	public void setParameterf(String name, float value1, float value2);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param value1
	 * @param value2
	 * @param value3
	 */
	public void setParameterf(String name, float value1, float value2, float value3);

	/**
	 * Sets the specified parameter (uniform or attribute). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 */
	public void setParameterf(String name, float value1, float value2, float value3, float value4);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param vec
	 */
	public void setParameterf(String name, Vector2 vec);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param vec
	 */
	public void setParameterf(String name, Vector3 vec);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param value
	 */
	public void setParameteri(String name, int value);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param matrix
	 */
	public void setParameterMatrix(String name, Matrix4 matrix);

	/**
	 * Sets the specified parameter (uniform). Note: {@link #begin()} must be called before using this method
	 * @param name The parameter name
	 * @param matrix
	 * @param transpose
	 */
	public void setParameterMatrix(String name, Matrix4 matrix, boolean transpose);

	public String getLog();

	/**
	 * Returns if this shader compiled successfully
	 * @return
	 */
	public abstract boolean isCompiled();

	/**
	 * Returns the {@link ShaderType} (Note: different platforms require different types)
	 * @return
	 */
	public abstract ShaderType getShaderType();
}
