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
package org.mini2Dx.ui.dummy;

import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.style.ParentStyleRule;

/**
 *
 */
public class DummyParentUiElement extends ParentUiElement {
	private float preferredContentWidth, preferredContentHeight;
	private ParentStyleRule styleRule = new ParentStyleRule();
	
	private DummyRenderNode renderNode;
	
	public DummyParentUiElement() {
		super();
		setVisibility(Visibility.VISIBLE);
	}
	
	public DummyParentUiElement(String id) {
		super(id);
		setVisibility(Visibility.VISIBLE);
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new DummyParentRenderNode(parent, this);
	}

	public ParentStyleRule getStyleRule() {
		return styleRule;
	}

	public void setStyleRule(ParentStyleRule styleRule) {
		this.styleRule = styleRule;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	public float getPreferredContentWidth() {
		return preferredContentWidth;
	}

	public void setPreferredContentWidth(float preferredContentWidth) {
		this.preferredContentWidth = preferredContentWidth;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}
	
	public float getPreferredContentHeight() {
		return preferredContentHeight;
	}

	public void setPreferredContentHeight(float preferredContentHeight) {
		this.preferredContentHeight = preferredContentHeight;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}
}
