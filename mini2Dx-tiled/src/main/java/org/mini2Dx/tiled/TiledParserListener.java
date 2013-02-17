package org.mini2Dx.tiled;

/**
 * 
 * 
 * @author Thomas Cashman
 */
public interface TiledParserListener {

	public void onBeginParsing(String orientation, int width, int height,
			int tileWidth, int tileHeight);
	
	public void onMapPropertyParsed(String propertyName, String value);
	
	public void onTilePropertyParsed(int tileId, String propertyName, String value);

	public void onTilesetParsed(Tileset parsedTileset);

	public void onTileLayerParsed(TileLayer parsedLayer);

	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup);
}
