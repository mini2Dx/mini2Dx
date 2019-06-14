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
using Microsoft.Xna.Framework.Input;
using monogame;
using monogame.Graphics;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx;
using Color = Microsoft.Xna.Framework.Color;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace uats_monogame
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
        private MonoGameGraphics mDxGraphics;
        private MonoGameGraphicsUtils graphicsUtils;
        private MonoGameFiles files;
        private MonoGameInput input;
        private Texture sampleTexture;
        private TextureRegion sampleRegion;
        private TextureRegion sampleRegion2;
        private Sprite sampleSprite;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
            IsMouseVisible = true;
            input = new MonoGameInput();
        }

        private class UATInputProcessor : InputProcessor
        {
            public bool keyDown(int keycode)
            {
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
                Console.WriteLine("mouseMoved({0}, {1})", screenX, screenY);
                return false;
            }

            public bool scrolled(int amount)
            {
                Console.WriteLine("scrolled({0})", amount);
                return false;
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
            input.setInputProcessor(new UATInputProcessor());
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            mDxGraphics = new MonoGameGraphics(GraphicsDevice);
            graphicsUtils = new MonoGameGraphicsUtils(GraphicsDevice);
            files = new MonoGameFiles(Content);
            sampleTexture = graphicsUtils.newTexture(files.@internal("mini2Dx.png"));
            sampleRegion = graphicsUtils.newTextureRegion(sampleTexture);
            sampleRegion2 = graphicsUtils.newTextureRegion(sampleTexture).split(16,17)[1][1];
            sampleRegion2.flip(false, true);
            sampleSprite = graphicsUtils.newSprite(sampleTexture);
            mDxGraphics.setColor(new MonoGameColor(255, 255, 255, 255));
            mDxGraphics.setBackgroundColor(new MonoGameColor(Color.Blue));
            sampleSprite.setOriginCenter();
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
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                Exit();

            input.update();
            base.Update(gameTime);
        }
        
        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            mDxGraphics.setColor(new MonoGameColor(Color.White));
            mDxGraphics.preRender(GraphicsDevice.Viewport.Bounds.Width, GraphicsDevice.Viewport.Bounds.Height);
            mDxGraphics.drawRect(mDxGraphics.getWindowWidth()/8f, mDxGraphics.getWindowHeight()/8f, 3 * mDxGraphics.getWindowWidth()/4f, 3 * mDxGraphics.getWindowHeight()/4f);
            mDxGraphics.fillRect(400, 300, 32, 32);
            mDxGraphics.drawCircle(200, 200, 40);
            mDxGraphics.fillCircle(300, 300, 20);
            mDxGraphics.setColor(new MonoGameColor(Color.Fuchsia));
            mDxGraphics.drawLineSegment(100, 100, 260, 340);
            mDxGraphics.fillTriangle(250, 74, 222, 108, 314, 147);
            mDxGraphics.drawTriangle(150, 74, 122, 108, 214, 147);
            mDxGraphics.drawTexture(sampleTexture, 200, 100);
            mDxGraphics.drawTextureRegion(sampleRegion, 500, 300);
            mDxGraphics.drawTextureRegion(sampleRegion2, 600, 150, 100, 100);
            sampleSprite.setOriginBasedPosition(mDxGraphics.getWindowWidth() / 2f, mDxGraphics.getWindowHeight() / 2f);
            mDxGraphics.drawSprite(sampleSprite);
            mDxGraphics.postRender();
            //Console.WriteLine("FPS: {0}", 1.0/gameTime.ElapsedGameTime.TotalSeconds);
            base.Draw(gameTime);
        }
    }
}
