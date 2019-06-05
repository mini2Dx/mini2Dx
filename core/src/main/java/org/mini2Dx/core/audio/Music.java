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
package org.mini2Dx.core.audio;

import org.mini2Dx.gdx.utils.Disposable;

/**
 * Base interface for Music stream-based playback
 */
public interface Music extends Disposable {
	/**
	 * Returns the unique ID for this instance
	 * @return The unique ID
	 */
	public long getId();
	/**
	 * Starts playing the music stream. If the stream was paused, it will resume playing. If the stream was finished or stopped, it will restart.
	 */
	public void play ();

	/**
	 * Pauses the playback if it is currently playing
	 */
	public void pause ();

	/**
	 * Stops the playback if it is paused or currently playing. The next call to {@link #play()} will start playback from the beginning.
	 */
	public void stop ();

	/**
	 * Returns if the music is playing
	 * @return True if playing
	 */
	public boolean isPlaying ();

	/**
	 * Sets if the music should loop. Can be called anytime.
	 * @param isLooping True if the music should loop.
	 */
	public void setLooping (boolean isLooping);

	/**
	 * Returns if the music will loop
	 * @return True if looping
	 */
	public boolean isLooping ();

	/**
	 * Sets the volume of the music
	 * @param volume A value between 0.0 and 1.0 where 0.0 is silent and 1.0 is maximum volume.
	 */
	public void setVolume (float volume);

	/**
	 * Returns the volume of the music
	 * @return A value between 0.0 and 1.0 where 0.0 is silent and 1.0 is maximum volume.
	 */
	public float getVolume ();
	
	/**
	 * Sets the panning and volume of the music
	 * @param pan The panning where -1.0 is full left, 1.0 is full right and 0.0 is center.
	 * @param volume A value between 0.0 and 1.0 where 0.0 is silent and 1.0 is maximum volume.
	 */
	public void setPan (float pan, float volume);

	/**
	 * Sets the playback position
	 * @param seconds The position in seconds
	 */
	public void setPosition (float seconds);

	/**
	 * Returns the playback position
	 * @return The position in seconds
	 */
	public float getPosition ();
}
