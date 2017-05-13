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
package org.mini2Dx.core.controller.xbox360;

import org.mini2Dx.core.controller.MdxControllerListener;
import org.mini2Dx.core.controller.Xbox360Controller;
import org.mini2Dx.core.controller.button.Xbox360Button;
import org.mini2Dx.core.controller.deadzone.DeadZone;

/**
 * Input listener interface for {@link Xbox360Controller}s
 */
public interface Xbox360ControllerListener extends MdxControllerListener {

	/**
	 * Called when a controller disconnects
	 * 
	 * @param controller
	 *            The controller that this event came from
	 */
	public void disconnected(Xbox360Controller controller);

	/**
	 * Called when a button is pressed down
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param button
	 *            The button that was pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean buttonDown(Xbox360Controller controller, Xbox360Button button);

	/**
	 * Called when a button is released
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param button
	 *            The button that was released
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean buttonUp(Xbox360Controller controller, Xbox360Button button);

	/**
	 * Called when the left trigger is moved
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~0f when released, ~1f when pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftTriggerMoved(Xbox360Controller controller, float value);

	/**
	 * Called when the right trigger is moved
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~0f when released, ~1f when pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightTriggerMoved(Xbox360Controller controller, float value);

	/**
	 * Called when left stick moves along its X axis
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~-1f at left, ~0f at center, ~1f at right (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            controller instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftStickXMoved(Xbox360Controller controller, float value);

	/**
	 * Called when left stick moves along its Y axis
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~-1f at top, ~0f at center, ~1f at bottom (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            controller instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftStickYMoved(Xbox360Controller controller, float value);

	/**
	 * Called when right stick moves along its X axis
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~-1f at left, ~0f at center, ~1f at right (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            controller instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightStickXMoved(Xbox360Controller controller, float value);

	/**
	 * Called when left stick moves along its Y axis
	 * 
	 * @param controller
	 *            The controller that this event came from
	 * @param value
	 *            ~-1f at top, ~0f at center, ~1f at bottom (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            controller instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightStickYMoved(Xbox360Controller controller, float value);
}
