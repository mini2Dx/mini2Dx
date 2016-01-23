/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.element;

import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.VerticalAlignment;
import org.mini2Dx.ui.render.AlignedModalRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 *
 */
public class AlignedModal extends Modal {
	private VerticalAlignment verticalAlignment = VerticalAlignment.MIDDLE;
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
	
	public AlignedModal() {
		this(null);
	}
	
	public AlignedModal(String id) {
		super(id);
	}
	
	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new AlignedModalRenderNode(parentRenderNode, this);
		for(int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
}
