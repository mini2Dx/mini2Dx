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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.CustomCursor;
import org.mini2Dx.core.graphics.Pixmap;

public class LibgdxCustomCursor extends CustomCursor {
	private final Cursor upCursor, downCursor;

	/**
	 * Constructor
	 *
	 * @param upPixmap   The image to use in the mouse button up state
	 * @param downPixmap The image to use in the mouse button down state
	 * @param xHotspot   The x location of the hotspot pixel within the cursor image (origin top-left corner)
	 * @param yHotspot   The y location of the hotspot pixel within the cursor image (origin top-left corner)
	 */
	public LibgdxCustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot) {
		super(upPixmap, downPixmap, xHotspot, yHotspot);

		switch(Mdx.platform) {
		case WINDOWS:
		case MAC:
		case LINUX:
			final LibgdxPixmap gdxUpPixmap = (LibgdxPixmap) upPixmap;
			final LibgdxPixmap gdxDownPixmap = (LibgdxPixmap) downPixmap;

			upCursor = Gdx.graphics.newCursor(gdxUpPixmap.pixmap, xHotspot, yHotspot);
			downCursor = Gdx.graphics.newCursor(gdxDownPixmap.pixmap, xHotspot, yHotspot);
			Gdx.graphics.setCursor(upCursor);
			break;
		default:
			upCursor = null;
			downCursor = null;
			break;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(downCursor != null) {
			Gdx.graphics.setCursor(downCursor);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(upCursor != null) {
			Gdx.graphics.setCursor(upCursor);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
