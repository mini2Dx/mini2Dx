package org.mini2Dx.core.graphics;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Implements an animation with frames of variable or fixed duration
 * 
 * @author Thomas Cashman
 */
public class Animation {
	private List<Texture> frameTextures;
	private List<TextureRegion> frameTextureRegions;
	private int currentFrame;
	
	public Animation() {
		currentFrame = 0;
	}
	
	public void update(float delta) {
		
	}
	
	public void render(Graphics g, int x, int y) {
		
	}
	
	public int getNumberOfFrames() {
		if(frameTextures == null && frameTextureRegions == null) {
			return 0;
		}
		
		if(frameTextures != null) {
			return frameTextures.size();
		} else {
			return frameTextureRegions.size();
		}
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
}
