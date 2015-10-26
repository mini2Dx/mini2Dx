package org.mini2Dx.core.engine.interpolator.primitive;

/**
 * The interpolated value of a float
 */
public final class InterpolatedFloatValue {

  private final FloatInterpolator interpolator;

  private float initialValue;
  private float targetValue;
  private float alpha;

  public InterpolatedFloatValue(FloatInterpolator interpolator) {
    this.interpolator = interpolator;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public float getInitialValue() {
    return initialValue;
  }

  public float getTargetValue() {
    return targetValue;
  }

  public float getInterpolatedValue() {
    return interpolator.interpolate(initialValue, targetValue, alpha);
  }

  public void setInitialValue(float initialValue) {
    this.initialValue = initialValue;
  }

  public void setTargetValue(float targetValue) {
    this.targetValue = targetValue;
  }
}
