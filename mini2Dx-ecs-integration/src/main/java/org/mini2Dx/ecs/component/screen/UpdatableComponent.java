/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.ecs.component.screen;

import org.mini2Dx.core.engine.Updatable;
import org.mini2Dx.ecs.component.Component;

/**
 *
 *
 * @author Thomas Cashman
 */
public abstract class UpdatableComponent extends Component implements Updatable {

	public UpdatableComponent(String name) {
		super(name);
	}
}
