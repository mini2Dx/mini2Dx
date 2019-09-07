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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;

public class TilingDrawable {

	private TextureRegion drawable;
	private TextureRegion sharedRegion;

	public TilingDrawable(TextureRegion region){
		drawable = region;
		sharedRegion = Mdx.graphics.newTextureRegion(drawable);
	}

	public void draw(Graphics g, float x, float y, float width, float height){
		int xCount = (int) (width / drawable.getRegionWidth());
		int yCount = (int) (height / drawable.getRegionHeight());
		int xRemainder = (int) (width - xCount * drawable.getRegionWidth());
		int yRemainder = (int) (height - yCount * drawable.getRegionHeight());

		for (int i = 0; i < xCount; i++)
		{
			for (int j = 0; j < yCount; j++)
			{
				g.drawTextureRegion(drawable, x + drawable.getRegionWidth() * i, y + j * drawable.getRegionHeight());
			}
		}

		if (xRemainder != 0)
		{
			sharedRegion.setRegionWidth(xRemainder);
			sharedRegion.setRegionHeight(drawable.getRegionHeight());

			for (int i = 0; i < yCount; i++)
			{
				g.drawTextureRegion(sharedRegion, x + xCount * drawable.getRegionWidth(), y + i * drawable.getRegionHeight());
			}
		}

		if (yRemainder != 0)
		{
			sharedRegion.setRegionWidth(drawable.getRegionWidth());
			sharedRegion.setRegionHeight(yRemainder);
			for (int i = 0; i < xCount; i++)
			{
				g.drawTextureRegion(sharedRegion, x + i * drawable.getRegionWidth(), y + yCount * drawable.getRegionHeight());
			}
		}

		if (xRemainder != 0 && yRemainder != 0)
		{
			sharedRegion.setRegionWidth(xRemainder);
			sharedRegion.setRegionHeight(yRemainder);
			g.drawTextureRegion(sharedRegion, x + xCount * drawable.getRegionWidth(), y + yCount * drawable.getRegionHeight());
		}
	}

	public void set(TextureRegion textureRegion) {
		this.drawable = textureRegion;
		sharedRegion.setRegion(textureRegion);
	}
}
