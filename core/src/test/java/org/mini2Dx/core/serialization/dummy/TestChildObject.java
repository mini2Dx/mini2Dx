/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.serialization.dummy;

import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;

/**
 * Child object class for testing serialization
 */
public class TestChildObject {
    @Field
    private int intValue;
    
    private boolean postDeserializeCalled = false;
    
    public TestChildObject() {}
    
    public TestChildObject(int value) {
    	this.intValue = value;
    }
    
    @PostDeserialize
    public void postDeserialize() {
    	postDeserializeCalled = true;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public boolean isPostDeserializeCalled() {
		return postDeserializeCalled;
	}

	public void setPostDeserializeCalled(boolean postDeserializeCalled) {
		this.postDeserializeCalled = postDeserializeCalled;
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
