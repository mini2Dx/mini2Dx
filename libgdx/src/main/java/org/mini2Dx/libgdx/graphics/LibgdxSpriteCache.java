package org.mini2Dx.libgdx.graphics;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.libgdx.LibgdxGraphics;

public class LibgdxSpriteCache implements SpriteCache {

    private com.badlogic.gdx.graphics.g2d.SpriteCache gdxCache;
    private int indexCounter;
    private Shader currentCustomShader;
    private Color currentCustomColor = Colors.WHITE().copy();
    private boolean isDrawing;

    public LibgdxSpriteCache(){
        gdxCache = new com.badlogic.gdx.graphics.g2d.SpriteCache();
        gdxCache.setColor(((LibgdxColor)currentCustomColor).color);
    }

    @Override
    public int add(Sprite sprite) {
        gdxCache.add(((LibgdxSprite) sprite).sprite);
        return indexCounter++;
    }

    @Override
    public int add(Texture texture, float x, float y) {
        return add(texture, x, y, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        return add(texture, x, y, srcX, srcY, srcWidth, srcHeight, false, false);
    }

    @Override
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        return add(texture, x, y, srcX, srcY, srcWidth, srcHeight, 1, 1, false, false);
    }

    @Override
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX, float scaleY, boolean flipX, boolean flipY) {
        return add(texture, x, y, srcX, srcY, srcWidth, srcHeight, 1, 1, 0, 0, 0, false, false);
    }

    @Override
    public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX, float scaleY, float originX, float originY, float rotation, boolean flipX, boolean flipY) {
        gdxCache.add((com.badlogic.gdx.graphics.Texture) texture, x, y, originX, originY, srcWidth * scaleX, srcHeight * scaleY, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
        return indexCounter++;
    }

    @Override
    public int add(TextureRegion region, float x, float y) {
        return add(region, x, y, 1, 1);
    }

    @Override
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY) {
        return add(region, x, y, scaleX, scaleY, 0, 0, 0);
    }

    @Override
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY, float rotation) {
        return add(region, x, y, scaleX, scaleY, 0, 0, 0, false, false);
    }

    @Override
    public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY, float rotation, boolean flipX, boolean flipY) {
        region.flip(flipX, flipY);
        gdxCache.add(((GdxTextureRegion) region).asGdxTextureRegion(), x, y, originX, originY, region.getRegionWidth() * scaleX, region.getRegionHeight() * scaleY, scaleX, scaleY, rotation);
        region.flip(flipX, flipY);
        return indexCounter++;
    }

    @Override
    public void clear() {
        gdxCache.clear();
        indexCounter = 0;
    }

    @Override
    public void dispose() {
        gdxCache.dispose();
    }

    @Override
    public void draw(Graphics g, int cacheID) {
        gdxCache.getProjectionMatrix().set(((LibgdxGraphics)Mdx.graphicsContext).spriteBatch.getProjectionMatrix());
        gdxCache.getTransformMatrix().set(((LibgdxGraphics)Mdx.graphicsContext).spriteBatch.getTransformMatrix());
        gdxCache.begin();
        gdxCache.draw(cacheID);
        gdxCache.end();
    }

    @Override
    public void draw(Graphics g, int cacheID, int offset, int length) {
        gdxCache.getProjectionMatrix().set(((LibgdxGraphics)Mdx.graphicsContext).spriteBatch.getProjectionMatrix());
        gdxCache.getTransformMatrix().set(((LibgdxGraphics)Mdx.graphicsContext).spriteBatch.getTransformMatrix());
        gdxCache.begin();
        gdxCache.draw(cacheID, offset, length);
        gdxCache.end();
    }

    @Override
    public void beginCache() {
        gdxCache.beginCache();
        isDrawing = true;
    }

    @Override
    public void beginCache(int cacheID) {
        gdxCache.beginCache(cacheID);
        isDrawing = true;
    }

    @Override
    public int endCache() {
        isDrawing = false;
        return gdxCache.endCache();
    }

    @Override
    public Color getColor() {
        return currentCustomColor.copy();
    }

    @Override
    public void setColor(Color tint) {
        currentCustomColor.set(tint);
        gdxCache.setColor(((LibgdxColor)tint).color);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        currentCustomColor.set(r, g, b, a);
        gdxCache.setColor(r, g, b, a);
    }

    @Override
    public Shader getCustomShader() {
        return currentCustomShader;
    }

    @Override
    public void setCustomShader(Shader shader) {
        currentCustomShader = shader;
        gdxCache.setShader(((LibgdxShader) shader).shaderProgram);
    }

    @Override
    public boolean isDrawing() {
        return isDrawing;
    }
}
