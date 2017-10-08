/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.ResponsiveSizeRule;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TabButtonRenderNode;

/**
 * Represents the button associated with each {@link Tab}
 */
public class TabButton extends Button {
	protected final Label label;
	protected final Image icon;
	
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
	
	public void setText(String text) {
		label.setText(text);
		if(text == null) {
			label.setVisibility(Visibility.HIDDEN);
		} else {
			label.setVisibility(Visibility.VISIBLE);
		}
	}
	
	public void setIconPath(String imagePath) {
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
		if(renderNode == null) {
			return 0;
		}
		if(renderNode.getHorizontalLayoutRuleset() == null) {
			return 0;
		}
		if(renderNode.getHorizontalLayoutRuleset().getCurrentSizeRule() == null) {
			return 0;
		}
		return ((ResponsiveSizeRule) renderNode.getHorizontalLayoutRuleset().getCurrentSizeRule()).getColumns();
	}
	
	@Override
	public void setHorizontalLayout(String layout) {
		if(layout.contains("px")) {
			throw new MdxException("Tab buttons do not support pixel sizes, please use columns");
		}
		super.setHorizontalLayout(layout);
	}

	public Label getLabel() {
		return label;
	}

	public Image getIcon() {
		return icon;
	}
}
