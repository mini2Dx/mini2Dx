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
import org.mini2Dx.ui.render.ImageRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 */
public class Image extends UiElement {
	private ImageRenderNode renderNode;
	private TextureRegion textureRegion;
	private String path;
	private boolean responsive = false;
	
	public Image() {
		super(null);
	}
	
	public Image(String id) {
		super(id);
	}
	
	public Image(String id, String texturePath) {
		super(id);
		setTexturePath(texturePath);
	}
	
	public Image(Texture texture) {
		this(null, texture);
	}
	
	public Image(TextureRegion textureRegion) {
		this(null, textureRegion);
	}
	
	public Image(String id, Texture texture) {
		super(id);
		this.textureRegion = new TextureRegion(texture);
	}
	
	public Image(String id, TextureRegion textureRegion) {
		super(id);
		this.textureRegion = textureRegion;
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new ImageRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
	}
	
	public TextureRegion getTextureRegion(AssetManager assetManager) {
		if(path != null) {
			textureRegion = new TextureRegion(assetManager.get(path, Texture.class));
			path = null;
		}
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public void setTexture(Texture texture) {
		setTextureRegion(new TextureRegion(texture));
	}
	
	public void setTexturePath(String texturePath) {
		this.path = texturePath;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public boolean isResponsive() {
		return responsive;
	}

	public void setResponsive(boolean responsive) {
		this.responsive = responsive;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public void setVisibility(Visibility visibility) {
		if(visibility == null) {
			return;
		}
		if(this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	@Override
	public void syncWithRenderNode() {
		while(!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
	}

	@Override
	public void setStyleId(String styleId) {
		if(styleId == null) {
			return;
		}
		this.styleId = styleId;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
}
