package org.mini2Dx.core.engine.interpolator.geom;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.core.engine.interpolator.InterpolatedValue;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.geom.Circle;
import org.mini2Dx.core.geom.Point;

/**
 *
 */
public class LinearCircleInterpolatorTest {
  private final Circle initialValue = new Circle(22f, 12f, 12f);
  private final Circle targetValue = new Circle(10f, 45f, 1f);
  private final InterpolatedValue<Circle> interpolatedValue = new InterpolatedValue<>(Interpolators.linearCircle());

  @Test
  public void testInterpolateValues() {
    interpolatedValue.setInitialValue(initialValue);
    interpolatedValue.setTargetValue(targetValue);

    Circle flyweight = new Circle(0f, 0f, 0f);
    for (float alpha = 0.0f; alpha <= 1.0f; alpha += 0.05f) {
      interpolatedValue.setAlpha(alpha);

      float x = initialValue.getX() + alpha * (targetValue.getX() - initialValue.getX());
      float y = initialValue.getY() + alpha * (targetValue.getY() - initialValue.getY());
      float radius = initialValue.getRadius() + alpha * (targetValue.getRadius() - initialValue.getRadius());

      interpolatedValue.getInterpolatedValue(flyweight);
      Assert.assertEquals("x at " + alpha, x, flyweight.getX(), 0.05f);
      Assert.assertEquals("y at "+ alpha, y, flyweight.getY(), 0.05f);
      Assert.assertEquals("radius at "+ alpha, radius, flyweight.getRadius(), 0.05f);
    }
  }
}
