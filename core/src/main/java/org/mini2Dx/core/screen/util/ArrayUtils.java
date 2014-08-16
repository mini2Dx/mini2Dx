/**
 * Copyright 2013 Thomas Cashman
 */
package org.mini2Dx.core.screen.util;

/**
 *
 * @author Thomas Cashman
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

	public static <T> T[] insert(T[] array, T item) {
		for(int i = 0; i < array.length; i++) {
			if(array[i] == null) {
				array[i] = item;
				return array;
			}
		}
		return add(array, item);
	}
	
	public static <T> int getNumberOfElements(T[] array) {
		int result = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] != null) {
				result++;
			}
		}
		return result;
	}
}
