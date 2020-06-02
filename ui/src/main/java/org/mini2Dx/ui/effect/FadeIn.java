/*******************************************************************************
 * Copyright 2020 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.effect;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.collision.CollisionArea;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.UiContainerRenderTree;

public class FadeIn implements UiEffect {
	private static final float DEFAULT_DURATION = 2f;

	private final float duration;

	private Color color, previousTint;
	private float timer = 0f;

	public FadeIn() {
		this(DEFAULT_DURATION);
	}

	public FadeIn(float duration) {
		this.duration = duration;
	}

	@Override
	public void preBegin(UiElement element) {
	}

	@Override
	public void postEnd(UiElement element) {
	}

	@Override
	public boolean update(UiContainerRenderTree uiContainer, CollisionArea currentArea, Rectangle targetArea, float delta) {
		if(color == null) {
			color = Colors.rgbaToColor("254,254,254,255");
		}
		if(timer >= duration) {
			color.setA(1f);
			return true;
		}
		color.setA(timer / duration);

		timer += delta;
		return true;
	}

	@Override
	public void preRender(Graphics g) {
		if(color == null) {
			color = Colors.rgbaToColor("254,254,254,255");
		}
		previousTint = g.getTint();
		g.setTint(color);
	}

	@Override
	public void postRender(Graphics g) {
		g.setTint(previousTint);
	}

	@Override
	public boolean isFinished() {
		return timer >= duration;
	}
}
