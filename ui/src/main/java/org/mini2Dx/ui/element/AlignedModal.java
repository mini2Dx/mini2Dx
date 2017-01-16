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
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.render.AlignedModalRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 * A {@link Modal} that can be auto-aligned using {@link VerticalAlignment} and
 * {@link HorizontalAlignment}
 */
public class AlignedModal extends Modal {
	@Field(optional = true)
	private VerticalAlignment verticalAlignment = VerticalAlignment.MIDDLE;
	@Field(optional = true)
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;

	/**
	 * Constructor. Generates a unique ID for this {@link AlignedModal}
	 */
	public AlignedModal() {
		this(null);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this {@link AlignedModal}
	 */
	public AlignedModal(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}
	
	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new AlignedModalRenderNode(parent, this);
	}

	/**
	 * Returns the {@link VerticalAlignment} of this {@link AlignedModal}
	 * @return {@link VerticalAlignment#MIDDLE} by default
	 */
	public VerticalAlignment getVerticalAlignment() {
		if (verticalAlignment == null) {
			verticalAlignment = VerticalAlignment.MIDDLE;
		}
		return verticalAlignment;
	}

	/**
	 * Sets the {@link VerticalAlignment} of this {@link AlignedModal}
	 * @param verticalAlignment
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	/**
	 * Returns the {@link HorizontalAlignment} of this {@link AlignedModal}
	 * @return {@link HorizontalAlignment#CENTER} by default
	 */
	public HorizontalAlignment getHorizontalAlignment() {
		if (horizontalAlignment == null) {
			horizontalAlignment = HorizontalAlignment.CENTER;
		}
		return horizontalAlignment;
	}

	/**
	 * Sets the {@link HorizontalAlignment} of this {@link AlignedModal}
	 * @param horizontalAlignment
	 */
	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
}
