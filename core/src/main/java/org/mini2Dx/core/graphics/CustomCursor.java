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

import org.mini2Dx.gdx.InputProcessor;

/**
 * Base class for implementing an {@link InputProcessor} that sets a custom mouse cursor image based on the mouse state.
 */
public abstract class CustomCursor implements InputProcessor {
	protected final Pixmap upPixmap, downPixmap;
	protected final int xHotspot, yHotspot;

	/**
	 * Constructor
	 * @param upPixmap The image to use in the mouse button up state
	 * @param downPixmap The image to use in the mouse button down state
	 * @param xHotspot The x location of the hotspot pixel within the cursor image (origin top-left corner)
	 * @param yHotspot The y location of the hotspot pixel within the cursor image (origin top-left corner)
	 */
	public CustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) {
		super();
		this.upPixmap = upPixmap;
		this.downPixmap = downPixmap;
		this.xHotspot = xHotspot;
		this.yHotspot = yHotspot;
	}

	public Pixmap getUpPixmap() {
		return upPixmap;
	}

	public Pixmap getDownPixmap() {
		return downPixmap;
	}

	public int getxHotspot() {
		return xHotspot;
	}

	public int getyHotspot() {
		return yHotspot;
	}
}
