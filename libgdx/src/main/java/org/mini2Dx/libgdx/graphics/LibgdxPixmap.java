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

import org.mini2Dx.core.graphics.*;

public class LibgdxPixmap implements Pixmap {
	public final com.badlogic.gdx.graphics.Pixmap pixmap;

	public LibgdxPixmap(com.badlogic.gdx.graphics.Pixmap pixmap) {
		this.pixmap = pixmap;
	}

	@Override
	public void fill() {
		pixmap.fill();
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2) {
		pixmap.drawLine(x, y, x2, y2);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		pixmap.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		final LibgdxPixmap gdxPixmap = (LibgdxPixmap) pixmap;
		this.pixmap.drawPixmap(gdxPixmap.pixmap, x, y);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight) {
		final LibgdxPixmap gdxPixmap = (LibgdxPixmap) pixmap;
		this.pixmap.drawPixmap(gdxPixmap.pixmap, x, y, srcx, srcy, srcWidth, srcHeight);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth, int dstHeight) {
		final LibgdxPixmap gdxPixmap = (LibgdxPixmap) pixmap;
		this.pixmap.drawPixmap(gdxPixmap.pixmap, srcx, srcy, srcWidth, srcHeight, dstx, dsty, dstWidth, dstHeight);
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		pixmap.fillRectangle(x, y, width, height);
	}

	@Override
	public void drawCircle(int x, int y, int radius) {
		pixmap.drawCircle(x, y, radius);
	}

	@Override
	public void fillCircle(int x, int y, int radius) {
		pixmap.fillCircle(x, y, radius);
	}

	@Override
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		pixmap.fillTriangle(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public void drawPixel(int x, int y) {
		pixmap.drawPixel(x, y);
	}

	@Override
	public void drawPixel(int x, int y, Color color) {
		pixmap.drawPixel(x, y, color.rgba8888());
	}

	@Override
	public int getPixel(int x, int y) {
		return pixmap.getPixel(x, y);
	}

	@Override
	public byte[] getPixels() {
		return null;
	}

	@Override
	public int getWidth() {
		return pixmap.getWidth();
	}

	@Override
	public int getHeight() {
		return pixmap.getHeight();
	}

	@Override
	public PixmapFormat getFormat() {
		switch(pixmap.getFormat()) {
		case Alpha:
			return PixmapFormat.ALPHA;
		case Intensity:
			return PixmapFormat.INTENSITY;
		case LuminanceAlpha:
			return PixmapFormat.LUMINANCE_ALPHA;
		case RGB565:
			return PixmapFormat.RGB565;
		case RGBA4444:
			return PixmapFormat.RGBA4444;
		case RGB888:
			return PixmapFormat.RGB888;
		default:
		case RGBA8888:
			return PixmapFormat.RGBA8888;
		}
	}

	@Override
	public PixmapBlending getBlending() {
		switch(pixmap.getBlending()) {
		default:
		case None:
			return PixmapBlending.NONE;
		case SourceOver:
			return PixmapBlending.SOURCE_OVER;
		}
	}

	@Override
	public void setBlending(PixmapBlending blending) {
		switch(blending) {
		default:
		case NONE:
			pixmap.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.None);
			break;
		case SOURCE_OVER:
			pixmap.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver);
			break;
		}
	}

	@Override
	public PixmapFilter getFilter() {
		switch(pixmap.getFilter()) {
		default:
		case NearestNeighbour:
			return PixmapFilter.NEAREST_NEIGHBOUR;
		case BiLinear:
			return PixmapFilter.BILINEAR;
		}
	}

	@Override
	public void setFilter(PixmapFilter filter) {
		switch(filter) {
		default:
		case NEAREST_NEIGHBOUR:
			pixmap.setFilter(com.badlogic.gdx.graphics.Pixmap.Filter.NearestNeighbour);
			break;
		case BILINEAR:
			pixmap.setFilter(com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear);
			break;
		}
	}

	@Override
	public void setColor(Color color) {
		final LibgdxColor gdxColor = (LibgdxColor) color;
		pixmap.setColor(gdxColor.color);
	}

	@Override
	public void dispose() {
		pixmap.dispose();
	}

	public static com.badlogic.gdx.graphics.Pixmap.Format toGdxPixmapFormat(PixmapFormat format) {
		switch(format) {
		case ALPHA:
			return com.badlogic.gdx.graphics.Pixmap.Format.Alpha;
		case INTENSITY:
			return com.badlogic.gdx.graphics.Pixmap.Format.Intensity;
		case LUMINANCE_ALPHA:
			return com.badlogic.gdx.graphics.Pixmap.Format.LuminanceAlpha;
		case RGB565:
			return com.badlogic.gdx.graphics.Pixmap.Format.RGB565;
		case RGBA4444:
			return com.badlogic.gdx.graphics.Pixmap.Format.RGBA4444;
		case RGB888:
			return com.badlogic.gdx.graphics.Pixmap.Format.RGB888;
		default:
		case RGBA8888:
			return com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
		}
	}
}
