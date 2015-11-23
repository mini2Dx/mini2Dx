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
public enum Xbox360Button implements ControllerButton {
	UP("xbox360-up"),
	DOWN("xbox360-down"),
	LEFT("xbox360-left"),
	RIGHT("xbox360-right"),
	
	START("xbox360-start"),
	BACK("xbox360-back"),
	GUIDE("xbox360-guide"),
	
	LEFT_STICK("xbox360-left-stick"),
	RIGHT_STICK("xbox360-right-stick"),
	
	LEFT_SHOULDER("xbox360-left-shoulder"),
	RIGHT_SHOULDER("xbox360-right-shoulder"),
	
	A("xbox360-a"),
	B("xbox360-b"),
	X("xbox360-x"),
	Y("xbox360-y");
	
	private final String absoluteValue;
	
	private Xbox360Button(String absoluteValue) {
		this.absoluteValue = absoluteValue;
	}

	@Override
	public String getAbsoluteValue() {
		return absoluteValue;
	}
	
	public static Xbox360Button fromAbsoluteValue(String value) {
		for(Xbox360Button button : Xbox360Button.values()) {
			if(button.getAbsoluteValue().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
