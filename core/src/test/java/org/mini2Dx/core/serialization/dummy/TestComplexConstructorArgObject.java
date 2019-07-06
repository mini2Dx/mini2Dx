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
package org.mini2Dx.core.serialization.dummy;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;

import java.util.Objects;

public class TestComplexConstructorArgObject {
	private final int id;
	private float x, y, width, height;

	public TestComplexConstructorArgObject() {
		this(9);
	}

	public TestComplexConstructorArgObject(@ConstructorArg(clazz = Integer.class, name = "id") int id) {
		this(id, 0f, 0f, 0f, 0f);
	}

	public TestComplexConstructorArgObject(@ConstructorArg(clazz = Integer.class, name = "id") int id,
										   @ConstructorArg(clazz = Float.class, name = "x") float x,
										   @ConstructorArg(clazz = Float.class, name = "y") float y,
										   @ConstructorArg(clazz = Float.class, name = "width") float width,
										   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@ConstructorArg(clazz = Integer.class, name = "id")
	public int getId() {
		return id;
	}

	@ConstructorArg(clazz = Float.class, name = "x")
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	@ConstructorArg(clazz = Float.class, name = "y")
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@ConstructorArg(clazz = Float.class, name = "width")
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	@ConstructorArg(clazz = Float.class, name = "height")
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TestComplexConstructorArgObject that = (TestComplexConstructorArgObject) o;
		if(id != that.id) {
			System.err.println(id + " (id) !=" + that.id);
			return false;
		}
		if(Float.compare(that.x, x) != 0) {
			System.err.println(x + " (x) !=" + that.x);
			return false;
		}
		if(Float.compare(that.y, y) != 0) {
			System.err.println(y + " (y) !=" + that.y);
			return false;
		}
		if(Float.compare(that.width, width) != 0) {
			System.err.println(width + " (width) !=" + that.width);
			return false;
		}
		if(Float.compare(that.height, height) != 0) {
			System.err.println(height + " (height) !=" + that.height);
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, x, y, width, height);
	}
}
