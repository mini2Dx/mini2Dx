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
 * Base interface for sound effect playback
 */
public interface Sound extends Disposable {
	/**
	 * Plays the sound at full volume, center panning. If it is already playing, it will be played again concurrently.
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long play ();

	/**
	 * Plays the sound at the specified volume, center panning. If it is already playing, it will be played again concurrently.
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long play (float volume);

	/** Plays the sound. If the sound is already playing, it will be played again, concurrently.
	 * @param volume the volume in the range [0,1]
	 * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
	 * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
	 * @return the id of the sound instance if successful, or -1 on failure. */

	/**
	 * Plays the sound at the specified volume, pitch and panning. If it is already playing, it will be played again concurrently.
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 * @param pitch The pitch multiplier where 1.0 is default, >1 is faster and <1 is slower. The value range must be between 0.5 and 2.0
	 * @param pan The panning where -1.0 is full left, 1.0 is full right and 0.0 is center.
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long play (float volume, float pitch, float pan);

	/**
	 * Loops the sound at full volume, center panning . If it is already playing, it will be played again concurrently.
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long loop ();

	/**
	 * Loops the sound at the specified volume, center panning. If it is already playing, it will be played again concurrently.
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long loop (float volume);

	/**
	 * Loops the sound at the specified volume, pitch and panning. If it is already playing, it will be played again concurrently.
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 * @param pitch The pitch multiplier where 1.0 is default, >1 is faster and <1 is slower. The value range must be between 0.5 and 2.0
	 * @param pan The panning where -1.0 is full left, 1.0 is full right and 0.0 is center.
	 * @return -1 on failure, otherwise the unique ID of the playing instance.
	 */
	public long loop (float volume, float pitch, float pan);

	/**
	 * Stops all playing instances of this sound
	 */
	public void stop ();

	/**
	 * Pauses all playing instances of this sound
	 */
	public void pause ();

	/**
	 * Resumes all playing instances of this sound
	 */
	public void resume ();

	/**
	 * Stops the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 */
	public void stop (long soundId);

	/**
	 * Pauses the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 */
	public void pause (long soundId);

	/**
	 * Resumes the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 */
	public void resume (long soundId);

	/**
	 * Sets if the specific instance of this sound should loop. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 * @param looping True if the sound should loop
	 */
	public void setLooping (long soundId, boolean looping);

	/**
	 * Sets the pitch of the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 * @param pitch The pitch multiplier where 1.0 is default, >1 is faster and <1 is slower. The value range must be between 0.5 and 2.0
	 */
	public void setPitch (long soundId, float pitch);

	/**
	 * Sets the volume of the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 */
	public void setVolume (long soundId, float volume);

	/**
	 * Sets the pan and volume of the specific instance of this sound. If it is no longer playing this has no effect.
	 * @param soundId The sound ID that was returned when play/loop was called
	 * @param pan The panning where -1.0 is full left, 1.0 is full right and 0.0 is center.
	 * @param volume The volume to play at where 0.0 is silent and 1.0 is full volume
	 */
	public void setPan (long soundId, float pan, float volume);
}
