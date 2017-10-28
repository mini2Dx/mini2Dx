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
public enum PS4Button implements ControllerButton {
	UP("ps4-up"),
	DOWN("ps4-down"),
	LEFT("ps4-left"),
	RIGHT("ps4-right"),
	
	X("ps4-x"),
	SQUARE("ps4-square"),
	CIRCLE("ps4-circle"),
	TRIANGLE("ps4-triangle"),
	
	L1("ps4-l1"),
	R1("ps4-r1"),
	L2("ps4-l2"),
	R2("ps4-r2"),
	L3("ps4-l3"),
	R3("ps4-r3"),
	
	PS("ps4-ps"),
	SHARE("ps4-share"),
	OPTIONS("ps4-options"),
	TOUCHPAD("ps4-touchpad");
	
	private final String absoluteValue;
	
	private PS4Button(String absoluteValue) {
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
	
	public static PS4Button fromAbsoluteValue(String value) {
		for(PS4Button button : PS4Button.values()) {
			if(button.getAbsoluteValue().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
