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

import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Utility implementation of {@link Button} that contains an {@link Image}
 */
public class ImageButton extends Button {	
	protected Image image;
	
	/**
	 * Constructor. Generates a unique ID for this {@link ImageButton}
	 */
	public ImageButton() {
		super();
	}
	
	/**
	 * Constructor
	 * @param id The unique ID for this {@link ImageButton}
	 */
	public ImageButton(@ConstructorArg(clazz=String.class, name = "id") String id) {
		super(id);
	}
	
	private void checkInitialised() {
		if(image != null) {
			return;
		}
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i) instanceof Image) {
				image = (Image) children.get(i);
				return;
			}
		}
		
		image = new Image(getId() + "-backingImage");
		image.setResponsive(false);
		image.setVisibility(Visibility.VISIBLE);
		add(image);
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
		return image.getTextureRegion(assetManager);
	}

	/**
	 * Sets the current {@link TextureRegion} used by this {@link ImageButton}
	 * 
	 * @param textureRegion
	 *            The {@link TextureRegion} to use
	 */
	public void setTextureRegion(TextureRegion textureRegion) {
		checkInitialised();
		image.setTextureRegion(textureRegion);
	}
	
	/**
	 * Sets the current {@link Texture} used by this {@link ImageButton}
	 * 
	 * @param texture
	 *            The {@link Texture} to use
	 */
	public void setTexture(Texture texture) {
		setTextureRegion(new TextureRegion(texture));
	}
	
	/**
	 * Returns the current texture path
	 * 
	 * @return Null if no path is used
	 */
	public String getTexturePath() {
		checkInitialised();
		return image.getTexturePath();
	}
	
	/**
	 * Sets the current texture path. This will set the current
	 * {@link TextureRegion} by loading it via the {@link AssetManager}
	 * 
	 * @param texturePath
	 *            The path to the texture
	 */
	public void setTexturePath(String texturePath) {
		checkInitialised();
		image.setTexturePath(texturePath);
	}
	
	/**
	 * Returns the atlas (if any) to look up the texture in
	 * @return Null by default
	 */
	public String getAtlas() {
		checkInitialised();
		return image.getAtlas();
	}

	/**
	 * Sets the atlas to look up the texture in
	 * @param atlas Null if the texture should not be looked up via an atlas
	 */
	public void setAtlas(String atlas) {
		checkInitialised();
		image.setAtlas(atlas);
	}

	/**
	 * Returns if the image should scale to the size of the {@link ImageButton}
	 * @return False by default
	 */
	public boolean isResponsive() {
		checkInitialised();
		return image.isResponsive();
	}

	/**
	 * Sets if the image should scale to the size of the {@link ImageButton}
	 * @param responsive
	 */
	public void setResponsive(boolean responsive) {
		checkInitialised();
		image.setResponsive(responsive);
	}
	
	/**
	 * Returns the backing {@link Image} for the button
	 * @return
	 */
	public Image getImage() {
		checkInitialised();
		return image;
	}
}
