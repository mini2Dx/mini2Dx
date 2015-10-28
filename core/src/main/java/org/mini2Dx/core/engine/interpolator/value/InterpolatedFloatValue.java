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
package org.mini2Dx.core.engine.interpolator.value;

import org.mini2Dx.core.engine.interpolator.FloatInterpolator;

/**
 * <p>An interpolated value stores the initial value as well as the current value of a variable. The interpolated value may be used during rendering.</p>
 *
 *
 * <pre>
 *   // Example: Updating health stored as a float
 *   FloatInterpolatedValue health = new FloatInterpolatedValue(Interpolators.linearFloat());
 *   health.setInitialValue(10f);
 *   health.setCurrentValue(10f);
 *
 *   // Set initial value prior to any changes
 *   float currentHealth = health.getCurrentValue();
 *   health.setInitialValue(currentHealth);
 *
 *   // Update current value as you normally would
 *   currentHealth -= 5f;
 *   health.setCurrentValue(currentHealth);
 *
 *   // Get the interpolated value
 *   float interpolatedHp = health.getInterpolatedValue(0.5f);
 *   Assert.assertEquals(interpolatedHp, 7.5f);
 * </pre>
 */
public final class InterpolatedFloatValue {

  private final FloatInterpolator interpolator;

  private float initialValue;
  private float currentValue;

  /**
   * Constructs the {@link InterpolatedFloatValue}
   *
   * @param interpolator the interpolator function
   */
  public InterpolatedFloatValue(FloatInterpolator interpolator) {
    this.interpolator = interpolator;
  }


  /**
   * The initial value
   *
   * @return the initial value
   */
  public float getInitialValue() {
    return initialValue;
  }

  /**
   * Sets the initial value
   *
   * @param initialValue the new initial value
   */
  public void setInitialValue(float initialValue) {
    this.initialValue = initialValue;
  }

  /**
   * Gets the current value
   *
   * @return the current value
   */
  public float getCurrentValue() {
    return currentValue;
  }

  /**
   * Sets the current value
   *
   * @param currentValue the new current value
   */
  public void setCurrentValue(float currentValue) {
    this.currentValue = currentValue;
  }

  /**
   * Gets the interpolated value at the specified alpha
   *
   * @param alpha the alpha is a value between <code>0.0f</code> and <code>1.0f</code>
   * @return the interpolated value
   */
  public float getInterpolatedValue(float alpha) {
    return interpolator.interpolate(initialValue, currentValue, alpha);
  }
}
