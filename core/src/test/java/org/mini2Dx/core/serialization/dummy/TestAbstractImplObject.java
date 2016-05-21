/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.core.serialization.dummy;

import org.mini2Dx.core.serialization.annotation.Field;

/**
 *
 */
public class TestAbstractImplObject extends TestAbstractObject {
	@Field
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
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
		TestAbstractImplObject other = (TestAbstractImplObject) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
