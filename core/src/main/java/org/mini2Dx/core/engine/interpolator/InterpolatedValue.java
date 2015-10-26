package org.mini2Dx.core.engine.interpolator;

/**
 * Generates an interpolated value
 */
public final class InterpolatedValue<T> {

  private final Interpolator<T> interpolator;

  private T initialValue;
  private T targetValue;
  private float alpha;

  public InterpolatedValue(Interpolator<T> interpolator) {
    this.interpolator = interpolator;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public T getInitialValue() {
    return initialValue;
  }

  public T getTargetValue() {
    return targetValue;
  }

  public T getInterpolatedValue(T flyweight) {
    return interpolator.interpolate(flyweight, initialValue, targetValue, alpha);
  }

  public void setInitialValue(T previousValue) {
    this.initialValue = previousValue;
  }

  public void setTargetValue(T targetValue) {
    this.targetValue = targetValue;
  }
}
