/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.core.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/**
 * An implementation of {@link NinePatch} that repeats instead of stretches
 */
public class RepeatedNinePatch extends NinePatch {
	private TiledDrawable topCenter, middleLeft, middleCenter, middleRight, bottomCenter;

	/**
	 * Create a {@link RepeatedNinePatch} by cutting up the given texture into nine
	 * patches. The subsequent parameters define the 4 lines that will cut the
	 * texture region into 9 pieces.
	 * 
	 * @param left
	 *            Pixels from left edge.
	 * @param right
	 *            Pixels from right edge.
	 * @param top
	 *            Pixels from top edge.
	 * @param bottom
	 *            Pixels from bottom edge.
	 */
	public RepeatedNinePatch(Texture texture, int left, int right, int top, int bottom) {
		super(texture, left, right, top, bottom);
		loadTiledDrawables();
	}
	
	/**
	 * Create a {@link RepeatedNinePatch} by cutting up the given texture region into
	 * nine patches. The subsequent parameters define the 4 lines that will cut
	 * the texture region into 9 pieces.
	 * 
	 * @param left
	 *            Pixels from left edge.
	 * @param right
	 *            Pixels from right edge.
	 * @param top
	 *            Pixels from top edge.
	 * @param bottom
	 *            Pixels from bottom edge.
	 */
	public RepeatedNinePatch(TextureRegion region, int left, int right, int top, int bottom) {
		super(region, left, right, top, bottom);
		loadTiledDrawables();
	}
	
	public RepeatedNinePatch(NinePatch ninePatch) {
		this(ninePatch, ninePatch.color);
	}
	
	public RepeatedNinePatch(NinePatch ninePatch, Color color) {
		super(ninePatch, color);
		loadTiledDrawables();
	}
	
	private void loadTiledDrawables() {
		if(patches[TOP_CENTER] != null) {
			topCenter = new TiledDrawable(patches[TOP_CENTER]);
		}
		if(patches[MIDDLE_LEFT] != null) {
			middleLeft = new TiledDrawable(patches[MIDDLE_LEFT]);
		}
		if(patches[MIDDLE_CENTER] != null) {
			middleCenter = new TiledDrawable(patches[MIDDLE_CENTER]);
		}
		if(patches[MIDDLE_RIGHT] != null) {
			middleRight = new TiledDrawable(patches[MIDDLE_RIGHT]);
		}
		if(patches[BOTTOM_CENTER] != null) {
			bottomCenter = new TiledDrawable(patches[BOTTOM_CENTER]);
		}
	}
	
	@Override
	void draw(Batch batch, float x, float y, float width, float height) {		
		if(patches[TOP_RIGHT] == null) {
			//Split only vertical
			final float halfWidth = width * 0.5f;
			final float leftWidth = patches[TOP_LEFT].getRegionWidth();
			final float centerWidth = patches[TOP_CENTER].getRegionWidth();
			final float totalMiddleHeight = height - patches[TOP_LEFT].getRegionHeight() - patches[BOTTOM_LEFT].getRegionHeight();
			
			patches[TOP_LEFT].flip(false, true);
			batch.draw(patches[TOP_LEFT], x, y, leftWidth, patches[TOP_LEFT].getRegionHeight());
			patches[TOP_LEFT].flip(false, true);
			
			patches[TOP_CENTER].flip(false, true);
			batch.draw(patches[TOP_CENTER], x + leftWidth, y, centerWidth, patches[TOP_CENTER].getRegionHeight());
			patches[TOP_CENTER].flip(false, true);
			
			patches[MIDDLE_LEFT].flip(false, true);
			middleLeft.draw(batch, x, y + patches[TOP_LEFT].getRegionHeight(), leftWidth, totalMiddleHeight);
			patches[MIDDLE_LEFT].flip(false, true);
			
			patches[MIDDLE_CENTER].flip(false, true);
			middleCenter.draw(batch, x + leftWidth, y + patches[TOP_LEFT].getRegionHeight(), centerWidth, totalMiddleHeight);
			patches[MIDDLE_CENTER].flip(false, true);
			
			patches[BOTTOM_LEFT].flip(false, true);
			batch.draw(patches[BOTTOM_LEFT], x, y + patches[TOP_LEFT].getRegionHeight() + totalMiddleHeight, leftWidth, patches[BOTTOM_LEFT].getRegionHeight());
			patches[BOTTOM_LEFT].flip(false, true);
			
			patches[BOTTOM_CENTER].flip(false, true);
			batch.draw(patches[BOTTOM_CENTER], x + leftWidth, y + patches[TOP_LEFT].getRegionHeight() + totalMiddleHeight, centerWidth, patches[BOTTOM_CENTER].getRegionHeight());
			patches[BOTTOM_CENTER].flip(false, true);
		} else if(patches[BOTTOM_LEFT] == null) {
			//Split only horizontal
			final float halfHeight = height * 0.5f;
			final float topHeight = patches[TOP_LEFT].getRegionHeight();
			final float middleHeight = patches[MIDDLE_LEFT].getRegionHeight();
			final float totalCenterWidth = width - patches[TOP_LEFT].getRegionWidth() - patches[TOP_RIGHT].getRegionWidth();
			
			patches[TOP_LEFT].flip(false, true);
			batch.draw(patches[TOP_LEFT], x, y, patches[TOP_LEFT].getRegionWidth(), topHeight);
			patches[TOP_LEFT].flip(false, true);
			
			patches[MIDDLE_LEFT].flip(false, true);
			batch.draw(patches[MIDDLE_LEFT], x, y + topHeight, patches[MIDDLE_LEFT].getRegionWidth(), middleHeight);
			patches[MIDDLE_LEFT].flip(false, true);
			
			patches[TOP_CENTER].flip(false, true);
			topCenter.draw(batch, x + patches[TOP_LEFT].getRegionWidth(), y, totalCenterWidth, topHeight);
			patches[TOP_CENTER].flip(false, true);
			
			patches[MIDDLE_CENTER].flip(false, true);
			middleCenter.draw(batch, x + patches[MIDDLE_LEFT].getRegionWidth(), y + topHeight, totalCenterWidth, middleHeight);
			patches[MIDDLE_CENTER].flip(false, true);
			
			patches[TOP_RIGHT].flip(false, true);
			batch.draw(patches[TOP_RIGHT], x + patches[TOP_LEFT].getRegionWidth() + totalCenterWidth, y, patches[TOP_RIGHT].getRegionWidth(), topHeight);
			patches[TOP_RIGHT].flip(false, true);
			
			patches[MIDDLE_RIGHT].flip(false, true);
			batch.draw(patches[MIDDLE_RIGHT], x + patches[MIDDLE_LEFT].getRegionWidth() + totalCenterWidth, y, patches[MIDDLE_RIGHT].getRegionWidth(), topHeight);
			patches[MIDDLE_RIGHT].flip(false, true);
		} else {
			//No split
			final float totalCenterWidth = width - patches[TOP_LEFT].getRegionWidth() - patches[TOP_RIGHT].getRegionWidth();
			final float totalMiddleHeight = height - patches[TOP_LEFT].getRegionHeight() - patches[BOTTOM_LEFT].getRegionHeight();
			
			//Render center images
			patches[MIDDLE_CENTER].flip(false, true);
			middleCenter.draw(batch, x + patches[TOP_LEFT].getRegionWidth(), y + patches[TOP_LEFT].getRegionHeight(), totalCenterWidth, totalMiddleHeight);
			patches[MIDDLE_CENTER].flip(false, true);
			
			patches[TOP_CENTER].flip(false, true);
			topCenter.draw(batch, x + patches[TOP_LEFT].getRegionWidth(), y, totalCenterWidth, patches[TOP_CENTER].getRegionHeight());
			patches[TOP_CENTER].flip(false, true);
			
			patches[BOTTOM_CENTER].flip(false, true);
			bottomCenter.draw(batch, x + patches[TOP_LEFT].getRegionWidth(), y + patches[TOP_LEFT].getRegionHeight() + totalMiddleHeight, totalCenterWidth, patches[BOTTOM_CENTER].getRegionHeight());
			patches[BOTTOM_CENTER].flip(false, true);
			
			patches[MIDDLE_LEFT].flip(false, true);
			middleLeft.draw(batch, x, y + patches[TOP_LEFT].getRegionHeight(), patches[MIDDLE_LEFT].getRegionWidth(), totalMiddleHeight);
			patches[MIDDLE_LEFT].flip(false, true);
			
			patches[MIDDLE_RIGHT].flip(false, true);
			middleRight.draw(batch, x + patches[MIDDLE_LEFT].getRegionWidth() + totalCenterWidth, y + patches[TOP_LEFT].getRegionHeight(), patches[MIDDLE_LEFT].getRegionWidth(), totalMiddleHeight);
			patches[MIDDLE_RIGHT].flip(false, true);
			
			//Render corners
			patches[TOP_LEFT].flip(false, true);
			batch.draw(patches[TOP_LEFT], x, y);
			patches[TOP_LEFT].flip(false, true);
			
			patches[TOP_RIGHT].flip(false, true);
			batch.draw(patches[TOP_RIGHT], x + patches[TOP_LEFT].getRegionWidth() + totalCenterWidth, y);
			patches[TOP_RIGHT].flip(false, true);
			
			patches[BOTTOM_LEFT].flip(false, true);
			batch.draw(patches[BOTTOM_LEFT], x, y + patches[TOP_LEFT].getRegionHeight() + totalMiddleHeight);
			patches[BOTTOM_LEFT].flip(false, true);
			
			patches[BOTTOM_RIGHT].flip(false, true);
			batch.draw(patches[BOTTOM_RIGHT], x + patches[TOP_LEFT].getRegionWidth() + totalCenterWidth, y + patches[TOP_LEFT].getRegionHeight() + totalMiddleHeight);
			patches[BOTTOM_RIGHT].flip(false, true);
		}
	}
}
