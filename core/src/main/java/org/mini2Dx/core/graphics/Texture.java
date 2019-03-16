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

import org.mini2Dx.gdx.utils.Disposable;

public interface Texture extends Disposable {
	/**
	 * Draws the given {@link Pixmap} to the texture at position x, y. No clipping is performed so you have to make sure that you
	 * draw only inside the texture region. Note that this will only draw to mipmap level 0!
	 *
	 * @param pixmap The Pixmap
	 * @param x The x coordinate in pixels
	 * @param y The y coordinate in pixels */
	public void draw (Pixmap pixmap, int x, int y);

	/**
	 * Returns the width of the texture in pixels
	 * @return
	 */
	public int getWidth ();

	/**
	 * Returns the height of the texture in pixels
	 * @return
	 */
	public int getHeight ();

	/**
	 * Returns if this texture needs to rebind when the graphics context is lost
	 * @return
	 */
	public boolean isManaged ();
}
