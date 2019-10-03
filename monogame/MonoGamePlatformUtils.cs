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

namespace monogame
{
    public class MonoGamePlatformUtils : Org.Mini2Dx.Core.PlatformUtils
    {
        public override void exit(bool b)
        {
            Mini2DxGame.instance.Exit();
        }

        public override long nanoTime()
        {
            return 100L * Stopwatch.GetTimestamp();
        }

        public override long currentTimeMillis()
        {
            var nano = Stopwatch.GetTimestamp();
            nano /= TimeSpan.TicksPerMillisecond;
            return nano;
        }

        public override long getTotalMemory()
        {
            return Process.GetCurrentProcess().VirtualMemorySize64;
        }

        public override long getAvailableMemory()
        {
            return getTotalMemory() - getUsedMemory();
        }

        public override long getUsedMemory()
        {
            return Process.GetCurrentProcess().WorkingSet64;
        }

        public override bool isGameThread()
        {
            return Thread.CurrentThread.ManagedThreadId == 1 && Thread.CurrentThread.Name == null;
        }
    }
}