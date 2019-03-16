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

public interface NinePatch {

	public void render(Graphics g, float x, float y, float width, float height);

	public Color getColor();

	/**
	 * Copy given color. The color will be blended with the batch color, then
	 * combined with the texture colors at draw time. Default is White.
	 */
	public void setColor(Color color);

	public float getLeftWidth();

	/**
	 * Set the draw-time width of the three left edge patches
	 */
	public void setLeftWidth(float leftWidth);

	public float getRightWidth();

	/**
	 * Set the draw-time width of the three right edge patches
	 */
	public void setRightWidth(float rightWidth);

	public float getTopHeight();

	/**
	 * Set the draw-time height of the three top edge patches
	 */
	public void setTopHeight(float topHeight);

	public float getBottomHeight();

	/**
	 * Set the draw-time height of the three bottom edge patches
	 */
	public void setBottomHeight(float bottomHeight);

	public float getMiddleWidth();

	/**
	 * Set the width of the middle column of the patch. At render time, this is
	 * implicitly the requested render-width of the entire nine patch, minus the
	 * left and right width. This value is only used for computing the
	 * {@link #getTotalWidth() default total width}.
	 */
	public void setMiddleWidth(float middleWidth);

	public float getMiddleHeight();

	/**
	 * Set the height of the middle row of the patch. At render time, this is
	 * implicitly the requested render-height of the entire nine patch, minus the
	 * top and bottom height. This value is only used for computing the
	 * {@link #getTotalHeight() default total height}.
	 */
	public void setMiddleHeight(float middleHeight);

	public float getTotalWidth();

	public float getTotalHeight();

	/**
	 * Set the padding for content inside this ninepatch. By default the padding is
	 * set to match the exterior of the ninepatch, so the content should fit exactly
	 * within the middle patch.
	 */
	public void setPadding(float left, float right, float top, float bottom);

	/**
	 * Returns the left padding if set, else returns {@link #getLeftWidth()}.
	 */
	public float getPaddingLeft();

	/**
	 * See {@link #setPadding(float, float, float, float)}
	 */
	public void setPaddingLeft(float left);

	/**
	 * Returns the right padding if set, else returns {@link #getRightWidth()}.
	 */
	public float getPaddingRight();

	/**
	 * See {@link #setPadding(float, float, float, float)}
	 */
	public void setPaddingRight(float right);

	/**
	 * Returns the top padding if set, else returns {@link #getTopHeight()}.
	 */
	public float getPaddingTop();

	/**
	 * See {@link #setPadding(float, float, float, float)}
	 */
	public void setPaddingTop(float top);

	/**
	 * Returns the bottom padding if set, else returns {@link #getBottomHeight()}.
	 */
	public float getPaddingBottom();

	/**
	 * See {@link #setPadding(float, float, float, float)}
	 */
	public void setPaddingBottom(float bottom);

	public void scale(float scaleX, float scaleY);

	public Texture getTexture();
}
