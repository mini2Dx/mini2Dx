/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.core.data.dummy;

import java.util.List;
import java.util.Map;

import org.mini2Dx.core.data.annotation.Field;

/**
 *
 * @author Thomas Cashman
 */
public class TestParentObject {
    @Field
    private int intValue;
    @Field
    private boolean booleanValue;
    @Field
    private short shortValue;
    @Field
    private long longValue;
    @Field
    private float floatValue;
    @Field
    private String stringValue;
    @Field
    private Map<String, Integer> mapValues;
    @Field
    private List<String> listValues;
    @Field
    private TestChildObject childObject;
    
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
}
