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
using monogame;
using monogame.Graphics;
using org.mini2Dx.core;
using org.mini2Dx.core.audio;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx;
using Color = Microsoft.Xna.Framework.Color;
using Input = org.mini2Dx.gdx.Input;
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
        private Sprite sampleSprite;
        private Music music;
        private Sound sound;
        private Vector2 mousePosition;
        
        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
        }

        private class UATInputProcessor : InputProcessor
        {
            private readonly Game1 game;

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
            Mdx.graphicsContext.setColor(new MonoGameColor(255, 255, 255, 255));
            Mdx.graphicsContext.setBackgroundColor(new MonoGameColor(Color.Blue));
            sampleSprite.setOriginCenter();
            music = Mdx.audio.newMusic(Mdx.files.@internal("music.ogg"));
            sound = Mdx.audio.newSound(Mdx.files.@internal("sound.wav"));
            
            Mdx.audio.addMusicCompletionListener(new AudioCompletionListener());
            Mdx.audio.addSoundCompletionListener(new AudioCompletionListener());
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
            Mdx.graphicsContext.preRender(GraphicsDevice.Viewport.Bounds.Width, GraphicsDevice.Viewport.Bounds.Height);
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.White));
            
            var windowWidth = Mdx.graphicsContext.getWindowWidth();
            var windowHeight = Mdx.graphicsContext.getWindowHeight();
            Mdx.graphicsContext.drawRect(windowWidth/8f, windowHeight/8f, 3 * windowWidth/4f, 3 * windowHeight/4f);
            Mdx.graphicsContext.fillRect(400, 300, 32, 32);
            Mdx.graphicsContext.drawCircle(200, 200, 40);
            Mdx.graphicsContext.fillCircle(300, 300, 20);
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.Fuchsia));
            Mdx.graphicsContext.drawLineSegment(100, 100, 260, 340);
            Mdx.graphicsContext.fillTriangle(250, 74, 222, 108, 314, 147);
            Mdx.graphicsContext.drawTriangle(150, 74, 122, 108, 214, 147);
            Mdx.graphicsContext.drawTexture(sampleTexture, 200, 100);
            Mdx.graphicsContext.drawTextureRegion(sampleRegion, 500, 300);
            Mdx.graphicsContext.drawTextureRegion(sampleRegion2, 600, 150, 100, 100);
            sampleSprite.setOriginBasedPosition(windowWidth / 2f, windowHeight / 2f);
            Mdx.graphicsContext.drawSprite(sampleSprite);
            Mdx.graphicsContext.setColor(new MonoGameColor(Color.Green));
            Mdx.graphicsContext.fillTriangle(mousePosition.X, mousePosition.Y, mousePosition.X + 10,
                mousePosition.Y + 20, mousePosition.X, mousePosition.Y + 20);
            
            Mdx.graphicsContext.postRender();
            Console.WriteLine("FPS: {0}", 1.0/gameTime.ElapsedGameTime.TotalSeconds);
            base.Draw(gameTime);
        }
    }
}
