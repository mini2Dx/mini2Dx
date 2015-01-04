/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.uats.common;

import org.mini2Dx.core.audio.CrossFadingMusicLoop;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.desktop.DesktopMini2DxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Utility instance for running a UAT for {@link CrossFadingMusicLoop}
 */
public class CrossFadingMusicLoopUAT extends GameContainer {
    private CrossFadingMusicLoop musicLoop;

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialise() {
        musicLoop = new CrossFadingMusicLoop(Gdx.files.classpath("crossfade.ogg"), 81.1618f, 10f);
    }

    @Override
    public void update(float delta) {
        if (!musicLoop.isPlaying()) {
            musicLoop.play();
        }
        musicLoop.update();
    }

    @Override
    public void interpolate(float alpha) {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "mini2Dx - CrossFadingMusicLoop Verification Test";
        cfg.width = 240;
        cfg.height = 240;
        cfg.stencil = 8;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 0;
        cfg.backgroundFPS = 0;
        new LwjglApplication(new DesktopMini2DxGame("org.mini2Dx.uats.common.CrossFadingMusicUAT", new CrossFadingMusicLoopUAT()), cfg);
    }
}
