package org.mini2Dx.core.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Implements an animation with frames of variable or fixed duration
 * 
 * @author Thomas Cashman
 */
public class Animation<T extends TextureRegion> {
	private List<T> frames;
	private List<Float> durations;
	private int currentFrame;
	private float elapsed;
	
	/**
	 * Constructor
	 */
	public Animation() {
		currentFrame = 0;
		frames = new ArrayList<T>();
		durations = new ArrayList<Float>();
		elapsed = 0f;
	}
	
	/**
	 * Adds a frame to the animation
	 * @param frame The frame to be added
	 * @param duration The duration of the frame in seconds
	 */
	public void addFrame(T frame, float duration) {
		durations.add(duration);
		frames.add(frame);
	}
	
	/**
	 * Updates the animation
	 * @param delta
	 */
	public void update(float delta) {
		elapsed += delta;
		if(elapsed > durations.get(currentFrame)) {
			elapsed = 0f;
			currentFrame = currentFrame == frames.size() - 1 ? 0 : currentFrame + 1;
		}
	}
	
	public void render(Graphics g, int x, int y) {
		g.drawTextureRegion(frames.get(currentFrame), x, y);
	}
	
	public int getNumberOfFrames() {
		return frames.size();
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
}
