/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.serialization.dummy;

import org.mini2Dx.core.serialization.annotation.Field;

/**
 * Super class for testing serialization
 */
public class TestSuperObject {
	@Field
	private String superField;

	public String getSuperField() {
		return superField;
	}

	public void setSuperField(String superField) {
		this.superField = superField;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((superField == null) ? 0 : superField.hashCode());
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
		TestSuperObject other = (TestSuperObject) obj;
		if (superField == null) {
			if (other.superField != null)
				return false;
		} else if (!superField.equals(other.superField))
			return false;
		return true;
	}
}
