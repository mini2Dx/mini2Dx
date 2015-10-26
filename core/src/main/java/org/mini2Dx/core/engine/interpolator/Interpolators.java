package org.mini2Dx.core.engine.interpolator;

import org.mini2Dx.core.engine.interpolator.geom.LinearCircleInterpolator;
import org.mini2Dx.core.engine.interpolator.geom.LinearPointInterpolator;
import org.mini2Dx.core.engine.interpolator.primitive.DiscreteFloatInterpolator;
import org.mini2Dx.core.engine.interpolator.primitive.LinearFloatInterpolator;

/**
 * This class is a suite of static methods to handle common interpolation
 */
public final class Interpolators {

  public static DiscreteFloatInterpolator discreteFloat() {
    return DiscreteFloatInterpolator.INSTANCE;
  }

  public static LinearFloatInterpolator linearFloat() {
    return LinearFloatInterpolator.INSTANCE;
  }

  public static LinearCircleInterpolator linearCircle() {
    return LinearCircleInterpolator.INSTANCE;
  }

  public static LinearPointInterpolator linearPoint() {
    return LinearPointInterpolator.INSTANCE;
  }
}
