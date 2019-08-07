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

public interface SpriteCache {

    /**
     * Adds a {@link Sprite} to the cache.
     * @param sprite The {@link Sprite} to be added.
     * @return The index of the added sprite
     */
    public int add(Sprite sprite);

    /**
     * Adds a {@link Texture} to the cache
     * @param texture The texture to be added
     * @param x x position of the texture
     * @param y y position of the texture
     * @return The index of the added texture
     */
    public int add(Texture texture, float x, float y);

    /**
     * Adds a region of a {@link Texture} to the cache
     * @param texture the texture to be added
     * @param x x position of the texture
     * @param y y position of the texture
     * @param srcX x point in the texture
     * @param srcY y point in the texture
     * @param srcWidth width of the region to be drawn
     * @param srcHeight height of the region to be drawn
     * @return The index of the added texture
     */
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight);

    /**
     * Adds a region of a {@link Texture} to the cache
     * @param texture the texture to be added
     * @param x x position of the texture
     * @param y y position of the texture
     * @param srcX x point in the texture
     * @param srcY y point in the texture
     * @param srcWidth width of the region to be drawn
     * @param srcHeight height of the region to be drawn
     * @param flipX indication of whether the region should be drawn x-flipped
     * @param flipY indication of whether the region should be drawn y-flipped
     * @return The index of the added texture
     */
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY);

    /**
     * Adds a transformed region of a {@link Texture} to the cache
     * @param texture the texture to be added
     * @param x x position of the texture
     * @param y y position of the texture
     * @param srcX x point in the texture
     * @param srcY y point in the texture
     * @param srcWidth width of the region to be drawn
     * @param srcHeight height of the region to be drawn
     * @param scaleX scale of the texture in the x direction
     * @param scaleY scale of the texture in the y direction
     * @param flipX indication of whether the region should be drawn x-flipped
     * @param flipY indication of whether the region should be drawn y-flipped
     * @return The index of the added texture
     */
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX, float scaleY, boolean flipX, boolean flipY);

    /**
     * Adds a transformed region of a {@link Texture} to the cache
     * @param texture the texture to be added
     * @param x x position of the texture
     * @param y y position of the texture
     * @param srcX x point in the texture
     * @param srcY y point in the texture
     * @param srcWidth width of the region to be drawn
     * @param srcHeight height of the region to be drawn
     * @param scaleX scale of the texture in the x direction
     * @param scaleY scale of the texture in the y direction
     * @param originX x position of the point around which the texture should be rotated
     * @param originY y position of the point around which the texture should be rotated
     * @param rotation rotation of the texture around the origin point (in degrees)
     * @param flipX indication of whether the region should be drawn x-flipped
     * @param flipY indication of whether the region should be drawn y-flipped
     * @return The index of the added texture
     */
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX, float scaleY, float originX, float originY, float rotation, boolean flipX, boolean flipY);

    /**
     * Adds a {@link TextureRegion} to the cache
     * @param region the texture region to be added
     * @param x x position of the texture region
     * @param y y position of the texture region
     * @return The index of the added texture region
     */
    public int add(TextureRegion region, float x, float y);

    /**
     * Adds a scaled {@link TextureRegion} to the cache
     * @param region the texture region to be added
     * @param x x position of the texture region
     * @param y y position of the texture region
     * @param scaleX scale of the texture region in the x direction
     * @param scaleY scale of the texture region in the y direction
     * @return The index of the added texture region
     */
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY);

    /**
     * Adds a scaled {@link TextureRegion} to the cache
     * @param region the texture region to be added
     * @param x x position of the texture region
     * @param y y position of the texture region
     * @param scaleX scale of the texture region in the x direction
     * @param scaleY scale of the texture region in the y direction
     * @param originX x position of the point around which the region should be rotated
     * @param originY y position of the point around which the region should be rotated
     * @param rotation rotation of the texture around the origin point (in degrees)
     * @return The index of the added texture region
     */
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY, float rotation);

    /**
     * Adds a scaled {@link TextureRegion} to the cache
     * @param region the texture region to be added
     * @param x x position of the texture region
     * @param y y position of the texture region
     * @param scaleX scale of the texture region in the x direction
     * @param scaleY scale of the texture region in the y direction
     * @param originX x position of the point around which the region should be rotated
     * @param originY y position of the point around which the region should be rotated
     * @param rotation rotation of the texture around the origin point (in degrees)
     * @param flipX indication of whether the region should be drawn x-flipped
     * @param flipY indication of whether the region should be drawn y-flipped
     * @return The index of the added texture region
     */
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY, float rotation, boolean flipX, boolean flipY);

    /**
     * Invalidates all the caches and resets the SpriteCache.
     */
    public void clear();

    /**
     * Releases all the resources held by this SpriteCache.
     */
    public void dispose();

    /**
     * Draws all the images defined in a SpriteCache to the screen.
     * @param g the current {@link Graphics} context
     * @param cacheID ID of the SpriteCache containing the images to draw.
     */
    public void draw(Graphics g, int cacheID);

    /**
     * Draws a subset of the images defined in a SpriteCache to the screen.
     * @param g the current {@link Graphics} context
     * @param cacheID ID of the SpriteCache containing the images to draw.
     * @param offset The index of the first image to draw
     * @param length The number of the images to draw
     */
    public void draw(Graphics g, int cacheID, int offset, int length);

    /**
     * Starts the definition of a new cache
     */
    public void beginCache();

    /**
     * Starts the redefinition of an existing cache
     * @param cacheID ID of the cache to be redefined
     */
    public void beginCache(int cacheID);

    /**
     * Ends the definition of the current cache
     * @return ID of the current cache
     */
    public int endCache();

    /**
     * Indicates the {@link Color} currently used to tint images when they are added to the SpriteCache
     * @return the color currently used to tint images when they are added to the SpriteCache
     */
    public Color getColor();

    /**
     * Sets the {@link Color} used to tint images when they are added to the SpriteCache
     * @param tint the new color to be used to tint images when they are added to the SpriteCache
     */
    public void setColor(Color tint);

    /**
     * Sets the {@link Color} used to tint images when they are added to the SpriteCache
     * @param r The red value of the new color (0.0-1.0)
     * @param g The green value of the new color (0.0-1.0)
     * @param b The blue value of the new color (0.0-1.0)
     * @param a The alpha value of the new color (0.0-1.0)
     */
    public void setColor(float r, float g, float b, float a);

    /**
     * Indicates the custom {@link Shader} used to draw images to the cache
     * @return the custom {@link Shader} used to draw images to the cache
     */
    public Shader getCustomShader();

    /**
     * Sets the custom {@link Shader} used to draw images to the cache
     * @param shader the new custom {@link Shader} used to draw images to the cache
     */
    public void setCustomShader(Shader shader);

    /**
     * Indicates if {@link #beginCache()} has been called
     * @return beginCache has been called
     */
    public boolean isDrawing();
}
