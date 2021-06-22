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
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Threading.Tasks;
using Java.Util.Concurrent;
using Org.Mini2Dx.Core.Executor;

namespace monogame
{
    public class MonoGameTaskExecutor : global::Java.Lang.Object, global::Org.Mini2Dx.Core.TaskExecutor
    {

        internal class MonoGameAsyncResult : global::Org.Mini2Dx.Core.Executor.AsyncResult
        {
            private global::Java.Lang.Runnable runnable;
            private Callable callable;

            private object @lock = new object();
            private bool finished = false;
            private object result = null;

            internal MonoGameAsyncResult(global::Java.Lang.Runnable runnable)
            {
                this.runnable = runnable;
            }

            internal MonoGameAsyncResult(Callable callable)
            {
                this.callable = callable;
            }

            public void Run()
            {
                try
                {
                    object result;
                    if(callable != null)
                    {
                        result = callable.call_6069C574();
                    }
                    else
                    {
                        runnable.run_EFE09FC0();
                        result = null;
                    }
                    lock (@lock)
                    {
                        this.result = result;
                        finished = true;
                    }
                } 
                catch(Exception e)
                {
                    Console.WriteLine(e.Message);
                }
                lock (@lock)
                {
                    finished = true;
                }
            }

            public object getResult_6069C574()
            {
                lock(@lock)
                {
                    return result;
                }
            }

            public bool isFinished_FBE0B2A4()
            {
                lock(@lock)
                {
                    return finished;
                }
            }
        }

        private const int DefaultMaxFrameTasksPerFrame = 32;
        private readonly List<FrameSpreadTask> spreadTasks = new List<FrameSpreadTask>();
        private int maxFrameTasksPerFrame = DefaultMaxFrameTasksPerFrame;

        private Thread[] threads = new Thread[4];
        private ConcurrentQueue<MonoGameAsyncResult> taskQueue = new ConcurrentQueue<MonoGameAsyncResult>();
        private object taskQueueMonitor = new object();
        private int running = 1;

        public MonoGameTaskExecutor()
        {
            for(int i = 0; i < threads.Length; i++)
            {
                threads[i] = new Thread(new ThreadStart(ExecuteTasks));
                threads[i].Start();
            }
        }

        void ExecuteTasks()
        {
            while(Interlocked.CompareExchange(ref running, 1, 1) == 1)
            {
                MonoGameAsyncResult task = null;
                if(taskQueue.TryDequeue(out task))
                {
                    task.Run();
                }

                while (taskQueue.IsEmpty)
                {
                    try
                    {
                        lock (taskQueueMonitor)
                        {
                            global::System.Threading.Monitor.Wait(taskQueueMonitor);
                        }
                    }
                    catch { }
                    continue;
                }
            }
        }

        public void dispose_EFE09FC0()
        {
            Interlocked.Exchange(ref running, 0);
        }

        public void update_97413DCA(float delta)
        {
            var taskCount = 0;

            for(int i = 0; i < spreadTasks.Count; i++)
            {
                if(spreadTasks[i].updateTask_FBE0B2A4())
                {
                    spreadTasks.RemoveAt(i);
                    i--;
                }
                taskCount++;

                if (taskCount >= maxFrameTasksPerFrame)
                {
                    break;
                }
            }
        }

        public void execute_67C91AEE(global::Java.Lang.Runnable r)
        {
            submit_CE72AB39(r);
        }

        public AsyncFuture submit_CE72AB39(global::Java.Lang.Runnable r)
        {
            MonoGameAsyncResult result = new MonoGameAsyncResult(r);
            taskQueue.Enqueue(result);

            lock (taskQueueMonitor)
            {
                global::System.Threading.Monitor.Pulse(taskQueueMonitor);
            }
            return result;
        }

        public AsyncResult submit_B4F5AEF2(Callable c)
        {
            MonoGameAsyncResult result = new MonoGameAsyncResult(c);
            taskQueue.Enqueue(result);

            lock (taskQueueMonitor)
            {
                global::System.Threading.Monitor.Pulse(taskQueueMonitor);
            }
            return result;
        }

        public void submit_51830217(FrameSpreadTask fst)
        {
            spreadTasks.Add(fst);
        }

        public void setMaxFrameTasksPerFrame_3518BA33(int i)
        {
            maxFrameTasksPerFrame = i;
        }

        public int getTotalQueuedAsyncTasks_0EE0D08D()
        {
            return taskQueue.Count;
        }

        public int getTotalQueuedFrameSpreadTasks_0EE0D08D()
        {
            return spreadTasks.Count;
        }
    }
}
