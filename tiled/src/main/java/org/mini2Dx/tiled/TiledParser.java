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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.ZlibStream;
import org.mini2Dx.gdx.Base64Coder;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.xml.XmlReader;
import org.mini2Dx.gdx.xml.XmlReader.Element;
import org.mini2Dx.tiled.renderer.AnimatedTileRenderer;
import org.mini2Dx.tiled.renderer.StaticTileRenderer;
import org.mini2Dx.tiled.renderer.TileFrame;
import org.mini2Dx.tiled.tileset.ImageTilesetSource;
import org.mini2Dx.tiled.tileset.TilesetSource;
import org.mini2Dx.tiled.tileset.TsxTilesetSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

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
	private TiledParserListener listener;
	private final ObjectMap<String, TiledObjectTemplate> objectTemplates;

	/**
	 * Constructor
	 */
	public TiledParser() {
		this(new ObjectMap<String, TiledObjectTemplate>());
	}

	public TiledParser(ObjectMap<String, TiledObjectTemplate> objectTemplates) {
		super();
		this.objectTemplates = objectTemplates;

		xmlReader = new XmlReader();
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
		Element root = xmlReader.parse(tmxFileHandle.reader());
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
				loadTileLayer(listener, element);
			} else if (name.equals("objectgroup")) {
				loadObjectGroup(listener, element, tmxFileHandle);
			} else if (name.equals("group")) {
				loadGroupLayer(listener, element, tmxFileHandle);
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
		Element element = xmlReader.parse(tsxFileHandle.reader());
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
		result.setTilesetImagePath(tsxFileHandle.sibling(imageSource).normalize());
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

	private Tileset loadTileSet(Element element, FileHandle tmxFile) {
		Tileset tileset = null;

		if (element.getName().equals("tileset")) {
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
				tilesetSource.setTilesetImagePath(tmxFile.sibling(imageSource).normalize());

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
				TsxTilesetSource tilesetSource = new TsxTilesetSource(tmxFile, source);
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
		return tileset;
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

	protected GroupLayer loadGroupLayer(TiledLayerParserListener parserListener, Element layerElement, FileHandle tmxFileHandle) throws IOException {
		if (!layerElement.getName().equals("group")) {
			return null;
		}

		final String groupName = layerElement.getAttribute("name", null);
		final boolean visible = layerElement.getIntAttribute("visible", 1) == 1;

		final GroupLayer groupLayer = new GroupLayer();
		groupLayer.setName(groupName);
		groupLayer.setVisible(visible);

		Element properties = layerElement.getChildByName("properties");
		if (properties != null) {
			for (Element property : properties.getChildrenByName("property")) {
				String propertyName = property.getAttribute("name", null);
				String propertyValue = property.getAttribute("value", null);
				if (propertyValue == null) {
					propertyValue = property.getText();
				}
				groupLayer.setProperty(propertyName, propertyValue);
			}
		}

		for (int i = 0, j = layerElement.getChildCount(); i < j; i++) {
			Element element = layerElement.getChild(i);
			String name = element.getName();

			if (name.equals("layer")) {
				loadTileLayer(groupLayer, element);
			} else if (name.equals("objectgroup")) {
				loadObjectGroup(groupLayer, element, tmxFileHandle);
			} else if (name.equals("group")) {
				loadGroupLayer(groupLayer, element, tmxFileHandle);
			}
		}

		notifyGroupLayerParsed(parserListener, groupLayer);
		return groupLayer;
	}

	protected TileLayer loadTileLayer(TiledLayerParserListener parserListener, Element element) {
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
				throw new MdxException("Unsupported encoding (XML) for TMX Layer Data");
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
							throw new MdxException(
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
									throw new MdxException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					} else if (compression.equals("zlib")) {
						ZlibStream zlibStream = Mdx.platformUtils.decompress(bytes);

						byte[] temp = new byte[4];

						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								try {
									zlibStream.read(temp);
									int id = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
											| unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
									boolean flipHorizontally = (id & FLAG_FLIP_HORIZONTALLY) != 0;
								    boolean flipVertically = (id & FLAG_FLIP_VERTICALLY) != 0;
								    boolean flipDiagonally = (id & FLAG_FLIP_DIAGONALLY) != 0;
									id = id & ~MASK_CLEAR;
									layer.setTileId(x, y, id, flipHorizontally, flipVertically, flipDiagonally);

								} catch (Exception e) {
									throw new MdxException("Error Reading TMX Layer Data.", e);
								}
							}
						}
					}
				} else {
					// any other value of 'encoding' is one we're not aware of,
					// probably a feature of a future version of Tiled
					throw new MdxException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
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
			notifyTileLayerParsed(parserListener, layer);
			return layer;
		}
		return null;
	}

	protected TiledObjectGroup loadObjectGroup(TiledLayerParserListener parserListener, Element element, FileHandle tmxFile) throws IOException {
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
				TiledObject tiledObject = loadObject(objectElement, tmxFile);
				if (tiledObject != null) {
					tiledObjectGroup.getObjects().add(tiledObject);
				}
			}
			notifyObjectGroupParsed(parserListener, tiledObjectGroup);
			return tiledObjectGroup;
		}
		return null;
	}

	protected TiledObject loadObject(Element element, FileHandle tmxFile) throws IOException {
		if (element.getName().equals("object")) {
			int id = element.getIntAttribute("id", -1);
			float x = element.getFloatAttribute("x", 0);
			float y = element.getFloatAttribute("y", 0);

			String template = element.getAttribute("template", null);

			boolean yAdjusted = false;

			final TiledObject objectTemplate;
			if(template != null) {
				final TiledObjectTemplate tiledObjectTemplate;
				if(!objectTemplates.containsKey(template)) {
					tiledObjectTemplate = loadObjectTemplate(template, tmxFile);
				} else {
					tiledObjectTemplate = objectTemplates.get(template, null);
				}
				if(tiledObjectTemplate == null) {
					objectTemplate = null;
				} else if(tiledObjectTemplate.getTiledObject() != null) {
					objectTemplate = tiledObjectTemplate.getTiledObject();

					if(tiledObjectTemplate.getTileset() != null) {
						//Origin is bottom-left, need to adjust to top-left
						y -= objectTemplate.getHeight();
						yAdjusted= true;
					}
				} else {
					objectTemplate = null;
				}
			} else {
				objectTemplate = null;
			}

			float width = element.getFloatAttribute("width", objectTemplate != null ? objectTemplate.getWidth() : 0);
			float height = element.getFloatAttribute("height", objectTemplate != null ? objectTemplate.getHeight() : 0);
			
			long rawGid = Long.parseLong(element.getAttribute("gid", "-1"));
			if (rawGid != -1 && !yAdjusted) {
				// Workaround for Tiled issue #386
				y -= height;
				yAdjusted= true;
			}

			TiledObject object = new TiledObject(id, x, y, width, height, objectTemplate != null);

			object.setName(element.getAttribute("name", objectTemplate != null ? objectTemplate.getName() : null));
			String type = element.getAttribute("type", objectTemplate != null ? objectTemplate.getType() : null);

			if (type != null) {
				object.setType(type);
			}
			
			if (rawGid != -1) {
				boolean gidFlipHorizontally = (rawGid & FLAG_FLIP_HORIZONTALLY) != 0;
			    boolean gidFlipVertically = (rawGid & FLAG_FLIP_VERTICALLY) != 0;
			    boolean gidFlipDiagonally = (rawGid & FLAG_FLIP_DIAGONALLY) != 0;
				int gid = (int) (rawGid & ~MASK_CLEAR);
				
				object.setGid(gid);
				object.setGidFlipDiagonally(gidFlipDiagonally);
				object.setGidFlipHorizontally(gidFlipHorizontally);
				object.setGidFlipVertically(gidFlipVertically);
			} else if(objectTemplate != null) {
				object.setGid(objectTemplate.getGid());
				object.setGidFlipDiagonally(objectTemplate.isGidFlipDiagonally());
				object.setGidFlipHorizontally(objectTemplate.isGidFlipHorizontally());
				object.setGidFlipVertically(objectTemplate.isGidFlipVertically());
			}
			object.setVisible(element.getIntAttribute("visible", 1) == 1);

			Element properties = element.getChildByName("properties");
			if(objectTemplate != null && objectTemplate.getProperties() != null) {
				for(String key : objectTemplate.getProperties().keys()) {
					object.setProperty(key, objectTemplate.getProperties().get(key));
				}
			}
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

			Element point = element.getChildByName("point");
			if(point != null) {
				object.setAsPoint();
			} else if(objectTemplate != null && objectTemplate.getObjectShape().equals(TiledObjectShape.POINT)) {
				object.setAsPoint();
			}
			Element ellipse = element.getChildByName("ellipse");
			if(ellipse != null) {
				object.setAsEllipse();
			} else if(objectTemplate != null && objectTemplate.getObjectShape().equals(TiledObjectShape.ELLIPSE)) {
				object.setAsEllipse();
			}
			Element polygon = element.getChildByName("polygon");
			if(polygon != null) {
				object.setAsPolygon(polygon.getAttribute("points", ""));
			} else if(objectTemplate != null && objectTemplate.getObjectShape().equals(TiledObjectShape.POLYGON)) {
				object.setAsPolygon(objectTemplate.getVertices());
			}
			Element polyline = element.getChildByName("polyline");
			if(polyline != null) {
				object.setAsPolyline(polyline.getAttribute("points", ""));
			} else if(objectTemplate != null && objectTemplate.getObjectShape().equals(TiledObjectShape.POLYGON)) {
				object.setAsPolyline(objectTemplate.getVertices());
			}
			Element text = element.getChildByName("text");
			if(text != null) {
				object.setAsText(text.getText(), text.getIntAttribute("wrap", 1) == 1);
			} else if(objectTemplate != null && objectTemplate.getObjectShape().equals(TiledObjectShape.TEXT)) {
				object.setAsText(objectTemplate.getText(), objectTemplate.isWrapText());
			}
			return object;
		}
		return null;
	}

	private TiledObjectTemplate loadObjectTemplate(String path, FileHandle tmxFile) throws IOException {
		final FileHandle txFile = tmxFile.sibling(path).normalizedHandle();
		Element root = xmlReader.parse(txFile.reader());
		Element tilesetElement = root.getChildByName("tileset");
		Element objectElement = root.getChildByName("object");

		final Tileset tileset;
		if(tilesetElement != null) {
			tileset = loadTileSet(tilesetElement, txFile);
		} else {
			tileset = null;
		}

		final TiledObject tiledObject;
		if(objectElement != null) {
			tiledObject = loadObject(objectElement, txFile);
		} else {
			tiledObject = null;
		}

		final TiledObjectTemplate objectTemplate = new TiledObjectTemplate(path, tileset, tiledObject);
		objectTemplates.put(path, objectTemplate);
		notifyObjectTemplateParsed(objectTemplate);
		return objectTemplate;
	}

	static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	@Override
	public void setListener(TiledParserListener tiledParserListener) {
		this.listener = tiledParserListener;
	}

	@Override
	public void notifyBeginParsing(String orientation, String staggerAxis, String staggerIndex, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight, int sideLength) {
		if (listener == null) {
			return;
		}
		listener.onBeginParsing(orientation, staggerAxis, staggerIndex, backgroundColor, width, height,
				tileWidth, tileHeight, sideLength);
	}

	@Override
	public void notifyMapPropertyParsed(String propertyName, String value) {
		if (listener == null) {
			return;
		}
		listener.onMapPropertyParsed(propertyName, value);
	}

	@Override
	public void notifyTilePropertyParsed(Tile tile) {
		if (listener == null) {
			return;
		}
		listener.onTilePropertiesParsed(tile);
	}

	@Override
	public void notifyTilesetParsed(Tileset parsedTileset) {
		if (listener == null) {
			return;
		}
		listener.onTilesetParsed(parsedTileset);
	}

	public void notifyTileLayerParsed(TiledLayerParserListener listener, TileLayer parsedLayer) {
		if (listener == null) {
			return;
		}
		listener.onTileLayerParsed(parsedLayer);
	}

	@Override
	public void notifyTileLayerParsed(TileLayer parsedLayer) {
		if (listener == null) {
			return;
		}
		listener.onTileLayerParsed(parsedLayer);
	}

	public void notifyObjectGroupParsed(TiledLayerParserListener listener, TiledObjectGroup parsedObjectGroup) {
		if (listener == null) {
			return;
		}
		listener.onObjectGroupParsed(parsedObjectGroup);
	}

	@Override
	public void notifyObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		if (listener == null) {
			return;
		}
		listener.onObjectGroupParsed(parsedObjectGroup);
	}

	public void notifyGroupLayerParsed(TiledLayerParserListener listener, GroupLayer parsedGroupLayer) {
		if (listener == null) {
			return;
		}
		listener.onGroupLayerParsed(parsedGroupLayer);
	}

	@Override
	public void notifyGroupLayerParsed(GroupLayer parsedGroupLayer) {
		if (listener == null) {
			return;
		}
		listener.onGroupLayerParsed(parsedGroupLayer);
	}

	@Override
	public void notifyObjectTemplateParsed(TiledObjectTemplate parsedObjectTemplate) {
		if (listener == null) {
			return;
		}
		listener.onObjectTemplateParsed(parsedObjectTemplate);
	}

	private Color convertHexColorToColor(String hexColor) {
		return Mdx.graphics.newColor((Integer.valueOf(hexColor.substring(1, 3), 16) / 255f),
				(Integer.valueOf(hexColor.substring(3, 5), 16) / 255f),
				(Integer.valueOf(hexColor.substring(5, 7), 16) / 255f), 0f);
	}
}
