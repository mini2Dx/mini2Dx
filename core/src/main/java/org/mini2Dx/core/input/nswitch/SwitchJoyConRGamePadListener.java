/**
 * Copyright 2019 Viridian Software Ltd.
 */
package org.mini2Dx.core.input.nswitch;

import org.mini2Dx.core.input.MappedGamePadListener;
import org.mini2Dx.core.input.button.SwitchJoyConRButton;

public interface SwitchJoyConRGamePadListener extends MappedGamePadListener {

	public void connected(SwitchJoyConRGamePad gamePad);

	public void disconnected(SwitchJoyConRGamePad gamePad);

	public boolean buttonDown(SwitchJoyConRGamePad gamePad, SwitchJoyConRButton button);

	public boolean buttonUp(SwitchJoyConRGamePad gamePad, SwitchJoyConRButton button);

	public boolean rightStickXMoved(SwitchJoyConRGamePad gamePad, float value);

	public boolean rightStickYMoved(SwitchJoyConRGamePad gamePad, float value);

	public boolean zrMoved(SwitchJoyConRGamePad gamePad, float value);
}
