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
package org.mini2Dx.core;

import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.gdx.math.Matrix4;

/**
 * Common interface to graphics rendering functionality
 */
public interface Graphics {

    /**
     * Called by mini2Dx before rendering begins
     *
     * @param gameWidth
     *            The current game window width
     * @param gameHeight
     *            The current game window height
     */
    public void preRender(int gameWidth, int gameHeight);

    /**
     * Called by mini2Dx after rendering
     */
    public void postRender();

    /**
     * Clears the graphics context (e.g. glClear on OpenGL platforms)
     */
    public void clearContext();

    /**
     * Renders a line segment to the window in the current {@link Color} with
     * the set line height
     *
     * @param x1
     *            X coordinate of point A
     * @param y1
     *            Y coordinate of point A
     * @param x2
     *            X coordinate of point B
     * @param y2
     *            Y coordinate of point B
     */
    public void drawLineSegment(float x1, float y1, float x2, float y2);

    /**
     * Renders a rectangle to the window in the current {@link Color} with the
     * set line height
     *
     * @param x
     *            The x coordinate to render at
     * @param y
     *            The y coordinate to render at
     * @param width
     *            The width of the rectangle
     * @param height
     *            The height of the rectangle
     */
    public void drawRect(float x, float y, float width, float height);

    /**
     * Fills a rectangle to the window in the current {@link Color}
     *
     * @param x
     *            The x coordinate to render at
     * @param y
     *            The y coordinate to render at
     * @param width
     *            The width of the rectangle
     * @param height
     *            The height of the rectangle
     */
    public void fillRect(float x, float y, float width, float height);

    /**
     * Draws a circle to the window in the current {@link Color} with the set
     * line height
     *
     * @param centerX
     *            The x coordinate of the center of the circle
     * @param centerY
     *            The y coordinate of the center of the circle
     * @param radius
     *            The radius of the circle
     */
    public void drawCircle(float centerX, float centerY, int radius);

    /**
     * Draws a circle to the window in the current {@link Color} with the set
     * line height
     *
     * @param centerX
     *            The x coordinate of the center of the circle
     * @param centerY
     *            The y coordinate of the center of the circle
     * @param radius
     *            The radius of the circle
     */
    public void drawCircle(float centerX, float centerY, float radius);

    /**
     * Fills a circle to the window in the current {@link Color}
     *
     * @param centerX
     *            The x coordinate of the center of the circle
     * @param centerY
     *            The y coordinate of the center of the circle
     * @param radius
     *            The radius of the circle
     */
    public void fillCircle(float centerX, float centerY, int radius);

    /**
     * Fills a circle to the window in the current {@link Color}
     *
     * @param centerX
     *            The x coordinate of the center of the circle
     * @param centerY
     *            The y coordinate of the center of the circle
     * @param radius
     *            The radius of the circle
     */
    public void fillCircle(float centerX, float centerY, float radius);

    /**
     * Draws a triangle to the window in the current {@link Color}
     *
     * @param x1
     *            The x coordinate of the first point
     * @param y1
     *            The y coordinate of the first point
     * @param x2
     *            The x coordinate of the second point
     * @param y2
     *            The y coordinate of the second point
     * @param x3
     *            The x coordinate of the third point
     * @param y3
     *            The y coordinate of the third point
     */
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3);

    /**
     * Draws a triangle to the window in the current {@link Color}
     *
     * @param x1
     *            The x coordinate of the first point
     * @param y1
     *            The y coordinate of the first point
     * @param x2
     *            The x coordinate of the second point
     * @param y2
     *            The y coordinate of the second point
     * @param x3
     *            The x coordinate of the third point
     * @param y3
     *            The y coordinate of the third point
     */
    public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3);

    /**
     * Draws a polygon to the window in the current {@link Color}
     *
     * @param vertices
     *            The vertices of the polygon in format x1,y1,x2,y2,x3,y3,etc.
     */
    public void drawPolygon(float[] vertices);

    /**
     * Fills a polygon to the window in the current {@link Color}
     *
     * @param vertices
     *            The vertices of the polygon in format x1,y1,x2,y2,x3,y3,etc.
     * @param triangles
     *            The indices in the vertices parameter that make up the
     *            triangles of the polygon
     */
    public void fillPolygon(float[] vertices, short[] triangles);

    /**
     * Draws a string to the window
     *
     * @param text
     *            The {@link String} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     */
    public void drawString(String text, float x, float y);

    /**
     * Draws a string to the window, automatically wrapping it within a
     * specified width
     *
     * @param text
     *            The {@link String} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param targetWidth
     *            The width to render the {@link String} at. Note: The string
     *            will automatically wrapped if it is longer.
     */
    public void drawString(String text, float x, float y, float targetWidth);

    /**
     * Draws a string to the window, automatically wrapping it within a
     * specified width and aligning it to the left, center or right of the width
     *
     * @param text
     *            The {@link String} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param targetWidth
     *            The width to render the {@link String} at. Note: The string
     *            will automatically wrapped if it is longer.
     * @param horizontalAlign
     *            The horizontal alignment
     */
    public void drawString(String text, float x, float y, float targetWidth, int horizontalAlign);

    /**
     * Draws a texture to this graphics context
     *
     * @param texture
     *            The {@link Texture} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     */
    public void drawTexture(Texture texture, float x, float y);

    /**
     * Draws a texture to this graphics context
     *
     * @param texture
     *            The {@link Texture} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param flipY
     *            True if the texture should be flipped vertically
     */
    public void drawTexture(Texture texture, float x, float y, boolean flipY);

    /**
     * Draws a texture to this graphics context
     *
     * @param texture
     *            The {@link Texture} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param width
     *            The width to draw the texture (this can stretch/shrink the
     *            texture if not matching the texture's width)
     * @param height
     *            The height to draw the texture (this can stretch/shrink the
     *            texture if not matching the texture's height)
     */
    public void drawTexture(Texture texture, float x, float y, float width, float height);

    /**
     *
     * @param texture
     *            The {@link Texture} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param width
     *            The width to draw the texture (this can stretch/shrink the
     *            texture if not matching the texture's width)
     * @param height
     *            The height to draw the texture (this can stretch/shrink the
     *            texture if not matching the texture's height)
     * @param flipY
     *            True if the texture should be flipped vertically
     */
    public void drawTexture(Texture texture, float x, float y, float width, float height, boolean flipY);

    /**
     * Draws a texture region to this graphics context
     *
     * @param textureRegion
     *            The {@link TextureRegion} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     */
    public void drawTextureRegion(TextureRegion textureRegion, float x, float y);

    /**
     * Draws a texture region to this graphics context
     *
     * @param textureRegion
     *            The {@link TextureRegion} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param width
     *            The width to draw the region (this can stretch/shrink the
     *            texture if not matching the region's width)
     * @param height
     *            The height to draw the region (this can stretch/shrink the
     *            texture if not matching the region's height)
     */
    public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height);

    /**
     * Draws a texture region to this graphics context
     *
     * @param textureRegion
     *            The {@link TextureRegion} to draw
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param width
     *            The width to draw the region (this can stretch/shrink the
     *            texture if not matching the region's width)
     * @param height
     *            The height to draw the region (this can stretch/shrink the
     *            texture if not matching the region's height)
     * @param rotation
     * 			  The rotation in degrees (rotated around the top-left corner of the region)
     */
    public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height, float rotation);

    /**
     * Draws an instance of {@link Shape}
     *
     * @param shape
     *            The implementation of {@link Shape} to draw
     */
    public void drawShape(Shape shape);

    /**
     * Fills an instance of {@link Shape}
     *
     * @param shape
     *            The implementation of {@link Shape} to fill
     */
    public void fillShape(Shape shape);

    /**
     * Draws a {@link Sprite} with all transformations applied to this graphics
     * context
     *
     * @param sprite
     *            The {@link Sprite} to draw
     */
    public void drawSprite(Sprite sprite);

    /**
     * Draws a {@link Sprite} at the given coordinates with all transformations
     * applied to this graphics context
     *
     * @param sprite
     *            The {@link Sprite} to draw
     * @param x
     *            The x coordinate to render at
     * @param y
     *            The y coordinate to render at
     */
    public void drawSprite(Sprite sprite, float x, float y);

    /**
     * Draws a {@link SpriteCache}
     *
     * @param spriteCache
     *            The {@link SpriteCache} to draw
     * @param cacheId
     *            The cacheId to draw
     */
    public void drawSpriteCache(SpriteCache spriteCache, int cacheId);

    /**
     * Draws a {@link ParticleEffect} to screen
     *
     * @param effect
     *            The effect to be drawn
     */
    public void drawParticleEffect(ParticleEffect effect);

    /**
     * Draws a {@link NinePatch} to screen
     *
     * @param ninePatch
     *            The {@link NinePatch} to be drawn
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordiante to draw at
     * @param width
     *            The width to apply to the {@link NinePatch}
     * @param height
     *            The height to apply to the {@link NinePatch}
     */
    public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height);

    /**
     * Draws a {@link TilingDrawable} to screen
     *
     * @param tilingDrawable
     *            The {@link TilingDrawable} to be drawn
     * @param x
     *            The x coordinate to draw at
     * @param y
     *            The y coordinate to draw at
     * @param width
     *            The width to render
     * @param height
     *            The height to render
     */
    public void drawTilingDrawable(TilingDrawable tilingDrawable, float x, float y, float width, float height);

    /**
     * Draws a {@link GameFontCache} to the screen
     *
     * @param gameFontCache
     *            The {@link GameFontCache} to render
     */
    public void drawFontCache(GameFontCache gameFontCache);

    /**
     * Sets the graphics context clip. Only pixels within this area will be
     * rendered
     *
     * @param x
     *            The x coordinate the clip begins at
     * @param y
     *            The y coordinate the clip begins at
     * @param width
     *            The width of the clip
     * @param height
     *            The height of the clip
     */
    public void setClip(float x, float y, float width, float height);

    /**
     * Sets the graphics context clip. Only pixels within this area will be
     * rendered
     *
     * @param clip
     *            The clip area
     */
    public void setClip(Rectangle clip);

    /**
     * Removes the applied clip
     * @return Null if there is no clip
     */
    public Rectangle removeClip();

    /**
     * Returns the current clip
     * @return A new {@link Rectangle} set to the clip coordinates. See {@link #peekClip(Rectangle)}
     */
    public Rectangle peekClip();

    /**
     * Returns the current clip
     * @param rectangle Applies the current clip coordinates to this {@link Rectangle}. If there is no clip it is set to the viewport dimensions.
     */
    public void peekClip(Rectangle rectangle);

    /**
     * Sets the {@link Color} to apply to draw operations
     *
     * @param tint
     *            The {@link Color} to tint with
     */
    public void setTint(Color tint);

    /**
     * Sets the {@link GameFont} to draw {@link String}s with
     *
     * @param font
     *            A non-null instance of {@link GameFont}
     */
    public void setFont(GameFont font);

    /**
     * Removes the tinting {@link Color}
     */
    public void removeTint();

    /**
     * Enables blending during rendering
     */
    public void enableBlending();

    /**
     * Disables blending during rendering
     */
    public void disableBlending();

    /**
     * Applies a {@link Shader} program to this instance
     *
     * @param shader
     *            The {@link Shader} to apply
     */
    public void setShader(Shader shader);

    /**
     * Returns the currently applied {@link Shader} program
     *
     * @return
     */
    public Shader getShader();

    /**
     * Clears the {@link Shader} program applied to this instance
     */
    public void clearShader();

    /**
     * Sets the blend function to be applied
     *
     * <a href=
     * "http://lessie2d.tumblr.com/post/28673280483/opengl-blend-function-cheat-sheet-well-this-is"
     * >OpenGL Blend Function Cheatsheet</a>
     *
     * @param srcFunc
     *            Source GL function
     * @param dstFunc
     *            Destination GL function
     */
    public void setBlendFunction(Mini2DxBlendFunction srcFunc, Mini2DxBlendFunction dstFunc);

    /**
     * Resets the blend function to its default
     */
    public void clearBlendFunction();

    /**
     * Immediately flushes everything rendered rather than waiting until the end
     * of rendering
     */
    public void flush();

    /**
     * Returns the line height used
     *
     * @return A value greater than 0
     */
    public int getLineHeight();

    /**
     * Sets the line height to be used
     *
     * @param lineHeight
     *            A value greater than 0
     */
    public void setLineHeight(int lineHeight);

    /**
     * Returns the {@link Color} to draw shapes with
     *
     * @return A non-null value
     */
    public Color getColor();

    /**
     * Sets the {@link Color} to draw shapes with
     *
     * @param color
     */
    public void setColor(Color color);

    /**
     * Returns the background {@link Color}
     *
     * @return A non-null value
     */
    public Color getBackgroundColor();

    /**
     * Sets the background {@link Color} to be used
     */
    public void setBackgroundColor(Color backgroundColor);

    /**
     * Returns the {@link GameFont} to draw {@link String}s with
     *
     * @return 15pt Arial font by default unless setFont() is called
     */
    public GameFont getFont();

    /**
     * Returns the {@link Color} tint being applied to all draw operations
     *
     * @return
     */
    public Color getTint();

    /**
     * Returns the X scaling factor
     * @return
     */
    public float getScaleX();

    /**
     * Returns the Y scaling factor
     * @return
     */
    public float getScaleY();

    /**
     * Scales the canvas (multiplies scale value)
     *
     * @param scaleX
     *            Scaling along the X axis
     * @param scaleY
     *            Scaling along the Y axis
     */
    public void scale(float scaleX, float scaleY);

    /**
     * Sets the canvas scale
     *
     * @param scaleX
     *            Scaling along the X axis
     * @param scaleY
     *            Scaling along the Y axis
     */
    public void setScale(float scaleX, float scaleY);

    /**
     * Resets scaling back to default values
     */
    public void clearScaling();

    /**
     * Returns the translation of the viewport along the X axis
     * @return
     */
    public float getTranslationX();

    /**
     * Returns the translation of the viewport along the Y axis
     * @return
     */
    public float getTranslationY();

    /**
     * Moves the graphics context by a certain amount of the X and Y axis
     *
     * @param translateX
     *            The x axis translation
     * @param translateY
     *            The y axis translation
     */
    public void translate(float translateX, float translateY);

    /**
     * Sets the translation coordinates
     *
     * @param translateX
     *            The x axis translation
     * @param translateY
     *            The y axis translation
     */
    public void setTranslation(float translateX, float translateY);

    /**
     * Returns the viewport rotation (in degrees)
     * @return Defaults to 0f
     */
    public float getRotation();

    /**
     * Returns the origin X coordinate of the viewport rotation
     * @return Defaults to 0f
     */
    public float getRotationX();

    /**
     * Returns the origin Y coordinate of the viewport rotation
     * @return Defaults to 0f
     */
    public float getRotationY();

    /**
     * Rotates the canvas by the provided degrees around the provided point
     *
     * @param degrees
     *            The degree value in a clockwise direction
     * @param x
     *            The x coordinate to rotate around
     * @param y
     *            The y coordinate to rotate around
     */
    public void rotate(float degrees, float x, float y);

    /**
     * Sets the canvas rotation around a provided point
     *
     * @param degrees
     *            The degree value in a clockwise direction
     * @param x
     *            The x coordinate to rotate around
     * @param y
     *            The y coordinate to rotate around
     */
    public void setRotation(float degrees, float x, float y);

    /**
     * Returns the {@link Matrix4} viewport projection
     * @return
     */
    public Matrix4 getProjectionMatrix();

    /**
     * Returns if the game window is initialised natively
     * @return False at startup, true once the window/game is visible to the user
     */
    public boolean isWindowReady();

    /**
     * Returns the width of the window
     *
     * @return
     */
    public int getWindowWidth();

    /**
     * Returns the height of the window
     *
     * @return
     */
    public int getWindowHeight();

    /**
     * Returns the X coordinate of the beginning of the safe area. This only applies to consoles running on television. See more info <a href="https://en.wikipedia.org/wiki/Safe_area_(television)">here</a>.
     *
     * @return
     */
    public int getWindowSafeX();

    /**
     * Returns the Y coordinate of the beginning of the safe area. This only applies to consoles running on television. See more info <a href="https://en.wikipedia.org/wiki/Safe_area_(television)">here</a>.
     *
     * @return
     */
    public int getWindowSafeY();

    /**
     * Returns the safe width of the window. This only applies to consoles running on television. See more info <a href="https://en.wikipedia.org/wiki/Safe_area_(television)">here</a>.
     *
     * @return
     */
    public int getWindowSafeWidth();

    /**
     * Returns the safe height of the window. This only applies to consoles running on television. See more info <a href="https://en.wikipedia.org/wiki/Safe_area_(television)">here</a>.
     *
     * @return
     */
    public int getWindowSafeHeight();

    /**
     * Returns the width of the viewport
     *
     * @return
     */
    public float getViewportWidth();

    /**
     * Returns the height of the viewport
     *
     * @return
     */
    public float getViewportHeight();

    /**
     * Returns the frame number
     * @return
     */
    public long getFrameId();

    /**
     * Returns the current min texture filter.
     *
     * @return The current min texture filter.
     */
    public TextureFilter getMinFilter();

    /**
     * Sets the texture min filter that will be used for all the subsequent texture drawing operations
     *
     * @param filter The min filter to be set.
     */
    public void setMinFilter(TextureFilter filter);

    /**
     * Returns the current mag texture filter.
     *
     * @return The current mag texture filter.
     */
    public TextureFilter getMagFilter();

    /**
     * Sets the texture mag filter that will be used for all the subsequent texture drawing operations
     *
     * @param filter The mag filter to be set.
     */
    public void setMagFilter(TextureFilter filter);
}
