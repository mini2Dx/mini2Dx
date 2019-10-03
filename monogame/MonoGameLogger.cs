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
using System.Diagnostics;
using Exception = Java.Lang.Exception;

namespace monogame
{
    public class MonoGameLogger : Org.Mini2Dx.Core.Logger
    {
        public void debug(Java.Lang.String tag, Java.Lang.String msg)
        {
            Debug.WriteLine($"D/[{tag}] {msg}");
        }

        public void error(Java.Lang.String tag, Java.Lang.String msg, Exception e)
        {
            Console.Error.WriteLine($"E/[{tag}] {msg}\n{e.StackTrace}");
        }

        public void error(Java.Lang.String tag, Java.Lang.String msg)
        {
            Console.Error.WriteLine($"E/[{tag}] {msg}");
        }

        public void info(Java.Lang.String tag, Java.Lang.String msg)
        {
            Console.WriteLine($"I/[{tag}] {msg}");
        }
    }
}
