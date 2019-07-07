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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.ui.element.Div;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ParentStyleRule;

/**
 * {@link RenderNode} implementation for {@link Div}
 */
public class DivRenderNode extends ParentRenderNode<Div, ParentStyleRule> {

	public DivRenderNode(ParentRenderNode<?, ?> parent, Div div) {
		super(parent, div);
	}

	@Override
	protected ParentStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}
}
