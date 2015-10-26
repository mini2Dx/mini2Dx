package org.mini2Dx.core.engine.interpolator.primitive;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.core.engine.interpolator.Interpolators;
import org.mini2Dx.core.engine.interpolator.primitive.DiscreteFloatInterpolator;
import org.mini2Dx.core.engine.interpolator.primitive.InterpolatedFloatValue;

/**
 * Tests the {@link DiscreteFloatInterpolator}
 */
public class DiscreteFloatInterpolationFunctionTest {
  float initialValue = 10.0f;
  float targetValue = 20.0f;

  @Test
  public void testInterpolatedValues_Below_AlphaOne() {
    InterpolatedFloatValue value = new InterpolatedFloatValue(Interpolators.discreteFloat());
    value.setInitialValue(initialValue);
    value.setTargetValue(targetValue);
    for (float alpha = 0.0f; alpha < 1.0f; alpha += 0.05f) {
      value.setAlpha(alpha);
      Assert.assertEquals(initialValue, value.getInterpolatedValue(), 0.0f);
    }
  }

  @Test
  public void testInterpolatedValues_At_AlphaOne() {
    InterpolatedFloatValue value = new InterpolatedFloatValue(Interpolators.discreteFloat());
    value.setInitialValue(initialValue);
    value.setTargetValue(targetValue);
    value.setAlpha(1.0f);
    Assert.assertEquals(targetValue, value.getInterpolatedValue(), 0.0f);
  }
}
