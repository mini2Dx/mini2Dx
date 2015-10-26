package org.mini2Dx.core.engine.interpolator.geom;

import org.mini2Dx.core.engine.interpolator.Interpolator;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.geom.Point;

/**
 * The point interpolation function
 */
public final class LinearPointInterpolator implements Interpolator<Point> {

  public static final LinearPointInterpolator INSTANCE = new LinearPointInterpolator();

  private LinearPointInterpolator() {
  }

  @Override
  public Point interpolate(Point flyweight, Point initialValue, Point targetValue, float alpha) {

    float x = Interpolators.linearFloat().interpolate(initialValue.getX(), targetValue.getX(), alpha);
    float y = Interpolators.linearFloat().interpolate(initialValue.getY(), targetValue.getY(), alpha);

    flyweight.set(x, y);
    return flyweight;
  }
}
