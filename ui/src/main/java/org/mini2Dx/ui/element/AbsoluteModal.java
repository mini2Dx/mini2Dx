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

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.render.AbsoluteModalRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 * A {@link Modal} that can be positioned manually
 */
public class AbsoluteModal extends Modal {
	@Field(optional=true)
	private float x;
	@Field(optional=true)
	private float y;
	
	/**
	 * Constructor. Generates a unique ID for this {@link Modal}
	 */
	public AbsoluteModal() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param id The unique ID of the {@link AbsoluteModal}
	 */
	public AbsoluteModal(@ConstructorArg(clazz=String.class, name = "id") String id) {
		super(id);
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new AbsoluteModalRenderNode(parent, this);
	}
	
	/**
	 * Returns the x coordinate of this {@link AbsoluteModal}
	 * @return 0 by default
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of this {@link AbsoluteModal}
	 * @param x The x coordinate in pixels
	 */
	public void setX(float x) {
		if(this.x == x) {
			return;
		}
		this.x = x;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Returns the y coordinate of this {@link AbsoluteModal}
	 * @return 0 by default
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of this {@link AbsoluteModal}
	 * @param y The y coordinate in pixels
	 */
	public void setY(float y) {
		if(this.y == y) {
			return;
		}
		this.y = y;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	/**
	 * Sets the x and y coordinate of this {@link AbsoluteModal}
	 * @param x The x coordinate in pixels
	 * @param y The y coordinate in pixels
	 */
	public void set(float x, float y) {
		if(this.x == x && this.y == y) {
			return;
		}
		this.x = x;
		this.y = y;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	/**
	 * Returns the width of the {@link AbsoluteModal} as determined by its
	 * {@link LayoutRuleset}
	 * 
	 * @return
	 */
	public float getWidth() {
		if(renderNode == null) {
			return 0f;
		}
		return renderNode.getOuterWidth();
	}
	
	/**
	 * Returns the height of the {@link AbsoluteModal} as determined by its
	 * child {@link UiElement}s
	 * 
	 * @return 0 is no elements have been added
	 */
	public float getHeight() {
		if(renderNode == null) {
			return 0f;
		}
		return renderNode.getOuterHeight();
	}
}
