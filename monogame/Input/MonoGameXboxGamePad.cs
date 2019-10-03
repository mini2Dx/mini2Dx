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
    public class MonoGameXboxGamePad : Org.Mini2Dx.Core.Input.Xbox.XboxGamePad
    {
        private PovState _prevPovState;
        
        public MonoGameXboxGamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base(gamePad, leftStickDeadZone, rightStickDeadZone)
        {
            _prevPovState = gamePad.getPov(0);
        }

        public MonoGameXboxGamePad(GamePad gamePad) : base()
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

        private XboxButton buttonCodeToXboxButton(int buttonCode)
        {
            switch ((Buttons)buttonCode)
            {
                case Buttons.Start:
                    return XboxButton.START_;
                case Buttons.Back:
                    return XboxButton.BACK_;
                case Buttons.LeftStick:
                    return XboxButton.LEFT_STICK_;
                case Buttons.RightStick:
                    return XboxButton.RIGHT_STICK_;
                case Buttons.LeftShoulder:
                    return XboxButton.LEFT_SHOULDER_;
                case Buttons.RightShoulder:
                    return XboxButton.RIGHT_SHOULDER_;
                case Buttons.A:
                    return XboxButton.A_;
                case Buttons.B:
                    return XboxButton.B_;
                case Buttons.X:
                    return XboxButton.X_;
                case Buttons.Y:
                    return XboxButton.Y_;
                case Buttons.BigButton:
                    return XboxButton.GUIDE_;
                default:
                    return null;
            }
        }

        public override void onButtonDown(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXboxButton(buttonCode);
            if (button != null)
            {
                notifyButtonDown(button);
            }
        }

        public override void onButtonUp(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXboxButton(buttonCode);
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
                    notifyButtonDown(XboxButton.UP_);
                }
                else
                {
                    notifyButtonUp(XboxButton.UP_);
                }
            }
            if (povState.isPressed(PovState.SOUTH_) != _prevPovState.isPressed(PovState.SOUTH_))
            {
                if (povState.isPressed(PovState.SOUTH_))
                {
                    notifyButtonDown(XboxButton.DOWN_);
                }
                else
                {
                    notifyButtonUp(XboxButton.DOWN_);
                }
            }
            if (povState.isPressed(PovState.EAST_) != _prevPovState.isPressed(PovState.EAST_))
            {
                if (povState.isPressed(PovState.EAST_))
                {
                    notifyButtonDown(XboxButton.RIGHT_);
                }
                else
                {
                    notifyButtonUp(XboxButton.RIGHT_);
                }
            }
            if (povState.isPressed(PovState.WEST_) != _prevPovState.isPressed(PovState.WEST_))
            {
                if (povState.isPressed(PovState.WEST_))
                {
                    notifyButtonDown(XboxButton.LEFT_);
                }
                else
                {
                    notifyButtonUp(XboxButton.LEFT_);
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