package org.mini2Dx.core.engine.interpolator.geom;

import org.mini2Dx.core.engine.interpolator.Interpolator;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.geom.Circle;

/**
 * The circle interpolation function
 */
public final class LinearCircleInterpolator implements Interpolator<Circle> {

  public static final LinearCircleInterpolator INSTANCE = new LinearCircleInterpolator();

  private LinearCircleInterpolator() {
  }

  @Override
  public Circle interpolate(Circle flyweight, Circle initialValue, Circle targetValue, float alpha) {
    float x = Interpolators.linearFloat().interpolate(initialValue.getX(), targetValue.getX(), alpha);
    float y = Interpolators.linearFloat().interpolate(initialValue.getY(), targetValue.getY(), alpha);
    float radius = Interpolators.linearFloat().interpolate(initialValue.getRadius(), targetValue.getRadius(), alpha);

    flyweight.setCenter(x, y);
    flyweight.setRadius(radius);
    return flyweight;
  }
}
