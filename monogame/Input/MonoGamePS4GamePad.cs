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

        public MonoGamePS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base()
        {
            base._init_(gamePad, leftStickDeadZone, rightStickDeadZone);
        }

        public MonoGamePS4GamePad(GamePad gamePad) : base()
        {
            base._init_(gamePad);
        }

        void O_M_C_Input_GamePadListener.onConnect_238EC38A(GamePad gamePad)
        {
            notifyConnected_FBE0B2A4();
        }

        void O_M_C_Input_GamePadListener.onDisconnect_238EC38A(GamePad gamePad)
        {
            notifyDisconnected_FBE0B2A4();
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

        void O_M_C_Input_GamePadListener.onButtonDown_7016EF7D(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
            if (button != null)
            {
                notifyButtonDown_4053C99B(button);
            }
        }

        void O_M_C_Input_GamePadListener.onButtonUp_7016EF7D(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToPS4Button(buttonCode);
            if (button != null)
            {
                notifyButtonUp_4053C99B(button);
            }
        }

        void O_M_C_Input_GamePadListener.onAxisChanged_AD47562B(GamePad gamePad, int axisCode, float axisValue)
        {
            switch ((MonoGameGamePad.AxisCodes)axisCode)
            {
                case MonoGameGamePad.AxisCodes.LeftThumbstickX:
                    notifyLeftStickXMoved_8B412AE6(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.LeftThumbstickY:
                    notifyLeftStickYMoved_8B412AE6(-axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightThumbstickX:
                    notifyRightStickXMoved_8B412AE6(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightThumbstickY:
                    notifyRightStickYMoved_8B412AE6(-axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.LeftTrigger:
                    notifyL2Moved_8B412AE6(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightTrigger:
                    notifyR2Moved_8B412AE6(axisValue);
                    break;
            }
        }

        void O_M_C_Input_GamePadListener.onAccelerometerChanged_FE43FF32(GamePad gamePad, int accelerometerCode, Vector3 value)
        {
        }
    }
}
