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

import com.badlogic.gdx.controllers.Controller;

/**
 * {@link Controller} types supported by mini2Dx
 */
public enum ControllerType {
	UNKNOWN("unknown"),
	OUYA("ouya"),
	PS3("ps3"),
	PS4("ps4"),
	XBOX_360("xbox360"),
	XBOX_ONE("xboxone");
	
	private final String friendlyString;
	
	private ControllerType(String friendlyString) {
		this.friendlyString = friendlyString;
	}
	
	public String toFriendlyString() {
		return friendlyString;
	}
	
	public static ControllerType fromFriendlyString(String value) {
		switch(value.toLowerCase()) {
		case "unknown":
			return ControllerType.UNKNOWN;
		case "ouya":
			return ControllerType.OUYA;
		case "ps3":
			return ControllerType.PS3;
		case "ps4":
			return ControllerType.PS4;
		case "xbox360":
			return ControllerType.XBOX_360;
		case "xboxone":
			return ControllerType.XBOX_ONE;
		}
		return ControllerType.UNKNOWN;
	}
}
