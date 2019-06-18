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
using org.mini2Dx.core.audio;
using org.mini2Dx.core.files;

namespace monogame.Audio
{
    public class MonoGameMusic : org.mini2Dx.core.audio.Music
    {
        private static int _songNum;
        private readonly int _songId;
        private Song _song;
        public static LinkedList<MusicCompletionListener> musicCompletionListeners;
        private static Music _currentlyPlayingMusic;
        
        public MonoGameMusic(FileHandle fileHandle)
        {
            _songId = ++_songNum;
            _song = Song.FromUri(fileHandle.name(), new Uri(((MonoGameFileHandle)fileHandle).fullPath(), UriKind.Relative));
        }
        
        public void dispose()
        {
            _song.Dispose();
        }

        public long getId()
        {
            return _songId;
        }

        public void play()
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

        public void pause()
        {
            MediaPlayer.Pause();
        }

        public void stop()
        {
            MediaPlayer.Stop();
        }

        public bool isPlaying()
        {
            return MediaPlayer.State == MediaState.Playing;
        }

        public void setLooping(bool isLooping)
        {
            MediaPlayer.IsRepeating = isLooping;
        }

        public bool isLooping()
        {
            return MediaPlayer.IsRepeating;
        }

        public void setVolume(float volume)
        {
            MediaPlayer.Volume = volume;
        }

        public float getVolume()
        {
            return MediaPlayer.Volume;
        }

        public float getPosition()
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
                    node.Value.onMusicCompleted(_currentlyPlayingMusic);
                    node = node.Next;
                }
            }
        }
    }
}