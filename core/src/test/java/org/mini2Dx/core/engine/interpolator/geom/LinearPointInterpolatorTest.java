package org.mini2Dx.core.engine.interpolator.geom;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.core.engine.interpolator.InterpolatedValue;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.geom.Point;

/**
 *
 */
public class LinearPointInterpolatorTest {
  private final Point initialValue = new Point(22f, 12f);
  private final Point targetValue = new Point(10f, 45f);
  private final InterpolatedValue<Point> interpolatedValue = new InterpolatedValue<>(Interpolators.linearPoint());

  @Test
  public void testInterpolateValues() {
    interpolatedValue.setInitialValue(initialValue);
    interpolatedValue.setTargetValue(targetValue);

    Point flyweight = new Point();
    for (float alpha = 0.0f; alpha <= 1.0f; alpha += 0.05f) {
      interpolatedValue.setAlpha(alpha);

      float x = initialValue.getX() + alpha * (targetValue.getX() - initialValue.getX());
      float y = initialValue.getY() + alpha * (targetValue.getY() - initialValue.getY());

      interpolatedValue.getInterpolatedValue(flyweight);
      Assert.assertEquals("x at "+ alpha, x, flyweight.getX(), 0.05f);
      Assert.assertEquals("y at "+ alpha, y, flyweight.getY(), 0.05f);
    }
  }
}
