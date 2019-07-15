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

namespace monogame.Input
{
    public class MonoGamePS4GamePad : org.mini2Dx.core.input.ps4.PS4GamePad
    {
        private PovState _prevPovState;

        public MonoGamePS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base(gamePad, leftStickDeadZone, rightStickDeadZone)
        {
            _prevPovState = gamePad.getPov(0);
        }

        public MonoGamePS4GamePad(GamePad gamePad) : base(gamePad)
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

        private PS4Button buttonCodeToPS4Button(int buttonCode)
        {
            switch ((Buttons)buttonCode)
            {
                case Buttons.Start:
                    return PS4Button.OPTIONS;
                case Buttons.Back:
                    return PS4Button.TOUCHPAD;
                case Buttons.LeftStick:
                    return PS4Button.L3;
                case Buttons.RightStick:
                    return PS4Button.R3;
                case Buttons.LeftShoulder:
                    return PS4Button.L1;
                case Buttons.RightShoulder:
                    return PS4Button.R1;
                case Buttons.A:
                    return PS4Button.CROSS;
                case Buttons.B:
                    return PS4Button.CIRCLE;
                case Buttons.X:
                    return PS4Button.SQUARE;
                case Buttons.Y:
                    return PS4Button.TRIANGLE;
                case Buttons.BigButton:
                    return PS4Button.PS;
                default:
                    return null;
            }
        }

        public override void onButtonDown(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
            if (button != null)
            {
                notifyButtonDown(button);
            }
        }

        public override void onButtonUp(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
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
                    notifyButtonDown(PS4Button.UP);
                }
                else
                {
                    notifyButtonUp(PS4Button.UP);
                }
            }
            if (povState.isPressed(PovState.SOUTH) != _prevPovState.isPressed(PovState.SOUTH))
            {
                if (povState.isPressed(PovState.SOUTH))
                {
                    notifyButtonDown(PS4Button.DOWN);
                }
                else
                {
                    notifyButtonUp(PS4Button.DOWN);
                }
            }
            if (povState.isPressed(PovState.EAST) != _prevPovState.isPressed(PovState.EAST))
            {
                if (povState.isPressed(PovState.EAST))
                {
                    notifyButtonDown(PS4Button.RIGHT);
                }
                else
                {
                    notifyButtonUp(PS4Button.RIGHT);
                }
            }
            if (povState.isPressed(PovState.WEST) != _prevPovState.isPressed(PovState.WEST))
            {
                if (povState.isPressed(PovState.WEST))
                {
                    notifyButtonDown(PS4Button.LEFT);
                }
                else
                {
                    notifyButtonUp(PS4Button.LEFT);
                }
            }

            _prevPovState = povState;
        }

        public override void onAxisChanged(GamePad gamePad, int axisCode, float axisValue)
        {
            switch ((MonoGameGamePad.AxisCodes)axisCode)
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
                    notifyL2Moved(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightTrigger:
                    notifyR2Moved(axisValue);
                    break;
            }
        }

        public override void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value)
        {
        }
    }
}
