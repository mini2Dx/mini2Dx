package org.mini2Dx.ui.listener;

import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.NodeState;

public interface NodeStateListener {

	public void onNodeStateChanged(UiElement element, NodeState nodeState);
}
