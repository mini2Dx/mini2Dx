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
package org.mini2Dx.ui.layout;

import org.mini2Dx.ui.exception.InvalidColumnSizeValueException;

/**
 *
 * @author Thomas Cashman
 */
public enum ColumnSize {
	XS_0(DeviceSize.XS, 0),
	XS_1(DeviceSize.XS, 1),
	XS_2(DeviceSize.XS, 2),
	XS_3(DeviceSize.XS, 3),
	XS_4(DeviceSize.XS, 4),
	XS_5(DeviceSize.XS, 5),
	XS_6(DeviceSize.XS, 6),
	XS_7(DeviceSize.XS, 7),
	XS_8(DeviceSize.XS, 8),
	XS_9(DeviceSize.XS, 9),
	XS_10(DeviceSize.XS, 10),
	XS_11(DeviceSize.XS, 11),
	XS_12(DeviceSize.XS, 12),
	S_0(DeviceSize.S, 0),
	S_1(DeviceSize.S, 1),
	S_2(DeviceSize.S, 2),
	S_3(DeviceSize.S, 3),
	S_4(DeviceSize.S, 4),
	S_5(DeviceSize.S, 5),
	S_6(DeviceSize.S, 6),
	S_7(DeviceSize.S, 7),
	S_8(DeviceSize.S, 8),
	S_9(DeviceSize.S, 9),
	S_10(DeviceSize.S, 10),
	S_11(DeviceSize.S, 11),
	S_12(DeviceSize.S, 12),
	M_0(DeviceSize.M, 0),
	M_1(DeviceSize.M, 1),
	M_2(DeviceSize.M, 2),
	M_3(DeviceSize.M, 3),
	M_4(DeviceSize.M, 4),
	M_5(DeviceSize.M, 5),
	M_6(DeviceSize.M, 6),
	M_7(DeviceSize.M, 7),
	M_8(DeviceSize.M, 8),
	M_9(DeviceSize.M, 9),
	M_10(DeviceSize.M, 10),
	M_11(DeviceSize.M, 11),
	M_12(DeviceSize.M, 12),
	L_0(DeviceSize.L, 0),
	L_1(DeviceSize.L, 1),
	L_2(DeviceSize.L, 2),
	L_3(DeviceSize.L, 3),
	L_4(DeviceSize.L, 4),
	L_5(DeviceSize.L, 5),
	L_6(DeviceSize.L, 6),
	L_7(DeviceSize.L, 7),
	L_8(DeviceSize.L, 8),
	L_9(DeviceSize.L, 9),
	L_10(DeviceSize.L, 10),
	L_11(DeviceSize.L, 11),
	L_12(DeviceSize.L, 12),
	XL_0(DeviceSize.XL, 0),
	XL_1(DeviceSize.XL, 1),
	XL_2(DeviceSize.XL, 2),
	XL_3(DeviceSize.XL, 3),
	XL_4(DeviceSize.XL, 4),
	XL_5(DeviceSize.XL, 5),
	XL_6(DeviceSize.XL, 6),
	XL_7(DeviceSize.XL, 7),
	XL_8(DeviceSize.XL, 8),
	XL_9(DeviceSize.XL, 9),
	XL_10(DeviceSize.XL, 10),
	XL_11(DeviceSize.XL, 11),
	XL_12(DeviceSize.XL, 12);
	
	private final DeviceSize deviceSize;
	private final int numericValue;
	
	private ColumnSize(DeviceSize deviceSize, int numericValue) {
		this.deviceSize = deviceSize;
		this.numericValue = numericValue;
	}

	public DeviceSize getDeviceSize() {
		return deviceSize;
	}

	public int getNumericValue() {
		return numericValue;
	}
	
	public static ColumnSize fromDeviceSizeAndValue(DeviceSize columnType, int numericValue) {
		if(numericValue < 0) {
			throw new InvalidColumnSizeValueException(numericValue);
		}
		if(numericValue > 12) {
			throw new InvalidColumnSizeValueException(numericValue);
		}
		return ColumnSize.valueOf(columnType.name() + "_" + numericValue);
	}
}
