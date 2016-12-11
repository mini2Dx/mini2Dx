/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.animation;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.ui.listener.TextAnimationListener;

/**
 * A base class for {@link TextAnimation} implementations
 */
public abstract class BaseTextAnimation implements TextAnimation {
	private List<TextAnimationListener> listeners;

	private boolean finished;
	
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
		if (this.finished) {
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
			listeners = new ArrayList<TextAnimationListener>(1);
		}
		listeners.add(listener);
	}

	@Override
	public void removeTextAnimationListener(TextAnimationListener listener) {
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}

	private void notifyTextAnimationListeners() {
		if (listeners == null) {
			return;
		}
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onAnimationFinished(this);
		}
	}
}
