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
using Microsoft.Xna.Framework.Media;
using monogame.Files;
using Org.Mini2Dx.Core.Audio;
using Org.Mini2Dx.Core.Files;

namespace monogame.Audio
{
    public class MonoGameMusic : global::Java.Lang.Object, Music
    {
        private static int _songNum;
        private readonly int _songId;
        private Song _song;
        public static LinkedList<MusicCompletionListener> musicCompletionListeners;
        private static Music _currentlyPlayingMusic;
        
        public MonoGameMusic(FileHandle fileHandle)
        {
            _songId = ++_songNum;
            _song = ((MonoGameFileHandle)fileHandle).loadFromContentManager<Song>();
        }

        //todo implement a proper solution for disposing Songs
        public void dispose_EFE09FC0()
        {
            //_song.Dispose();
        }

        public long getId_0BE0CBD4()
        {
            return _songId;
        }

        public void play_EFE09FC0()
        {
            _currentlyPlayingMusic = this;
            if (MediaPlayer.State == MediaState.Paused)
            {
                MediaPlayer.Resume();
            }
            else
            {
                MediaPlayer.Play(_song);
            }
        }

        public void pause_EFE09FC0()
        {
            MediaPlayer.Pause();
        }

        public void stop_EFE09FC0()
        {
            MediaPlayer.Stop();
        }

        public bool isPlaying_FBE0B2A4()
        {
            return MediaPlayer.State == MediaState.Playing;
        }

        public void setLooping_AA5A2C66(bool isLooping)
        {
            MediaPlayer.IsRepeating = isLooping;
        }

        public bool isLooping_FBE0B2A4()
        {
            return MediaPlayer.IsRepeating;
        }

        public void setVolume_97413DCA(float volume)
        {
            MediaPlayer.Volume = volume;
        }

        public float getVolume_FFE0B8F0()
        {
            return MediaPlayer.Volume;
        }

        public float getPosition_FFE0B8F0()
        {
            return (float) MediaPlayer.PlayPosition.TotalSeconds;
        }

        public static void MediaPlayerOnMediaStateChanged(object sender, EventArgs e)
        {
            if (MediaPlayer.State == MediaState.Stopped)
            {
                var node = musicCompletionListeners.First;
                while (node != null)
                {
                    node.Value.onMusicCompleted_2E9F961C(_currentlyPlayingMusic);
                    node = node.Next;
                }
            }
        }
    }
}