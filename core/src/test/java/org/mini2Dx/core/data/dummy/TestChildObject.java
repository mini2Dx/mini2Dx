/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.core.data.dummy;

import org.mini2Dx.core.data.annotation.Field;
import org.mini2Dx.core.data.annotation.Root;

/**
 *
 * @author Thomas Cashman
 */
@Root
public class TestChildObject {
    @Field
    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + intValue;
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
        TestChildObject other = (TestChildObject) obj;
        if (intValue != other.intValue)
            return false;
        return true;
    }
}
