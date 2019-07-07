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
package org.mini2Dx.ui.animation;

import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.RenderNode;

/**
 * Internal class for {@link ScrollBox} scroll to element operations
 */
public class ScrollTo {
	private final UiElement targetElement;
	private final boolean immediate;

	private RenderNode<?, ?> targetRenderNode;
	
	public ScrollTo(UiElement targetElement, boolean immediate) {
		this.targetElement = targetElement;
		this.immediate = immediate;
	}
	
	public UiElement getTargetElement() {
		return targetElement;
	}

	public boolean isImmediate() {
		return immediate;
	}

	public RenderNode<?, ?> getTargetRenderNode() {
		return targetRenderNode;
	}

	public void setTargetRenderNode(RenderNode<?, ?> targetRenderNode) {
		this.targetRenderNode = targetRenderNode;
	}
}
