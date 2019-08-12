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
using org.mini2Dx.core;
using org.mini2Dx.core.game;
using org.mini2Dx.core.reflect.jvm;
using org.mini2Dx.core.serialization;

namespace monogame
{
    public class Mini2DxGame : Game
    {
        public static Mini2DxGame instance;
        
        GraphicsDeviceManager graphics;
        private GameContainer game;
        private const float targetFPS = 60;
        private const float targetTimeStep = 1 / targetFPS;

        private const float _updateMaximumDelta = targetTimeStep;
        private float _timeAccumulator;

        public Mini2DxGame(GameContainer game)
        {
            Window.AllowUserResizing = true;
            IsMouseVisible = true;
            IsFixedTimeStep = false;
            graphics = new GraphicsDeviceManager(this);
            graphics.PreparingDeviceSettings += (object s, PreparingDeviceSettingsEventArgs args) =>
            {
                args.GraphicsDeviceInformation.PresentationParameters.RenderTargetUsage = RenderTargetUsage.PreserveContents;
            };
            Content.RootDirectory = "Content";
            graphics.PreferredBackBufferWidth = 800;
            graphics.PreferredBackBufferHeight = 720;
            this.game = game;
            instance = this;
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            if (Environment.OSVersion.Platform == PlatformID.Unix)
            {
                Mdx.platform = Platform.LINUX;
            }
            else if (Environment.OSVersion.Platform == PlatformID.MacOSX)
            {
                Mdx.platform = Platform.MAC;
            }
            else if (Environment.OSVersion.Platform == PlatformID.Win32NT)
            {
                Mdx.platform = Platform.WINDOWS;
            }
            else
            {
                throw new PlatformNotSupportedException();
            }
            Mdx.input = new MonoGameInput();
            Mdx.files = new MonoGameFiles(Content);
            Mdx.fonts = new MonoGameFonts();
            Mdx.executor = new MonoGameTaskExecutor();
            Mdx.log = new MonoGameLogger();
            Mdx.playerData = new MonoGamePlayerData();
            Mdx.reflect = new JvmReflection();
            Mdx.platformUtils = new MonoGamePlatformUtils();
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
            game.start(Mdx.graphicsContext);
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
            float delta = (float)gameTime.ElapsedGameTime.TotalSeconds;

            if (delta > _updateMaximumDelta)
            {
                delta = _updateMaximumDelta;
            }

            _timeAccumulator += delta;

            while (_timeAccumulator >= targetTimeStep)
            {
                ((MonoGameInput)Mdx.input).update();
                ((MonoGameAudio)Mdx.audio).update();
                game.update(targetTimeStep);
                _timeAccumulator -= targetTimeStep;
            }
            game.interpolate(_timeAccumulator / targetTimeStep);

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            Mdx.graphicsContext.preRender(Mdx.graphicsContext.getWindowWidth(), Mdx.graphicsContext.getWindowHeight());
            game.render(Mdx.graphicsContext);
            Mdx.graphicsContext.postRender();
            base.Draw(gameTime);
        }
    }
}
