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

import org.mini2Dx.core.controller.button.XboxOneButton;
import org.mini2Dx.core.controller.deadzone.DeadZone;
import org.mini2Dx.core.controller.deadzone.NoopDeadZone;
import org.mini2Dx.core.controller.xbox360.Xbox360ControllerListener;
import org.mini2Dx.core.controller.xboxone.XboxOneControllerListener;

import com.badlogic.gdx.controllers.Controller;

/**
 * Base class for Xbox One controller mapping implementations
 */
public abstract class XboxOneController implements MdxController<XboxOneControllerListener> {
	public static final String ID = "xbox one";

	private final Controller controller;
	private final List<XboxOneControllerListener> listeners = new ArrayList<>();
	
	private DeadZone leftStickDeadZone, rightStickDeadZone;
	
	public XboxOneController(Controller controller) {
		this(controller, new NoopDeadZone(), new NoopDeadZone());
	}
	
	public XboxOneController(Controller controller, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		this.controller = controller;
		this.leftStickDeadZone = leftStickDeadZone;
		this.rightStickDeadZone = rightStickDeadZone;
		controller.addListener(this);
	}
	
	protected boolean notifyDisconnected() {
		for(XboxOneControllerListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}
	
	protected boolean notifyButtonDown(XboxOneButton button) {
		for(XboxOneControllerListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyButtonUp(XboxOneButton button) {
		for(XboxOneControllerListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftTriggerMoved(float value) {
		for(XboxOneControllerListener listener : listeners) {
			if(listener.leftTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightTriggerMoved(float value) {
		for(XboxOneControllerListener listener : listeners) {
			if(listener.rightTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(XboxOneControllerListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(XboxOneControllerListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(XboxOneControllerListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(XboxOneControllerListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void addListener(int index, XboxOneControllerListener listener) {
    	listeners.add(index, listener);
    }
	
	@Override
    public void addListener(XboxOneControllerListener listener) {
    	listeners.add(listener);
    }
	
	@Override
    public void removeListener(int index) {
    	listeners.remove(index);
    }
    
    @Override
    public void removeListener(XboxOneControllerListener listener) {
    	listeners.remove(listener);
    }
    
    @Override
	public void clearListeners() {
		listeners.clear();
	}
    
    @Override
    public XboxOneControllerListener getListener(int index) {
    	return listeners.get(index);
    }
	
    @Override
	public int getTotalListeners() {
		return listeners.size();
	}
	
	@Override
	public ControllerType getControllerType() {
		return ControllerType.XBOX_ONE;
	}

	public DeadZone getLeftStickDeadZone() {
		return leftStickDeadZone;
	}

	public void setLeftStickDeadZone(DeadZone leftStickDeadZone) {
		if(leftStickDeadZone == null) {
			leftStickDeadZone = new NoopDeadZone();
		}
		this.leftStickDeadZone = leftStickDeadZone;
	}

	public DeadZone getRightStickDeadZone() {
		return rightStickDeadZone;
	}

	public void setRightStickDeadZone(DeadZone rightStickDeadZone) {
		if(rightStickDeadZone == null) {
			rightStickDeadZone = new NoopDeadZone();
		}
		this.rightStickDeadZone = rightStickDeadZone;
	}
}
