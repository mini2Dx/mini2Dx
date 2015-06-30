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
package org.mini2Dx.core.serialization.dummy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;

/**
 * Parent object class for testing serialization
 */
public class TestParentObject {
    @Field
    private int intValue;
    @Field
    private boolean booleanValue;
    @Field
    private byte byteValue;
    @Field
    private short shortValue;
    @Field
    private long longValue;
    @Field
    private float floatValue;
    @Field(optional=true)
    private int [] intArrayValue;
    @Field(optional=true)
    private String [] stringArrayValue;
    @Field
    private String stringValue;
    @Field
    private Map<String, Integer> mapValues;
    @Field
    private List<String> listValues;
    @Field
    private TestChildObject childObject;
    @Field(optional=true)
    private TestChildObject optionalChildObject;
    @Field
    private List<TestChildObject> children;
    
    private int ignoredValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public byte getByteValue() {
		return byteValue;
	}

	public void setByteValue(byte byteValue) {
		this.byteValue = byteValue;
	}

	public short getShortValue() {
        return shortValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Map<String, Integer> getMapValues() {
        return mapValues;
    }

    public void setMapValues(Map<String, Integer> mapValues) {
        this.mapValues = mapValues;
    }

    public List<String> getListValues() {
        return listValues;
    }

    public void setListValues(List<String> listValues) {
        this.listValues = listValues;
    }

    public int getIgnoredValue() {
        return ignoredValue;
    }

    public void setIgnoredValue(int ignoredValue) {
        this.ignoredValue = ignoredValue;
    }

    public TestChildObject getChildObject() {
        return childObject;
    }

    public void setChildObject(TestChildObject childObject) {
        this.childObject = childObject;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

	public int[] getIntArrayValue() {
		return intArrayValue;
	}

	public void setIntArrayValue(int[] intArrayValue) {
		this.intArrayValue = intArrayValue;
	}

	public String[] getStringArrayValue() {
		return stringArrayValue;
	}

	public void setStringArrayValue(String[] stringArrayValue) {
		this.stringArrayValue = stringArrayValue;
	}

	public TestChildObject getOptionalChildObject() {
		return optionalChildObject;
	}

	public void setOptionalChildObject(TestChildObject optionalChildObject) {
		this.optionalChildObject = optionalChildObject;
	}

	public List<TestChildObject> getChildren() {
		return children;
	}

	public void setChildren(List<TestChildObject> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (booleanValue ? 1231 : 1237);
		result = prime * result + byteValue;
		result = prime * result
				+ ((childObject == null) ? 0 : childObject.hashCode());
		result = prime * result + Float.floatToIntBits(floatValue);
		result = prime * result + Arrays.hashCode(intArrayValue);
		result = prime * result + intValue;
		result = prime * result
				+ ((listValues == null) ? 0 : listValues.hashCode());
		result = prime * result + (int) (longValue ^ (longValue >>> 32));
		result = prime * result
				+ ((mapValues == null) ? 0 : mapValues.hashCode());
		result = prime * result + shortValue;
		result = prime * result + Arrays.hashCode(stringArrayValue);
		result = prime * result
				+ ((stringValue == null) ? 0 : stringValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestParentObject other = (TestParentObject) obj;
		if (booleanValue != other.booleanValue)
			return false;
		if (byteValue != other.byteValue)
			return false;
		if (childObject == null) {
			if (other.childObject != null)
				return false;
		} else if (!childObject.equals(other.childObject))
			return false;
		if (Float.floatToIntBits(floatValue) != Float
				.floatToIntBits(other.floatValue))
			return false;
		if (!Arrays.equals(intArrayValue, other.intArrayValue))
			return false;
		if (intValue != other.intValue)
			return false;
		if (listValues == null) {
			if (other.listValues != null)
				return false;
		} else if (!listValues.equals(other.listValues))
			return false;
		if (longValue != other.longValue)
			return false;
		if (mapValues == null) {
			if (other.mapValues != null)
				return false;
		} else if (!mapValues.equals(other.mapValues))
			return false;
		if (shortValue != other.shortValue)
			return false;
		if (!Arrays.equals(stringArrayValue, other.stringArrayValue))
			return false;
		if (stringValue == null) {
			if (other.stringValue != null)
				return false;
		} else if (!stringValue.equals(other.stringValue))
			return false;
		return true;
	}
}
