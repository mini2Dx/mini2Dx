using System;
using System.Diagnostics;
using Exception = java.lang.Exception;

namespace monogame
{
    class MonoGameLogger : org.mini2Dx.core.Logger
    {
        public void debug(string tag, string msg)
        {
            Debug.WriteLine($"D/[{tag}] {msg}");
        }

        public void error(string tag, string msg, Exception e)
        {
            Console.Error.WriteLine($"D/[{tag}] {msg}\n{e.StackTrace}");
        }

        public void error(string tag, string msg)
        {
            Console.Error.WriteLine($"E/[{tag}] {msg}");
        }

        public void info(string tag, string msg)
        {
            Console.WriteLine($"I/[{tag}] {msg}");
        }
    }
}
