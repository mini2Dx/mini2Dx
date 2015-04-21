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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.exception.DuplicateColumnSizeTypeException;

/**
 *
 * @author Thomas Cashman
 */
public abstract class LayoutColumn implements UiElement {
	private Rectangle dimensions;
	private Map<DeviceSize, ColumnSize> sizes;
	private Map<DeviceSize, ColumnSize> offsets;
	
	public LayoutColumn() {
		dimensions = new Rectangle(-1f, -1f, 0f, 0f);
	}
	
	@Override
	public ColumnSize getSize(DeviceSize deviceSize) {
		if(sizes == null) {
			return deviceSize.getDefaultSize();
		}
		return sizes.get(deviceSize);
	}
	
	public void setSize(ColumnSize... columnSizes) {
		if(sizes == null) {
			sizes = new HashMap<DeviceSize, ColumnSize>();
		}
		
		Set<DeviceSize> deviceSizes = new HashSet<DeviceSize>();
		for(ColumnSize size : columnSizes) {
			if(!deviceSizes.add(size.getDeviceSize())) {
				throw new DuplicateColumnSizeTypeException(size.getDeviceSize());
			}
			sizes.put(size.getDeviceSize(), size);
			switch(size.getDeviceSize()) {
			case XL:
				if(!sizes.containsKey(DeviceSize.L)) {
					sizes.put(DeviceSize.L, ColumnSize.L_12);
				}
			case L:
				if(!sizes.containsKey(DeviceSize.M)) {
					sizes.put(DeviceSize.M, ColumnSize.M_12);
				}
				if(!sizes.containsKey(DeviceSize.XL)) {
					sizes.put(DeviceSize.XL,
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.XL, size.getNumericValue()));
				}
			case M:
				if(!sizes.containsKey(DeviceSize.S)) {
					sizes.put(DeviceSize.S, ColumnSize.S_12);
				}
				if(!sizes.containsKey(DeviceSize.L)) {
					sizes.put(DeviceSize.L, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.L, size.getNumericValue()));
				}
			case S:
				if(!sizes.containsKey(DeviceSize.XS)) {
					sizes.put(DeviceSize.XS, ColumnSize.XS_12);
				}
				if(!sizes.containsKey(DeviceSize.M)) {
					sizes.put(DeviceSize.M, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.M, size.getNumericValue()));
				}
			case XS:
				if(!sizes.containsKey(DeviceSize.S)) {
					sizes.put(DeviceSize.S, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.S, size.getNumericValue()));
				}
			}
		}
	}
	
	@Override
	public ColumnSize getOffset(DeviceSize deviceSize) {
		if(offsets == null) {
			return deviceSize.getDefaultOffset();
		}
		return offsets.get(deviceSize);
	}
	
	public void setOffset(ColumnSize... columnSizes) {
		if(offsets == null) {
			offsets = new HashMap<DeviceSize, ColumnSize>();
		}
		
		Set<DeviceSize> deviceSizes = new HashSet<DeviceSize>();
		for(ColumnSize size : columnSizes) {
			if(!deviceSizes.add(size.getDeviceSize())) {
				throw new DuplicateColumnSizeTypeException(size.getDeviceSize());
			}
			offsets.put(size.getDeviceSize(), size);
			switch(size.getDeviceSize()) {
			case XL:
				if(!offsets.containsKey(DeviceSize.L)) {
					offsets.put(DeviceSize.L, ColumnSize.L_0);
				}
			case L:
				if(!offsets.containsKey(DeviceSize.M)) {
					offsets.put(DeviceSize.M, ColumnSize.M_0);
				}
				if(!offsets.containsKey(DeviceSize.XL)) {
					offsets.put(DeviceSize.XL,
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.XL, size.getNumericValue()));
				}
			case M:
				if(!offsets.containsKey(DeviceSize.S)) {
					offsets.put(DeviceSize.S, ColumnSize.S_0);
				}
				if(!offsets.containsKey(DeviceSize.L)) {
					offsets.put(DeviceSize.L, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.L, size.getNumericValue()));
				}
			case S:
				if(!offsets.containsKey(DeviceSize.XS)) {
					offsets.put(DeviceSize.XS, ColumnSize.XS_0);
				}
				if(!offsets.containsKey(DeviceSize.M)) {
					offsets.put(DeviceSize.M, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.M, size.getNumericValue()));
				}
			case XS:
				if(!offsets.containsKey(DeviceSize.S)) {
					offsets.put(DeviceSize.S, 
							ColumnSize.fromDeviceSizeAndValue(DeviceSize.S, size.getNumericValue()));
				}
			}
		}
	}

	@Override
	public float getX() {
		return dimensions.getX();
	}

	@Override
	public float getY() {
		return dimensions.getY();
	}

	@Override
	public float getWidth() {
		return dimensions.getWidth();
	}

	@Override
	public float getHeight() {
		return dimensions.getHeight();
	}
}
