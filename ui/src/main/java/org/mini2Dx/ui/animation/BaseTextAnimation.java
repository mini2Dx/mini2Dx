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
package org.mini2Dx.ui.animation;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.listener.TextAnimationListener;

/**
 * A base class for {@link TextAnimation} implementations
 */
public abstract class BaseTextAnimation implements TextAnimation {
	private Array<TextAnimationListener> listeners;

	protected boolean finished;
	
	protected abstract void resetState();

	@Override
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Resets the finished state to false
	 */
	public void reset() {
		resetState();
		finished = false;
	}

	/**
	 * Sets if this {@link TextAnimation} has finished. Will notify all
	 * {@link TextAnimationListener}s when true.
	 * 
	 * @param finished
	 *            True if finished
	 */
	protected void setFinished(boolean finished) {
		if (this.finished == finished) {
			return;
		}
		this.finished = finished;
		if (finished) {
			notifyTextAnimationListeners();
		}
	}

	@Override
	public void addTextAnimationListener(TextAnimationListener listener) {
		if (listeners == null) {
			listeners = new Array<TextAnimationListener>(true, 1, TextAnimationListener.class);
		}
		listeners.add(listener);
	}

	@Override
	public void removeTextAnimationListener(TextAnimationListener listener) {
		if (listeners == null) {
			return;
		}
		listeners.removeValue(listener, false);
	}

	private void notifyTextAnimationListeners() {
		if (listeners == null) {
			return;
		}
		for (int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onAnimationFinished(this);
		}
	}
}
