/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.engine.interpolator;

import org.mini2Dx.core.engine.interpolator.geom.LinearCircleInterpolator;
import org.mini2Dx.core.engine.interpolator.geom.LinearPointInterpolator;
import org.mini2Dx.core.engine.interpolator.primitive.DiscreteFloatInterpolator;
import org.mini2Dx.core.engine.interpolator.primitive.LinearFloatInterpolator;

/**
 * <p>This class is a suite of static methods to handle common interpolation.</p>
 */
public final class Interpolators {

  /**
   * @return the discrete float interpolator
   * @see DiscreteFloatInterpolator
   */
  public static DiscreteFloatInterpolator discreteFloat() {
    return DiscreteFloatInterpolator.INSTANCE;
  }

  /**
   * @return the linear float interpolator
   * @see LinearFloatInterpolator
   */
  public static LinearFloatInterpolator linearFloat() {
    return LinearFloatInterpolator.INSTANCE;
  }

  /**
   * @return the linear circle interpolator
   * @see LinearCircleInterpolator
   */
  public static LinearCircleInterpolator linearCircle() {
    return LinearCircleInterpolator.INSTANCE;
  }

  /**
   * @return the linear point interpolator
   * @see LinearPointInterpolator
   */
  public static LinearPointInterpolator linearPoint() {
    return LinearPointInterpolator.INSTANCE;
  }
}
