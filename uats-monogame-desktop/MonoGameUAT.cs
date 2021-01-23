/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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

using System;
using Microsoft.Xna.Framework;
using monogame.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Core.Input;
using Org.Mini2Dx.Core.Input.Button;
using Org.Mini2Dx.Core.Input.Xbox;
using Org.Mini2Dx.Core.Util;
using Org.Mini2Dx.Gdx;
using Org.Mini2Dx.Tiled;
using Color = Microsoft.Xna.Framework.Color;
using Input = Org.Mini2Dx.Gdx.Input;
using Rectangle = Org.Mini2Dx.Core.Geom.Rectangle;

namespace mini2Dx_common_uats
{
    class MonoGameUAT : Org.Mini2Dx.Core.Game.BasicGame, IDisposable
    {
        private class UATInputProcessor : InputProcessor, XboxGamePadListener
        {
            private readonly MonoGameUAT game;
            private bool isShaderApplied;
            private bool isClipApplied;
            private bool isAlphaChanged;
            public UATInputProcessor(MonoGameUAT game)
            {
                this.game = game;
            }

            public bool keyDown(int keycode)
            {
                switch (keycode)
                {
                    case Input_n_Keys.ESCAPE_:
                        Console.WriteLine("Exiting...");
                        game.exit();
                        break;
                    case Input_n_Keys.S_:
                        if (isShaderApplied)
                        {
                            Mdx.graphicsContext_.clearShader();
                        }
                        else
                        {
                            Mdx.graphicsContext_.setShader(game.sampleShader);
                        }

                        isShaderApplied = !isShaderApplied;
                        break;

                    case Input_n_Keys.C_:
                        if (isClipApplied)
                        {
                            Mdx.graphicsContext_.removeClip();
                        }
                        else
                        {
                            Mdx.graphicsContext_.setClip(game.sampleClipRectangle);
                        }

                        isClipApplied = !isClipApplied;
                        break;

                    case Input_n_Keys.Z_:
                        game.sampleFontCache.clear();
                        break;
                    case Input_n_Keys.NUM_1_:
                        game.sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT_, true);
                        break;
                    case Input_n_Keys.NUM_2_:
                        game.sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER_, true);
                        break;
                    case Input_n_Keys.NUM_3_:
                        game.sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT_, true);
                        break;
                    case Input_n_Keys.NUM_6_:
                        if (isAlphaChanged)
                        {
                            game.sampleFontCache.setAllAlphas(1);
                        }
                        else
                        {
                            game.sampleFontCache.setAllAlphas(0.2f);
                        }
                        isAlphaChanged = !isAlphaChanged;
                        break;
                }
                Console.WriteLine("keyDown({0})", keycode);
                return false;
            }

            public bool keyUp(int keycode)
            {
                Console.WriteLine("keyUp({0})", keycode);
                return false;
            }

            public bool keyTyped(char character)
            {
                Console.WriteLine("keyTyped({0})", character);
                return false;
            }

            public bool touchDown(int screenX, int screenY, int pointer, int button)
            {
                switch (button)
                {
                    case Input_n_Buttons.LEFT_:
                        {
                            if (game.music.isPlaying())
                            {
                                game.music.pause();
                            }
                            else
                            {
                                game.music.play();
                            }
                            Console.WriteLine("isPlaying: {0}", game.music.isPlaying());
                            break;
                        }

                    case Input_n_Buttons.RIGHT_:
                        game.music.setLooping(!game.music.isLooping());
                        Console.WriteLine("isLooping: {0}", game.music.isLooping());
                        break;

                    case Input_n_Buttons.MIDDLE_:
                        game.sound.play();
                        break;
                }

                Console.WriteLine("touchDown({0}, {1}, {2}, {3})", screenX, screenY, pointer, button);
                return false;
            }

            public bool touchUp(int screenX, int screenY, int pointer, int button)
            {
                Console.WriteLine("touchUp({0}, {1}, {2}, {3})", screenX, screenY, pointer, button);
                return false;
            }

            public bool touchDragged(int screenX, int screenY, int pointer)
            {
                Console.WriteLine("touchDragged({0}, {1}, {2})", screenX, screenY, pointer);
                return false;
            }

            public bool mouseMoved(int screenX, int screenY)
            {
                game.mousePosition.X = screenX;
                game.mousePosition.Y = screenY;
                //Console.WriteLine("mouseMoved({0}, {1})", screenX, screenY);
                return false;
            }


            public bool scrolled(float scrollX, float scrollY)
            {
                Console.WriteLine("scrolled({0}, {1})", scrollX, scrollY);
                return false;
            }

            public void connected(XboxGamePad xgp)
            {
                Console.WriteLine("connected({0})", xgp);
            }

            public void disconnected(XboxGamePad xgp)
            {
                Console.WriteLine("disconnected({0})", xgp);
            }

            public bool buttonDown(XboxGamePad xgp, XboxButton xb)
            {
                Console.WriteLine("buttonDown({0}, {1})", xgp, xb);
                return false;
            }

            public bool buttonUp(XboxGamePad xgp, XboxButton xb)
            {
                Console.WriteLine("buttonUp({0}, {1})", xgp, xb);
                return false;
            }

            public bool leftTriggerMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftTriggerMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightTriggerMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightTriggerMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftStickXMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftStickYMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftStickYMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickXMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickYMoved(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightStickYMoved({0}, {1})", xgp, f);
                return false;
            }
        }

        private class AudioCompletionListener : SoundCompletionListener, MusicCompletionListener
        {
            public void onSoundCompleted(long l)
            {
                Console.WriteLine("onSoundCompleted({0})", l);
            }

            public void onMusicCompleted(Music m)
            {
                Console.WriteLine("onMusicCompleted({0})", m);
            }
        }

        private Texture sampleTexture;
        private TextureRegion sampleRegion;
        private TextureRegion sampleRegion2;
        private NinePatch sampleNinePatch;
        private TilingDrawable sampleTilingDrawable;
        private TextureAtlas sampleAtlas;
        private TextureRegion sampleAtlasRegion;
        private Sprite sampleSprite;
        private Music music;
        private Sound sound;
        private Shader sampleShader;
        private Rectangle sampleClipRectangle = new Rectangle();
        private Vector2 mousePosition;
        private GameFontCache sampleFontCache;
        private TiledMap sampleMap;


        public override void initialise()
        {
            sampleClipRectangle._init_(100, 200, 250, 150);

            sampleTexture = Mdx.graphics_.newTexture(Mdx.files_.@internal("mini2Dx.png"));
            sampleRegion = Mdx.graphics_.newTextureRegion(sampleTexture);
            sampleRegion2 = Mdx.graphics_.newTextureRegion(sampleTexture).split(16, 17)[1][1];
            sampleRegion2.flip(false, true);
            sampleSprite = Mdx.graphics_.newSprite(sampleTexture);
            sampleNinePatch = Mdx.graphics_.newNinePatch(Mdx.graphics_.newTexture(Mdx.files_.@internal("ninepatch.png")), 6, 6, 6, 6);
            sampleAtlas = Mdx.graphics_.newTextureAtlas(Mdx.files_.@internal("packfile.atlas"));
            sampleAtlasRegion = sampleAtlas.findRegion("tileGreen", 47);
            sampleTilingDrawable = Mdx.graphics_.newTilingDrawable(Mdx.graphics_.newTextureRegion(Mdx.graphics_.newTexture(Mdx.files_.@internal("background.png"))));
            Mdx.graphicsContext_.setColor(Mdx.graphics_.newColor(1f, 1f, 1f, 1f));
            Mdx.graphicsContext_.setBackgroundColor(new MonoGameColor(Color.Blue));
            sampleSprite.setOriginCenter();
            music = Mdx.audio_.newMusic(Mdx.files_.@internal("music.ogg"));
            sound = Mdx.audio_.newSound(Mdx.files_.@internal("sound.wav"));

            Mdx.audio_.addMusicCompletionListener(new AudioCompletionListener());
            Mdx.audio_.addSoundCompletionListener(new AudioCompletionListener());

            Mdx.input_.newXboxGamePad((GamePad)Mdx.input_.getGamePads().get(0)).addListener(new UATInputProcessor(this));

            sampleShader = Mdx.graphics_.newShader("grayscaleShader");
            Mdx.graphicsContext_.setFont(Mdx.fonts_.newBitmapFont(Mdx.files_.@internal("arial24.fnt")));
            Mdx.graphicsContext_.getFont().setColor(Mdx.graphics_.newColor(255, 255, 255, 255));
            sampleFontCache = Mdx.graphicsContext_.getFont().newCache();
            sampleFontCache.setColor(Mdx.graphics_.newColor(255, 255, 255, 255));
            sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT_, true);
            sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER_, true);
            sampleFontCache.addText((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT_, true);
            sampleMap = new TiledMap();
            sampleMap._init_(Mdx.files_.@internal("orthogonal_no_cache.tmx"));
            Mdx.input_.setInputProcessor(new UATInputProcessor(this));
        }

        public override void update(float f)
        {
            sampleMap.update(f);
            sampleSprite.setX(sampleSprite.getX() + 1);
        }

        public override void interpolate(float alpha)
        {
            sampleSprite.setX(sampleSprite.getX() + 1 * alpha);
        }

        private void exit()
        {
            throw new NotImplementedException();
        }

        public override void render(_Graphics g)
        {
            g.setColor(Mdx.graphics_.newColor(255, 255, 255, 255));
            var gameWidth = g.getViewportWidth();
            var gameHeight = g.getViewportHeight();
            g.setTint(Mdx.graphics_.newColor(32, 32, 32, 255));
            g.drawTilingDrawable(sampleTilingDrawable, gameWidth / 8f, gameHeight / 8f, 3 * gameWidth / 4f, 3 * gameHeight / 4f);
            g.setTint(Mdx.graphics_.newColor(255, 255, 255, 255));
            g.drawRect(gameWidth / 8f, gameHeight / 8f, 3 * gameWidth / 4f, 3 * gameHeight / 4f);
            g.fillRect(400, 300, 32, 32);
            g.drawCircle(200, 200, 40);
            g.fillCircle(300, 300, 20);
            g.setColor(Mdx.graphics_.newColor(255, 0, 255, 255));
            g.drawLineSegment(100, 100, 260, 340);
            g.fillTriangle(250, 74, 222, 108, 314, 147);
            g.drawTriangle(150, 74, 122, 108, 214, 147);
            g.drawTexture(sampleTexture, 200, 100);
            g.drawTextureRegion(sampleRegion, 500, 300);
            var prevShader = g.getShader();
            g.setShader(sampleShader);
            g.drawTextureRegion(sampleAtlasRegion, 400, 200);
            g.setShader(prevShader);
            g.drawTextureRegion(sampleRegion2, 600, 150, 100, 100);
            g.drawSprite(sampleSprite);
            g.setColor(Mdx.graphics_.newColor(0, 255, 0, 255));
            g.drawNinePatch(sampleNinePatch, 150, 300, 100, 100);
            g.fillTriangle(mousePosition.X, mousePosition.Y, mousePosition.X + 10,
                mousePosition.Y + 20, mousePosition.X, mousePosition.Y + 20);
            g.drawRect(400, 65, 100, 100);
            g.drawRect(500, 65, 100, 100);
            g.drawRect(600, 65, 100, 100);
            g.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 65, 100, Align.LEFT_);
            g.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 65, 100, Align.CENTER_);
            g.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 65, 100, Align.RIGHT_);
            g.drawRect(400, 165, 100, 100);
            g.drawRect(500, 165, 100, 100);
            g.drawRect(600, 165, 100, 100);
            g.drawFontCache(sampleFontCache);
            //sampleMap.draw(g, (int) (gameWidth / 2), (int) (gameHeight / 4));
        }

        public void Dispose()
        {
            throw new NotImplementedException();
        }
    }
}
