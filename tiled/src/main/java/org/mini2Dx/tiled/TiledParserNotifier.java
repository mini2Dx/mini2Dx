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

import org.mini2Dx.core.graphics.Color;

/**
 * A common interface to notify {@link TiledParserListener}s
 */
public interface TiledParserNotifier {
	/**
	 * Sets the listener to be notified of parsing results
	 * 
	 * @param tiledParserListener
	 *            The {@link TiledParserListener} to be added
	 */
	public void setListener(TiledParserListener tiledParserListener);

	public void notifyBeginParsing(String orientation, String staggerAxis, String staggerIndex, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight, int sideLength);

	public void notifyEndParsing();

	public void notifyMapPropertyParsed(String propertyName, String value);

	public void notifyTilePropertyParsed(Tile tile);

	public void notifyTilesetParsed(Tileset parsedTileset);

	public void notifyTileLayerParsed(TileLayer parsedLayer);

	public void notifyObjectGroupParsed(TiledObjectGroup parsedObjectGroup);

	public void notifyGroupLayerParsed(GroupLayer parsedGroupLayer);

	public void notifyObjectTemplateParsed(TiledObjectTemplate parsedObjectTemplate);
}
