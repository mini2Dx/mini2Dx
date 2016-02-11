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
package com.artemis.system;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.artemis.EntitySystem;
import com.artemis.MdxWorld;
import com.artemis.World;
import com.artemis.utils.IntBag;

/**
 * Implements {@link EntitySystem} to add mini2Dx's update/interpolate methods
 */
public abstract class InterpolatingEntitySystem extends EntitySystem {
	private MdxWorld mdxWorld;
	
	private IntBag activeEntityBag;
	private int[] activeEntityIds;

	/**
	 * Creates a new InterpolatingEntitySystem
	 * @param aspect The {@link Aspect} to match entities
	 */
	public InterpolatingEntitySystem(Builder aspect) {
		super(aspect);
	}
	
	protected abstract void update(int entityId, float delta);
	
	protected abstract void interpolate(int entityId, float alpha);

	@Override
	protected void processSystem() {
		activeEntityBag = subscription.getEntities();
		activeEntityIds = activeEntityBag.getData();
		for (int i = 0, s = activeEntityBag.size(); s > i; i++) {
			update(activeEntityIds[i], world.delta);
		}
	}
	
	public void interpolateSystem() {
		if(mdxWorld == null) {
			return;
		}
		if(activeEntityBag == null) {
			return;
		}
		
		for (int i = 0, s = activeEntityBag.size(); s > i; i++) {
			interpolate(activeEntityIds[i], mdxWorld.alpha);
		}
	}
	
	@Override
	public void setWorld(World world) {
		super.setWorld(world);

		if(world instanceof MdxWorld) {
			this.mdxWorld = (MdxWorld) world;
		}
	}
}
