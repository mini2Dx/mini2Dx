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

import org.mini2Dx.core.geom.Rectangle;

public interface Sprite extends TextureRegion {

	public void set(Sprite sprite);

	/**
	 * Sets the position and size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale
	 * are changed, it is slightly more efficient to set the bounds after those operations.
	 */
	public void setBounds(float x, float y, float width, float height);

	/**
	 * Sets the size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale are changed,
	 * it is slightly more efficient to set the size after those operations. If both position and size are to be changed, it is
	 * better to use {@link #setBounds(float, float, float, float)}.
	 */
	public void setSize(float width, float height);

	/**
	 * Sets the position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}.
	 */
	public void setPosition(float x, float y);

	/**
	 * Sets the position where the sprite will be drawn, relative to its current origin.
	 */
	public void setOriginBasedPosition(float x, float y);

	/**
	 * Sets the x position so that it is centered on the given x parameter
	 */
	public void setCenterX(float x);

	/**
	 * Sets the y position so that it is centered on the given y parameter
	 */
	public void setCenterY(float y);

	/**
	 * Sets the position so that the sprite is centered on (x, y)
	 */
	public void setCenter(float x, float y);

	/**
	 * Sets the x position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations.
	 */
	public void translateX(float xAmount);

	/**
	 * Sets the y position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations.
	 */
	public void translateY(float yAmount);

	/**
	 * Sets the position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
	 * changed, it is slightly more efficient to translate after those operations.
	 */
	public void translate(float xAmount, float yAmount);

	/**
	 * Sets the origin in relation to the sprite's position for scaling and rotation.
	 */
	public void setOrigin(float originX, float originY);

	/**
	 * Place origin in the center of the sprite
	 */
	public void setOriginCenter();

	/**
	 * @return the rotation of the sprite in degrees
	 */
	public float getRotation();

	/**
	 * Sets the rotation of the sprite in degrees. Rotation is centered on the origin set in {@link #setOrigin(float, float)}
	 */
	public void setRotation(float degrees);

	/**
	 * Sets the sprite's rotation in degrees relative to the current rotation. Rotation is centered on the origin set in
	 * {@link #setOrigin(float, float)}
	 */
	public void rotate(float degrees);

	/**
	 * Rotates this sprite 90 degrees in-place by rotating the texture coordinates. This rotation is unaffected by
	 * {@link #setRotation(float)} and {@link #rotate(float)}.
	 */
	public void rotate90(boolean clockwise);

	/**
	 * Sets the sprite's scale for both X and Y uniformly. The sprite scales out from the origin. This will not affect the values
	 * returned by {@link #getWidth()} and {@link #getHeight()}
	 */
	public void setScale(float scaleXY);

	/**
	 * Sets the sprite's scale for both X and Y. The sprite scales out from the origin. This will not affect the values returned by
	 * {@link #getWidth()} and {@link #getHeight()}
	 */
	public void setScale(float scaleX, float scaleY);

	/**
	 * Sets the sprite's scale relative to the current scale. for example: original scale 2 -&gt; sprite.scale(4) -&gt; final scale 6.
	 * The sprite scales out from the origin. This will not affect the values returned by {@link #getWidth()} and
	 * {@link #getHeight()}
	 */
	public void scale(float amount);

	/**
	 * Returns the packed vertices, colors, and texture coordinates for this sprite.
	 */
	public float[] getVertices();

	/**
	 * Returns the bounding axis aligned {@link Rectangle} that bounds this sprite. The rectangles x and y coordinates describe its
	 * bottom left corner. If you change the position or size of the sprite, you have to fetch the triangle again for it to be
	 * recomputed.
	 *
	 * @return the bounding Rectangle
	 */
	public Rectangle getBoundingRectangle();

	public float getX();

	/**
	 * Sets the x position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}.
	 */
	public void setX(float x);

	public float getY();

	/**
	 * Sets the y position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
	 * to set the position after those operations. If both position and size are to be changed, it is better to use
	 * {@link #setBounds(float, float, float, float)}.
	 */
	public void setY(float y);

	/**
	 * @return the width of the sprite, not accounting for scale.
	 */
	public float getWidth();

	/**
	 * @return the height of the sprite, not accounting for scale.
	 */
	public float getHeight();

	/**
	 * The origin influences {@link #setPosition(float, float)}, {@link #setRotation(float)} and the expansion direction of scaling
	 * {@link #setScale(float, float)}
	 */
	public float getOriginX();

	/**
	 * The origin influences {@link #setPosition(float, float)}, {@link #setRotation(float)} and the expansion direction of scaling
	 * {@link #setScale(float, float)}
	 */
	public float getOriginY();

	/**
	 * X scale of the sprite, independent of size set by {@link #setSize(float, float)}
	 */
	public float getScaleX();

	/**
	 * Y scale of the sprite, independent of size set by {@link #setSize(float, float)}
	 */
	public float getScaleY();

	/**
	 * Returns the color of this sprite. Changing the returned color will have no affect.
	 */
	public Color getTint();

	/**
	 * Sets the color used to tint this sprite. Default is White.
	 */
	public void setTint(Color tint);

	/**
	 * Set the sprite's flip state regardless of current condition
	 *
	 * @param x the desired horizontal flip state
	 * @param y the desired vertical flip state
	 */
	public void setFlip(boolean x, boolean y);

	public float getAlpha();

	/**
	 * Sets the alpha portion of the color used to tint this sprite.
	 */
	public void setAlpha(float a);

	public void setFlipX(boolean flipX);

	public void setFlipY(boolean flipY);
}
