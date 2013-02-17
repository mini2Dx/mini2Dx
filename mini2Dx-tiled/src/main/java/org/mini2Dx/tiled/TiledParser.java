package org.mini2Dx.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

/**
 * Parses Tiled XML files and notifies {@link TiledParserListener}s of map data
 * 
 * @author Thomas Cashman
 * @author David Fraska (parsing logic from LibGDX)
 */
public class TiledParser {
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
	 * Adds a listener to be notified of parsing results
	 * @param listener The {@link TiledParserListener} to be added
	 */
	public void addListener(TiledParserListener listener) {
		listeners.add(listener);
	}

	/**
	 * Parses a TMX file and notifies any {@link TiledParserListener}s of parsing results
	 * @param fileHandle A {@link FileHandle} to a TMX file exported from Tiled
	 * @throws IOException Thrown if the map file could not be parsed
	 */
	public void parse(FileHandle fileHandle) throws IOException {
		xmlReader = new XmlReader() {

			Stack<String> currBranch = new Stack<String>();

			boolean awaitingData = false;
			TileLayer currLayer;
			int currLayerWidth = 0, currLayerHeight = 0;
			Tileset currTileSet;
			TiledObjectGroup currObjectGroup;
			TiledObject currObject;
			int currTile;
			String mapOrientation;
			int mapWidth, mapHeight, mapTileWidth, mapTileHeight;

			class Polyline {
				String name;
				String points;

				public Polyline(String name) {
					this.name = name;
				}

				public Polyline() {
				}
			}

			Polyline polyline, polygon;

			class Property {
				String parentType, name, value;
			}

			Property currProperty;

			String encoding, dataString, compression;
			byte[] data;

			int dataCounter = 0, row, col;

			@Override
			protected void open(String name) {
				currBranch.push(name);

				if ("layer".equals(name)) {
					currLayer = new TileLayer();
					return;
				}

				if ("tileset".equals(name)) {
					currTileSet = new Tileset();
					return;
				}

				if ("data".equals(name)) {
					dataString = ""; // clear the string for new data
					awaitingData = true;
					return;
				}

				if ("objectgroup".equals(name)) {
					currObjectGroup = new TiledObjectGroup();
					return;
				}

				if ("object".equals(name)) {
					currObject = new TiledObject();
					return;
				}

				if ("property".equals(name)) {
					currProperty = new Property();
					currProperty.parentType = currBranch
							.get(currBranch.size() - 3);
					return;
				}

				if ("polyline".equals(name)) {
					polyline = new Polyline("polyline");
					return;
				}

				if ("polygon".equals(name)) {
					polygon = new Polyline("polygon");
					return;
				}
			}

			@Override
			protected void attribute(String name, String value) {
				String element = currBranch.peek();

				if ("layer".equals(element)) {
					if ("width".equals(name)) {
						currLayerWidth = Integer.parseInt(value);
					} else if ("height".equals(name)) {
						currLayerHeight = Integer.parseInt(value);
					}

					if (currLayerWidth != 0 && currLayerHeight != 0) {
						currLayer
								.setDimensions(currLayerWidth, currLayerHeight);
					}
					if ("name".equals(name)) {
						currLayer.setName(value);
					}
					return;
				}

				if ("tileset".equals(element)) {
					if ("firstgid".equals(name)) {
						currTileSet.setFirstGid(Integer.parseInt(value));
						return;
					}
					if ("tilewidth".equals(name)) {
						currTileSet.setTileWidth(Integer.parseInt(value));
						return;
					}
					if ("tileheight".equals(name)) {
						currTileSet.setTileHeight(Integer.parseInt(value));
						return;
					}
					if ("name".equals(name)) {
						currTileSet.setName(value);
						return;
					}
					if ("spacing".equals(name)) {
						currTileSet.setSpacing(Integer.parseInt(value));
						return;
					}
					if ("margin".equals(name)) {
						currTileSet.setMargin(Integer.parseInt(value));
						return;
					}
					return;
				}

				if ("image".equals(element)) {
					if ("source".equals(name)) {
						currTileSet.setTilesetImagePath(value);
						return;
					}
					if("width".equals(name)) {
						currTileSet.setWidth(Integer.parseInt(value));
						return;
					}
					if("height".equals(name)) {
						currTileSet.setHeight(Integer.parseInt(value));
						return;
					}
					return;
				}

				if ("data".equals(element)) {
					if ("encoding".equals(name)) {
						encoding = value;
						return;
					}
					if ("compression".equals(name)) {
						compression = value;
						return;
					}
					return;
				}

				if ("objectgroup".equals(element)) {
					if ("name".equals(name)) {
						currObjectGroup.setName(value);
						return;
					}
					if ("height".equals(name)) {
						currObjectGroup.setHeight(Integer.parseInt(value));
						return;
					}
					if ("width".equals(name)) {
						currObjectGroup.setWidth(Integer.parseInt(value));
						return;
					}
					return;
				}

				if ("object".equals(element)) {
					if ("name".equals(name)) {
						currObject.setName(value);
						return;
					}
					if ("type".equals(name)) {
						currObject.setType(value);
						return;
					}
					if ("x".equals(name)) {
						currObject.setX(Integer.parseInt(value));
						return;
					}
					if ("y".equals(name)) {
						currObject.setY(Integer.parseInt(value));
						return;
					}
					if ("width".equals(name)) {
						currObject.setWidth(Integer.parseInt(value));
						return;
					}
					if ("height".equals(name)) {
						currObject.setHeight(Integer.parseInt(value));
						return;
					}
					if ("gid".equals(name)) {
						currObject.setGid(Integer.parseInt(value));
						return;
					}
					return;
				}

				if ("map".equals(element)) {
					if ("orientation".equals(name)) {
						mapOrientation = value;
					}
					if ("width".equals(name)) {
						mapWidth = Integer.parseInt(value);
					}
					if ("height".equals(name)) {
						mapHeight = Integer.parseInt(value);
					}
					if ("tilewidth".equals(name)) {
						mapTileWidth = Integer.parseInt(value);
					}
					if ("tileheight".equals(name)) {
						mapTileHeight = Integer.parseInt(value);
					}

					if (mapOrientation != null && mapWidth > 0 && mapHeight > 0
							&& mapTileWidth > 0 && mapHeight > 0) {
						for (TiledParserListener listener : listeners) {
							listener.onBeginParsing(mapOrientation, mapWidth,
									mapHeight, mapTileWidth, mapTileHeight);
						}
					}
					return;
				}

				if ("tile".equals(element)) {
					if (awaitingData) { // Actually getting tile data
						if ("gid".equals(name)) {
							col = dataCounter % currLayerWidth;
							row = dataCounter / currLayerWidth;
							if (row < currLayerHeight) {
								currLayer.setTileId(col, row,
										Integer.parseInt(value));
							} else {
								Gdx.app.log("TiledLoader",
										"Warning: extra XML gid values ignored! Your map is likely corrupt!");
							}
							dataCounter++;
						}
					} else { // Not getting tile data, must be a tile Id (for
								// properties)
						if ("id".equals(name)) {
							currTile = Integer.parseInt(value);
						}
					}
					return;
				}

				if ("property".equals(element)) {
					if ("name".equals(name)) {
						currProperty.name = value;
						return;
					}
					if ("value".equals(name)) {
						currProperty.value = value;
						return;
					}
					return;
				}

				if ("polyline".equals(element)) {
					if ("points".equals(name)) {
						polyline.points = value;
						return;
					}
					return;
				}

				if ("polygon".equals(element)) {
					if ("points".equals(name)) {
						polygon.points = value;
						return;
					}
					return;
				}
			}

			@Override
			protected void text(String text) {
				if (awaitingData) {
					dataString = dataString.concat(text);
				}
			}

			@Override
			protected void close() {
				String element = currBranch.pop();

				if ("layer".equals(element)) {
					for (TiledParserListener listener : listeners) {
						listener.onTileLayerParsed(currLayer);
					}
					currLayer = null;
					return;
				}

				if ("tileset".equals(element)) {
					for (TiledParserListener listener : listeners) {
						listener.onTilesetParsed(currTileSet);
					}
					currTileSet = null;
					return;
				}

				if ("object".equals(element)) {
					currObjectGroup.getObjects().add(currObject);
					currObject = null;
					return;
				}

				if ("objectgroup".equals(element)) {
					for (TiledParserListener listener : listeners) {
						listener.onObjectGroupParsed(currObjectGroup);
					}
					currObjectGroup = null;
					return;
				}

				if ("property".equals(element)) {
					putProperty(currProperty);
					currProperty = null;
					return;
				}

				if ("polyline".equals(element)) {
					putPolyLine(polyline);
					polyline = null;
					return;
				}

				if ("polygon".equals(element)) {
					putPolyLine(polygon);
					polygon = null;
					return;
				}

				if ("data".equals(element)) {

					// decode and uncompress the data
					if ("base64".equals(encoding)) {
						if (dataString == null | "".equals(dataString.trim()))
							return;

						data = Base64Coder.decode(dataString.trim());

						if ("gzip".equals(compression)) {
							unGZip();
						} else if ("zlib".equals(compression)) {
							unZlib();
						} else if (compression == null) {
							arrangeData();
						}

					} else if ("csv".equals(encoding) && compression == null) {
						fromCSV();

					} else if (encoding == null && compression == null) {
						// startElement() handles most of this
						dataCounter = 0;// reset counter in case another layer
										// comes through
					} else {
						throw new GdxRuntimeException(
								"Unsupported encoding and/or compression format");
					}

					awaitingData = false;
					return;
				}

				if ("property".equals(element)) {
					putProperty(currProperty);
					currProperty = null;
				}
			}

			private void putPolyLine(Polyline polyLine) {
				if (polyLine == null) {
					return;
				}

				if ("polyline".equals(polyLine.name)) {
					currObject.setPolyline(polyLine.points);
					return;
				}

				if ("polygon".equals(polyLine.name)) {
					currObject.setPolygon(polyLine.points);
					return;
				}

				return;
			}

			private void putProperty(Property property) {
				if ("tile".equals(property.parentType)) {
					for(TiledParserListener listener: listeners) {
						listener.onTilePropertyParsed(currTile + currTileSet.getFirstGid(),
								property.name, property.value);
					}
					return;
				}

				if ("map".equals(property.parentType)) {
					for(TiledParserListener listener: listeners) {
						listener.onMapPropertyParsed(property.name, property.value);
					}
					return;
				}

				if ("layer".equals(property.parentType)) {
					currLayer.setProperty(property.name, property.value);
					return;
				}

				if ("objectgroup".equals(property.parentType)) {
					currObjectGroup.setProperty(property.name, property.value);
					return;
				}

				if ("object".equals(property.parentType)) {
					currObject.setProperty(property.name, property.value);
					return;
				}
			}

			private void fromCSV() {
				StringTokenizer st = new StringTokenizer(dataString.trim(), ",");
				for (int row = 0; row < currLayerHeight; row++) {
					for (int col = 0; col < currLayerWidth; col++) {
						currLayer.setTileId(col, row,
								(int) Long.parseLong(st.nextToken().trim()));
					}
				}
			}

			private void arrangeData() {
				int byteCounter = 0;
				for (int row = 0; row < currLayerHeight; row++) {
					for (int col = 0; col < currLayerWidth; col++) {
						currLayer
								.setTileId(
										col,
										row,
										unsignedByteToInt(data[byteCounter++])
												| unsignedByteToInt(data[byteCounter++]) << 8
												| unsignedByteToInt(data[byteCounter++]) << 16
												| unsignedByteToInt(data[byteCounter++]) << 24);
					}
				}
			}

			private void unZlib() {
				Inflater zlib = new Inflater();
				byte[] readTemp = new byte[4];

				zlib.setInput(data, 0, data.length);

				for (int row = 0; row < currLayerHeight; row++) {
					for (int col = 0; col < currLayerWidth; col++) {
						try {
							zlib.inflate(readTemp, 0, 4);
							currLayer
									.setTileId(
											col,
											row,
											unsignedByteToInt(readTemp[0])
													| unsignedByteToInt(readTemp[1]) << 8
													| unsignedByteToInt(readTemp[2]) << 16
													| unsignedByteToInt(readTemp[3]) << 24);
						} catch (DataFormatException e) {
							throw new GdxRuntimeException(
									"Error Reading TMX Layer Data.", e);
						}
					}
				}
			}

			private void unGZip() {
				GZIPInputStream GZIS = null;
				try {
					GZIS = new GZIPInputStream(new ByteArrayInputStream(data),
							data.length);
				} catch (IOException e) {
					throw new GdxRuntimeException(
							"Error Reading TMX Layer Data - IOException: "
									+ e.getMessage());
				}

				// Read the GZIS data into an array, 4 bytes = 1 GID
				byte[] readTemp = new byte[4];
				for (int row = 0; row < currLayerHeight; row++) {
					for (int col = 0; col < currLayerWidth; col++) {
						try {
							GZIS.read(readTemp, 0, 4);
							currLayer
									.setTileId(
											col,
											row,
											unsignedByteToInt(readTemp[0])
													| unsignedByteToInt(readTemp[1]) << 8
													| unsignedByteToInt(readTemp[2]) << 16
													| unsignedByteToInt(readTemp[3]) << 24);
						} catch (IOException e) {
							throw new GdxRuntimeException(
									"Error Reading TMX Layer Data.", e);
						}
					}
				}
			}
		};
		xmlReader.parse(fileHandle);
	}

	static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
}
