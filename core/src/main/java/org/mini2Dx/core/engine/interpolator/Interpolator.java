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

/**
 * <p>The interpolation function for objects.</p>
 *
 * <p>The initial value and current value are instantiated only once (on construction) to reduce object allocation.</p>
 *
 * <pre>
 *   // Example: Interpolating on health
 *   class Health {
 *     public float hp;
 *     public float maxHp;
 *
 *     public void set(Health o) {
 *       set(o.hp, o.maxHp);
 *     }
 *     public void set(float hp, float maxHp) {
 *       this.hp = hp;
 *       this.maxHp = maxHp;
 *     }
 *   }
 *
 *   class LinearHealthInterpolator implements Interpolator&lt;Health&gt; {
 *     void interpolate(Health flyweight, Health initialValue, Health currentValue, float alpha) {
 *       // Note that we're cascading the interpolators to individual fields within the Health component.
 *       flyweight.hp = Interpolators.linearFloat().interpolate(initialValue.hp, currentValue.hp, alpha);
 *       flyweight.maxHp = Interpolators.linearFloat().interpolate(initialValue.maxHp, currentValue.maxHp, alpha);
 *     }
 *   }
 *
 *   // Initialize the interpolated health value
 *   InterpolatedValue&lt;Health&gt; health = new InterpolatedValue&lt;&gt;(new LinearHealthInterpolator(), new Health(), new Health());
 *   health.getInitialValue().set(10f, 10f);
 *   Health currentValue = health.getCurrentValue().set(10f, 10f);
 *
 *   // Update the interpolated health value
 *   health.getInitialValue().set(health.getCurrentValue()); // before updating the value, the initial value needs to be set
 *   health.getCurrentValue().hp = 5f; // The current value can be updated any time afterwards
 *
 *   // Use the interpolated value at 50% the current value
 *   Health flyweight = new Health();
 *   health.getInterpolatedValue(flyweight, 0.5f);
 *   Assert.assertEquals(7.5f, flyweight.hp);
 * </pre>
 */
public interface Interpolator<T> {

  /**
   * The interpolation algorithm. This takes the initial value, the current value, and the ratio of initial value to current value. The result is then stored in the flyweight argument for use.
   *
   * @param flyweight    the flyweight value is where the results are stored
   * @param initialValue the initial value
   * @param currentValue the current value
   * @param alpha        a value between 0.0f to 1.0f. When alpha is 0.0f, the value should always be the initial value. When alpha is at 1.0f, the value should always be the current value.
   */
  void interpolate(T flyweight, T initialValue, T currentValue, float alpha);
}
