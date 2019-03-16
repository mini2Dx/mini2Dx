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
package org.mini2Dx.core.graphics;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public interface TextureAtlas extends Disposable {

	/**
	 * Returns all regions in the atlas.
	 */
	public Array<TextureAtlasRegion> getRegions();

	/**
	 * Returns the first region found with the specified name. This method uses string comparison to find the region, so the result
	 * should be cached rather than calling this method multiple times.
	 *
	 * @return The region, or null.
	 */
	public TextureAtlasRegion findRegion(String name);

	/**
	 * Returns the first region found with the specified name and index. This method uses string comparison to find the region, so
	 * the result should be cached rather than calling this method multiple times.
	 *
	 * @return The region, or null.
	 */
	public TextureAtlasRegion findRegion(String name, int index);

	/**
	 * Returns all regions with the specified name, ordered by smallest to largest index. This method
	 * uses string comparison to find the regions, so the result should be cached rather than calling this method multiple times.
	 */
	public Array<TextureAtlasRegion> findRegions(String name);
}
