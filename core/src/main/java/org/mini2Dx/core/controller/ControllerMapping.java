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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.controller.xbox360.LinuxXbox360Controller;
import org.mini2Dx.core.controller.xbox360.MacXbox360Controller;
import org.mini2Dx.core.controller.xbox360.WindowsXbox360Controller;
import org.mini2Dx.core.controller.xboxone.LinuxXboxOneController;
import org.mini2Dx.core.controller.xboxone.MacXboxOneController;
import org.mini2Dx.core.controller.xboxone.WindowsXboxOneController;
import org.mini2Dx.core.exception.ControllerPlatformException;
import org.mini2Dx.core.util.Os;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Ouya;

/**
 * Provides mappings to common {@link Controller}s based on the current {@link Os}
 */
public class ControllerMapping {
	
	public Xbox360Controller xbox360(Controller controller) throws ControllerPlatformException {
		switch(Mdx.os) {
		case WINDOWS:
			return new WindowsXbox360Controller(controller);
		case MAC:
			return new MacXbox360Controller(controller);
		case UNIX:
			return new LinuxXbox360Controller(controller);
		case ANDROID:
		case IOS:
		case UNKNOWN:
		default:
			throw new ControllerPlatformException(ControllerType.XBOX_360, Mdx.os);
		}
	}
	
	public XboxOneController xboxOne(Controller controller) throws ControllerPlatformException {
		switch(Mdx.os) {
		case WINDOWS:
			return new WindowsXboxOneController(controller);
		case MAC:
			return new MacXboxOneController(controller);
		case UNIX:
			return new LinuxXboxOneController(controller);
		case ANDROID:
		case IOS:
		case UNKNOWN:
		default:
			throw new ControllerPlatformException(ControllerType.XBOX_ONE, Mdx.os);
		}
	}

	public ControllerType getControllerType(Controller controller) {
		if(controller.getName().equals(Ouya.ID)) {
			return ControllerType.OUYA;
		}
		if(controller.getName().toLowerCase().contains(XboxOneController.ID)) {
			return ControllerType.XBOX_ONE;
		}
		return ControllerType.UNKNOWN;
	}
}
