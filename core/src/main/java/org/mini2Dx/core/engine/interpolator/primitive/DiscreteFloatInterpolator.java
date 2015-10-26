package org.mini2Dx.core.engine.interpolator.primitive;

/**
 * The discrete float interpolation function
 */
public final class DiscreteFloatInterpolator implements FloatInterpolator {
  public static final DiscreteFloatInterpolator INSTANCE = new DiscreteFloatInterpolator();

  private DiscreteFloatInterpolator() {
  }

  @Override
  public float interpolate(float initialValue, float targetValue, float alpha) {
    return alpha < 1.0f ? initialValue : targetValue;
  }
}
