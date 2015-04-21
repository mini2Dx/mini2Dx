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

/**
 *
 * @author Thomas Cashman
 */
public enum DeviceSize {
	/**
	 * Screen width less than 768px
	 */
	XS(ColumnSize.XS_12, ColumnSize.XS_0),
	/**
	 * Screen width 768px and up
	 */
	S(ColumnSize.S_12, ColumnSize.S_0),
	/**
	 * Screen width 992px and up
	 */
	M(ColumnSize.M_12, ColumnSize.M_0),
	/**
	 * Screen width 1280px and up
	 */
	L(ColumnSize.L_12, ColumnSize.L_0),
	/**
	 * Screen width 1920px and up
	 */
	XL(ColumnSize.XL_12, ColumnSize.XL_0);
	
	private final ColumnSize defaultSize;
	private final ColumnSize defaultOffset;
	
	private DeviceSize(ColumnSize defaultSize, ColumnSize defaultOffset) {
		this.defaultSize = defaultSize;
		this.defaultOffset = defaultOffset;
	}

	public ColumnSize getDefaultSize() {
		return defaultSize;
	}

	public ColumnSize getDefaultOffset() {
		return defaultOffset;
	}
}
