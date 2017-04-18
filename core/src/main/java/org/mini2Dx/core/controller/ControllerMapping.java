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
import org.mini2Dx.core.controller.deadzone.DeadZone;
import org.mini2Dx.core.controller.ps4.WindowsPS4Controller;
import org.mini2Dx.core.controller.xbox360.LinuxXbox360Controller;
import org.mini2Dx.core.controller.xbox360.MacXbox360Controller;
import org.mini2Dx.core.controller.xbox360.WindowsXbox360Controller;
import org.mini2Dx.core.controller.xboxone.LinuxXboxOneController;
import org.mini2Dx.core.controller.xboxone.MacXboxOneController;
import org.mini2Dx.core.controller.xboxone.WindowsXboxOneController;
import org.mini2Dx.core.exception.ControllerPlatformException;
import org.mini2Dx.natives.Os;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Ouya;

/**
 * Provides mappings to common {@link Controller}s based on the current
 * {@link Os}
 */
public class ControllerMapping {
	private static final String LOGGING_TAG = ControllerMapping.class.getSimpleName();

	public static PS4Controller ps4(Controller controller) throws ControllerPlatformException {
		return ps4(controller, null, null);
	}

	public static PS4Controller ps4(Controller controller, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone)
			throws ControllerPlatformException {
		switch (Mdx.os) {
		case WINDOWS:
			if(leftStickDeadZone == null || rightStickDeadZone == null) {
				return new WindowsPS4Controller(controller);
			} else {
				return new WindowsPS4Controller(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case MAC:
			return null;
		case UNIX:
			return null;
		case ANDROID:
		case IOS:
		case UNKNOWN:
		default:
			throw new ControllerPlatformException(ControllerType.XBOX_360, Mdx.os);
		}
	}

	public static Xbox360Controller xbox360(Controller controller) throws ControllerPlatformException {
		return xbox360(controller, null, null);
	}

	public static Xbox360Controller xbox360(Controller controller, DeadZone leftStickDeadZone,
			DeadZone rightStickDeadZone) throws ControllerPlatformException {
		switch (Mdx.os) {
		case WINDOWS:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new WindowsXbox360Controller(controller);
			} else {
				return new WindowsXbox360Controller(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case MAC:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new MacXbox360Controller(controller);
			} else {
				return new MacXbox360Controller(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case UNIX:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new LinuxXbox360Controller(controller);
			} else {
				return new LinuxXbox360Controller(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case ANDROID:
		case IOS:
		case UNKNOWN:
		default:
			throw new ControllerPlatformException(ControllerType.XBOX_360, Mdx.os);
		}
	}

	public static XboxOneController xboxOne(Controller controller) throws ControllerPlatformException {
		return xboxOne(controller, null, null);
	}

	public static XboxOneController xboxOne(Controller controller, DeadZone leftStickDeadZone,
			DeadZone rightStickDeadZone) throws ControllerPlatformException {
		switch (Mdx.os) {
		case WINDOWS:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new WindowsXboxOneController(controller);
			} else {
				return new WindowsXboxOneController(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case MAC:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new MacXboxOneController(controller);
			} else {
				return new MacXboxOneController(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case UNIX:
			if (leftStickDeadZone == null || rightStickDeadZone == null) {
				return new LinuxXboxOneController(controller);
			} else {
				return new LinuxXboxOneController(controller, leftStickDeadZone, rightStickDeadZone);
			}
		case ANDROID:
		case IOS:
		case UNKNOWN:
		default:
			throw new ControllerPlatformException(ControllerType.XBOX_ONE, Mdx.os);
		}
	}

	public static ControllerType getControllerType(Controller controller) {
		String controllerName = controller.getName();
		String lowercaseControllerName = controllerName.toLowerCase();

		if (controller.getName().equals(Ouya.ID)) {
			return ControllerType.OUYA;
		}
		if (lowercaseControllerName.contains(XboxOneController.ID)) {
			return ControllerType.XBOX_ONE;
		}
		if (lowercaseControllerName.contains(Xbox360Controller.ID)) {
			return ControllerType.XBOX_360;
		}
		if (lowercaseControllerName.contains(PS3Controller.ID_FULL)) {
			return ControllerType.PS3;
		} else if (lowercaseControllerName.contains(PS3Controller.ID_PREFIX)
				&& lowercaseControllerName.contains(PS3Controller.ID_SUFFIX)) {
			return ControllerType.PS3;
		}
		if (Mdx.os == Os.WINDOWS && lowercaseControllerName.equals(WindowsPS4Controller.ID)) {
			return ControllerType.PS4;
		}
		Gdx.app.log(LOGGING_TAG, "Could not find controller mappings for " + controller.getName());
		return ControllerType.UNKNOWN;
	}
}
