package org.mini2Dx.core.engine.interpolator.primitive;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.engine.interpolator.primitive.InterpolatedFloatValue;

/**
 * Tests the linear float interpolation
 */
public class LinearFloatInterpolatorTest {
  float initialValue = 10.0f;
  float targetValue = 20.0f;

  @Test
  public void testInterpolatedValues() {
    InterpolatedFloatValue value = new InterpolatedFloatValue(Interpolators.linearFloat());
    value.setInitialValue(initialValue);
    value.setTargetValue(targetValue);
    for (float alpha = 0.0f; alpha <= 1.0f; alpha += 0.05f) {
      value.setAlpha(alpha);
      Assert.assertEquals(initialValue + alpha * (targetValue - initialValue), value.getInterpolatedValue(), 0.0f);
    }
  }
}
