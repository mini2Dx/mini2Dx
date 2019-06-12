using System;

namespace monogame.Util
{
    public static class MonoGameMathsUtil
    {
        public static double radianToDegree(double angle)
        {
            return Math.PI * angle / 180.0;
        }
        public static float radianToDegree(float angle)
        {
            return (float)Math.PI * angle / 180f;
        }
    }
}