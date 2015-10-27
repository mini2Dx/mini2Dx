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

import org.mini2Dx.core.engine.interpolator.Interpolator;

/**
 * <p>An interpolated value stores the initial value as well as the current value of a variable. During the interpolation phase, the interpolated value will be used for display.</p>
 *
 * <pre>
 *   // Example: Updating health stored as a struct
 *   InterpolatedValue<Health> health = mHealth.get(entityId).health;
 *   Health currentHealth = health.getCurrentValue();
 *
 *   // Set initial value prior to any changes
 *   health.getInitialValue().set(currentHealth);
 *
 *   // Update current value
 *   currentHealth.hp = Math.min(currentHealth.hp + 5f, currentHealth.maxHp);
 *
 *   // To use the interpolated value
 *   Health flyweight = new Health();
 *   health.getInterpolatedValue(flyweight, 0.5f); // mid-way
 *   flyweight.hp;
 * </pre>
 */
public final class InterpolatedValue<T> {

  private final Interpolator<T> interpolator;
  private final T initialValue;
  private final T currentValue;

  /**
   * Constructor for the {@link InterpolatedValue}
   *
   * @param interpolator the interpolator function
   * @param initialValue the initial value
   * @param currentValue the current value
   */
  public InterpolatedValue(Interpolator<T> interpolator, T initialValue, T currentValue) {
    this.interpolator = interpolator;
    this.initialValue = initialValue;
    this.currentValue = currentValue;
  }

  /**
   * Get the initial value
   *
   * @return the initial value
   */
  public T getInitialValue() {
    return initialValue;
  }

  /**
   * Get the current value
   *
   * @return the current value
   */
  public T getCurrentValue() {
    return currentValue;
  }

  /**
   * Sets the flyweight to the interpolated value at the specified alpha
   *
   * @param flyweight the flyweight for the interpolated value
   * @param alpha     the alpha is a value between <code>0.0f</code> and <code>1.0f</code>
   */
  public void getInterpolatedValue(T flyweight, float alpha) {
    interpolator.interpolate(flyweight, initialValue, currentValue, alpha);
  }
}
