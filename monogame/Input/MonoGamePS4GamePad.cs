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
using O_M_C_Input_GamePadListener = Org.Mini2Dx.Core.Input.GamePadListener;
using O_M_C_Input_MappedGamePadListener = Org.Mini2Dx.Core.Input.MappedGamePadListener;
using GamePad = Org.Mini2Dx.Core.Input.GamePad;

namespace monogame.Input
{
    public class MonoGamePS4GamePad : Org.Mini2Dx.Core.Input.Ps4.PS4GamePad, O_M_C_Input_GamePadListener
    {
        private PovState _prevPovState;

        public MonoGamePS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base()
        {
            base._init_(gamePad, leftStickDeadZone, rightStickDeadZone);
            _prevPovState = gamePad.getPov(0);
        }

        public MonoGamePS4GamePad(GamePad gamePad) : base()
        {
            base._init_(gamePad);
            _prevPovState = gamePad.getPov(0);
        }

        void O_M_C_Input_GamePadListener.onConnect(GamePad gamePad)
        {
            notifyConnected();
        }

        void O_M_C_Input_GamePadListener.onDisconnect(GamePad gamePad)
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

        void O_M_C_Input_GamePadListener.onButtonDown(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
            if (button != null)
            {
                notifyButtonDown(button);
            }
        }

        void O_M_C_Input_GamePadListener.onButtonUp(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
            if (button != null)
            {
                notifyButtonUp(button);
            }
        }

        void O_M_C_Input_GamePadListener.onPovChanged(GamePad gamePad, int povCode, PovState povState)
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

        void O_M_C_Input_GamePadListener.onAxisChanged(GamePad gamePad, int axisCode, float axisValue)
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

        void O_M_C_Input_GamePadListener.onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value)
        {
        }
    }
}
