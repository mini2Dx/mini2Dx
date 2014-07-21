/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.ecs.component.screen;

import org.mini2Dx.core.engine.Renderable;
import org.mini2Dx.ecs.component.PriorityComponent;

/**
 *
 *
 * @author Thomas Cashman
 */
public abstract class RenderableComponent extends PriorityComponent implements Renderable {
	
	public RenderableComponent(String name, int priority) {
		super(name, priority);
	}

	public RenderableComponent(int priority) {
		this(String.valueOf(priority), priority);
	}
}
