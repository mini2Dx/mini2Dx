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
using System.Collections.Generic;
using Microsoft.Xna.Framework.Input;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Input;
using Org.Mini2Dx.Core.Input.Ps4;
using Org.Mini2Dx.Core.Input.Xbox;
using GamePad = Microsoft.Xna.Framework.Input.GamePad;
using GamePadType = Org.Mini2Dx.Core.Input.GamePadType;
using Vector3 = Org.Mini2Dx.Gdx.Math.Vector3;

namespace monogame.Input
{
    public class MonoGameGamePad : global::Java.Lang.Object, Org.Mini2Dx.Core.Input.GamePad
    {
        public enum AxisCodes
        {
            LeftThumbstickX = 0,
            LeftThumbstickY = 1,
            RightThumbstickX = 2,
            RightThumbstickY = 3,
            LeftTrigger = 4,
            RightTrigger = 5
        }
        
        private struct GamePadStatus
        {
            public bool isConnected;
            public bool[] buttons;
            public float[] axes;
            public Vector3[] accelerometers;
        }
        
        private readonly int _playerIndex;
        private bool _isVibrating;
        private float _vibrationStrength;
        private LinkedList<GamePadListener> _gamePadListeners;

        private int[] buttonCodes = (int[]) Enum.GetValues(typeof(Buttons));
        private GamePadStatus _prevStatus, _currentStatus;
        

        public MonoGameGamePad(int index)
        {
            _playerIndex = index;
            _gamePadListeners = new LinkedList<GamePadListener>();
            _prevStatus = new GamePadStatus();

            var state = GamePad.GetState(_playerIndex);
            if (state != null)
            {
                GetStatus(ref state, ref _prevStatus, buttonCodes);
            }

            _currentStatus = new GamePadStatus();
            _currentStatus = getStatus();
        }

        private GamePadStatus getStatus()
        {
            var state = GamePad.GetState(_playerIndex);

            if(state == null)
            {
                _currentStatus.isConnected = _prevStatus.isConnected;
                if(_prevStatus.accelerometers != null)
                {
                    if(_currentStatus.accelerometers == null)
                    {
                        _currentStatus.accelerometers = new Vector3[_prevStatus.accelerometers.Length];
                    }
                    Array.Copy(_prevStatus.accelerometers, _currentStatus.accelerometers, _prevStatus.accelerometers.Length);
                }
                if (_prevStatus.buttons != null)
                {
                    if (_currentStatus.buttons == null)
                    {
                        _currentStatus.buttons = new bool[_prevStatus.buttons.Length];
                    }
                    Array.Copy(_prevStatus.buttons, _currentStatus.buttons, _prevStatus.buttons.Length);
                }
                if (_prevStatus.axes != null)
                {
                    if (_currentStatus.axes == null)
                    {
                        _currentStatus.axes = new float[_prevStatus.axes.Length];
                    }
                    Array.Copy(_prevStatus.axes, _currentStatus.axes, _prevStatus.axes.Length);
                }
                return _currentStatus;
            }

            GetStatus(ref state, ref _currentStatus, buttonCodes);
            return _currentStatus;
        }

        private static void GetStatus(ref GamePadState state, ref GamePadStatus status, int[] buttonCodes)
        {
            status.isConnected = state.IsConnected;
            if (status.buttons == null || status.buttons.Length != buttonCodes.Length)
            {
                status.buttons = new bool[buttonCodes.Length];
            }
            for (var i = 0; i < buttonCodes.Length; i++)
            {
                Buttons buttonCode = (Buttons)buttonCodes[i];
                status.buttons[i] = state.IsButtonDown(buttonCode);
            }
            if (status.axes == null)
            {
                status.axes = new float[6];
            }

            status.axes[0] = state.ThumbSticks.Left.X;
            status.axes[1] = state.ThumbSticks.Left.Y;
            status.axes[2] = state.ThumbSticks.Right.X;
            status.axes[3] = state.ThumbSticks.Right.Y;
            status.axes[4] = state.Triggers.Left;
            status.axes[5] = state.Triggers.Right;

            if (status.accelerometers == null)
            {
                status.accelerometers = new Vector3[0];
            }
        }
        
        public void addListener_1B7E98A8(GamePadListener gpl)
        {
            _gamePadListeners.AddLast(gpl);
        }

        public void removeListener_1B7E98A8(GamePadListener gpl)
        {
            _gamePadListeners.Remove(gpl);
        }

        public GamePadType getGamePadType_2F51C410()
        {
            string displayName = GamePad.GetCapabilities(_playerIndex).DisplayName;
            if(displayName == null)
            {
                displayName = "";
            }
            else
            {
                displayName = displayName.ToLower();
            }

            for (int i = 0; i < PS4GamePad.ID_.Length; i++)
            {
                if (displayName.Contains(PS4GamePad.ID_[i]))
                {
                    return GamePadType.PS4_;
                }
            }
            for (int i = 0; i < XboxGamePad.ID_.Length; i++)
            {
                if(displayName.Contains(XboxGamePad.ID_[i]))
                {
                    return GamePadType.XBOX_;
                }
            }

            if(Mdx.platform_.Equals(Platform.XBOX_))
            {
                return GamePadType.XBOX_;
            }
            if (Mdx.platform_.Equals(Platform.PLAYSTATION_))
            {
                return GamePadType.PS4_;
            }
            return GamePadType.UNKNOWN_;
        }

        public bool wasConnected()
        {
            return _prevStatus.isConnected;
        }

        public bool isConnected_FBE0B2A4()
        {
            var state = GamePad.GetState(_playerIndex);

            if (state == null)
            {
                return _prevStatus.isConnected;
            }
            return state.IsConnected;
        }

        public bool isPlayerIndicesSupported_FBE0B2A4()
        {
            return true;
        }

        public int getPlayerIndex_0EE0D08D()
        {
            return _playerIndex;
        }

        public void setPlayerIndex_3518BA33(int i)
        {
            throw new NotSupportedException();
        }

        public bool isVibrateSupported_FBE0B2A4()
        {
            var capabilities = GamePad.GetCapabilities(_playerIndex);
            return capabilities.HasRightVibrationMotor || capabilities.HasLeftVibrationMotor;
        }

        public bool isVibrating_FBE0B2A4()
        {
            return _isVibrating;
        }

        public float getVibrationStrength_FFE0B8F0()
        {
            return _vibrationStrength;
        }

        public void startVibration_97413DCA(float strength)
        {
            _isVibrating = true;
            _vibrationStrength = strength;
            GamePad.SetVibration(_playerIndex, strength, strength);
        }

        public void stopVibration_EFE09FC0()
        {
            _isVibrating = false;
            _vibrationStrength = 0;
            GamePad.SetVibration(_playerIndex, 0, 0);
        }

        public bool isButtonDown_4118CD17(int buttonCode)
        {
            return GamePad.GetState(_playerIndex).IsButtonDown((Buttons) buttonCode);
        }

        public bool isButtonUp_4118CD17(int buttonCode)
        {
            return GamePad.GetState(_playerIndex).IsButtonUp((Buttons) buttonCode);
        }

        public float getAxis_4518D363(int axisCode)
        {
            var state = GamePad.GetState(_playerIndex);

            if(state == null)
            {
                switch ((AxisCodes)axisCode)
                {
                    case AxisCodes.LeftThumbstickX:
                        return _prevStatus.axes[0];
                    case AxisCodes.LeftThumbstickY:
                        return _prevStatus.axes[1];
                    case AxisCodes.RightThumbstickX:
                        return _prevStatus.axes[2];
                    case AxisCodes.RightThumbstickY:
                        return _prevStatus.axes[3];
                    case AxisCodes.LeftTrigger:
                        return _prevStatus.axes[4];
                    case AxisCodes.RightTrigger:
                        return _prevStatus.axes[5];
                    default:
                        return 0;
                }
            }

            switch ((AxisCodes)axisCode)
            {
                case AxisCodes.LeftThumbstickX:
                    return state.ThumbSticks.Left.X;
                case AxisCodes.LeftThumbstickY:
                    return state.ThumbSticks.Left.Y;
                case AxisCodes.RightThumbstickX:
                    return state.ThumbSticks.Right.X;
                case AxisCodes.RightThumbstickY:
                    return state.ThumbSticks.Right.Y;
                case AxisCodes.LeftTrigger:
                    return state.Triggers.Left;
                case AxisCodes.RightTrigger:
                    return state.Triggers.Right;
                default:
                    return 0;
            }
        }

        public bool isAccelerometerSupported_FBE0B2A4()
        {
            return false;
        }
        
        public Vector3 getAccelerometer_287A9422(int i)
        {
            throw new NotSupportedException();
        }

        public float getAccelerometerSensitivity_FFE0B8F0()
        {
            throw new NotSupportedException();
        }

        public void setAccelerometerSensitivity_97413DCA(float f)
        {
            throw new NotSupportedException();
        }

        public void update()
        {
            var currentStatus = getStatus();

            if (currentStatus.isConnected != _prevStatus.isConnected)
            {
                if (currentStatus.isConnected)
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onConnect_238EC38A(this);
                        node = node.Next;
                    }
                }
                else
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onDisconnect_238EC38A(this);
                        node = node.Next;
                    }
                }
            }
            
            if(_prevStatus.buttons != null)
            {
                for (int i = 0; i < _prevStatus.buttons.Length; i++)
                {
                    if (_prevStatus.buttons[i] != currentStatus.buttons[i])
                    {
                        if (currentStatus.buttons[i])
                        {
                            var node = _gamePadListeners.First;
                            while (node != null)
                            {
                                node.Value.onButtonDown_7016EF7D(this, buttonCodes[i]);
                                node = node.Next;
                            }
                        }
                        else
                        {
                            var node = _gamePadListeners.First;
                            while (node != null)
                            {
                                node.Value.onButtonUp_7016EF7D(this, buttonCodes[i]);
                                node = node.Next;
                            }
                        }
                    }
                }
            }

            if(_prevStatus.axes != null)
            {
                for (int i = 0; i < currentStatus.axes.Length; i++)
                {
                    if (_prevStatus.axes[i] != currentStatus.axes[i])
                    {
                        var node = _gamePadListeners.First;
                        while (node != null)
                        {
                            node.Value.onAxisChanged_AD47562B(this, i, currentStatus.axes[i]);
                            node = node.Next;
                        }
                    }
                }
            }

            if(_prevStatus.accelerometers != null)
            {
                for (int i = 0; i < currentStatus.accelerometers.Length; i++)
                {
                    if (_prevStatus.accelerometers[i] != currentStatus.accelerometers[i])
                    {
                        var node = _gamePadListeners.First;
                        while (node != null)
                        {
                            node.Value.onAccelerometerChanged_FE43FF32(this, i, currentStatus.accelerometers[i]);
                            node = node.Next;
                        }
                    }
                }
            }

            GamePadStatus nextStatus = _prevStatus;
            _prevStatus = currentStatus;
            _currentStatus = nextStatus;
        }

        public Java.Lang.String getInstanceId_E605312C()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName + "#" + _playerIndex;
        }

        public Java.Lang.String getModelInfo_E605312C()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName;
        }
    }
}