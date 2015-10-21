/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;

/**
 * Stores a sequences of {@link RenderOperation}s to be applied and (optionally) unapplied.
 * 
 * By default this will go from 0 - length and apply the operations, then unapply the operations
 * in reverse order.
 * 
 * If you do not require the operations to be unapplied, {@link #setOneWay(boolean)} to true.
 */
public class RenderPipeline {
	private List<RenderOperation> operations;
	private boolean oneWay;
	
	public RenderPipeline() {
		operations = new ArrayList<RenderOperation>();
		oneWay = false;
	}
	
	public void update(GameContainer gc, float delta) {
		for(RenderOperation stage : operations) {
			stage.update(gc, delta);
		}
	}
	
	public void interpolate(GameContainer gc, float alpha) {
		for(RenderOperation stage : operations) {
			stage.interpolate(gc, alpha);
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		for(RenderOperation stage : operations) {
			stage.apply(gc, g);
		}
		
		if(oneWay) {
			return;
		}
		
		for(int i = operations.size() - 1; i >= 0; i--) {
			operations.get(i).unapply(gc, g);
		}
	}
	
	public void add(RenderOperation stage) {
		operations.add(stage);
	}
	
	public void remove(RenderOperation stage) {
		operations.remove(stage);
	}

	public boolean isOneWay() {
		return oneWay;
	}

	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}
}
