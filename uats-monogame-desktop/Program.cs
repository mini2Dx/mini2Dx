using monogame;
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

            GameContainer game = (UAT_APP ? new UATApplication() as GameContainer : new MonoGameUAT());
            game._init_();

            using (var mini2DxGame = new Mini2DxGame(game))
                mini2DxGame.Run();
        }
    }
}
