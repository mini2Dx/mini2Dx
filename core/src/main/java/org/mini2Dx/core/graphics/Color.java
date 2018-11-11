/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of mini2Dx nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
}
