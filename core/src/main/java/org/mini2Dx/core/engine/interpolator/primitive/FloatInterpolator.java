package org.mini2Dx.core.engine.interpolator.primitive;

/**
 * The interpolation function for floats
 */
public interface FloatInterpolator {
  float interpolate(float initialValue, float targetValue, float alpha);
}
