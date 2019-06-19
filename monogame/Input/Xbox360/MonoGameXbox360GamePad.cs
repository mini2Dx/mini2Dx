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
using Microsoft.Xna.Framework.Input;
using org.mini2Dx.core.input;
using org.mini2Dx.core.input.button;
using org.mini2Dx.core.input.deadzone;
using org.mini2Dx.gdx.math;
using GamePad = org.mini2Dx.core.input.GamePad;

namespace monogame.Input.Xbox360
{
    public class MonoGameXbox360GamePad : org.mini2Dx.core.input.xbox360.Xbox360GamePad
    {
        private PovState _prevPovState;
        
        public MonoGameXbox360GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base(gamePad, leftStickDeadZone, rightStickDeadZone)
        {
            _prevPovState = gamePad.getPov(0);
        }

        public MonoGameXbox360GamePad(GamePad gamePad) : base(gamePad)
        {
            _prevPovState = gamePad.getPov(0);
        }

        public override void onConnect(GamePad gamePad)
        {
            notifyConnected();
        }

        public override void onDisconnect(GamePad gamePad)
        {
            notifyDisconnected();
        }

        private Xbox360Button buttonCodeToXBox360Button(int buttonCode)
        {
            switch ((Buttons)buttonCode)
            {
                case Buttons.Start:
                    return Xbox360Button.START;
                case Buttons.Back:
                    return Xbox360Button.BACK;
                case Buttons.LeftStick:
                    return Xbox360Button.LEFT_STICK;
                case Buttons.RightStick:
                    return Xbox360Button.RIGHT_STICK;
                case Buttons.LeftShoulder:
                    return Xbox360Button.LEFT_SHOULDER;
                case Buttons.RightShoulder:
                    return Xbox360Button.RIGHT_SHOULDER;
                case Buttons.A:
                    return Xbox360Button.A;
                case Buttons.B:
                    return Xbox360Button.B;
                case Buttons.X:
                    return Xbox360Button.X;
                case Buttons.Y:
                    return Xbox360Button.Y;
                case Buttons.BigButton:
                    return Xbox360Button.GUIDE;
                default:
                    return null;
            }
        }

        public override void onButtonDown(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXBox360Button(buttonCode);
            if (button != null)
            {
                notifyButtonDown(button);
            }
        }

        public override void onButtonUp(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXBox360Button(buttonCode);
            if (button != null)
            {
                notifyButtonUp(button);
            }
        }

        public override void onPovChanged(GamePad gamePad, int povCode, PovState povState)
        {
            if (povState.isPressed(PovState.NORTH) != _prevPovState.isPressed(PovState.NORTH))
            {
                if (povState.isPressed(PovState.NORTH))
                {
                    notifyButtonDown(Xbox360Button.UP);
                }
                else
                {
                    notifyButtonUp(Xbox360Button.UP);
                }
            }
            if (povState.isPressed(PovState.SOUTH) != _prevPovState.isPressed(PovState.SOUTH))
            {
                if (povState.isPressed(PovState.SOUTH))
                {
                    notifyButtonDown(Xbox360Button.DOWN);
                }
                else
                {
                    notifyButtonUp(Xbox360Button.DOWN);
                }
            }
            if (povState.isPressed(PovState.EAST) != _prevPovState.isPressed(PovState.EAST))
            {
                if (povState.isPressed(PovState.EAST))
                {
                    notifyButtonDown(Xbox360Button.RIGHT);
                }
                else
                {
                    notifyButtonUp(Xbox360Button.RIGHT);
                }
            }
            if (povState.isPressed(PovState.WEST) != _prevPovState.isPressed(PovState.WEST))
            {
                if (povState.isPressed(PovState.WEST))
                {
                    notifyButtonDown(Xbox360Button.LEFT);
                }
                else
                {
                    notifyButtonUp(Xbox360Button.LEFT);
                }
            }

            _prevPovState = povState;
        }

        public override void onAxisChanged(GamePad gamePad, int axisCode, float axisValue)
        {
            switch ((MonoGameGamePad.AxisCodes) axisCode)
            {
                case MonoGameGamePad.AxisCodes.LeftThumbstickX:
                    notifyLeftStickXMoved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.LeftThumbstickY:
                    notifyLeftStickYMoved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightThumbstickX:
                    notifyRightStickXMoved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightThumbstickY:
                    notifyRightStickYMoved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.LeftTrigger:
                    notifyLeftTriggerMoved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightTrigger:
                    notifyRightTriggerMoved(axisValue);
                    break;
            }
        }

        public override void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value)
        {
        }
    }
}