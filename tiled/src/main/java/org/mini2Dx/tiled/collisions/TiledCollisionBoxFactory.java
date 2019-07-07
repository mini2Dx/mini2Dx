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
package org.mini2Dx.tiled.collisions;

import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledObject;

/**
 * A default implementation of {@link TiledCollisionFactory} that creates
 * {@link CollisionBox} instances
 */
public class TiledCollisionBoxFactory implements TiledCollisionFactory<CollisionBox> {

	@Override
	public CollisionBox createCollision(TiledMap map, Tile tile, float x, float y, float width, float height) {
		return new CollisionBox(x, y, width, height);
	}

	@Override
	public CollisionBox createCollision(TiledMap map, TiledObject tiledObject) {
		return new CollisionBox(tiledObject.getX(), tiledObject.getY(), tiledObject.getWidth(),
				tiledObject.getHeight());
	}

}
