/**
 * Copyright 2015 Thomas Cashman
 */
package com.artemis.listener;

import com.artemis.MdxWorld;

/**
 * Common interface for listening to entity creation/deletion {@link MdxWorld} events
 */
public interface WorldListener {

	public void afterEntityCreated(MdxWorld world, int entityId);

	public void beforeEntityDeleted(MdxWorld world, int entityId);
}
