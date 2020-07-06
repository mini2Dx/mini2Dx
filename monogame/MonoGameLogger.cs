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
using Org.Mini2Dx.Core;
using Exception = Java.Lang.Exception;

namespace monogame
{
    public class MonoGameLogger : global::Java.Lang.Object, Org.Mini2Dx.Core.Logger
    {
        private int _logLevel = _static_Logger.LOG_INFO_;
        public void debug(Java.Lang.String tag, Java.Lang.String msg)
        {
            if (_logLevel <= _static_Logger.LOG_DEBUG_)
            {
                Debug.WriteLine("[" + ((string) tag) + "] " + ((string) msg));
            }
        }

        public void error(Java.Lang.String tag, Java.Lang.String msg, Exception e)
        {
            if (_logLevel <= _static_Logger.LOG_ERROR_)
            {
                Console.Error.WriteLine("[" + ((string) tag) + "] " + ((string) msg) + " " + e.ToString());
            }
        }

        public void error(Java.Lang.String tag, Java.Lang.String msg)
        {
            if (_logLevel <= _static_Logger.LOG_ERROR_)
            {
                Console.Error.WriteLine("[" + ((string) tag) + "] " + ((string) msg));
            }
        }

        public void info(Java.Lang.String tag, Java.Lang.String msg)
        {
            if (_logLevel <= _static_Logger.LOG_INFO_)
            {
                Console.WriteLine("[" + ((string) tag) + "] " + ((string) msg));
            }
        }

        public void setLoglevel(int arg0)
        {
            _logLevel = arg0;
        }
    }
}
