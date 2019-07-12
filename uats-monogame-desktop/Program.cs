using monogame;
using org.mini2Dx.core.game;
using org.mini2Dx.uats.util;
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

            GameContainer game = (UAT_APP ? new UATApplication() as GameContainer : new MonoGameUAT());

            using (var mini2DxGame = new Mini2DxGame(game))
                mini2DxGame.Run();
        }
    }
}
