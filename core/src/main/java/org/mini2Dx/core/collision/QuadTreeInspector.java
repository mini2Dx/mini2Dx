/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

public interface QuadTreeInspector<T> {
	public static final int CONTINUE_INSPECTING_ELEMENTS = 1;
	public static final int STOP_INSPECTING_QUAD = 0;
	public static final int STOP_INSPECTING_TREE = -1;

	/**
	 * Inspect an element within the tree
	 *
	 * @return {@link #CONTINUE_INSPECTING_ELEMENTS} if inspection of this quad should continue.
	 * {@link #STOP_INSPECTING_QUAD} if inspection of the quad should stop.
	 * {@link #STOP_INSPECTING_TREE} is inspection of the entire quad tree should stop.
	 */
	public int inspect(T element);

	/**
	 * Checks if a {@link Quad} should be inspected
	 *
	 * @param quad The quad being considered for inspection
	 * @return True if the elements of the quad should be inspected
	 */
	public boolean isQuadValidForInspection(Quad quad);
}
