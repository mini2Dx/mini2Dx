/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.miniECx.core.component;

/**
 * 
 * @author Thomas Cashman
 */
public interface CollisionListener extends Component {

	public void handle(Body source, Body body);
}
