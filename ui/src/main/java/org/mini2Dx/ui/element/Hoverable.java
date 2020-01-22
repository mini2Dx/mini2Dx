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
package org.mini2Dx.ui.element;

import org.mini2Dx.ui.listener.HoverListener;

/**
 * Common interface for hoverable {@link UiElement}s
 */
public interface Hoverable {
	/**
	 * Returns the unique id
	 * @return
	 */
	public String getId();
	
	/**
	 * Adds a {@link HoverListener} to listen for hover events
	 * @param listener The {@link HoverListener} to add
	 */
	public void addHoverListener(HoverListener listener);
	
	/**
	 * Removes a {@link HoverListener} from this {@link Hoverable}
	 * @param listener The {@link HoverListener} to remove
	 */
	public void removeHoverListener(HoverListener listener);
	
	/**
	 * Notifies all {@link HoverListener}s of a begin hover event
	 */
	public void notifyHoverListenersOnBeginHover();
	
	/**
	 * Notifies all {@link HoverListener}s of an end hover event
	 */
	public void notifyHoverListenersOnEndHover();

	/**
	 * Triggers the begin hover event
	 */
	public void invokeBeginHover();

	/**
	 * Triggers the end hover event
	 */
	public void invokeEndHover();
	/*
	 * 
	 */
	public boolean isHoverEnabled();
	/*
	 * 
	 */
	public void setHoverEnabled(boolean hoverEnabled);
}
