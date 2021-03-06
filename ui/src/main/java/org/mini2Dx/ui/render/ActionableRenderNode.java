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

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;

/**
 * Interface for {@link RenderNode}s that have actions
 */
public interface ActionableRenderNode extends HoverableRenderNode {
	
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams);
	
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams);
	
	public void mouseUp(int screenX, int screenY, int pointer, int button);
	
	public NodeState getState();
	
	public void setState(NodeState state);

	public boolean isEnabled();
}
