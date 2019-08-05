/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.artemis;

import org.mini2Dx.core.Graphics;

/**
 * Implements {@link BaseSystem} to add mini2Dx's render method
 */
public abstract class BaseRenderingSystem extends BaseSystem implements RenderingSystem {
    private MdxWorld mdxWorld;

    /**
     * An overridable method called once before the system renders
     * @param g
     */
    protected void preRender(Graphics g) {
    }

    /**
     * Renders the system
     * @param g The {@link Graphics} context
     */
    protected abstract void render(Graphics g);

    /**
     * An overridable method called once after the system renders
     * @param g
     */
    protected void postRender(Graphics g) {
    }

    @Override
    public void renderSystem(Graphics g) {
        if (mdxWorld == null) {
            return;
        }
        preRender(g);
        render(g);
        postRender(g);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if (world instanceof MdxWorld) {
            this.mdxWorld = (MdxWorld) world;
        }
    }
}
