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

            public bool keyDown_4118CD17(int keycode)
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
                            Mdx.graphicsContext_.clearShader_EFE09FC0();
                        }
                        else
                        {
                            Mdx.graphicsContext_.setShader_09F44B85(game.sampleShader);
                        }

                        isShaderApplied = !isShaderApplied;
                        break;

                    case Input_n_Keys.C_:
                        if (isClipApplied)
                        {
                            Mdx.graphicsContext_.removeClip_A029B76C();
                        }
                        else
                        {
                            Mdx.graphicsContext_.setClip_477DF50E(game.sampleClipRectangle);
                        }

                        isClipApplied = !isClipApplied;
                        break;

                    case Input_n_Keys.Z_:
                        game.sampleFontCache.clear_EFE09FC0();
                        break;
                    case Input_n_Keys.NUM_1_:
                        game.sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT_, true);
                        break;
                    case Input_n_Keys.NUM_2_:
                        game.sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER_, true);
                        break;
                    case Input_n_Keys.NUM_3_:
                        game.sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT_, true);
                        break;
                    case Input_n_Keys.NUM_6_:
                        if (isAlphaChanged)
                        {
                            game.sampleFontCache.setAllAlphas_97413DCA(1);
                        }
                        else
                        {
                            game.sampleFontCache.setAllAlphas_97413DCA(0.2f);
                        }
                        isAlphaChanged = !isAlphaChanged;
                        break;
                }
                Console.WriteLine("keyDown({0})", keycode);
                return false;
            }

            public bool keyTyped_0E996675(char character)
            {
                Console.WriteLine("keyTyped({0})", character);
                return false;
            }

            public bool keyUp_4118CD17(int keycode)
            {
                Console.WriteLine("keyUp({0})", keycode);
                return false;
            }

            public bool mouseMoved_1E4D20DC(int screenX, int screenY)
            {
                game.mousePosition.X = screenX;
                game.mousePosition.Y = screenY;
                //Console.WriteLine("mouseMoved({0}, {1})", screenX, screenY);
                return false;
            }

            public bool scrolled_1548FAA4(float scrollX, float scrollY)
            {
                Console.WriteLine("scrolled({0}, {1})", scrollX, scrollY);
                return false;
            }

            public bool touchDown_A890D1B4(int screenX, int screenY, int pointer, int button)
            {
                switch (button)
                {
                    case Input_n_Buttons.LEFT_:
                        {
                            if (game.music.isPlaying_FBE0B2A4())
                            {
                                game.music.pause_EFE09FC0();
                            }
                            else
                            {
                                game.music.play_EFE09FC0();
                            }
                            Console.WriteLine("isPlaying: {0}", game.music.isPlaying_FBE0B2A4());
                            break;
                        }

                    case Input_n_Buttons.RIGHT_:
                        game.music.setLooping_AA5A2C66(!game.music.isLooping_FBE0B2A4());
                        Console.WriteLine("isLooping: {0}", game.music.isLooping_FBE0B2A4());
                        break;

                    case Input_n_Buttons.MIDDLE_:
                        game.sound.play_0BE0CBD4();
                        break;
                }

                Console.WriteLine("touchDown({0}, {1}, {2}, {3})", screenX, screenY, pointer, button);
                return false;
            }

            public bool touchDragged_F8B7DE3F(int screenX, int screenY, int pointer)
            {
                Console.WriteLine("touchDragged({0}, {1}, {2})", screenX, screenY, pointer);
                return false;
            }

            public bool touchUp_A890D1B4(int screenX, int screenY, int pointer, int button)
            {
                Console.WriteLine("touchUp({0}, {1}, {2}, {3})", screenX, screenY, pointer, button);
                return false;
            }

            public void connected_D359CF93(XboxGamePad xgp)
            {
                Console.WriteLine("connected({0})", xgp);
            }

            public void disconnected_D359CF93(XboxGamePad xgp)
            {
                Console.WriteLine("disconnected({0})", xgp);
            }

            public bool buttonDown_5B3EED86(XboxGamePad xgp, XboxButton xb)
            {
                Console.WriteLine("buttonDown({0}, {1})", xgp, xb);
                return false;
            }

            public bool buttonUp_5B3EED86(XboxGamePad xgp, XboxButton xb)
            {
                Console.WriteLine("buttonUp({0}, {1})", xgp, xb);
                return false;
            }

            public bool leftStickXMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftStickYMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftStickYMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftTriggerMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("leftTriggerMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickXMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickYMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightStickYMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightTriggerMoved_2D777521(XboxGamePad xgp, float f)
            {
                Console.WriteLine("rightTriggerMoved({0}, {1})", xgp, f);
                return false;
            }
        }

        private class AudioCompletionListener : SoundCompletionListener, MusicCompletionListener
        {

            public void onSoundCompleted_5FE5E296(long l)
            {
                Console.WriteLine("onSoundCompleted({0})", l);
            }

            public void onMusicCompleted_2E9F961C(Music m)
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

        public override void initialise_EFE09FC0()
        {
            sampleClipRectangle._init_C2EDAFC0(100, 200, 250, 150);

            sampleTexture = Mdx.graphics_.newTexture_69120FDF(Mdx.files_.internal_1F3F44D2("mini2Dx.png"));
            sampleRegion = Mdx.graphics_.newTextureRegion_F6DA8362(sampleTexture);
            sampleRegion2 = Mdx.graphics_.newTextureRegion_F6DA8362(sampleTexture).split_B458A465(16, 17)[1][1];
            sampleRegion2.flip_62FCD310(false, true);
            sampleSprite = Mdx.graphics_.newSprite_768542D8(sampleTexture);
            sampleNinePatch = Mdx.graphics_.newNinePatch_D8829279(Mdx.graphics_.newTexture_69120FDF(Mdx.files_.internal_1F3F44D2("ninepatch.png")), 6, 6, 6, 6);
            sampleAtlas = Mdx.graphics_.newTextureAtlas_FCEC0F4A(Mdx.files_.internal_1F3F44D2("packfile.atlas"));
            sampleAtlasRegion = sampleAtlas.findRegion_2DF11685("tileGreen", 47);
            sampleTilingDrawable = Mdx.graphics_.newTilingDrawable_765BF89E(Mdx.graphics_.newTextureRegion_F6DA8362(Mdx.graphics_.newTexture_69120FDF(Mdx.files_.internal_1F3F44D2("background.png"))));
            Mdx.graphicsContext_.setColor_24D51C91(Mdx.graphics_.newColor_DF74E9CF(1f, 1f, 1f, 1f));
            Mdx.graphicsContext_.setBackgroundColor_24D51C91(new MonoGameColor(Color.Blue));
            sampleSprite.setOriginCenter_EFE09FC0();
            music = Mdx.audio_.newMusic_C995EB38(Mdx.files_.internal_1F3F44D2("music.ogg"));
            sound = Mdx.audio_.newSound_F8578266(Mdx.files_.internal_1F3F44D2("sound.wav"));

            Mdx.audio_.addMusicCompletionListener_FA743BBA(new AudioCompletionListener());
            Mdx.audio_.addSoundCompletionListener_6D852ABC(new AudioCompletionListener());

            Mdx.input_.newXboxGamePad_1507DC33((GamePad)Mdx.input_.getGamePads_9BE31B41().get_6B44366F(0)).addListener_14A332D5(new UATInputProcessor(this));

            sampleShader = Mdx.graphics_.newShader_FAA50909("grayscaleShader");
            Mdx.graphicsContext_.setFont_6B60E80F(Mdx.fonts_.newBitmapFont_4C54323B(Mdx.files_.internal_1F3F44D2("arial24.fnt")));
            Mdx.graphicsContext_.getFont_0370ED29().setColor_24D51C91(Mdx.graphics_.newColor_AD27635F(255, 255, 255, 255));
            sampleFontCache = Mdx.graphicsContext_.getFont_0370ED29().newCache_8BFDA5A5();
            sampleFontCache.setColor_24D51C91(Mdx.graphics_.newColor_AD27635F(255, 255, 255, 255));
            sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT_, true);
            sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER_, true);
            sampleFontCache.addText_EAACE007((Java.Lang.String)"Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT_, true);
            sampleMap = new TiledMap();
            sampleMap._init_88D76E6E(Mdx.files_.internal_1F3F44D2("orthogonal_no_cache.tmx"));
            Mdx.input_.setInputProcessor_8E738C44(new UATInputProcessor(this));
        }

        public override void update_97413DCA(float f)
        {
            sampleMap.update_97413DCA(f);
            sampleSprite.setX_97413DCA(sampleSprite.getX_FFE0B8F0() + 1);
        }

        public override void interpolate_97413DCA(float alpha)
        {
            sampleSprite.setX_97413DCA(sampleSprite.getX_FFE0B8F0() + 1 * alpha);
        }

        public override void render_2CFA5803(_Graphics g)
        {
            g.setColor_24D51C91(Mdx.graphics_.newColor_AD27635F(255, 255, 255, 255));
            var gameWidth = g.getViewportWidth_FFE0B8F0();
            var gameHeight = g.getViewportHeight_FFE0B8F0();
            g.setTint_24D51C91(Mdx.graphics_.newColor_AD27635F(32, 32, 32, 255));
            g.drawTilingDrawable_68C092E7(sampleTilingDrawable, gameWidth / 8f, gameHeight / 8f, 3 * gameWidth / 4f, 3 * gameHeight / 4f);
            g.setTint_24D51C91(Mdx.graphics_.newColor_AD27635F(255, 255, 255, 255));
            g.drawRect_C2EDAFC0(gameWidth / 8f, gameHeight / 8f, 3 * gameWidth / 4f, 3 * gameHeight / 4f);
            g.fillRect_C2EDAFC0(400, 300, 32, 32);
            g.drawCircle_4556C5CA(200, 200, 40);
            g.fillCircle_4556C5CA(300, 300, 20);
            g.setColor_24D51C91(Mdx.graphics_.newColor_AD27635F(255, 0, 255, 255));
            g.drawLineSegment_C2EDAFC0(100, 100, 260, 340);
            g.fillTriangle_0416F7C0(250, 74, 222, 108, 314, 147);
            g.drawTriangle_0416F7C0(150, 74, 122, 108, 214, 147);
            g.drawTexture_95EC6133(sampleTexture, 200, 100);
            g.drawTextureRegion_C70A626B(sampleRegion, 500, 300);
            var prevShader = g.getShader_364FDDC3();
            g.setShader_09F44B85(sampleShader);
            g.drawTextureRegion_C70A626B(sampleAtlasRegion, 400, 200);
            g.setShader_09F44B85(prevShader);
            g.drawTextureRegion_1F25C0DB(sampleRegion2, 600, 150, 100, 100);
            g.drawSprite_615359F5(sampleSprite);
            g.setColor_24D51C91(Mdx.graphics_.newColor_AD27635F(0, 255, 0, 255));
            g.drawNinePatch_EE654A84(sampleNinePatch, 150, 300, 100, 100);
            g.fillTriangle_0416F7C0(mousePosition.X, mousePosition.Y, mousePosition.X + 10,
                mousePosition.Y + 20, mousePosition.X, mousePosition.Y + 20);
            g.drawRect_C2EDAFC0(400, 65, 100, 100);
            g.drawRect_C2EDAFC0(500, 65, 100, 100);
            g.drawRect_C2EDAFC0(600, 65, 100, 100);
            g.drawString_8338FD87("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 65, 100, Align.LEFT_);
            g.drawString_8338FD87("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 65, 100, Align.CENTER_);
            g.drawString_8338FD87("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 65, 100, Align.RIGHT_);
            g.drawRect_C2EDAFC0(400, 165, 100, 100);
            g.drawRect_C2EDAFC0(500, 165, 100, 100);
            g.drawRect_C2EDAFC0(600, 165, 100, 100);
            g.drawFontCache_CF61996B(sampleFontCache);
            //sampleMap.draw(g, (int) (gameWidth / 2), (int) (gameHeight / 4));
        }

        private void exit()
        {
            throw new NotImplementedException();
        }

        public void Dispose()
        {
            throw new NotImplementedException();
        }
    }
}
