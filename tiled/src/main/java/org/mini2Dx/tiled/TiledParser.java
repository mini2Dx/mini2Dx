/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import org.mini2Dx.tiled.renderer.AnimatedTileRenderer;
import org.mini2Dx.tiled.renderer.StaticTileRenderer;
import org.mini2Dx.tiled.renderer.TileFrame;
import org.mini2Dx.tiled.tileset.ImageTilesetSource;
import org.mini2Dx.tiled.tileset.TilesetSource;
import org.mini2Dx.tiled.tileset.TsxTilesetSource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Parses Tiled XML files and notifies {@link TiledParserListener}s of map data
 * 
 * @author MobiDevelop (parsing logic from LibGDX)
 */
public class TiledParser implements TiledParserNotifier {
	protected static final int FLAG_FLIP_HORIZONTALLY = 0x80000000;
	protected static final int FLAG_FLIP_VERTICALLY = 0x40000000;
	protected static final int FLAG_FLIP_DIAGONALLY = 0x20000000;
	protected static final int MASK_CLEAR = 0xE0000000;

	private XmlReader xmlReader;
	private List<TiledParserListener> listeners;

	/**
	 * Constructor
	 */
	public TiledParser() {
		xmlReader = new XmlReader();
		listeners = new ArrayList<TiledParserListener>();
	}

	/**
	 * Parses a TMX file and notifies any {@link TiledParserListener}s of
	 * parsing results
	 * 
	 * @param tmxFileHandle
	 *            A {@link FileHandle} to a TMX file exported from Tiled
	 * @throws IOException
	 *             Thrown if the map file could not be parsed
	 */
	public void parseTmx(FileHandle tmxFileHandle) throws IOException {
		Element root = xmlReader.parse(tmxFileHandle);
		String mapOrientation = root.getAttribute("orientation", null);
		int mapWidth = root.getIntAttribute("width", 0);
		int mapHeight = root.getIntAttribute("height", 0);
		int tileWidth = root.getIntAttribute("tilewidth", 0);
		int tileHeight = root.getIntAttribute("tileheight", 0);
		int sideLength = root.getInt("hexsidelength", -1);
		String staggerAxis = root.getAttribute("staggeraxis", null);
		String staggerIndex = root.getAttribute("staggerindex", null);
		String mapBackgroundColor = root.getAttribute("backgroundcolor", null);
		Color backgroundColor = null;
		if (mapBackgroundColor != null) {
			backgroundColor = convertHexColorToColor(mapBackgroundColor);
		}

		notifyBeginParsing(mapOrientation, staggerAxis, staggerIndex, backgroundColor, mapWidth, mapHeight, tileWidth, tileHeight, sideLength);

		Element properties = root.getChildByName("properties");
		if (properties != null) {
			loadMapProperties(properties);
		}
		Array<Element> tilesets = root.getChildrenByName("tileset");
		for (Element element : tilesets) {
			loadTileSet(element, tmxFileHandle);
			root.removeChild(element);
		}
		for (int i = 0, j = root.getChildCount(); i < j; i++) {
			Element element = root.getChild(i);
			String name = element.getName();
			if (name.equals("layer")) {
				loadTileLayer(element);
			} else if (name.equals("objectgroup")) {
				loadObjectGroup(element);
			}
		}
	}

	/**
	 * Parses a TSX file
	 * 
	 * @param tsxFileHandle
	 *            A {@link FileHandle} to a TSX file exported from Tiled
	 * @return The resulting {@link ImageTilesetSource}
	 * @throws IOException
	 *             Thrown if the tileset file could not be parsed
	 */
	public ImageTilesetSource parseTsx(FileHandle tsxFileHandle) throws IOException {
		Element element = xmlReader.parse(tsxFileHandle);
		String name = element.get("name", null);
		int tileWidth = element.getIntAttribute("tilewidth", 0);
		int tileHeight = element.getIntAttribute("tileheight", 0);
		int spacing = element.getIntAttribute("spacing", 0);
		int margin = element.getIntAttribute("margin", 0);
		String imageSource = element.getChildByName("image").getAttribute("source");
		int imageWidth = element.getChildByName("image").getIntAttribute("width", 0);
		int imageHeight = element.getChildByName("image").getIntAttribute("height", 0);
		String transparentColor = element.getChildByName("image").get("trans", null);

		ImageTilesetSource result = new ImageTilesetSource(imageWidth, imageHeight, tileWidth, tileHeight, spacing,
				margin);
		result.setName(name);
		result.setTilesetImagePath(imageSource);
		result.setTransparentColorValue(transparentColor);

		loadTileProperties(result, element.getChildrenByName("tile"));

		Element properties = element.getChildByName("properties");
		if (properties != null) {
			for (Element property : properties.getChildrenByName("property")) {
				String propertyName = property.getAttribute("name", null);
				String propertyValue = property.getAttribute("value", null);
				if (propertyValue == null) {
					propertyValue = property.getText();
				}
				result.setProperty(propertyName, propertyValue);
			}
		}
		return result;
	}

	private void loadMapProperties(Element element) {
		if (element.getName().equals("properties")) {
			for (Element property : element.getChildrenByName("property")) {
				String name = property.getAttribute("name", null);
				String value = property.getAttribute("value", null);
				if (value == null) {
					value = property.getText();
				}
				notifyMapPropertyParsed(name, value);
			}
		}
	}

	private void loadTileSet(Element element, FileHandle tmxFile) {
		if (element.getName().equals("tileset")) {
			Tileset tileset = null;
			String source = element.getAttribute("source", null);
			int firstGid = element.getIntAttribute("firstgid", 1);

			if (source == null) {
				// Image tileset
				String name = element.get("name", null);
				int tileWidth = element.getIntAttribute("tilewidth", 0);
				int tileHeight = element.getIntAttribute("tileheight", 0);
				int spacing = element.getIntAttribute("spacing", 0);
				int margin = element.getIntAttribute("margin", 0);
				String transparentColor = null;

				String imageSource = "";
				int imageWidth = 0, imageHeight = 0;

				imageSource = element.getChildByName("image").getAttribute("source");
				imageWidth = element.getChildByName("image").getIntAttribute("width", 0);
				imageHeight = element.getChildByName("image").getIntAttribute("height", 0);
				transparentColor = element.getChildByName("image").get("trans", null);

				ImageTilesetSource tilesetSource = new ImageTilesetSource(imageWidth, imageHeight, tileWidth,
						tileHeight, spacing, margin);
				tilesetSource.setName(name);
				tilesetSource.setTransparentColorValue(transparentColor);
				tilesetSource.setTilesetImagePath(imageSource);

				tileset = new Tileset(firstGid, tilesetSource);

				loadTileProperties(tilesetSource, element.getChildrenByName("tile"));

				Element properties = element.getChildByName("properties");
				if (properties != null) {
					for (Element property : properties.getChildrenByName("property")) {
						String propertyName = property.getAttribute("name", null);
						String propertyValue = property.getAttribute("value", null);
						if (propertyValue == null) {
							propertyValue = property.getText();
						}
						tileset.setProperty(propertyName, propertyValue);
					}
				}
			} else {
				// TSX tileset
				TsxTilesetSource tilesetSource = new TsxTilesetSource(tmxFile.parent(), source);
				tileset = new Tileset(firstGid, tilesetSource);
				
				for(int x = 0; x < tilesetSource.getWidthInTiles(); x++) {
					for(int y = 0; y < tilesetSource.getHeightInTiles(); y++) {
						Tile tile = tilesetSource.getTileByPosition(x, y);
						if (tile == null) {
							continue;
						}
						if (tile.getTileRenderer() == null) {
							continue;
						}
						if (tile.getTileRenderer() instanceof AnimatedTileRenderer) {
							notifyTilePropertyParsed(tile);
						} else if(tile.getProperties() != null) {
							notifyTilePropertyParsed(tile);
						}
					}
				}
			}

			notifyTilesetParsed(tileset);
		}
	}

	private void loadTileProperties(TilesetSource tilesetSource, Array<Element> tileElements) {
		for (Element tileElement : tileElements) {
			int localtid = tileElement.getIntAttribute("id", 0);
			Tile tile = tilesetSource.getTile(localtid, 0);
			if (tile != null) {
				String type = tileElement.getAttribute("type", null);
				if (type != null) {
					tile.setProperty("type", type);
				}
				String terrain = tileElement.getAttribute("terrain", null);
				if (terrain != null) {
					tile.setProperty("terrain", terrain);
				}
				String probability = tileElement.getAttribute("probability", null);
				if (probability != null) {
					tile.setProperty("probability", probability);
				}
				Element properties = tileElement.getChildByName("properties");
				if (properties != null) {
					for (Element property : properties.getChildrenByName("property")) {
						String propertyName = property.getAttribute("name", null);
						String propertyValue = property.getAttribute("value", null);
						if (propertyValue == null) {
							propertyValue = property.getText();
						}
						tile.setProperty(propertyName, propertyValue);
					}
				}
				Element animation = tileElement.getChildByName("animation");
				if(animation != null) {
					Array<Element> frameElements = animation.getChildrenByName("frame");
					TileFrame [] frames = new TileFrame[frameElements.size];
					for (int i = 0; i < frameElements.size; i++) {
						Element frameElement = frameElements.get(i);
						int tileId = frameElement.getIntAttribute("tileid");
						float duration = frameElement.getFloatAttribute("duration") / 1000f;
						frames[i] = new TileFrame(duration, tileId);
					}
					
					tile.setTileRenderer(new AnimatedTileRenderer(tilesetSource, frames));
				}
				
				notifyTilePropertyParsed(tile);
			}
		}
		
		for(int x = 0; x < tilesetSource.getWidthInTiles(); x++) {
			for(int y = 0; y < tilesetSource.getHeightInTiles(); y++) {
				Tile tile = tilesetSource.getTileByPosition(x, y);
				if (tile == null) {
					continue;
				}
				if (tile.getTileRenderer() != null) {
					continue;
				}
				tile.setTileRenderer(new StaticTileRenderer(tilesetSource, tile));
			}
		}
	}

	protected void loadTileLayer(Element element) {
		if (element.getName().equals("layer")) {
			String name = element.getAttribute("name", null);
			int width = element.getIntAttribute("width", 0);
			int height = element.getIntAttribute("height", 0);
			boolean visible = element.getIntAttribute("visible", 1) == 1;
			TileLayer layer = new TileLayer(width, height);
			layer.setVisible(visible);
			layer.setName(name);

			Element data = element.getChildByName("data");
			String encoding = data.getAttribute("encoding", null);
			String compression = data.getAttribute("compression", null);
			if (encoding == null) { // no 'encoding' attribute means that the
									// encoding is XML
				throw new GdxRuntimeException("Unsupported encoding (XML) for TMX Layer Data");
			}
			if (encoding.equals("csv")) {
				String[] array = data.getText().split(",");
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int id = (int) Long.parseLong(array[y * width + x].trim());
						id = id & ~MASK_CLEAR;
						layer.setTileId(x, y, id);
					}
				}
			} else {
				if (encoding.equals("base64")) {
					byte[] bytes = Base64Coder.decode(data.getText());
					if (compression == null) {
						int read = 0;
						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {

								int id = unsignedByteToInt(bytes[read++]) | unsignedByteToInt(bytes[read++]) << 8
										| unsignedByteToInt(bytes[read++]) << 16
										| unsignedByteToInt(bytes[read++]) << 24;
								boolean flipHorizontally = (id & FLAG_FLIP_HORIZONTALLY) != 0;
							    boolean flipVertically = (id & FLAG_FLIP_VERTICALLY) != 0;
							    boolean flipDiagonally = (id & FLAG_FLIP_DIAGONALLY) != 0;
								id = id & ~MASK_CLEAR;
								layer.setTileId(x, y, id, flipHorizontally, flipVertically, flipDiagonally);
							}
						}
					} else if (compression.equals("gzip")) {
						GZIPInputStream GZIS = null;
						try {
							GZIS = new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length);
						} catch (IOException e) {
							throw new GdxRuntimeException(
									"Error Reading TMX Layer Data - IOException: " + e.getMessage());
						}

						byte[] temp = new byte[4];
						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								try {
									GZIS.read(temp, 0, 4);
									int id = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
											| unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
									boolean flipHorizontally = (id & FLAG_FLIP_HORIZONTALLY) != 0;
								    boolean flipVertically = (id & FLAG_FLIP_VERTICALLY) != 0;
								    boolean flipDiagonally = (id & FLAG_FLIP_DIAGONALLY) != 0;
									id = id & ~MASK_CLEAR;
									layer.setTileId(x, y, id, flipHorizontally, flipVertically, flipDiagonally);
								} catch (IOException e) {
									throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					} else if (compression.equals("zlib")) {
						Inflater zlib = new Inflater();

						byte[] temp = new byte[4];

						zlib.setInput(bytes, 0, bytes.length);

						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								try {
									zlib.inflate(temp, 0, 4);
									int id = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
											| unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
									boolean flipHorizontally = (id & FLAG_FLIP_HORIZONTALLY) != 0;
								    boolean flipVertically = (id & FLAG_FLIP_VERTICALLY) != 0;
								    boolean flipDiagonally = (id & FLAG_FLIP_DIAGONALLY) != 0;
									id = id & ~MASK_CLEAR;
									layer.setTileId(x, y, id, flipHorizontally, flipVertically, flipDiagonally);

								} catch (DataFormatException e) {
									throw new GdxRuntimeException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					}
				} else {
					// any other value of 'encoding' is one we're not aware of,
					// probably a feature of a future version of Tiled
					throw new GdxRuntimeException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
				}
			}
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				for (Element property : properties.getChildrenByName("property")) {
					String propertyName = property.getAttribute("name", null);
					String propertyValue = property.getAttribute("value", null);
					if (propertyValue == null) {
						propertyValue = property.getText();
					}
					layer.setProperty(propertyName, propertyValue);
				}
			}
			notifyTileLayerParsed(layer);
		}
	}

	protected void loadObjectGroup(Element element) {
		if (element.getName().equals("objectgroup")) {
			String name = element.getAttribute("name", null);
			TiledObjectGroup tiledObjectGroup = new TiledObjectGroup();
			tiledObjectGroup.setName(name);
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				for (Element property : properties.getChildrenByName("property")) {
					String propertyName = property.getAttribute("name", null);
					String propertyValue = property.getAttribute("value", null);
					if (propertyValue == null) {
						propertyValue = property.getText();
					}
					tiledObjectGroup.setProperty(propertyName, propertyValue);
				}
			}

			for (Element objectElement : element.getChildrenByName("object")) {
				TiledObject tiledObject = loadObject(objectElement);
				if (tiledObject != null) {
					tiledObjectGroup.getObjects().add(tiledObject);
				}
			}
			notifyObjectGroupParsed(tiledObjectGroup);
		}
	}

	protected TiledObject loadObject(Element element) {
		if (element.getName().equals("object")) {
			float x = element.getFloatAttribute("x", 0);
			float y = element.getFloatAttribute("y", 0);

			float width = element.getFloatAttribute("width", 0);
			float height = element.getFloatAttribute("height", 0);

			TiledObject object = new TiledObject(x, y, width, height);

			object.setName(element.getAttribute("name", null));
			String type = element.getAttribute("type", null);
			if (type != null) {
				object.setType(type);
			}
			int gid = element.getIntAttribute("gid", -1);
			if (gid != -1) {
				object.setGid(gid);
			}
			object.setVisible(element.getIntAttribute("visible", 1) == 1);
			Element properties = element.getChildByName("properties");
			if (properties != null) {
				for (Element property : properties.getChildrenByName("property")) {
					String propertyName = property.getAttribute("name", null);
					String propertyValue = property.getAttribute("value", null);
					if (propertyValue == null) {
						propertyValue = property.getText();
					}
					object.setProperty(propertyName, propertyValue);
				}
			}
			return object;
		}
		return null;
	}

	static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/**
	 * Adds a listener to be notified of parsing results
	 * 
	 * @param tiledParserListener
	 *            The {@link TiledParserListener} to be added
	 */
	@Override
	public void addListener(TiledParserListener tiledParserListener) {
		listeners.add(tiledParserListener);
	}

	/**
	 * Removes a listener from being notified of parsing results
	 * 
	 * @param tiledParserListener
	 *            The {@link TiledParserListener} to be removed
	 */
	@Override
	public void removeListener(TiledParserListener tiledParserListener) {
		listeners.remove(tiledParserListener);
	}

	@Override
	public void notifyBeginParsing(String orientation, String staggerAxis, String staggerIndex, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight, int sideLength) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onBeginParsing(orientation, staggerAxis, staggerIndex, backgroundColor, width, height,
					tileWidth, tileHeight, sideLength);
		}
	}

	@Override
	public void notifyMapPropertyParsed(String propertyName, String value) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onMapPropertyParsed(propertyName, value);
		}
	}

	@Override
	public void notifyTilePropertyParsed(Tile tile) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onTilePropertiesParsed(tile);
		}
	}

	@Override
	public void notifyTilesetParsed(Tileset parsedTileset) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onTilesetParsed(parsedTileset);
		}
	}

	@Override
	public void notifyTileLayerParsed(TileLayer parsedLayer) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onTileLayerParsed(parsedLayer);
		}
	}

	@Override
	public void notifyObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		for (TiledParserListener tiledParserListener : listeners) {
			tiledParserListener.onObjectGroupParsed(parsedObjectGroup);
		}
	}

	private Color convertHexColorToColor(String hexColor) {
		return new Color((Integer.valueOf(hexColor.substring(1, 3), 16) / 255f),
				(Integer.valueOf(hexColor.substring(3, 5), 16) / 255f),
				(Integer.valueOf(hexColor.substring(5, 7), 16) / 255f), 0f);
	}
}
