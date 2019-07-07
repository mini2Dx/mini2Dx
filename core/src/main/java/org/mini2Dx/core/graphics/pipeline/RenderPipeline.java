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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.gdx.utils.Array;

/**
 * Stores a sequences of {@link RenderOperation}s to be applied and (optionally) unapplied.
 * 
 * By default this will go from 0 - length and apply the operations, then unapply the operations
 * in reverse order.
 * 
 * If you do not require the operations to be unapplied, {@link #setOneWay(boolean)} to true.
 */
public class RenderPipeline {
	private Array<RenderOperation> operations;
	private boolean oneWay;
	
	public RenderPipeline() {
		operations = new Array<RenderOperation>(true, 2);
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
		
		for(int i = operations.size - 1; i >= 0; i--) {
			operations.get(i).unapply(gc, g);
		}
	}
	
	public void add(RenderOperation operation) {
		operations.add(operation);
	}
	
	public void remove(RenderOperation operation) {
		operations.removeValue(operation, true);
	}

	public boolean isOneWay() {
		return oneWay;
	}

	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}
}
