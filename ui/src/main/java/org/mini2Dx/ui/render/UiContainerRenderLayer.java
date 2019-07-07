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
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;

/**
 * {@link RenderLayer} implementation for {@link UiContainer}
 */
public class UiContainerRenderLayer extends RenderLayer {

	public UiContainerRenderLayer(ParentRenderNode<?, ?> owner, int zIndex) {
		super(owner, zIndex);
	}

	@Override
	public void layout(LayoutState layoutState, LayoutRuleset layoutRuleset) {
		for (int i = 0; i < children.size; i++) {
			RenderNode<?, ?> node = children.get(i);
			node.layout(layoutState);
			
			if(!node.isIncludedInLayout()) {
				continue;
			}
			
			node.setRelativeX(node.getXOffset());
			node.setRelativeY(node.getYOffset());
			LayoutRuleset.setElementSize(owner, node);
		}
	}
}
