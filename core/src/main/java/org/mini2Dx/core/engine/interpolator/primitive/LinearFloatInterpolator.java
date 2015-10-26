package org.mini2Dx.core.engine.interpolator.primitive;

/**
 * The linear float interpolation function
 */
public final class LinearFloatInterpolator implements FloatInterpolator {

  public static final LinearFloatInterpolator INSTANCE = new LinearFloatInterpolator();

  private LinearFloatInterpolator() {
  }

  @Override
  public float interpolate(float initialValue, float targetValue, float alpha) {
    float inverseAlpha = 1.0f - alpha;
    return (inverseAlpha * initialValue) + (alpha * targetValue);
  }
}
