/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
using System.Threading;
using monogame.Util;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Util;

namespace monogame
{
    public class MonoGamePlatformUtils : Org.Mini2Dx.Core.PlatformUtils
    {
        public static PlatformMemoryInfoProvider MEMORY_INFO_PROVIDER = null;
        public static PlatformTimeInfoProvider TIME_INFO_PROVIDER = null;
        public static Thread GAME_THREAD = null;

        public MonoGamePlatformUtils() : base()
        {
            base._init_();
        }

        public override void exit_AA5A2C66(bool b)
        {
            Mini2DxGame.instance.Exit();
        }

        public override long nanoTime_0BE0CBD4()
        {
            if(TIME_INFO_PROVIDER != null)
            {
                return TIME_INFO_PROVIDER.GetCurrentTimeNanos();
            }
            return 100L * DateTime.UtcNow.Ticks;
        }

        public override long currentTimeMillis_0BE0CBD4()
        {
            if (TIME_INFO_PROVIDER != null)
            {
                return TIME_INFO_PROVIDER.GetCurrentTimeMillis();
            }
            var nano = DateTime.UtcNow.Ticks;
            nano /= TimeSpan.TicksPerMillisecond;
            return nano;
        }

        public override long getTotalMemory_0BE0CBD4()
        {
            if(MEMORY_INFO_PROVIDER != null)
            {
                return MEMORY_INFO_PROVIDER.GetTotalMemory();
            }
            return Process.GetCurrentProcess().VirtualMemorySize64;
        }

        public override long getAvailableMemory_0BE0CBD4()
        {
            return getTotalMemory_0BE0CBD4() - getUsedMemory_0BE0CBD4();
        }

        public override long getUsedMemory_0BE0CBD4()
        {
            if(MEMORY_INFO_PROVIDER != null)
            {
                return MEMORY_INFO_PROVIDER.GetUsedMemory();
            }
            return Process.GetCurrentProcess().WorkingSet64;
        }

        public override bool isGameThread_FBE0B2A4()
        {
            return Thread.CurrentThread == GAME_THREAD;
        }

        public override ZlibStream decompress_5F233ABB(sbyte[] arg0)
        {
            return new MonoGameZlibStream(arg0);
        }

        public override Java.Lang.String timestampToDateFormat_FC36C8CC(long millis, Java.Lang.String jformat)
        {
            string format = jformat;
            var timespan = TimeSpan.FromMilliseconds(millis);
            return new DateTime(1970, 1, 1).AddTicks(timespan.Ticks).ToString(format);
        }
    }

    public abstract class PlatformMemoryInfoProvider
    {
        public abstract long GetUsedMemory();

        public abstract long GetTotalMemory();
    }

    public abstract class PlatformTimeInfoProvider
    {
        public abstract long GetCurrentTimeNanos();

        public abstract long GetCurrentTimeMillis();
    }
}