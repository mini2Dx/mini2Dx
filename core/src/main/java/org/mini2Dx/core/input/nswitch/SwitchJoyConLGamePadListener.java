/**
 * Copyright 2019 Viridian Software Ltd.
 */
package org.mini2Dx.core.input.nswitch;

import org.mini2Dx.core.input.MappedGamePadListener;
import org.mini2Dx.core.input.button.SwitchJoyConLButton;

public interface SwitchJoyConLGamePadListener extends MappedGamePadListener {

	public void connected(SwitchJoyConLGamePad gamePad);

	public void disconnected(SwitchJoyConLGamePad gamePad);

	public boolean buttonDown(SwitchJoyConLGamePad gamePad, SwitchJoyConLButton button);

	public boolean buttonUp(SwitchJoyConLGamePad gamePad, SwitchJoyConLButton button);

	public boolean leftStickXMoved(SwitchJoyConLGamePad gamePad, float value);

	public boolean leftStickYMoved(SwitchJoyConLGamePad gamePad, float value);

	public boolean zlMoved(SwitchJoyConLGamePad gamePad, float value);
}
