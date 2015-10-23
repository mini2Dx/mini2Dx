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
package org.mini2Dx.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.controller.button.Xbox360Button;
import org.mini2Dx.core.controller.xbox360.Xbox360ControllerListener;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Base class for Xbox 360 controller implementations
 */
public abstract class Xbox360Controller implements MdxController<Xbox360ControllerListener> {
    private final Controller controller;
    private final List<Xbox360ControllerListener> listeners = new ArrayList<>();
    
	public Xbox360Controller(Controller controller) {
		this.controller = controller;
		controller.addListener(this);
	}

	protected boolean notifyDisconnected() {
		for(Xbox360ControllerListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}
	
	protected boolean notifyButtonDown(Xbox360Button button) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyButtonUp(Xbox360Button button) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftTriggerMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.leftTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightTriggerMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.rightTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftStickXMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.leftStickXMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftStickYMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.leftStickYMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightStickXMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.rightStickXMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightStickYMoved(float value) {
		for(Xbox360ControllerListener listener : listeners) {
			if(listener.rightStickYMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
    
    public void addListener(Xbox360ControllerListener listener) {
    	listeners.add(listener);
    }
    
    public void removeListener(Xbox360ControllerListener listener) {
    	listeners.remove(listener);
    }
    
    public ControllerType getControllerType() {
    	return ControllerType.XBOX_360;
    }
}
