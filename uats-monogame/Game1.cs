using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;
using monogame;
using monogame.Graphics;
using org.mini2Dx.core.graphics;
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
        private Texture sampleTexture;
        private TextureRegion sampleRegion;
        private TextureRegion sampleRegion2;
        private Sprite sampleSprite;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
            IsMouseVisible = true;
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
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
            Console.WriteLine("FPS: {0}", 1.0/gameTime.ElapsedGameTime.TotalSeconds);
            base.Draw(gameTime);
        }
    }
}
