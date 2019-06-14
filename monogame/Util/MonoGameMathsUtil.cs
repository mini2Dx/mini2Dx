using System;

namespace monogame.Util
{
    public static class MonoGameMathsUtil
    {
        public static double degreeToRadian(double angle)
        {
            return Math.PI * angle / 180.0;
        }
        public static float degreeToRadian(float angle)
        {
            return (float)Math.PI * angle / 180f;
        }
    }
}