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
package org.mini2Dx.tiled;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Circle;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.serialization.GameDataSerializable;
import org.mini2Dx.core.serialization.GameDataSerializableUtils;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an object loaded from a {@link TiledMap}
 */
public class TiledObject implements GameDataSerializable {
	/**
	 * Default property map capacity
	 */
	public static int DEFAULT_PROPERTY_MAP_SIZE = 16;
	/**
	 * True if Tiled objects should be discarded on load. Useful for reducing memory consumption.
	 */
	public static boolean DISCARD_TILED_OBJECTS = false;

	private static final TiledObject DISCARD_OBJECT = new TiledObject(0, 0, 0, 0, 0);

	private final int id;
	private final float x, y, width, height;
	private final boolean builtFromTemplate;

	private TiledObjectShape objectShape = TiledObjectShape.RECTANGLE;
	private String name;
	private String type;
	private boolean visible;
	private int gid;
	private boolean gidFlipHorizontally, gidFlipVertically, gidFlipDiagonally;
	private ObjectMap<String, String> properties;

	private float [] vertices;
	private String text;
	private boolean wrapText;

	public TiledObject(int id, float x, float y, float width, float height) {
		this(id, x, y, width, height, false);
	}

	TiledObject(int id, float x, float y, float width, float height, boolean builtFromTemplate) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.builtFromTemplate = builtFromTemplate;
	}

	public static TiledObject fromInputStream(DataInputStream inputStream) throws IOException {
		final int id = inputStream.readInt();
		final float x = inputStream.readFloat();
		final float y = inputStream.readFloat();
		final float width = inputStream.readFloat();
		final float height = inputStream.readFloat();
		final boolean builtFromTemplate = inputStream.readBoolean();

		final TiledObject result = DISCARD_TILED_OBJECTS ? DISCARD_OBJECT : new TiledObject(id, x, y, width, height, builtFromTemplate);
		result.readData(inputStream);
		return result;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(id);
		outputStream.writeFloat(x);
		outputStream.writeFloat(y);
		outputStream.writeFloat(width);
		outputStream.writeFloat(height);
		outputStream.writeBoolean(builtFromTemplate);

		outputStream.writeUTF(objectShape.name());
		GameDataSerializableUtils.writeString(name, outputStream);
		GameDataSerializableUtils.writeString(type, outputStream);
		outputStream.writeBoolean(visible);
		outputStream.writeInt(gid);
		outputStream.writeBoolean(gidFlipHorizontally);
		outputStream.writeBoolean(gidFlipVertically);
		outputStream.writeBoolean(gidFlipDiagonally);

		outputStream.writeInt(properties == null ? 0 : properties.size);
		if(properties != null) {
			for(String key : properties.keys()) {
				outputStream.writeUTF(key);
				GameDataSerializableUtils.writeString(properties.get(key, null), outputStream);
			}
		}

		outputStream.writeInt(vertices == null ? -1 : vertices.length);
		if(vertices != null) {
			for(int i = 0; i < vertices.length; i++) {
				outputStream.writeFloat(vertices[i]);
			}
		}

		GameDataSerializableUtils.writeString(text, outputStream);
		outputStream.writeBoolean(wrapText);
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		objectShape = TiledObjectShape.valueOf(inputStream.readUTF());
		name = GameDataSerializableUtils.readString(inputStream);
		type = GameDataSerializableUtils.readString(inputStream);
		visible = inputStream.readBoolean();
		gid = inputStream.readInt();
		gidFlipHorizontally = inputStream.readBoolean();
		gidFlipVertically = inputStream.readBoolean();
		gidFlipDiagonally = inputStream.readBoolean();

		final int totalProperties = inputStream.readInt();
		if(totalProperties > 0) {
			if(properties != null) {
				properties.clear();
			} else {
				properties = new ObjectMap<>(DEFAULT_PROPERTY_MAP_SIZE);
			}
			for(int i = 0; i < totalProperties; i++) {
				String key = inputStream.readUTF();
				String value = GameDataSerializableUtils.readString(inputStream);

				if(DISCARD_TILED_OBJECTS) {
					continue;
				}

				if(builtFromTemplate) {
					key = key.intern();
					value = value.intern();
				}

				properties.put(key, value);
			}
		}

		final int totalVertices = inputStream.readInt();
		if(totalVertices > -1) {
			if(DISCARD_TILED_OBJECTS) {
				for(int i = 0; i < totalVertices; i++) {
					inputStream.readFloat();
				}
			} else {
				vertices = new float[totalVertices];
				for(int i = 0; i < vertices.length; i++) {
					vertices[i] = inputStream.readFloat();
				}
			}
		}

		text = GameDataSerializableUtils.readString(inputStream);
		wrapText = inputStream.readBoolean();

		if(builtFromTemplate) {
			name = name != null ? name.intern() : null;
			type = type != null ? type.intern() : null;
			text = text != null ? text.intern() : null;
		}
	}
	
	/**
	 * Returns this object's properties
	 * @return Null if there are no properties
	 */
	public ObjectMap<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * Returns if the object contains the specified property
	 * @param propertyName The property name to search for
	 * @return True if the object contains the property
	 */
	public boolean containsProperty(String propertyName) {
		if(properties == null)
			return false;
		return properties.containsKey(propertyName);
	}
	
	/**
	 * Returns the value of a specified property
	 * @param propertyName The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		if(properties == null)
			return null;
		return properties.get(propertyName);
	}
	
	/**
	 * Sets the value of a specified property
	 * @param propertyName The property name to set the value for
	 * @param value The value of the property to set
	 */
	public void setProperty(String propertyName, String value) {
		if(properties == null)
			properties = new ObjectMap<String, String>(DEFAULT_PROPERTY_MAP_SIZE);
		properties.put(propertyName, value);
	}

	/**
	 * Returns the ID of this object
	 * @return -1 if using a version of Tiled where IDs are not generated
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of this object
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this object
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the type of this object
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type of this object
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns the x coordinate of this object in pixels
	 * @return
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate of this object in pixels
	 * @return
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Returns the width of this object in pixels
	 * @return
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of this object in pixels
	 * @return
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Returns the Tile GID of this object
	 * @return
	 */
	public int getGid() {
		return gid;
	}
	
	/**
	 * Sets the Tile GID of this object
	 * @param gid
	 */
	public void setGid(int gid) {
		this.gid = gid;
	}

	public boolean isGidFlipHorizontally() {
		return gidFlipHorizontally;
	}

	public void setGidFlipHorizontally(boolean gidFlipHorizontally) {
		this.gidFlipHorizontally = gidFlipHorizontally;
	}

	public boolean isGidFlipVertically() {
		return gidFlipVertically;
	}

	public void setGidFlipVertically(boolean gidFlipVertically) {
		this.gidFlipVertically = gidFlipVertically;
	}

	public boolean isGidFlipDiagonally() {
		return gidFlipDiagonally;
	}

	public void setGidFlipDiagonally(boolean gidFlipDiagonally) {
		this.gidFlipDiagonally = gidFlipDiagonally;
	}

	/**
	 * Returns the {@link TiledObjectShape} of this object
	 * @return Defaults to {@link TiledObjectShape#RECTANGLE}
	 */
	public TiledObjectShape getObjectShape() {
		return objectShape;
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#ELLIPSE}
	 */
	public void setAsEllipse() {
		objectShape = TiledObjectShape.ELLIPSE;
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#POINT}
	 */
	public void setAsPoint() {
		objectShape = TiledObjectShape.POINT;
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#POLYGON}
	 * @param points The points in the format <em>x1,y1 x2,y2</em> per the Tiled specification
	 */
	public void setAsPolygon(String points) {
		objectShape = TiledObjectShape.POLYGON;
		final String [] pointEntries = points.split(" ");

		this.vertices = new float[pointEntries.length * 2];
		for(int i = 0; i < pointEntries.length; i++) {
			final String [] xy = pointEntries[i].split(",");

			this.vertices[(i * 2)] = x + Float.parseFloat(xy[0]);
			this.vertices[(i * 2) + 1] = y + Float.parseFloat(xy[1]);
		}
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#POLYGON}
	 * @param vertices The vertices to set
	 */
	public void setAsPolygon(float [] vertices) {
		objectShape = TiledObjectShape.POLYGON;

		this.vertices = new float[vertices.length];
		for(int i = 0; i < vertices.length; i += 2) {
			this.vertices[i] = x + vertices[i];
			this.vertices[i + 1] = y + vertices[i + 1];
		}
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#TEXT}
	 * @param text The text to display
	 * @param wrapText True if the text wraps
	 */
	public void setAsText(String text, boolean wrapText) {
		objectShape = TiledObjectShape.TEXT;
		this.text = text;
		this.wrapText = wrapText;
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#POLYLINE}
	 * @param points The points in the format <em>x1,y1 x2,y2</em> per the Tiled specification
	 */
	public void setAsPolyline(String points) {
		objectShape = TiledObjectShape.POLYLINE;
		final String [] pointEntries = points.split(" ");

		this.vertices = new float[pointEntries.length * 2];
		for(int i = 0; i < pointEntries.length; i++) {
			final String [] xy = pointEntries[i].split(",");

			this.vertices[(i * 2)] = x + Float.parseFloat(xy[0]);
			this.vertices[(i * 2) + 1] = y + Float.parseFloat(xy[1]);
		}
	}

	/**
	 * Marks this object as a {@link TiledObjectShape#POLYLINE}
	 * @param vertices The line point vertices
	 */
	public void setAsPolyline(float [] vertices) {
		objectShape = TiledObjectShape.POLYLINE;

		this.vertices = new float[vertices.length];
		for(int i = 0; i < vertices.length; i += 2) {
			this.vertices[i] = x + vertices[i];
			this.vertices[i + 1] = y + vertices[i + 1];
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Creates a new {@link Point} from this object.
	 * Warning: If this object's shape is not {@link TiledObjectShape#POINT}, an exception will be thrown.
	 * @return A new {@link Point}
	 */
	public Point toPoint() {
		if(!objectShape.equals(TiledObjectShape.POINT)) {
			throw new MdxException("TiledObject " + id + " is not a point");
		}
		return new Point(x, y);
	}

	/**
	 * Creates a new {@link Circle} from this object.
	 * Warning: If this object's shape is not {@link TiledObjectShape#ELLIPSE}, an exception will be thrown.
	 * @return A new {@link Circle}
	 */
	public Circle toCircle() {
		if(!objectShape.equals(TiledObjectShape.ELLIPSE)) {
			throw new MdxException("TiledObject " + id + " is not a circle");
		}
		if(!MathUtils.isEqual(width, height)) {
			throw new MdxException("TiledObject " + id + " is an ellipse, not a circle");
		}
		return new Circle(x + (width * 0.5f), y + (height * 0.5f), width * 0.5f);
	}

	/**
	 * Creates a new {@link Rectangle} from this object.
	 * Warning: If this object's shape is not {@link TiledObjectShape#RECTANGLE}, an exception will be thrown.
	 * @return A new {@link Rectangle}
	 */
	public Rectangle toRectangle() {
		if(!objectShape.equals(TiledObjectShape.RECTANGLE)) {
			throw new MdxException("TiledObject " + id + " is not a rectangle");
		}
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Creates a new {@link Polygon} from this object.
	 * Warning: If this object's shape is not {@link TiledObjectShape#POLYGON}, an exception will be thrown.
	 * @return A new {@link Polygon}
	 */
	public Polygon toPolygon() {
		if(!objectShape.equals(TiledObjectShape.POLYGON)) {
			throw new MdxException("TiledObject " + id + " is not a polygon");
		}
		return new Polygon(vertices);
	}

	/**
	 * Creates a new {@link Array} of {@link Point}s from this object.
	 * Warning: If this object's shape is not {@link TiledObjectShape#POLYLINE}, an exception will be thrown.
	 * @return A new {@link Array} of {@link Point}s in the line
	 */
	public Array<Point> toPolyline() {
		if(!objectShape.equals(TiledObjectShape.POLYLINE)) {
			throw new MdxException("TiledObject " + id + " is not a polyline");
		}
		final Array<Point> result = new Array<Point>();
		for(int i = 0; i < vertices.length; i += 2) {
			result.add(new Point(vertices[i], vertices[i + 1]));
		}
		return result;
	}

	public float[] getVertices() {
		return vertices;
	}

	public String getText() {
		return text;
	}

	public boolean isWrapText() {
		return wrapText;
	}

	public boolean isBuiltFromTemplate() {
		return builtFromTemplate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TiledObject that = (TiledObject) o;
		return id == that.id && Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0 && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0 && builtFromTemplate == that.builtFromTemplate && visible == that.visible && gid == that.gid && gidFlipHorizontally == that.gidFlipHorizontally && gidFlipVertically == that.gidFlipVertically && gidFlipDiagonally == that.gidFlipDiagonally && wrapText == that.wrapText && objectShape == that.objectShape && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(properties, that.properties) && Arrays.equals(vertices, that.vertices) && Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, x, y, width, height, builtFromTemplate, objectShape, name, type, visible, gid, gidFlipHorizontally, gidFlipVertically, gidFlipDiagonally, properties, text, wrapText);
		result = 31 * result + Arrays.hashCode(vertices);
		return result;
	}

	@Override
	public String toString() {
		return "TiledObject{" +
				"id=" + id +
				", x=" + x +
				", y=" + y +
				", width=" + width +
				", height=" + height +
				", builtFromTemplate=" + builtFromTemplate +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", visible=" + visible +
				", gid=" + gid +
				", gidFlipHorizontally=" + gidFlipHorizontally +
				", gidFlipVertically=" + gidFlipVertically +
				", gidFlipDiagonally=" + gidFlipDiagonally +
				", properties=" + properties +
				", text='" + text + '\'' +
				", wrapText=" + wrapText +
				'}';
	}
}
