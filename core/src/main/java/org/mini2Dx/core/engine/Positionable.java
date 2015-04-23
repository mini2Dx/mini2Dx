/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.engine;

/**
 * A common interface for objects that can be positioned with an x and y
 * coordinate
 */
public interface Positionable extends Updatable {
	/**
	 * Returns the x coordinate of this object
	 * 
	 * @return 0 by default
	 */
	public float getX();

	/**
	 * Returns the y coordinate of this object
	 * 
	 * @return 0 by default
	 */
	public float getY();

	/**
	 * Returns this distance between this object's x,y coordinates and the
	 * provided {@link Positionable}'s xy coordinates
	 * 
	 * @param positionable
	 *            The {@link Positionable} to retrieve the distance from
	 * @return 0 if the xy coordinates are the same
	 */
	public float getDistanceTo(Positionable positionable);

	/**
	 * Adds a {@link PositionChangeListener} to be notified of coordinate
	 * changes
	 * 
	 * @param listener
	 *            The {@link PositionChangeListener} to add
	 */
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener);

	/**
	 * Removes a {@link PositionChangeListener} to stop it being notified of
	 * coordinate changes
	 * 
	 * @param listener
	 *            The {@link PositionChangeListener} to remove
	 */
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener);
}
