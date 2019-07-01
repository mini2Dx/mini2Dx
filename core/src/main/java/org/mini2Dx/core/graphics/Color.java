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

/**
 * Common interface for platform native Color representations
 */
public interface Color {

	/**
	 * Creates a new {@link Color} with the same R,G,B and A components as this {@link Color}
	 * @return A new {@link Color} instance
	 */
	public Color copy();

	/**
	 * Sets the RGBA components of this {@link Color} from another {@link Color}
	 * @param color
	 * @return This {@link Color} for chaining
	 */
	public Color set(Color color);

	/**
	 * Sets the RGBA components
	 * @param r The red component (between 0.0 and 1.0)
	 * @param g The green component (between 0.0 and 1.0)
	 * @param b The blue component (between 0.0 and 1.0)
	 * @param a The alpha component (between 0.0 and 1.0)
	 * @return This {@link Color} for chaining
	 */
	public Color set(float r, float g, float b, float a);

	/**
	 * Sets the RGBA components
	 * @param r The red component (between 0 and 255)
	 * @param g The green component (between 0 and 255)
	 * @param b The blue component (between 0 and 255)
	 * @param a The alpha component (between 0 and 255)
	 * @return This {@link Color} for chaining
	 */
	public Color set(byte r, byte g, byte b, byte a);

	/**
	 * Adds another {@link Color} to this {@link Color}.
	 * @param color The {@link Color} to add
	 * @return This {@link Color} for chaining
	 */
	public Color add(Color color);

	/**
	 * Adds RGBA components to this {@link Color}
	 * @param r The red component (between 0.0 and 1.0)
	 * @param g The green component (between 0.0 and 1.0)
	 * @param b The blue component (between 0.0 and 1.0)
	 * @param a The alpha component (between 0.0 and 1.0)
	 * @return This {@link Color} for chaining
	 */
	public Color add(float r, float g, float b, float a);

	/**
	 * Adds RGBA components to this {@link Color}
	 * @param r The red component (between 0 and 255)
	 * @param g The green component (between 0 and 255)
	 * @param b The blue component (between 0 and 255)
	 * @param a The alpha component (between 0 and 255)
	 * @return This {@link Color} for chaining
	 */
	public Color add(byte r, byte g, byte b, byte a);

	/**
	 * Multiplies this {@link Color} by the given {@link Color}
	 * @param color The {@link Color} to multiply by
	 * @return This {@link Color} for chaining
	 */
	public Color multiply(Color color);

	/**
	 * Multiplies this {@link Color} by the given RGBA components
	 * @param r The red component (between 0.0 and 1.0)
	 * @param g The green component (between 0.0 and 1.0)
	 * @param b The blue component (between 0.0 and 1.0)
	 * @param a The alpha component (between 0.0 and 1.0)
	 * @return This {@link Color} for chaining
	 */
	public Color multiply(float r, float g, float b, float a);

	/**
	 * Multiplies this {@link Color} by the given RGBA components
	 * @param r The red component (between 0 and 255)
	 * @param g The green component (between 0 and 255)
	 * @param b The blue component (between 0 and 255)
	 * @param a The alpha component (between 0 and 255)
	 * @return This {@link Color} for chaining
	 */
	public Color multiply(byte r, byte g, byte b, byte a);

	/**
	 * Multiplies this {@link Color} by the given multiplier
	 * @param multiplier The value to multiply the RGBA components by
	 * @return This {@link Color} for chaining
	 */
	public Color multiply(float multiplier);

	/**
	 * Subtracts a {@link Color} from this {@link Color}
	 * @param color The {@link Color} to subtract
	 * @return This {@link Color} for chaining
	 */
	public Color subtract(Color color);

	/**
	 * Subtracts RGBA components from this {@link Color}
	 * @param r The red component (between 0.0 and 1.0)
	 * @param g The green component (between 0.0 and 1.0)
	 * @param b The blue component (between 0.0 and 1.0)
	 * @param a The alpha component (between 0.0 and 1.0)
	 * @return This {@link Color} for chaining
	 */
	public Color subtract(float r, float g, float b, float a);

	/**
	 * Subtracts RGBA components from this {@link Color}
	 * @param r The red component (between 0 and 255)
	 * @param g The green component (between 0 and 255)
	 * @param b The blue component (between 0 and 255)
	 * @param a The alpha component (between 0 and 255)
	 * @return This {@link Color} for chaining
	 */
	public Color subtract(byte r, byte g, byte b, byte a);

	/**
	 * Linearly interpolates between this color and the target color by t
	 * @param color The target {@link Color}
	 * @param t The interpolation coefficient
	 * @return This {@link Color} for chaining
	 */
	public Color lerp(Color color, float t);

	/**
	 * Linearly interpolates between this color and the target color by t
	 * @param r The red component of the target {@link Color} (A value between 0.0. and 1.0)
	 * @param g The green component of the target {@link Color} (A value between 0.0. and 1.0)
	 * @param b The blue component of the target {@link Color} (A value between 0.0. and 1.0)
	 * @param a The alpha component of the target {@link Color} (A value between 0.0. and 1.0)
	 * @param t The interpolation coefficient
	 * @return This {@link Color} for chaining
	 */
	public Color lerp(float r, float g, float b, float a, float t);

	/**
	 * Linearly interpolates between this color and the target color by t
	 * @param r The red component of the target {@link Color} (A value between 0 and 255)
	 * @param g The green component of the target {@link Color} (A value between 0 and 255)
	 * @param b The blue component of the target {@link Color} (A value between 0 and 255)
	 * @param a The alpha component of the target {@link Color} (A value between 0 and 255)
	 * @param t The interpolation coefficient
	 * @return This {@link Color} for chaining
	 */
	public Color lerp(byte r, byte g, byte b, byte a, float t);

	/**
	 * Returns the red component of this {@link Color}
	 * @return A value between 0.0 and 1.0
	 */
	public float getRAsFloat();

	/**
	 * Returns the green component of this {@link Color}
	 * @return A value between 0.0 and 1.0
	 */
	public float getGAsFloat();

	/**
	 * Returns the blue component of this {@link Color}
	 * @return A value between 0.0 and 1.0
	 */
	public float getBAsFloat();

	/**
	 * Returns the alpha component of this {@link Color}
	 * @return A value between 0.0 and 1.0
	 */
	public float getAAsFloat();

	/**
	 * Returns the red component of this {@link Color}
	 * @return A value between 0 and 255
	 */
	public byte getRAsByte();

	/**
	 * Returns the green component of this {@link Color}
	 * @return A value between 0 and 255
	 */
	public byte getGAsByte();

	/**
	 * Returns the blue component of this {@link Color}
	 * @return A value between 0 and 255
	 */
	public byte getBAsByte();

	/**
	 * Returns the alpha component of this {@link Color}
	 * @return A value between 0 and 255
	 */
	public byte getAAsByte();

	/**
	 * Sets the red component
	 * @param r A value between 0f and 1f
	 */
	public void setR(float r);

	/**
	 * Sets the green component
	 * @param g A value between 0f and 1f
	 */
	public void setG(float g);

	/**
	 * Sets the blue component
	 * @param b A value between 0f and 1f
	 */
	public void setB(float b);

	/**
	 * Sets the alpha component
	 * @param a A value between 0f and 1f
	 */
	public void setA(float a);

	public void setR(byte r);

	public void setG(byte g);

	public void setB(byte b);

	public void setA(byte a);

	/**
	 * Returns if this {@link Color} is the same as another {@link Color}
	 * @param color The {@link Color} to compare to
	 * @return True if same RGBA
	 */
	public boolean equals(Color color);
}
