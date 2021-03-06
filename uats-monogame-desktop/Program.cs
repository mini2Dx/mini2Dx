﻿using monogame;
using Org.Mini2Dx.Core.Game;
using Org.Mini2Dx.Uats.Util;
using System;

namespace mini2Dx_common_uats
{
    /// <summary>
    /// The main class.
    /// </summary>
    public static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            bool UAT_APP = true;

            Java.Lang.SystemPropertiesOverride.OVERRIDE_USER_LANGUAGE = "en";

            GameContainer game = (UAT_APP ? new UATApplication() as GameContainer : new MonoGameUAT());
            game._init_();

            UATApplication.USE_AOT_DATA_ = true;

            MonoGameConfig config = new MonoGameConfig();
            config.IsMouseVisible = true;
            config.PreferredBackBufferWidth = 800;
            config.PreferredBackBufferHeight = 720;
            config.AllowUserResizing = true;

            using (var mini2DxGame = new Mini2DxGame("org.mini2Dx.uat", game, config))
                mini2DxGame.Run();

            Java.Lang.System.exit_3518BA33(0);
        }
    }
}
