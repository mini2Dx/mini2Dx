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
using Org.Mini2Dx.Core.Input;
using Org.Mini2Dx.Core.Input.Button;
using Org.Mini2Dx.Core.Input.Deadzone;
using Org.Mini2Dx.Gdx.Math;
using GamePad = Org.Mini2Dx.Core.Input.GamePad;

namespace monogame.Input
{
    public class MonoGamePS4GamePad : Org.Mini2Dx.Core.Input.Ps4.PS4GamePad
    {
        private PovState _prevPovState;

        public MonoGamePS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base(gamePad, leftStickDeadZone, rightStickDeadZone)
        {
            _prevPovState = gamePad.getPov(0);
        }

        public MonoGamePS4GamePad(GamePad gamePad) : base()
        {
            _prevPovState = gamePad.getPov(0);
            base._init_(gamePad);
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
                    return PS4Button.OPTIONS_;
                case Buttons.Back:
                    return PS4Button.TOUCHPAD_;
                case Buttons.LeftStick:
                    return PS4Button.L3_;
                case Buttons.RightStick:
                    return PS4Button.R3_;
                case Buttons.LeftShoulder:
                    return PS4Button.L1_;
                case Buttons.RightShoulder:
                    return PS4Button.R1_;
                case Buttons.A:
                    return PS4Button.CROSS_;
                case Buttons.B:
                    return PS4Button.CIRCLE_;
                case Buttons.X:
                    return PS4Button.SQUARE_;
                case Buttons.Y:
                    return PS4Button.TRIANGLE_;
                case Buttons.BigButton:
                    return PS4Button.PS_;
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
            if (povState.isPressed(PovState.NORTH_) != _prevPovState.isPressed(PovState.NORTH_))
            {
                if (povState.isPressed(PovState.NORTH_))
                {
                    notifyButtonDown(PS4Button.UP_);
                }
                else
                {
                    notifyButtonUp(PS4Button.UP_);
                }
            }
            if (povState.isPressed(PovState.SOUTH_) != _prevPovState.isPressed(PovState.SOUTH_))
            {
                if (povState.isPressed(PovState.SOUTH_))
                {
                    notifyButtonDown(PS4Button.DOWN_);
                }
                else
                {
                    notifyButtonUp(PS4Button.DOWN_);
                }
            }
            if (povState.isPressed(PovState.EAST_) != _prevPovState.isPressed(PovState.EAST_))
            {
                if (povState.isPressed(PovState.EAST_))
                {
                    notifyButtonDown(PS4Button.RIGHT_);
                }
                else
                {
                    notifyButtonUp(PS4Button.RIGHT_);
                }
            }
            if (povState.isPressed(PovState.WEST_) != _prevPovState.isPressed(PovState.WEST_))
            {
                if (povState.isPressed(PovState.WEST_))
                {
                    notifyButtonDown(PS4Button.LEFT_);
                }
                else
                {
                    notifyButtonUp(PS4Button.LEFT_);
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
