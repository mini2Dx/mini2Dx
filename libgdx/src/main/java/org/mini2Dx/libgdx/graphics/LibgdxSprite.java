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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.*;

public class LibgdxSprite implements Sprite, GdxTextureRegion {
	public final com.badlogic.gdx.graphics.g2d.Sprite sprite;
	private final LibgdxColor color;
	private final Rectangle boundingRectangle = new Rectangle();

	/**
	 * Creates an uninitialized sprite. The sprite will need a texture region
	 * and bounds set before it can be drawn.
	 */
	public LibgdxSprite() {
		super();
		sprite = new com.badlogic.gdx.graphics.g2d.Sprite();
		color = new LibgdxColor(sprite.getColor());
		setBoundingRectangle();
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the size
	 * of the texture.
	 */
	public LibgdxSprite(Texture texture) {
		this(texture, texture.getWidth(), texture.getHeight());
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size. The texture region's upper left corner will be 0,0.
	 *
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public LibgdxSprite(Texture texture, int srcWidth, int srcHeight) {
		this(texture, 0, 0, srcWidth, srcHeight);
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size.
	 *
	 * @param srcX The x coordinate of the region to draw
	 * @param srcY The y coordinate of the region to draw
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public LibgdxSprite(Texture texture, int srcX, int srcY, int srcWidth,
	              int srcHeight) {
		final LibgdxTexture libgdxTexture = (LibgdxTexture) texture;
		sprite = new com.badlogic.gdx.graphics.g2d.Sprite(libgdxTexture);
		color = new LibgdxColor(sprite.getColor());
		flip(false, true);
		setBoundingRectangle();
	}

	/**
	 * Creates a sprite based on a specific TextureRegion, the new sprite's
	 * region is a copy of the parameter region - altering one does not affect
	 * the other
	 */
	public LibgdxSprite(TextureRegion region) {
		super();
		final GdxTextureRegion libgdxTextureRegion = (GdxTextureRegion) region;
		sprite = new com.badlogic.gdx.graphics.g2d.Sprite(libgdxTextureRegion.asGdxTextureRegion());
		color = new LibgdxColor(sprite.getColor());
		setFlip(region.isFlipX(), region.isFlipY());
		setBoundingRectangle();
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size, relative to specified sprite's texture region.
	 *
	 * @param srcX The x coordinate of the region to draw
	 * @param srcY The y coordinate of the region to draw
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public LibgdxSprite(TextureRegion region, int srcX, int srcY, int srcWidth,
	              int srcHeight) {
		final GdxTextureRegion libgdxTextureRegion = (GdxTextureRegion) region;
		sprite = new com.badlogic.gdx.graphics.g2d.Sprite(libgdxTextureRegion.asGdxTextureRegion(), srcX, srcY, srcWidth, srcHeight);
		color = new LibgdxColor(sprite.getColor());
		setFlip(region.isFlipX(), region.isFlipY());
		setBoundingRectangle();
	}

	/** Creates a sprite that is a copy in every way of the specified sprite. */
	public LibgdxSprite(Sprite sprite) {
		this();
		set(sprite);
	}

	private void setBoundingRectangle() {
		final com.badlogic.gdx.math.Rectangle boundingRectangle = sprite.getBoundingRectangle();
		this.boundingRectangle.set(boundingRectangle.x, boundingRectangle.y, boundingRectangle.width, boundingRectangle.height);
	}

	@Override
	public void set(Sprite sprite) {
		this.sprite.set(((LibgdxSprite) sprite).sprite);
		this.color.color = ((LibgdxSprite) sprite).sprite.getColor();
		setBoundingRectangle();
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		sprite.setBounds(x, y, width, height);
	}

	@Override
	public void setSize(float width, float height) {
		sprite.setSize(width, height);
	}

	@Override
	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
	}

	@Override
	public void setOriginBasedPosition(float x, float y) {
		sprite.setOriginBasedPosition(x, y);
	}

	@Override
	public void setCenterX(float x) {
		sprite.setCenterX(x);
	}

	@Override
	public void setCenterY(float y) {
		sprite.setCenterY(y);
	}

	@Override
	public void setCenter(float x, float y) {
		sprite.setCenter(x, y);
	}

	@Override
	public void translateX(float xAmount) {
		sprite.translateX(xAmount);
	}

	@Override
	public void translateY(float yAmount) {
		sprite.translateY(yAmount);
	}

	@Override
	public void translate(float xAmount, float yAmount) {
		sprite.translate(xAmount, yAmount);
	}

	@Override
	public void setOrigin(float originX, float originY) {
		sprite.setOrigin(originX, originY);
	}

	@Override
	public void setOriginCenter() {
		sprite.setOriginCenter();
	}

	@Override
	public float getRotation() {
		return sprite.getRotation();
	}

	@Override
	public void setRotation(float degrees) {
		sprite.setRotation(degrees);
	}

	@Override
	public void rotate(float degrees) {
		sprite.rotate(degrees);
	}

	@Override
	public void rotate90(boolean clockwise) {
		sprite.rotate90(clockwise);
	}

	@Override
	public void setScale(float scaleXY) {
		sprite.setScale(scaleXY);
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		sprite.setScale(scaleX, scaleY);
	}

	@Override
	public void scale(float amount) {
		sprite.setScale(amount);
	}

	@Override
	public float[] getVertices() {
		return sprite.getVertices();
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}

	@Override
	public float getX() {
		return sprite.getX();
	}

	@Override
	public void setX(float x) {
		sprite.setX(x);
	}

	@Override
	public float getY() {
		return sprite.getY();
	}

	@Override
	public void setY(float y) {
		sprite.setY(y);
	}

	@Override
	public float getWidth() {
		return sprite.getWidth();
	}

	@Override
	public float getHeight() {
		return sprite.getHeight();
	}

	@Override
	public float getOriginX() {
		return sprite.getOriginX();
	}

	@Override
	public float getOriginY() {
		return sprite.getOriginY();
	}

	@Override
	public float getScaleX() {
		return sprite.getScaleX();
	}

	@Override
	public float getScaleY() {
		return sprite.getScaleY();
	}

	@Override
	public Color getTint() {
		return color;
	}

	@Override
	public void setTint(Color tint) {
		sprite.setColor(tint.rf(), tint.gf(), tint.bf(), tint.af());
	}

	@Override
	public void setRegion(Texture texture) {
		sprite.setRegion((LibgdxTexture) texture);
	}

	@Override
	public void setRegion(int x, int y, int width, int height) {
		sprite.setRegion(x, y, width, height);
	}

	@Override
	public void setRegion(float u, float v, float u2, float v2) {
		sprite.setRegion(u, v, u2, v2);
	}

	@Override
	public void setRegion(TextureRegion region) {
		sprite.setRegion(((GdxTextureRegion) region).asGdxTextureRegion());
	}

	@Override
	public void setRegion(TextureRegion region, int x, int y, int width, int height) {
		sprite.setRegion(((GdxTextureRegion) region).asGdxTextureRegion(), x, y, width, height);
	}

	@Override
	public Texture getTexture() {
		return (LibgdxTexture) sprite.getTexture();
	}

	@Override
	public void setTexture(Texture texture) {
		sprite.setTexture((LibgdxTexture) texture);
	}

	@Override
	public float getU() {
		return sprite.getU();
	}

	@Override
	public void setU(float u) {
		sprite.setU(u);
	}

	@Override
	public float getV() {
		return sprite.getV();
	}

	@Override
	public void setV(float v) {
		sprite.setV(v);
	}

	@Override
	public float getU2() {
		return sprite.getU2();
	}

	@Override
	public void setU2(float u2) {
		sprite.setU2(u2);
	}

	@Override
	public float getV2() {
		return sprite.getV2();
	}

	@Override
	public void setV2(float v2) {
		sprite.setV2(v2);
	}

	@Override
	public int getRegionX() {
		return sprite.getRegionX();
	}

	@Override
	public void setRegionX(int x) {
		sprite.setRegionX(x);
	}

	@Override
	public int getRegionY() {
		setFlipY(!isFlipY());
		int result = sprite.getRegionY();
		setFlipY(!isFlipY());
		return result;
	}

	@Override
	public void setRegionY(int y) {
		setFlipY(!isFlipY());
		sprite.setRegionY(y);
		setFlipY(!isFlipY());
	}

	@Override
	public int getRegionWidth() {
		return sprite.getRegionWidth();
	}

	@Override
	public void setRegionWidth(int width) {
		sprite.setRegionWidth(width);
	}

	@Override
	public int getRegionHeight() {
		return sprite.getRegionHeight();
	}

	@Override
	public void setRegionHeight(int height) {
		sprite.setRegionHeight(height);
	}

	@Override
	public void flip(boolean x, boolean y) {
		sprite.flip(x, y);
	}

	@Override
	public void setFlip(boolean x, boolean y) {
		boolean performX = false;
		boolean performY = false;
		if (isFlipX() != x) {
			performX = true;
		}
		if (isFlipY() != y) {
			performY = true;
		}
		flip(performX, performY);
	}

	@Override
	public boolean isFlipX() {
		return sprite.isFlipX();
	}

	@Override
	public float getAlpha() {
		return sprite.getColor().a;
	}

	@Override
	public void setAlpha(float a) {
		sprite.setAlpha(a);
	}

	@Override
	public void setFlipX(boolean flipX) {
		if(flipX == isFlipX()) {
			return;
		}
		flip(true, false);
	}

	@Override
	public boolean isFlipY() {
		return !sprite.isFlipY();
	}

	@Override
	public void setFlipY(boolean flipY) {
		if(flipY == isFlipY()) {
			return;
		}
		flip(false, true);
	}

	@Override
	public void scroll(float xAmount, float yAmount) {
		sprite.scroll(xAmount, yAmount);
	}

	@Override
	public Pixmap toPixmap() {
		final LibgdxTexture texture = (LibgdxTexture) getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		final com.badlogic.gdx.graphics.Pixmap texturePixmap = texture.getTextureData().consumePixmap();
		final LibgdxPixmap result = (LibgdxPixmap) Mdx.graphics.newPixmap(getRegionWidth(), getRegionHeight(), PixmapFormat.RGBA8888);
		result.pixmap.drawPixmap(texturePixmap, 0, 0, getRegionX(), getRegionY(), getRegionWidth(), getRegionHeight());
		return result;
	}

	@Override
	public TextureRegion[][] split(int tileWidth, int tileHeight) {
		int x = getRegionX();
		int y = getRegionY();
		final int width = getRegionWidth();
		final int height = getRegionHeight();

		final int rows = height / tileHeight;
		final int columns = width / tileWidth;

		int startX = x;
		final TextureRegion[][] result = new TextureRegion[rows][columns];
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			for (int col = 0; col < columns; col++, x += tileWidth) {
				result[row][col] = Mdx.graphics.newTextureRegion(getTexture(), x, y, tileWidth, tileHeight);
			}
		}
		return result;
	}

	@Override
	public com.badlogic.gdx.graphics.g2d.TextureRegion asGdxTextureRegion() {
		return sprite;
	}
}
