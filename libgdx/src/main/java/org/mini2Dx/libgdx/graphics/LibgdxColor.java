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

import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.gdx.math.MathUtils;

import java.util.Objects;

public class LibgdxColor implements Color {
	public com.badlogic.gdx.graphics.Color color;

	public LibgdxColor(int rgba8888) {
		this(new com.badlogic.gdx.graphics.Color(rgba8888));
	}

	public LibgdxColor(int r, int g, int b, int a) {
		this(new com.badlogic.gdx.graphics.Color(r / 255f, g / 255f, b / 255f, a / 255f));
	}

	public LibgdxColor(float r, float g, float b, float a) {
		this(new com.badlogic.gdx.graphics.Color(r, g, b, a));
	}

	public LibgdxColor(byte r, byte g, byte b, byte a) {
		this.color = new com.badlogic.gdx.graphics.Color(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	public LibgdxColor(com.badlogic.gdx.graphics.Color color) {
		this.color = color;
	}

	@Override
	public Color copy() {
		return new LibgdxColor(color.cpy());
	}

	@Override
	public Color set(Color color) {
		return set(color.getRAsFloat(), color.getGAsFloat(), color.getBAsFloat(), color.getAAsFloat());
	}

	@Override
	public Color set(float r, float g, float b, float a) {
		color.set(r, g, b, a);
		return this;
	}

	@Override
	public Color set(byte r, byte g, byte b, byte a) {
		return set(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	@Override
	public Color add(Color color) {
		return add(color.getRAsFloat(), color.getGAsFloat(), color.getBAsFloat(), color.getAAsFloat());
	}

	@Override
	public Color add(float r, float g, float b, float a) {
		color.add(r, g, b, a);
		return this;
	}

	@Override
	public Color add(byte r, byte g, byte b, byte a) {
		return add(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	@Override
	public Color multiply(Color color) {
		return multiply(color.getRAsFloat(), color.getGAsFloat(), color.getBAsFloat(), color.getAAsFloat());
	}

	@Override
	public Color multiply(float r, float g, float b, float a) {
		color.mul(r, g, b, a);
		return this;
	}

	@Override
	public Color multiply(byte r, byte g, byte b, byte a) {
		return multiply(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	@Override
	public Color multiply(float multiplier) {
		color.mul(multiplier);
		return this;
	}

	@Override
	public Color subtract(Color color) {
		return subtract(color.getRAsFloat(), color.getGAsFloat(), color.getBAsFloat(), color.getAAsFloat());
	}

	@Override
	public Color subtract(float r, float g, float b, float a) {
		color.sub(r, g, b, a);
		return this;
	}

	@Override
	public Color subtract(byte r, byte g, byte b, byte a) {
		return subtract(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	@Override
	public Color lerp(Color color, float t) {
		return lerp(color.getRAsFloat(), color.getGAsFloat(), color.getBAsFloat(), color.getAAsFloat(), t);
	}

	@Override
	public Color lerp(float r, float g, float b, float a, float t) {
		color.lerp(r, g, b, a, t);
		return this;
	}

	@Override
	public Color lerp(byte r, byte g, byte b, byte a, float t) {
		return lerp(r / 255f, g / 255f, b / 255f, a / 255f, t);
	}

	@Override
	public float getRAsFloat() {
		return color.r;
	}

	@Override
	public float getGAsFloat() {
		return color.g;
	}

	@Override
	public float getBAsFloat() {
		return color.b;
	}

	@Override
	public float getAAsFloat() {
		return color.a;
	}

	@Override
	public byte getRAsByte() {
		return (byte) MathUtils.round(color.r * 255f);
	}

	@Override
	public byte getGAsByte() {
		return (byte) MathUtils.round(color.g * 255f);
	}

	@Override
	public byte getBAsByte() {
		return (byte) MathUtils.round(color.b * 255f);
	}

	@Override
	public byte getAAsByte() {
		return (byte) MathUtils.round(color.a * 255f);
	}

	@Override
	public void setR(float r) {
		color.r = r;
	}

	@Override
	public void setG(float g) {
		color.g = g;
	}

	@Override
	public void setB(float b) {
		color.b = b;
	}

	@Override
	public void setA(float a) {
		color.a = a;
	}

	@Override
	public void setR(byte r) {
		color.r = r / 255f;
	}

	@Override
	public void setG(byte g) {
		color.g = g / 255f;
	}

	@Override
	public void setB(byte b) {
		color.b = b / 255f;
	}

	@Override
	public void setA(byte a) {
		color.a = a / 255f;
	}

	@Override
	public boolean equals(Color color) {
		if(color instanceof LibgdxColor) {
			LibgdxColor gdxColor = (LibgdxColor) color;
			return this.color.equals(gdxColor.color);
		}
		if(!MathUtils.isEqual(getRAsFloat(), color.getRAsFloat())) {
			return false;
		}
		if(!MathUtils.isEqual(getGAsFloat(), color.getGAsFloat())) {
			return false;
		}
		if(!MathUtils.isEqual(getBAsFloat(), color.getBAsFloat())) {
			return false;
		}
		if(!MathUtils.isEqual(getAAsFloat(), color.getAAsFloat())) {
			return false;
		}
		return true;
	}

	@Override
	public float rf() {
		return getRAsFloat();
	}

	@Override
	public float gf() {
		return getGAsFloat();
	}

	@Override
	public float bf() {
		return getBAsFloat();
	}

	@Override
	public float af() {
		return getAAsFloat();
	}

	@Override
	public float rb() {
		return getRAsByte();
	}

	@Override
	public float gb() {
		return getGAsByte();
	}

	@Override
	public float bb() {
		return getBAsByte();
	}

	@Override
	public float ab() {
		return getAAsByte();
	}

	@Override
	public int argb8888() {
		return com.badlogic.gdx.graphics.Color.argb8888(color);
	}

	@Override
	public int rgba8888() {
		return com.badlogic.gdx.graphics.Color.rgba8888(color);
	}

	@Override
	public int rgba4444() {
		return com.badlogic.gdx.graphics.Color.rgba4444(color);
	}

	@Override
	public int rgb888() {
		return com.badlogic.gdx.graphics.Color.rgb888(color);
	}

	@Override
	public int rgb565() {
		return com.badlogic.gdx.graphics.Color.rgb565(color);
	}

	@Override
	public int bgr565() {
		return ((int)(color.b * 31) << 11) | ((int)(color.g * 63) << 5) | (int)(color.r * 31);
	}

	@Override
	public int bgra4444() {
		return ((int)(color.b * 15) << 12) | ((int)(color.g * 15) << 8) | ((int)(color.r * 15) << 4) | (int)(color.a * 15);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LibgdxColor that = (LibgdxColor) o;
		return color.equals(that.color);
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}
}
