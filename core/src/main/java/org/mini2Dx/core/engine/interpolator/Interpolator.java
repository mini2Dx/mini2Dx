package org.mini2Dx.core.engine.interpolator;

/**
 * The interpolation function
 */
public interface Interpolator<T> {
  T interpolate(T flyweight, T initialValue, T targetValue, float alpha);
}
