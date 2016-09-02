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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.util.Os;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Parent object class for testing serialization
 */
public class TestParentObject extends TestSuperObject {
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
    @Field
    private TestChildObject [] childObjectArray;
    @Field(optional=true)
    private TestChildObject optionalChildObject;
    @Field
    private List<TestChildObject> children;
    @Field
    private Map<String, TestChildObject> mapObjectValues;
    @Field
    private Os enumValue;
    @Field
    private TestConstuctorArgObject argObject;
    @Field
    private TestInterface interfaceObject;
    @Field
    private List<TestInterface> interfaceObjectList;
	@Field
	private final List<String> finalStringList = new ArrayList<String>();
	@Field
	private final Map<String, String> finalStringMap = new HashMap<String, String>();
	@Field
	private final String [] finalStringArray = new String[5];
	@Field
	private TestAbstractObject abstractObject;
	@Field
	private ObjectMap<String, String> gdxObjectMap;
    
    private int ignoredValue;
    
    public TestParentObject() {}
    
    public TestParentObject(int intValue) {
    	this.intValue = intValue;
    }

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

    public TestChildObject[] getChildObjectArray() {
		return childObjectArray;
	}

	public void setChildObjectArray(TestChildObject[] childObjectArray) {
		this.childObjectArray = childObjectArray;
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

	public Map<String, TestChildObject> getMapObjectValues() {
		return mapObjectValues;
	}

	public void setMapObjectValues(Map<String, TestChildObject> mapObjectValues) {
		this.mapObjectValues = mapObjectValues;
	}

	public Os getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(Os enumValue) {
		this.enumValue = enumValue;
	}

	public TestConstuctorArgObject getArgObject() {
		return argObject;
	}

	public void setArgObject(TestConstuctorArgObject argObject) {
		this.argObject = argObject;
	}

	public TestInterface getInterfaceObject() {
		return interfaceObject;
	}

	public void setInterfaceObject(TestInterface interfaceObject) {
		this.interfaceObject = interfaceObject;
	}

	public List<TestInterface> getInterfaceObjectList() {
		return interfaceObjectList;
	}

	public void setInterfaceObjectList(List<TestInterface> interfaceObjectList) {
		this.interfaceObjectList = interfaceObjectList;
	}

	public List<String> getFinalStringList() {
		return finalStringList;
	}

	public Map<String, String> getFinalStringMap() {
		return finalStringMap;
	}

	public String[] getFinalStringArray() {
		return finalStringArray;
	}

	public TestAbstractObject getAbstractObject() {
		return abstractObject;
	}

	public void setAbstractObject(TestAbstractObject abstractObject) {
		this.abstractObject = abstractObject;
	}

	public ObjectMap<String, String> getGdxObjectMap() {
		return gdxObjectMap;
	}

	public void setGdxObjectMap(ObjectMap<String, String> gdxObjectMap) {
		this.gdxObjectMap = gdxObjectMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((abstractObject == null) ? 0 : abstractObject.hashCode());
		result = prime * result + ((argObject == null) ? 0 : argObject.hashCode());
		result = prime * result + (booleanValue ? 1231 : 1237);
		result = prime * result + byteValue;
		result = prime * result + ((childObject == null) ? 0 : childObject.hashCode());
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((enumValue == null) ? 0 : enumValue.hashCode());
		result = prime * result + Arrays.hashCode(finalStringArray);
		result = prime * result + ((finalStringList == null) ? 0 : finalStringList.hashCode());
		result = prime * result + ((finalStringMap == null) ? 0 : finalStringMap.hashCode());
		result = prime * result + Float.floatToIntBits(floatValue);
		result = prime * result + ignoredValue;
		result = prime * result + Arrays.hashCode(intArrayValue);
		result = prime * result + intValue;
		result = prime * result + ((interfaceObject == null) ? 0 : interfaceObject.hashCode());
		result = prime * result + ((interfaceObjectList == null) ? 0 : interfaceObjectList.hashCode());
		result = prime * result + ((listValues == null) ? 0 : listValues.hashCode());
		result = prime * result + (int) (longValue ^ (longValue >>> 32));
		result = prime * result + ((mapObjectValues == null) ? 0 : mapObjectValues.hashCode());
		result = prime * result + ((mapValues == null) ? 0 : mapValues.hashCode());
		result = prime * result + ((optionalChildObject == null) ? 0 : optionalChildObject.hashCode());
		result = prime * result + shortValue;
		result = prime * result + Arrays.hashCode(stringArrayValue);
		result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestParentObject other = (TestParentObject) obj;
		if (abstractObject == null) {
			if (other.abstractObject != null)
				return false;
		} else if (!abstractObject.equals(other.abstractObject))
			return false;
		if (argObject == null) {
			if (other.argObject != null)
				return false;
		} else if (!argObject.equals(other.argObject))
			return false;
		if (booleanValue != other.booleanValue)
			return false;
		if (byteValue != other.byteValue)
			return false;
		if (childObject == null) {
			if (other.childObject != null)
				return false;
		} else if (!childObject.equals(other.childObject))
			return false;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (enumValue != other.enumValue)
			return false;
		if (!Arrays.equals(finalStringArray, other.finalStringArray))
			return false;
		if (finalStringList == null) {
			if (other.finalStringList != null)
				return false;
		} else if (!finalStringList.equals(other.finalStringList))
			return false;
		if (finalStringMap == null) {
			if (other.finalStringMap != null)
				return false;
		} else if (!finalStringMap.equals(other.finalStringMap))
			return false;
		if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue))
			return false;
		if (ignoredValue != other.ignoredValue)
			return false;
		if (!Arrays.equals(intArrayValue, other.intArrayValue))
			return false;
		if (intValue != other.intValue)
			return false;
		if (interfaceObject == null) {
			if (other.interfaceObject != null)
				return false;
		} else if (!interfaceObject.equals(other.interfaceObject))
			return false;
		if (interfaceObjectList == null) {
			if (other.interfaceObjectList != null)
				return false;
		} else if (!interfaceObjectList.equals(other.interfaceObjectList))
			return false;
		if (listValues == null) {
			if (other.listValues != null)
				return false;
		} else if (!listValues.equals(other.listValues))
			return false;
		if (longValue != other.longValue)
			return false;
		if (mapObjectValues == null) {
			if (other.mapObjectValues != null)
				return false;
		} else if (!mapObjectValues.equals(other.mapObjectValues))
			return false;
		if (mapValues == null) {
			if (other.mapValues != null)
				return false;
		} else if (!mapValues.equals(other.mapValues))
			return false;
		if (optionalChildObject == null) {
			if (other.optionalChildObject != null)
				return false;
		} else if (!optionalChildObject.equals(other.optionalChildObject))
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
