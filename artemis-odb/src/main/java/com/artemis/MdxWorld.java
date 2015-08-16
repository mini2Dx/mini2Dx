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
package com.artemis;

import org.mini2Dx.core.graphics.Graphics;

import com.artemis.system.InterpolatingEntitySystem;
import com.artemis.system.RenderingEntitySystem;
import com.artemis.utils.Bag;

/**
 * Extends {@link World} to allow for interpolating and rendering of {@link System}s
 */
public class MdxWorld extends World {
	private final Bag<InterpolatingEntitySystem> interpolatingSystemsBag;
	private final Bag<RenderingEntitySystem> renderingSystemsBag;
	
	private final MdxInvocationStrategy mdxInvocationStrategy;
	
	public float alpha;
	
	/**
	 * Creates a new world
	 * 
	 * @param configuration The configuration to be applied
	 */
	public MdxWorld(WorldConfiguration configuration) {
		super(configuration);
		interpolatingSystemsBag = new Bag<InterpolatingEntitySystem>();
		renderingSystemsBag = new Bag<RenderingEntitySystem>();
		mdxInvocationStrategy = new MdxInvocationStrategy();
		
		for(BaseSystem system : configuration.systems) {
			if(system instanceof InterpolatingEntitySystem) {
				interpolatingSystemsBag.add((InterpolatingEntitySystem) system);
			}
			if(system instanceof RenderingEntitySystem) {
				renderingSystemsBag.add((RenderingEntitySystem)system);
			}
		}
		
		setInvocationStrategy(mdxInvocationStrategy);
	}
	
	/**
	 * Invokes interpolate on all {@link InterpolatingEntitySystem}s
	 */
	public void interpolate() {
		mdxInvocationStrategy.interpolate(interpolatingSystemsBag);
	}
	
	/**
	 * Invokes render on all {@link RenderingEntitySystem}s
	 * @param g The {@link Graphics}s instance
	 */
	public void render(Graphics g) {
		mdxInvocationStrategy.render(renderingSystemsBag, g);
	}

	/**
	 * Returns the interpolation alpha
	 * @return
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * Sets the interpolation alpha
	 * @param alpha
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
}
