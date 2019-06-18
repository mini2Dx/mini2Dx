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
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xboxOne.XboxOneGamePad;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.utils.Array;

public interface Input {
    /**
     * Sets the {@link InputProcessor} for handling mouse/keyboard/touch events
     * @param inputProcessor The {@link InputProcessor} to use
     */
    public void setInputProcessor(InputProcessor inputProcessor);

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
     * Wraps a {@link GamePad} as a {@link XboxOneGamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link XboxOneGamePad}
     */
    public XboxOneGamePad newXboxOneGamePad(GamePad gamePad);

    /**
     * Wraps a {@link GamePad} as a {@link Xbox360GamePad}
     * @param gamePad The underlying {@link GamePad} instance
     * @return A new instance of {@link Xbox360GamePad}
     */
    public Xbox360GamePad newXbox360GamePad(GamePad gamePad);
}
