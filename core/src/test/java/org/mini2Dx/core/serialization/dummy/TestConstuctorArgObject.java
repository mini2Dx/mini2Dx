/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.core.serialization.dummy;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;

/**
 *
 */
public class TestConstuctorArgObject {
	private final String value;

	public TestConstuctorArgObject(@ConstructorArg(name = "argValue", clazz = String.class) String value) {
		this.value = value;
	}

	@ConstructorArg(name = "argValue", clazz = String.class)
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		TestConstuctorArgObject other = (TestConstuctorArgObject) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
