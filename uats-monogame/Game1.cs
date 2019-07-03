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
using Microsoft.Xna.Framework.Graphics;
using monogame;
using monogame.Graphics;
using org.mini2Dx.core;
using org.mini2Dx.core.audio;
using org.mini2Dx.core.font;
using org.mini2Dx.core.graphics;
using org.mini2Dx.core.input.button;
using org.mini2Dx.core.input.xbox360;
using org.mini2Dx.core.util;
using org.mini2Dx.gdx;
using Color = Microsoft.Xna.Framework.Color;
using GamePad = org.mini2Dx.core.input.GamePad;
using Input = org.mini2Dx.gdx.Input;
using Rectangle = org.mini2Dx.core.geom.Rectangle;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace uats_monogame
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
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
        private Rectangle sampleClipRectangle = new Rectangle(100, 200, 250, 150);
        private Vector2 mousePosition;
        private GameFontCache sampleFontCache;
        
        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            graphics.PreparingDeviceSettings += (object s, PreparingDeviceSettingsEventArgs args) =>
            {
                args.GraphicsDeviceInformation.PresentationParameters.RenderTargetUsage = RenderTargetUsage.PreserveContents;
            };
            Content.RootDirectory = "Content";
        }

        private class UATInputProcessor : InputProcessor, Xbox360GamePadListener
        {
            private readonly Game1 game;
            private bool isShaderApplied;
            private bool isClipApplied;
            private bool isAlphaChanged;
            public UATInputProcessor(Game1 game)
            {
                this.game = game;
            }

            public bool keyDown(int keycode)
            {
                if (keycode == Input.Keys.ESCAPE)
                {
                    Console.WriteLine("Exiting...");
                    game.Exit();
                }
                else if (keycode == Input.Keys.S)
                {
                    if (isShaderApplied)
                    {
                        Mdx.graphicsContext.clearShader();
                    }
                    else
                    {
                        Mdx.graphicsContext.setShader(game.sampleShader);
                    }

                    isShaderApplied = !isShaderApplied;
                }
                else if (keycode == Input.Keys.C)
                {
                    if (isClipApplied)
                    {
                        Mdx.graphicsContext.removeClip();
                    }
                    else
                    {
                        Mdx.graphicsContext.setClip(game.sampleClipRectangle);
                    }

                    isClipApplied = !isClipApplied;
                }
                else if (keycode == Input.Keys.Z)
                {
                    game.sampleFontCache.clear();
                }
                else if (keycode == Input.Keys.NUM_1)
                {
                    game.sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT, true);
                }
                else if (keycode == Input.Keys.NUM_2)
                {
                    game.sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER, true);
                }
                else if (keycode == Input.Keys.NUM_3)
                {
                    game.sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT, true);
                }
                else if (keycode == Input.Keys.NUM_4)
                {
                    if (((MonoGameColor)game.sampleFontCache.getColor()).toMonoGameColor() == Color.White)
                    {
                        game.sampleFontCache.setColor(new MonoGameColor(Color.Blue));
                    }
                    else
                    {
                        game.sampleFontCache.setColor(new MonoGameColor(Color.White));
                    }
                }
                else if (keycode == Input.Keys.NUM_5)
                {
                    if (((MonoGameColor)game.sampleFontCache.getColor()).toMonoGameColor() == Color.White)
                    {
                        game.sampleFontCache.setAllColors(new MonoGameColor(Color.Blue));
                    }
                    else
                    {
                        game.sampleFontCache.setAllColors(new MonoGameColor(Color.White));
                    }
                }
                else if (keycode == Input.Keys.NUM_6)
                {
                    if (isAlphaChanged)
                    {
                        game.sampleFontCache.setAllAlphas(1);
                    }
                    else
                    {
                        game.sampleFontCache.setAllAlphas(0.2f);
                    }

                    isAlphaChanged = !isAlphaChanged;
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
                    case Input.Buttons.LEFT:
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

                    case Input.Buttons.RIGHT:
                        game.music.setLooping(!game.music.isLooping());
                        Console.WriteLine("isLooping: {0}", game.music.isLooping());
                        break;
                    
                    case Input.Buttons.MIDDLE:
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
                Console.WriteLine("mouseMoved({0}, {1})", screenX, screenY);
                return false;
            }

            public bool scrolled(int amount)
            {
                Console.WriteLine("scrolled({0})", amount);
                return false;
            }

            public void connected(Xbox360GamePad xgp)
            {
                Console.WriteLine("connected({0})", xgp);
            }

            public void disconnected(Xbox360GamePad xgp)
            {
                Console.WriteLine("disconnected({0})", xgp);
            }

            public bool buttonDown(Xbox360GamePad xgp, Xbox360Button xb)
            {
                Console.WriteLine("buttonDown({0}, {1})", xgp, xb);
                return false;
            }

            public bool buttonUp(Xbox360GamePad xgp, Xbox360Button xb)
            {
                Console.WriteLine("buttonUp({0}, {1})", xgp, xb);
                return false;
            }

            public bool leftTriggerMoved(Xbox360GamePad xgp, float f)
            {
                Console.WriteLine("leftTriggerMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightTriggerMoved(Xbox360GamePad xgp, float f)
            {
                Console.WriteLine("rightTriggerMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftStickXMoved(Xbox360GamePad xgp, float f)
            {
                Console.WriteLine("leftStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool leftStickYMoved(Xbox360GamePad xgp, float f)
            {
                Console.WriteLine("leftStickYMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickXMoved(Xbox360GamePad xgp, float f)
            {
                Console.WriteLine("rightStickXMoved({0}, {1})", xgp, f);
                return false;
            }

            public bool rightStickYMoved(Xbox360GamePad xgp, float f)
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
        
        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            Mdx.input = new MonoGameInput();
            Mdx.files = new MonoGameFiles(Content);
            Mdx.fonts = new MonoGameFonts();
            Mdx.input.setInputProcessor(new UATInputProcessor(this));
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            Mdx.graphicsContext = new MonoGameGraphics(GraphicsDevice);
            Mdx.graphics = new MonoGameGraphicsUtils(GraphicsDevice);
            Mdx.audio = new MonoGameAudio();

            sampleTexture = Mdx.graphics.newTexture(Mdx.files.@internal("mini2Dx.png"));
            sampleRegion = Mdx.graphics.newTextureRegion(sampleTexture);
            sampleRegion2 = Mdx.graphics.newTextureRegion(sampleTexture).split(16,17)[1][1];
            sampleRegion2.flip(false, true);
            sampleSprite = Mdx.graphics.newSprite(sampleTexture);
            sampleNinePatch = Mdx.graphics.newNinePatch(Mdx.graphics.newTexture(Mdx.files.@internal("ninepatch.png")), 6, 6, 6, 6);
            sampleAtlas = Mdx.graphics.newTextureAtlas(Mdx.files.@internal("packfile.atlas"));
            sampleAtlasRegion = sampleAtlas.findRegion("tileGreen", 47);
            sampleTilingDrawable = new MonoGameTilingDrawable(Mdx.graphics.newTexture(Mdx.files.@internal("background.png")));
            Mdx.graphicsContext.setColor(new MonoGameColor(255, 255, 255, 255));
            Mdx.graphicsContext.setBackgroundColor(new MonoGameColor(Color.Blue));
            sampleSprite.setOriginCenter();
            music = Mdx.audio.newMusic(Mdx.files.@internal("music.ogg"));
            sound = Mdx.audio.newSound(Mdx.files.@internal("sound.wav"));
            
            Mdx.audio.addMusicCompletionListener(new AudioCompletionListener());
            Mdx.audio.addSoundCompletionListener(new AudioCompletionListener());
            
            Mdx.input.newXbox360GamePad((GamePad)Mdx.input.getGamePads().get(0)).addListener(new UATInputProcessor(this));

            sampleShader = new MonoGameShader("grayscaleShader");
            Mdx.graphicsContext.setFont(Mdx.fonts.newBitmapFont(Mdx.files.@internal("arial24.fnt")));
            sampleFontCache = Mdx.graphicsContext.getFont().newCache();
            sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400, 165, 100, Align.LEFT, true);
            sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 165, 100, Align.CENTER, true);
            sampleFontCache.addText("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 165, 100, Align.RIGHT, true);
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// game-specific content.
        /// </summary>
        protected override void UnloadContent()
        {
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            ((MonoGameInput)Mdx.input).update();
            ((MonoGameAudio)Mdx.audio).update();
            base.Update(gameTime);
        }
        
        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            Mdx.graphicsContext.preRender(Mdx.graphicsContext.getWindowWidth(), Mdx.graphicsContext.getWindowHeight());
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.White));
            
            var gameWidth = Mdx.graphicsContext.getViewportWidth();
            var gameHeight = Mdx.graphicsContext.getViewportHeight();
            Mdx.graphicsContext.setTint(new MonoGameColor(32, 32, 32, 255));
            Mdx.graphicsContext.drawTilingDrawable(sampleTilingDrawable, gameWidth/8f, gameHeight/8f, 3 * gameWidth/4f, 3 * gameHeight/4f);
            Mdx.graphicsContext.setTint(new MonoGameColor(Color.White));
            Mdx.graphicsContext.drawRect(gameWidth/8f, gameHeight/8f, 3 * gameWidth/4f, 3 * gameHeight/4f);
            Mdx.graphicsContext.fillRect(400, 300, 32, 32);
            Mdx.graphicsContext.drawCircle(200, 200, 40);
            Mdx.graphicsContext.fillCircle(300, 300, 20);
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.Fuchsia));
            Mdx.graphicsContext.drawLineSegment(100, 100, 260, 340);
            Mdx.graphicsContext.fillTriangle(250, 74, 222, 108, 314, 147);
            Mdx.graphicsContext.drawTriangle(150, 74, 122, 108, 214, 147);
            Mdx.graphicsContext.drawTexture(sampleTexture, 200, 100);
            Mdx.graphicsContext.drawTextureRegion(sampleRegion, 500, 300);
            var prevShader = Mdx.graphicsContext.getShader();
            Mdx.graphicsContext.setShader(sampleShader);
            Mdx.graphicsContext.drawTextureRegion(sampleAtlasRegion, 400, 200);
            Mdx.graphicsContext.setShader(prevShader);
            Mdx.graphicsContext.drawTextureRegion(sampleRegion2, 600, 150, 100, 100);
            sampleSprite.setOriginBasedPosition(gameWidth / 2, gameHeight / 2);
            Mdx.graphicsContext.drawSprite(sampleSprite);
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.Green));
            Mdx.graphicsContext.drawNinePatch(sampleNinePatch, 150, 300, 100, 100);
            Mdx.graphicsContext.fillTriangle(mousePosition.X, mousePosition.Y, mousePosition.X + 10,
                mousePosition.Y + 20, mousePosition.X, mousePosition.Y + 20);
            Mdx.graphicsContext.drawRect(400, 65, 100, 100);
            Mdx.graphicsContext.drawRect(500, 65, 100, 100);
            Mdx.graphicsContext.drawRect(600, 65, 100, 100);
            Mdx.graphicsContext.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 400,65, 100, Align.LEFT);
            Mdx.graphicsContext.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 500, 65, 100, Align.CENTER);
            Mdx.graphicsContext.drawString("Hello!\nBonjour!\nCiao!\nGuten tag!\nNamaste!", 600, 65, 100, Align.RIGHT);
            Mdx.graphicsContext.drawRect(400, 165, 100, 100);
            Mdx.graphicsContext.drawRect(500, 165, 100, 100);
            Mdx.graphicsContext.drawRect(600, 165, 100, 100);
            Mdx.graphicsContext.drawFontCache(sampleFontCache);
            Mdx.graphicsContext.postRender();
            //Console.WriteLine("FPS: {0}", 1.0/gameTime.ElapsedGameTime.TotalSeconds);
            base.Draw(gameTime);
        }
    }
}
