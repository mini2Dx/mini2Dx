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
package org.mini2Dx.core.controller.button;

/**
 *
 */
public enum XboxOneButton implements ControllerButton {
	UP("xboxOne-up"),
	DOWN("xboxOne-down"),
	LEFT("xboxOne-left"),
	RIGHT("xboxOne-right"),
	
	MENU("xboxOne-menu"),
	VIEW("xboxOne-view"),
	HOME("xboxOne-home"),
	
	LEFT_STICK("xboxOne-left-stick"),
	RIGHT_STICK("xboxOne-right-stick"),
	
	LEFT_SHOULDER("xboxOne-left-shoulder"),
	RIGHT_SHOULDER("xboxOne-right-shoulder"),
	
	A("xboxOne-a"),
	B("xboxOne-b"),
	X("xboxOne-x"),
	Y("xboxOne-y");
	
	private final String absoluteValue;
	
	private XboxOneButton(String absoluteValue) {
		this.absoluteValue = absoluteValue;
	}

	@Override
	public String getAbsoluteValue() {
		return absoluteValue;
	}
	
	@Override
	public boolean equals(ControllerButton controllerButton) {
		return absoluteValue.equals(controllerButton.getAbsoluteValue());
	}
	
	public static XboxOneButton fromAbsoluteValue(String value) {
		for(XboxOneButton button : XboxOneButton.values()) {
			if(button.getAbsoluteValue().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
