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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureAtlasRegion;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.AnimatedImageRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 *
 */
public class AnimatedImage extends UiElement {
	private static final String LOGGING_TAG = AnimatedImage.class.getSimpleName();

	protected AnimatedImageRenderNode renderNode;
	private TextureRegion[] textureRegions;

	@Field
	private String[] texturePaths;
	@Field
	private float[] frameDurations;
	@Field(optional = true)
	private String atlas;
	@Field(optional = true)
	private boolean responsive = false;
	@Field(optional = true)
	private boolean flipX = false;
	@Field(optional = true)
	private boolean flipY = false;

	private String[] cachedTexturePaths;

	/**
	 * Constructor. Generates a unique ID for this {@link AnimatedImage}
	 */
	public AnimatedImage() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link AnimatedImage}
	 */
	public AnimatedImage(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 1f, 1f);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public AnimatedImage(@ConstructorArg(clazz = String.class, name = "id") String id,
				 @ConstructorArg(clazz = Float.class, name = "x") float x,
				 @ConstructorArg(clazz = Float.class, name = "y") float y,
				 @ConstructorArg(clazz = Float.class, name = "width") float width,
				 @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link AnimatedImage}
	 * @param texturePaths
	 *            The paths for the textures to be loaded by the
	 *            {@link AssetManager}
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public AnimatedImage(String id, String[] texturePaths, float[] durations) {
		super(id);
		setTexturePaths(texturePaths, durations);
	}

	/**
	 * Constructor. Generates a unique ID for this {@link AnimatedImage}
	 * 
	 * @param textures
	 *            The {@link Texture}s to use for each frame
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public AnimatedImage(Texture[] textures, float[] durations) {
		this(null, textures, durations);
	}

	/**
	 * Constructor. Generates a unique ID for this {@link AnimatedImage}
	 * 
	 * @param textureRegions
	 *            The {@link TextureRegion}s to use for each frame
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public AnimatedImage(TextureRegion[] textureRegions, float[] durations) {
		this(null, textureRegions, durations);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link AnimatedImage}
	 * @param textures
	 *            The {@link Texture}s to use for each frame
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public AnimatedImage(String id, Texture[] textures, float[] durations) {
		super(id);
		setTextures(textures, durations);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link AnimatedImage}
	 * @param textureRegions
	 *            The {@link TextureRegion}s to use for each frame
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public AnimatedImage(String id, TextureRegion[] textureRegions, float[] durations) {
		super(id);
		this.textureRegions = textureRegions;
		this.frameDurations = durations;
	}

	@PostDeserialize
	public void postDeserialize() {
		this.cachedTexturePaths = texturePaths;

		if (texturePaths != null) {
			if (texturePaths.length != frameDurations.length) {
				throw new MdxException(
						"Frame duration array must be same length as texture paths array for " + LOGGING_TAG);
			}
		}
	}

	protected void estimateSize() {
		if(renderNode != null) {
			return;
		}
		if(!UiContainer.isThemeApplied()) {
			return;
		}
		final StyleRule styleRule = UiContainer.getTheme().getImageStyleRule(styleId, ScreenSize.XS);
		if(textureRegions != null && textureRegions.length > 0) {
			width = textureRegions[0].getRegionWidth() + styleRule.getMarginLeft() + styleRule.getMarginRight() + styleRule.getPaddingLeft() + styleRule.getPaddingRight();
			height = textureRegions[0].getRegionHeight() + styleRule.getMarginTop() + styleRule.getMarginBottom() + styleRule.getPaddingTop() + styleRule.getPaddingBottom();
		}
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new AnimatedImageRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
		renderNode.dispose();
		renderNode = null;
	}

	@Override
	public void invokeBeginHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.beginFakeHover();
	}

	@Override
	public void invokeEndHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.endFakeHover();
	}

	/**
	 * Returns the current {@link TextureRegion} array for this
	 * {@link AnimatedImage}
	 * 
	 * @param assetManager
	 *            The game's {@link AssetManager}
	 * @return Null if no animation has been set
	 */
	public TextureRegion[] getTextureRegions(AssetManager assetManager) {
		if (atlas != null) {
			if (texturePaths != null) {
				TextureAtlas textureAtlas = assetManager.get(atlas, TextureAtlas.class);
				if (textureAtlas == null) {
					Mdx.log.error(LOGGING_TAG, "Could not find texture atlas " + atlas);
					return null;
				}

				textureRegions = new TextureRegion[texturePaths.length];
				for (int i = 0; i < texturePaths.length; i++) {
					TextureAtlasRegion atlasRegion = textureAtlas.findRegion(texturePaths[i]);
					if (atlasRegion == null) {
						Mdx.log.error(LOGGING_TAG, "Could not find " + texturePaths[i] + " in texture atlas " + atlas);
						return null;
					}
					textureRegions[i] = Mdx.graphics.newTextureRegion(atlasRegion);
				}
				cachedTexturePaths = texturePaths;
				texturePaths = null;
			}
		} else if (texturePaths != null) {
			textureRegions = new TextureRegion[texturePaths.length];
			for (int i = 0; i < texturePaths.length; i++) {
				textureRegions[i] = Mdx.graphics.newTextureRegion(assetManager.get(texturePaths[i], Texture.class));
			}
			cachedTexturePaths = texturePaths;
			texturePaths = null;
		}
		return textureRegions;
	}

	/**
	 * Sets the current {@link TextureRegion} array used by this
	 * {@link AnimatedImage}
	 * 
	 * @param textureRegions
	 *            The {@link TextureRegion}s to use for the animation
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public void setTextureRegions(TextureRegion[] textureRegions, float[] durations) {
		this.textureRegions = textureRegions;
		this.frameDurations = durations;
		estimateSize();

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Sets the current {@link Texture} array used by this {@link AnimatedImage}
	 * 
	 * @param textures
	 *            The {@link Texture}s to use for the animation
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public void setTextures(Texture[] textures, float[] durations) {
		if (textures == null) {
			setTextureRegions(null, null);
		} else {
			TextureRegion[] regions = new TextureRegion[textures.length];
			for (int i = 0; i < textures.length; i++) {
				regions[i] = Mdx.graphics.newTextureRegion(textures[i]);
			}
			setTextureRegions(regions, durations);
			estimateSize();
		}
	}

	/**
	 * Returns the texture paths for the animation
	 * 
	 * @return Null if no paths are used
	 */
	public String[] getTexturePaths() {
		return cachedTexturePaths;
	}

	/**
	 * Returns the animation frame durations
	 * 
	 * @return Null if no durations have been set
	 */
	public float[] getFrameDurations() {
		return frameDurations;
	}

	/**
	 * Sets the current texture path array. This will set the current
	 * {@link TextureRegion} array by loading it via the {@link AssetManager}
	 * 
	 * @param texturePaths
	 *            The paths to the textures for the animation
	 * @param durations
	 *            The durations for each frame (in seconds)
	 */
	public void setTexturePaths(String[] texturePaths, float[] durations) {
		this.cachedTexturePaths = texturePaths;
		this.texturePaths = texturePaths;
		this.frameDurations = durations;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Returns the atlas (if any) to look up the texture in
	 * 
	 * @return Null by default
	 */
	public String getAtlas() {
		return atlas;
	}

	/**
	 * Sets the atlas to look up the texture in
	 * 
	 * @param atlas
	 *            Null if the texture should not be looked up via an atlas
	 */
	public void setAtlas(String atlas) {
		if (this.atlas != null && this.atlas.equals(atlas)) {
			return;
		}

		this.atlas = atlas;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	/**
	 * Returns if this {@link AnimatedImage} should scale to the size of its
	 * parent
	 * 
	 * @return False by default
	 */
	public boolean isResponsive() {
		return responsive;
	}

	/**
	 * Sets if this {@link AnimatedImage} should scale to the size of its parent
	 * 
	 * @param responsive
	 */
	public void setResponsive(boolean responsive) {
		this.responsive = responsive;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setVisibility(Visibility visibility) {
		if (visibility == null) {
			return;
		}
		if (this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		while (effects.size > 0) {
			renderNode.applyEffect(effects.removeFirst());
		}

		super.syncWithUpdate(rootNode);
	}

	@Override
	public void setStyleId(String styleId) {
		if (styleId == null) {
			return;
		}
		if (this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;
		estimateSize();

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getImageStyleRule(styleId, ScreenSize.XS);
	}

	/**
	 * Returns if the texture should be flipped horizontally during rendering
	 * 
	 * @return
	 */
	public boolean isFlipX() {
		return flipX;
	}

	/**
	 * Sets if the texture should be flipped horizontally during rendering
	 * 
	 * @param flipX
	 *            True if the texture should be flipped
	 */
	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	/**
	 * Returns if the texture should be flipped vertically during rendering
	 * 
	 * @return
	 */
	public boolean isFlipY() {
		return flipY;
	}

	/**
	 * Sets if the texture should be flipped vertically during rendering
	 * 
	 * @param flipY
	 *            True if the texture should be flipped
	 */
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}

	@Override
	public boolean isRenderNodeDirty() {
		if (renderNode == null) {
			return true;
		}
		return renderNode.isDirty();
	}

	@Override
	public void setRenderNodeDirty() {
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public boolean isInitialLayoutOccurred() {
		if (renderNode == null) {
			return false;
		}
		return renderNode.isInitialLayoutOccurred();
	}

	@Override
	public boolean isInitialUpdateOccurred() {
		if(renderNode == null) {
			return false;
		}
		return renderNode.isInitialUpdateOccurred();
	}

	@Override
	public int getRenderX() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderX();
	}

	@Override
	public int getRenderY() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderY();
	}

	@Override
	public int getRenderWidth() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderWidth();
	}

	@Override
	public int getRenderHeight() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderHeight();
	}
}