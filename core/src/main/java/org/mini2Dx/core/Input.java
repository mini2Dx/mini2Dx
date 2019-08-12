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
package org.mini2Dx.core;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.nswitch.SwitchDualJoyConGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConRGamePad;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox.XboxGamePad;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.utils.Array;

public interface Input {
    /**
     * Sets the {@link InputProcessor} for handling mouse/keyboard/touch events
     * @param inputProcessor The {@link InputProcessor} to use
     */
    public void setInputProcessor(InputProcessor inputProcessor);

    /**
     * Sets on mobile and consoles of the on-screen keyboard should appear
     * @param visible True if the keyboard should appear
     */
    public void setOnScreenKeyboardVisible(boolean visible);

    /**
     * Returns the list of known {@link GamePad}s connected to the device.
     * If a {@link GamePad} disconnects/unplugs it will remain in this array.
     * @return An empty {@link Array} if no {@link GamePad}s are present.
     */
    public Array<GamePad> getGamePads();

    /**
     * Wraps a {@link GamePad} as a {@link PS4GamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link PS4GamePad}
     */
    public PS4GamePad newPS4GamePad(GamePad gamePad);

    /**
     * Wraps a {@link GamePad} as a {@link SwitchDualJoyConGamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link SwitchDualJoyConGamePad}
     */
    public SwitchDualJoyConGamePad newSwitchDualJoyConGamePad(GamePad gamePad);

    /**
     * Wraps a {@link GamePad} as a {@link SwitchJoyConLGamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link SwitchJoyConLGamePad}
     */
    public SwitchJoyConLGamePad newSwitchJoyConLGamePad(GamePad gamePad);

    /**
     * Wraps a {@link GamePad} as a {@link SwitchJoyConRGamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link SwitchJoyConRGamePad}
     */
    public SwitchJoyConRGamePad newSwitchJoyConRGamePad(GamePad gamePad);

    /**
     * Wraps a {@link GamePad} as a {@link XboxGamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link XboxGamePad}
     */
    public XboxGamePad newXboxGamePad(GamePad gamePad);

    /**
     * Returns the mouse/touch screen X coordinate
     * @return The x coordinate
     */
    public int getX();

    /**
     * Returns the mouse/touch screen Y coordinate
     * @return The y coordinate
     */
    public int getY();

    /**
     * Returns if a keyboard key was just pressed (down, then released)
     * @param key The keyboard keycode
     * @return True if the key was just pressed (down, then released)
     */
    public boolean isKeyJustPressed(int key);

    /**
     * Returns if a keyboard key is down
     * @param key The keyboard keycode
     * @return True if the key is down
     */
    public boolean isKeyDown(int key);

    /**
     * Returns if a keyboard key is up
     * @param key The keyboard keycode
     * @return True if the key is down
     */
    public boolean isKeyUp(int key);

    /**
     * Returns if the the screen was just touched or the mouse was just clicked
     * @return True if the screen was touched or mouse button pressed down then released
     */
    public boolean justTouched();
}
