/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.TabButtonLayoutRuleset;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TabButtonRenderNode;

/**
 *
 */
public class TabButton extends ContentButton {
	protected final Label label;
	protected final Image icon;
	
	private TabButtonLayoutRuleset tabButtonLayoutRuleset = TabButtonLayoutRuleset.DEFAULT_RULESET;
	private boolean currentTab = false;
	
	public TabButton() {
		this(null);
	}
	
	public TabButton(@ConstructorArg(clazz=String.class, name = "id") String id) {
		this(id, HorizontalAlignment.LEFT);
	}
	
	public TabButton(String id, HorizontalAlignment iconAlignment) {
		super(id);
		label = new Label(getId() + "-label");
		icon = new Image(getId() + "-icon");
		
		switch(iconAlignment) {
		case RIGHT:
			add(label);
			add(icon);
			break;
		default:
			add(icon);
			add(label);
			break;
		}
		setVisibility(Visibility.VISIBLE);
	}
	
	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new TabButtonRenderNode(parentRenderNode, this);
		for(int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}
	
	void setText(String text) {
		label.setText(text);
		if(text == null) {
			label.setVisibility(Visibility.HIDDEN);
		} else {
			label.setVisibility(Visibility.VISIBLE);
		}
	}
	
	void setIconPath(String imagePath) {
		icon.setTexturePath(imagePath);
		if(imagePath == null) {
			icon.setVisibility(Visibility.HIDDEN);
		} else {
			icon.setVisibility(Visibility.VISIBLE);
		}
	}
	
	void setLabelStyle(String styleId) {
		label.setStyleId(styleId);
	}
	
	void setIconStyle(String styleId) {
		icon.setStyleId(styleId);
	}

	public boolean isCurrentTab() {
		return currentTab;
	}

	void setCurrentTab(boolean currentTab) {
		this.currentTab = currentTab;
	}
	
	int getCurrentSizeInColumns() {
		return tabButtonLayoutRuleset.getCurrentSizeInColumns();
	}
	
	@Override
	public void setLayout(LayoutRuleset layoutRuleset) {
		throw new MdxException("setLayout(LayoutRuleset) not supported by TabButton");
	}
	
	public void setLayout(TabButtonLayoutRuleset layoutRuleset) {
		this.tabButtonLayoutRuleset = layoutRuleset;
		super.setLayout(layoutRuleset);
	}
}
