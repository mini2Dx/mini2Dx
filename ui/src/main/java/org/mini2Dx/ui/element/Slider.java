/**
 * Copyright (c) 2017 See AUTHORS file
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.SliderRenderNode;

/**
 * A slider UI element that wraps a float value
 */
public class Slider extends UiElement implements Actionable {
	@Field(optional = true)
	private float value;
	@Field(optional = true)
	private float valueStep = 0.1f;
	@Field(optional = true)
	private boolean changedOnBeginEvent = false;
	@Field(optional = true)
	private boolean enabled = true;

	private SliderRenderNode renderNode;
	private List<ActionListener> actionListeners;

	/**
	 * Constructor. Generates a unique ID for this {@link Slider}
	 */
	public Slider() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link Slider}
	 */
	public Slider(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new SliderRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
	}

	@Override
	public void setVisibility(Visibility visibility) {
		if (this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void setStyleId(String styleId) {
		if (styleId == null) {
			return;
		}
		if (this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void defer(Runnable runnable) {
		deferredQueue.offer(runnable);
	}
	
	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
		while (!deferredQueue.isEmpty()) {
			deferredQueue.poll().run();
		}
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void notifyActionListenersOfBeginEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if (actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for (int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionBegin(event);
		}
		ActionEventPool.release(event);
	}

	@Override
	public void notifyActionListenersOfEndEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if (actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for (int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionEnd(event);
		}
		ActionEventPool.release(event);
	}

	@Override
	public void addActionListener(ActionListener listener) {
		if (actionListeners == null) {
			actionListeners = new ArrayList<ActionListener>(1);
		}
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		if (actionListeners == null) {
			return;
		}
		actionListeners.remove(listener);
	}

	/**
	 * Returns the {@link Slider} value
	 * 
	 * @return A value between 0.0 and 1.0
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Sets the {@link Slider} value.
	 * 
	 * @param value
	 *            A value between 0.0 and 1.0
	 */
	public void setValue(float value) {
		if (value < 0f) {
			this.value = 0f;
		} else if (value > 1f) {
			this.value = 1f;
		} else {
			this.value = value;
		}
	}

	/**
	 * Returns the amount to change the value by on each keyboard/controller
	 * event
	 * 
	 * @return 0.1 by default
	 */
	public float getValueStep() {
		return valueStep;
	}

	/**
	 * Sets the amount to change the value by on each keyboard/controller event
	 * 
	 * @param valueStep
	 *            A non-zero value
	 */
	public void setValueStep(float valueStep) {
		if (valueStep == 0f) {
			return;
		}
		this.valueStep = Math.abs(valueStep);
	}

	/**
	 * Returns if the value should change when a keyboard/controller event
	 * begins and change the value until the event ends.
	 * 
	 * @return False by default
	 */
	public boolean isChangedOnBeginEvent() {
		return changedOnBeginEvent;
	}

	/**
	 * Sets if the value should change when a keyboard/controller event
	 * begins and change the value until the event ends.
	 * 
	 * @param changedOnBeginEvent False to only change on the end event
	 */
	public void setChangedOnBeginEvent(boolean changedOnBeginEvent) {
		this.changedOnBeginEvent = changedOnBeginEvent;
	}
}
