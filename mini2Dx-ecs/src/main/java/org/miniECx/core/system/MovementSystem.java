/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.miniECx.core.system;

import java.util.List;

import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.quadtree.RegionalQuadTree;
import org.miniECx.core.component.Body;
import org.miniECx.core.component.CollisionListener;
import org.miniECx.core.component.Component;
import org.miniECx.core.entity.Entity;
import org.miniECx.core.entity.EntityListener;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author Thomas Cashman
 */
public class MovementSystem extends AbstractSystem implements EntityListener {
	private RegionalQuadTree<Body> bodies;
	
	public MovementSystem(float gameAreaWidth, float gameAreaHeight) {
		this(20, gameAreaWidth, gameAreaHeight);
	}
	
	public MovementSystem(int entitiesPerCollisionRegion, float gameAreaWidth, float gameAreaHeight) {
		super();
		bodies = new RegionalQuadTree<Body>(entitiesPerCollisionRegion, gameAreaWidth, gameAreaHeight);
	}

	@Override
	public void update(Entity entity, float delta) {
		List<Body> entityBodies = entity.getComponents(Body.class);
		List<CollisionListener> collisionListeners = entity.getComponents(CollisionListener.class);

		for (Body body : entityBodies) {
			Vector2 netVelocity = body.calculateNetVelocity();
			LineSegment line = new LineSegment(body.getX(), body.getY(), body.getX() + netVelocity.x, body.getY() + netVelocity.y);
			
			List<Body> collisions = bodies.getIntersectionsFor(line);
			if(collisions.size() > 0) {
				Body nearestBody = collisions.get(0);
				
				for(int i = 1; i < collisions.size(); i++) {
					Body otherBody = collisions.get(i);
					if(body.getDistanceTo(otherBody) < body.getDistanceTo(nearestBody)) {
						nearestBody = otherBody;
					}
				}
				
				
			} else {
				body.move(netVelocity);
			}
		}
	}

	@Override
	public void interpolate(Entity entity, float alpha) {
		List<Body> bodies = entity.getComponents(Body.class);
		for (Body body : bodies) {
			body.interpolate(alpha);
		}
	}

	@Override
	public void render(Entity entity, Graphics g) {

	}
	
	@Override
	public void componentAdded(Entity source, Component component) {
		if(component instanceof Body) {
			bodies.add((Body) component);
		}
	}

	@Override
	public void componentRemoved(Entity source, Component component) {
		if(component instanceof Body) {
			bodies.remove((Body) component);
		}
	}
	
	@Override
	public void addEntity(Entity entity) {
		super.addEntity(entity);
		entity.addEntityListener(this);
	}
	
	@Override
	public void removeEntity(Entity entity) {
		super.removeEntity(entity);
		entity.removeEntityListener(this);
	}
}
