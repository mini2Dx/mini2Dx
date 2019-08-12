namespace monogame
{
    public class MonoGamePlatformUtils : org.mini2Dx.core.PlatformUtils
    {
        public void exit(bool b)
        {
            Mini2DxGame.instance.Exit();
        }
    }
}