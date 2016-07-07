/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

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
	 * Draws a triangle to the window in the current {@link Color}
	 * @param x1 The x coordinate of the first point
	 * @param y1 The y coordinate of the first point
	 * @param x2 The x coordinate of the second point
	 * @param y2 The y coordinate of the second point
	 * @param x3 The x coordinate of the third point
	 * @param y3 The y coordinate of the third point
	 */
	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3);
	
	/**
	 * Draws a triangle to the window in the current {@link Color}
	 * @param x1 The x coordinate of the first point
	 * @param y1 The y coordinate of the first point
	 * @param x2 The x coordinate of the second point
	 * @param y2 The y coordinate of the second point
	 * @param x3 The x coordinate of the third point
	 * @param y3 The y coordinate of the third point
	 */
	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3);
	
	/**
	 * Draws a polygon to the window in the current {@link Color}
	 * @param vertices The vertices of the polygon in format x1,y1,x2,y2,x3,y3,etc.
	 */
	public void drawPolygon(float[] vertices);
	
	/**
	 * Fills a polygon to the window in the current {@link Color}
	 * @param vertices The vertices of the polygon in format x1,y1,x2,y2,x3,y3,etc.
	 * @param triangles The indices in the vertices parameter that make up the triangles of the polygon
	 */
	public void fillPolygon(float [] vertices, short [] triangles);

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
	 *            The horizontal alignment. Note: Use {@link Align} to retrieve
	 *            the appropriate value.
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
	 * Draws an instance of a LibGDX {@link Stage}
	 * 
	 * @param stage
	 *            The {@link Stage} to be drawn to screen
	 */
	public void drawStage(Stage stage);

	/**
	 * Draws a {@link ParticleEffect} or {@link PooledParticleEffect} to screen
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
	 * Draws a {@link NinePatchDrawable} to screen
	 * 
	 * @param ninePatchDrawable
	 *            The {@link NinePatchDrawable} to be drawn
	 * @param x
	 *            The x coordinate to draw at
	 * @param y
	 *            The y coordiante to draw at
	 * @param width
	 *            The width to apply to the {@link NinePatchDrawable}
	 * @param height
	 *            The height to apply to the {@link NinePatchDrawable}
	 */
	public void drawNinePatch(NinePatchDrawable ninePatchDrawable, float x, float y, float width, float height);

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
	 * @param degrees The degree value in a clockwise direction
	 * @param x The x coordinate to rotate around
	 * @param y The y coordinate to rotate around
	 */
	public void setRotation(float degrees, float x, float y);

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
	 * @param translateX
	 *            The x axis translation
	 * @param translateY
	 *            The y axis translation
	 */
	public void setTranslation(float translateX, float translateY);

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
	 */
	public Rectangle removeClip();

	/**
	 * Sets the {@link Color} to apply to draw operations
	 * 
	 * @param tint
	 *            The {@link Color} to tint with
	 */
	public void setTint(Color tint);

	/**
	 * Sets the {@link BitmapFont} to draw {@link String}s with
	 * 
	 * @param font
	 *            A non-null instance of {@link BitmapFont}
	 */
	public void setFont(BitmapFont font);

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
	 * Applies a {@link ShaderProgram} to this instance
	 * 
	 * @param shaderProgram
	 *            The {@link ShaderProgram} to apply
	 */
	public void setShaderProgram(ShaderProgram shaderProgram);

	/**
	 * Returns the currently applied {@link ShaderProgram}
	 * 
	 * @return
	 */
	public ShaderProgram getShaderProgram();

	/**
	 * Clears the {@link ShaderProgram} applied to this instance
	 */
	public void clearShaderProgram();

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
	public void setBlendFunction(int srcFunc, int dstFunc);

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
	 * Returns the {@link BitmapFont} to draw {@link String}s with
	 * 
	 * @return 15pt Arial font by default unless setFont() is called
	 */
	public BitmapFont getFont();

	/**
	 * Returns the {@link Color} tint being applied to all draw operations
	 * 
	 * @return
	 */
	public Color getTint();

	public float getScaleX();

	public float getScaleY();

	public float getTranslationX();

	public float getTranslationY();

	public float getRotation();

	public float getRotationX();

	public float getRotationY();

	public Matrix4 getProjectionMatrix();

	/**
	 * Returns the width of the window width
	 * @return
	 */
	public int getWindowWidth();

	/**
	 * Returns the height of the window height
	 * @return
	 */
	public int getWindowHeight();
	
	/**
	 * Returns the width of the viewport width
	 * @return
	 */
	public float getViewportWidth();

	/**
	 * Returns the height of the viewport height
	 * @return
	 */
	public float getViewportHeight();
}
