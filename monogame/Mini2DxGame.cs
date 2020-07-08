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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Game;
using Org.Mini2Dx.Core.Reflect.Jvm;
using Org.Mini2Dx.Core.Serialization;

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
        private MonoGameConfig config;

        public Mini2DxGame(string gameIdentifier, GameContainer game, MonoGameConfig config)
        {
            Mdx.gameIdentifier_ = gameIdentifier;
            Mdx.locks_ = new MonoGameLocks();
            XmlSerializer xmlSerializer = new XmlSerializer();
            xmlSerializer._init_();
            Mdx.xml_ = xmlSerializer;
            JsonSerializer jsonSerializer = new JsonSerializer();
            jsonSerializer._init_();
            Mdx.json_ = jsonSerializer;

            if(config.AllowUserResizing.HasValue)
            {
                Window.AllowUserResizing = config.AllowUserResizing.Value;
            }
            if (config.IsMouseVisible.HasValue)
            {
                IsMouseVisible = config.IsMouseVisible.Value;
            }

            IsFixedTimeStep = config.IsFixedTimeStep;
            graphics = new GraphicsDeviceManager(this);
            graphics.PreparingDeviceSettings += (object s, PreparingDeviceSettingsEventArgs args) =>
            {
                args.GraphicsDeviceInformation.PresentationParameters.RenderTargetUsage = RenderTargetUsage.PreserveContents;
            };
            Content.RootDirectory = "Content";

            if(config.PreferredBackBufferWidth.HasValue && config.PreferredBackBufferHeight.HasValue)
            {
                graphics.PreferredBackBufferWidth = config.PreferredBackBufferWidth.Value;
                graphics.PreferredBackBufferHeight = config.PreferredBackBufferHeight.Value;
            }
            if(config.IsFullScreen.HasValue)
            {
                graphics.IsFullScreen = config.IsFullScreen.Value;
            }
            
            this.game = game;
            this.config = config;
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
            if (config.OverridePlatform != null)
            {
                Mdx.platform_ = config.OverridePlatform;
            }
            else if (Environment.OSVersion.Platform == PlatformID.Unix)
            {
                Mdx.platform_ = Platform.LINUX_;
            }
            else if (Environment.OSVersion.Platform == PlatformID.MacOSX)
            {
                Mdx.platform_ = Platform.MAC_;
            }
            else if (Environment.OSVersion.Platform == PlatformID.Win32NT)
            {
                Mdx.platform_ = Platform.WINDOWS_;
            }
            else
            {
                throw new PlatformNotSupportedException();
            }
            Mdx.runtime_ = ApiRuntime.MONOGAME_;
            Mdx.input_ = new MonoGameInput();
            Mdx.files_ = new MonoGameFiles(Content);
            Mdx.fonts_ = new MonoGameFonts();
            Mdx.executor_ = new MonoGameTaskExecutor();
            Mdx.log_ = new MonoGameLogger();
            Mdx.playerData_ = new MonoGamePlayerData();
            Mdx.reflect_ = new JvmReflection();
            Mdx.platformUtils_ = new MonoGamePlatformUtils();
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            Mdx.graphicsContext_ = new MonoGameGraphics(GraphicsDevice);
            Mdx.graphics_ = new MonoGameGraphicsUtils(GraphicsDevice);
            Mdx.audio_ = new MonoGameAudio();
            game.start(Mdx.graphicsContext_);
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
                Mdx.platformUtils_.markUpdateBegin();
                ((MonoGameInput)Mdx.input_).update();
                ((MonoGameAudio)Mdx.audio_).update();
                game.update(targetTimeStep);
                _timeAccumulator -= targetTimeStep;
                Mdx.platformUtils_.markUpdateEnd();
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
            Mdx.platformUtils_.markFrame();
            Mdx.graphicsContext_.preRender(Mdx.graphicsContext_.getWindowWidth(), Mdx.graphicsContext_.getWindowHeight());
            Mdx.platformUtils_.markRenderBegin();
            game.render(Mdx.graphicsContext_);
            Mdx.platformUtils_.markRenderEnd();
            Mdx.graphicsContext_.postRender();
            base.Draw(gameTime);
        }

        public MonoGameConfig getConfig()
        {
            return config;
        }
    }
}
