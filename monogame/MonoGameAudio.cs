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
using System.Collections.Generic;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Media;
using monogame.Audio;
using monogame.Util;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Files;

namespace monogame
{
    public class MonoGameAudio : global::Java.Lang.Object, _Audio
    {
        private static readonly LinkedList<MusicCompletionListener> _musicCompletionListeners;
        private static readonly LinkedList<SoundCompletionListener> _soundCompletionListeners;

        static MonoGameAudio()
        {
            _musicCompletionListeners = new LinkedList<MusicCompletionListener>();
            _soundCompletionListeners = new LinkedList<SoundCompletionListener>();
            MediaPlayer.MediaStateChanged += MonoGameMusic.MediaPlayerOnMediaStateChanged;
            MonoGameMusic.musicCompletionListeners = _musicCompletionListeners;
        }

        public Music newMusic_C995EB38(FileHandle fileHandle)
        {
            return new MonoGameMusic(fileHandle);
        }

        public void addMusicCompletionListener_FA743BBA(MusicCompletionListener completionListener)
        {
            _musicCompletionListeners.AddLast(completionListener);
        }

        public void removeMusicCompletionListener_FA743BBA(MusicCompletionListener completionListener)
        {
            _musicCompletionListeners.Remove(completionListener);
        }

        public void addSoundCompletionListener_6D852ABC(SoundCompletionListener completionListener)
        {
            _soundCompletionListeners.AddLast(completionListener);
        }

        public AsyncSoundResult newAsyncSound_0F0BBE0F(FileHandle arg0)
        {
            return new MonoGameAsyncSoundResult(arg0);
        }

        public void removeSoundCompletionListener_6D852ABC(SoundCompletionListener completionListener)
        {
            _soundCompletionListeners.Remove(completionListener);
        }

        public Sound newSound_F8578266(FileHandle fileHandle)
        {
            return new MonoGameSound(fileHandle);
        }

        public static void soundCompleted(long id)
        {
            var node = _soundCompletionListeners.First;
            while (node != null)
            {
                node.Value.onSoundCompleted_5FE5E296(id);
                node = node.Next;
            }
        }

        public void update()
        {
            for (int i = 0; i < MonoGameSoundInstance.Instances.Count; i++)
            {
                var currentSound = MonoGameSoundInstance.Instances[i];
                if (currentSound != null && currentSound.SoundEffectInstance.State == SoundState.Stopped)
                {
                    soundCompleted(currentSound.Id);
                    currentSound.Dispose();
                    i--;
                }
            }
        }
    }
}
