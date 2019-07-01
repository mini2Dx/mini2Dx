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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.listener.NodeStateListener;
import org.mini2Dx.ui.render.NodeState;

/**
 * Utility implementation of {@link Button} that contains an {@link Image}
 */
public class ImageButton extends Button implements NodeStateListener {
	protected Image normalImage, hoverImage, actionImage, disabledImage;
	
	/**
	 * Constructor. Generates a unique ID for this {@link ImageButton}
	 */
	public ImageButton() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param id The unique ID for this {@link ImageButton}
	 */
	public ImageButton(@ConstructorArg(clazz=String.class, name = "id") String id) {
		this(id, 0f, 0f, 40f, 20f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ImageButton(@ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public ImageButton(@ConstructorArg(clazz = String.class, name = "id") String id,
				   @ConstructorArg(clazz = Float.class, name = "x") float x,
				   @ConstructorArg(clazz = Float.class, name = "y") float y,
				   @ConstructorArg(clazz = Float.class, name = "width") float width,
				   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
		addNodeStateListener(this);
	}
	
	private void checkInitialised() {
		if(normalImage != null) {
			return;
		}
		for(int i = 0; i < children.size; i++) {
			if(!(children.get(i) instanceof Image)) {
				continue;
			}
			final Image image = (Image) children.get(i);
			final String imageId = image.getId().toLowerCase();
			if(imageId.contains("hover")) {
				hoverImage = image;
			} else if(imageId.contains("action")) {
				actionImage = image;
			} else if(imageId.contains("disable")) {
				disabledImage = image;
			} else if(imageId.contains("normal")) {
				normalImage = image;
			} else if(normalImage == null) {
				normalImage = image;
			}
		}

		if(actionImage == null) {
			actionImage = new Image(getId() + "-actionImage");
			if(hoverImage != null) {
				actionImage.setAtlas(hoverImage.getAtlas());
				actionImage.setTexturePath(hoverImage.getTexturePath());
				actionImage.setResponsive(hoverImage.isResponsive());
			} else if(normalImage != null) {
				actionImage.setAtlas(normalImage.getAtlas());
				actionImage.setTexturePath(normalImage.getTexturePath());
				actionImage.setResponsive(normalImage.isResponsive());
			} else {
				actionImage.setResponsive(false);
			}
			add(actionImage);
		}
		if(hoverImage == null) {
			hoverImage = new Image(getId() + "-hoverImage");
			if(normalImage != null) {
				hoverImage.setAtlas(normalImage.getAtlas());
				hoverImage.setTexturePath(normalImage.getTexturePath());
				hoverImage.setResponsive(normalImage.isResponsive());
			} else {
				hoverImage.setResponsive(false);
			}
			add(hoverImage);
		}
		if(disabledImage == null) {
			disabledImage = new Image(getId() + "-disabledImage");
			if(normalImage != null) {
				disabledImage.setAtlas(normalImage.getAtlas());
				disabledImage.setTexturePath(normalImage.getTexturePath());
				disabledImage.setResponsive(normalImage.isResponsive());
			} else {
				disabledImage.setResponsive(false);
			}
			add(disabledImage);
		}
		if(normalImage == null) {
			normalImage = new Image(getId() + "-normalImage");
			normalImage.setResponsive(false);
			add(normalImage);
		}

		if(isEnabled()) {
			normalImage.setVisibility(Visibility.VISIBLE);
			hoverImage.setVisibility(Visibility.HIDDEN);
			actionImage.setVisibility(Visibility.HIDDEN);
			disabledImage.setVisibility(Visibility.HIDDEN);
		} else {
			normalImage.setVisibility(Visibility.HIDDEN);
			hoverImage.setVisibility(Visibility.HIDDEN);
			actionImage.setVisibility(Visibility.HIDDEN);
			disabledImage.setVisibility(Visibility.VISIBLE);
		}
	}
	
	/**
	 * Returns the current {@link TextureRegion} for this {@link ImageButton}
	 * 
	 * @param assetManager
	 *            The game's {@link AssetManager}
	 * @return Null if no {@link TextureRegion} has been set
	 */
	public TextureRegion getTextureRegion(AssetManager assetManager) {
		checkInitialised();
		if(isEnabled()) {
			switch(renderNode.getState()) {
			case HOVER:
				return hoverImage.getTextureRegion(assetManager);
			case ACTION:
				return actionImage.getTextureRegion(assetManager);
			default:
			case NORMAL:
				return normalImage.getTextureRegion(assetManager);
			}
		}
		return disabledImage.getTextureRegion(assetManager);
	}

	/**
	 * Sets the {@link TextureRegion} used by this {@link ImageButton} when in its normal state
	 * 
	 * @param textureRegion
	 *            The {@link TextureRegion} to use
	 */
	public void setNormalTextureRegion(TextureRegion textureRegion) {
		checkInitialised();
		normalImage.setTextureRegion(textureRegion);
	}
	
	/**
	 * Sets the {@link Texture} used by this {@link ImageButton} when in its normal state
	 * 
	 * @param texture
	 *            The {@link Texture} to use
	 */
	public void setNormalTexture(Texture texture) {
		setNormalTextureRegion(Mdx.graphics.newTextureRegion(texture));
	}
	
	/**
	 * Returns the texture path when button is in normal state
	 * 
	 * @return Null if no path is used
	 */
	public String getNormalTexturePath() {
		checkInitialised();
		return normalImage.getTexturePath();
	}
	
	/**
	 * Sets the texture path to use in normal state. This will set the
	 * {@link TextureRegion} by loading it via the {@link AssetManager}
	 * 
	 * @param texturePath
	 *            The path to the texture
	 */
	public void setNormalTexturePath(String texturePath) {
		checkInitialised();
		normalImage.setTexturePath(texturePath);
	}

	/**
	 * Sets the {@link TextureRegion} used by this {@link ImageButton} when in its hover state
	 *
	 * @param textureRegion
	 *            The {@link TextureRegion} to use
	 */
	public void setHoverTextureRegion(TextureRegion textureRegion) {
		checkInitialised();
		hoverImage.setTextureRegion(textureRegion);
	}

	/**
	 * Sets the {@link Texture} used by this {@link ImageButton} when in its hover state
	 *
	 * @param texture
	 *            The {@link Texture} to use
	 */
	public void setHoverTexture(Texture texture) {
		setHoverTextureRegion(Mdx.graphics.newTextureRegion(texture));
	}

	/**
	 * Returns the texture path when button is in hover state
	 *
	 * @return Null if no path is used
	 */
	public String getHoverTexturePath() {
		checkInitialised();
		return hoverImage.getTexturePath();
	}

	/**
	 * Sets the texture path to use in hover state. This will set the
	 * {@link TextureRegion} by loading it via the {@link AssetManager}
	 *
	 * @param texturePath
	 *            The path to the texture
	 */
	public void setHoverTexturePath(String texturePath) {
		checkInitialised();
		hoverImage.setTexturePath(texturePath);
	}

	/**
	 * Sets the {@link TextureRegion} used by this {@link ImageButton} when in its action state
	 *
	 * @param textureRegion
	 *            The {@link TextureRegion} to use
	 */
	public void setActionTextureRegion(TextureRegion textureRegion) {
		checkInitialised();
		actionImage.setTextureRegion(textureRegion);
	}

	/**
	 * Sets the {@link Texture} used by this {@link ImageButton} when in its action state
	 *
	 * @param texture
	 *            The {@link Texture} to use
	 */
	public void setActionTexture(Texture texture) {
		setActionTextureRegion(Mdx.graphics.newTextureRegion(texture));
	}

	/**
	 * Returns the texture path when button is in action state
	 *
	 * @return Null if no path is used
	 */
	public String getActionTexturePath() {
		checkInitialised();
		return actionImage.getTexturePath();
	}

	/**
	 * Sets the texture path to use in action state. This will set the
	 * {@link TextureRegion} by loading it via the {@link AssetManager}
	 *
	 * @param texturePath
	 *            The path to the texture
	 */
	public void setActionTexturePath(String texturePath) {
		checkInitialised();
		actionImage.setTexturePath(texturePath);
	}

	/**
	 * Sets the {@link TextureRegion} used by this {@link ImageButton} when in its disabled state
	 *
	 * @param textureRegion
	 *            The {@link TextureRegion} to use
	 */
	public void setDisabledTextureRegion(TextureRegion textureRegion) {
		checkInitialised();
		disabledImage.setTextureRegion(textureRegion);
	}

	/**
	 * Sets the {@link Texture} used by this {@link ImageButton} when in its disabled state
	 *
	 * @param texture
	 *            The {@link Texture} to use
	 */
	public void setDisabledTexture(Texture texture) {
		setDisabledTextureRegion(Mdx.graphics.newTextureRegion(texture));
	}

	/**
	 * Returns the texture path when button is in disabled state
	 *
	 * @return Null if no path is used
	 */
	public String getDisabledTexturePath() {
		checkInitialised();
		return disabledImage.getTexturePath();
	}

	/**
	 * Sets the texture path to use in disabled state. This will set the
	 * {@link TextureRegion} by loading it via the {@link AssetManager}
	 *
	 * @param texturePath
	 *            The path to the texture
	 */
	public void setDisabledTexturePath(String texturePath) {
		checkInitialised();
		disabledImage.setTexturePath(texturePath);
	}
	
	/**
	 * Returns the atlas (if any) to look up the texture in
	 * @return Null by default
	 */
	public String getAtlas() {
		checkInitialised();
		return normalImage.getAtlas();
	}

	/**
	 * Sets the atlas to look up the texture in
	 * @param atlas Null if the texture should not be looked up via an atlas
	 */
	public void setAtlas(String atlas) {
		checkInitialised();
		normalImage.setAtlas(atlas);
		hoverImage.setAtlas(atlas);
		actionImage.setAtlas(atlas);
		disabledImage.setAtlas(atlas);
	}

	/**
	 * Returns if the image should scale to the size of the {@link ImageButton}
	 * @return False by default
	 */
	public boolean isResponsive() {
		checkInitialised();
		return normalImage.isResponsive();
	}

	/**
	 * Sets if the image should scale to the size of the {@link ImageButton}
	 * @param responsive
	 */
	public void setResponsive(boolean responsive) {
		checkInitialised();
		normalImage.setResponsive(responsive);
		hoverImage.setResponsive(responsive);
		actionImage.setResponsive(responsive);
		disabledImage.setResponsive(responsive);
	}
	
	/**
	 * Returns the backing {@link Image} for the button in its default state
	 * @return
	 */
	public Image getNormalImage() {
		checkInitialised();
		return normalImage;
	}

	/**
	 * Returns the backing {@link Image} for the button in its hover state
	 * @return
	 */
	public Image getHoverImage() {
		checkInitialised();
		return hoverImage;
	}

	/**
	 * Returns the backing {@link Image} for the button in its action state
	 * @return
	 */
	public Image getActionImage() {
		checkInitialised();
		return actionImage;
	}

	/**
	 * Returns the backing {@link Image} for the button in its disabled state
	 * @return
	 */
	public Image getDisabledImage() {
		checkInitialised();
		return disabledImage;
	}

	@Override
	public void onNodeStateChanged(UiElement element, NodeState nodeState) {
		if(!element.getId().equals(getId())) {
			return;
		}
		checkInitialised();

		if(isEnabled()) {
			switch(nodeState) {
			case NORMAL:
				normalImage.setVisibility(Visibility.VISIBLE);
				hoverImage.setVisibility(Visibility.HIDDEN);
				actionImage.setVisibility(Visibility.HIDDEN);
				disabledImage.setVisibility(Visibility.HIDDEN);
				break;
			case HOVER:
				normalImage.setVisibility(Visibility.HIDDEN);
				hoverImage.setVisibility(Visibility.VISIBLE);
				actionImage.setVisibility(Visibility.HIDDEN);
				disabledImage.setVisibility(Visibility.HIDDEN);
				break;
			case ACTION:
				normalImage.setVisibility(Visibility.HIDDEN);
				hoverImage.setVisibility(Visibility.HIDDEN);
				actionImage.setVisibility(Visibility.VISIBLE);
				disabledImage.setVisibility(Visibility.HIDDEN);
				break;
			}
		} else {
			normalImage.setVisibility(Visibility.HIDDEN);
			hoverImage.setVisibility(Visibility.HIDDEN);
			actionImage.setVisibility(Visibility.HIDDEN);
			disabledImage.setVisibility(Visibility.VISIBLE);
		}
	}
}
