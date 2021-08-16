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
    public class MonoGameXboxGamePad : Org.Mini2Dx.Core.Input.Xbox.XboxGamePad, O_M_C_Input_GamePadListener
    {
        
        public MonoGameXboxGamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) : base()
        {
            base._init_58DC904A(gamePad, leftStickDeadZone, rightStickDeadZone);
            
        }

        public MonoGameXboxGamePad(GamePad gamePad) : base()
        {
            base._init_238EC38A(gamePad);
        }

        void O_M_C_Input_GamePadListener.onConnect_238EC38A(GamePad gamePad)
        {
            notifyConnected_FBE0B2A4();
        }

        void O_M_C_Input_GamePadListener.onDisconnect_238EC38A(GamePad gamePad)
        {
            notifyDisconnected_FBE0B2A4();
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
                case Buttons.DPadLeft:
                    return XboxButton.LEFT_;
                case Buttons.DPadRight:
                    return XboxButton.RIGHT_;
                case Buttons.DPadUp:
                    return XboxButton.UP_;
                case Buttons.DPadDown:
                    return XboxButton.DOWN_;
                default:
                    return null;
            }
        }

        void O_M_C_Input_GamePadListener.onButtonDown_7016EF7D(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXboxButton(buttonCode);
            if (button != null)
            {
                notifyButtonDown_967FA8D9(button);
            }
        }

        void O_M_C_Input_GamePadListener.onButtonUp_7016EF7D(GamePad gamePad, int buttonCode)
        {
            var button = buttonCodeToXboxButton(buttonCode);
            if (button != null)
            {
                notifyButtonUp_967FA8D9(button);
            }
        }

        void O_M_C_Input_GamePadListener.onAxisChanged_AD47562B(GamePad gamePad, int axisCode, float axisValue)
        {
            switch ((MonoGameGamePad.AxisCodes) axisCode)
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
                    notifyLeftTriggerMoved_8B412AE6(axisValue);
                    break;
                case MonoGameGamePad.AxisCodes.RightTrigger:
                    notifyRightTriggerMoved_8B412AE6(axisValue);
                    break;
            }
        }

        void O_M_C_Input_GamePadListener.onAccelerometerChanged_FE43FF32(GamePad gamePad, int accelerometerCode, Vector3 value)
        {
        }
    }
}