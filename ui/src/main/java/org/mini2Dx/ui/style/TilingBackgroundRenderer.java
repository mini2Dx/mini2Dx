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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.TextureAtlasRegion;
import org.mini2Dx.core.graphics.TilingDrawable;

public class TilingBackgroundRenderer extends BackgroundRenderer {
	private TilingDrawable tilingDrawable;

	public TilingBackgroundRenderer(String imagePath) {
		super(imagePath);
	}

	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if(theme.isHeadless()) {
			return;
		}
		final TextureAtlasRegion atlasRegion = theme.getTextureAtlas().findRegion(imagePath);
		if(atlasRegion == null) {
			throw new MdxException("No such texture '" + imagePath + "'");
		}
		tilingDrawable = Mdx.graphics.newTilingDrawable(Mdx.graphics.newTextureRegion(atlasRegion));
	}

	@Override
	public void render(Graphics g, float x, float y, float width, float height) {
		g.drawTilingDrawable(tilingDrawable, x, y, width, height);
	}
}
