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
        private GamePadStatus _prevStatus;
        

        public MonoGameGamePad(int index)
        {
            _playerIndex = index;
            _gamePadListeners = new LinkedList<GamePadListener>();
            _prevStatus = getStatus();
        }

        private GamePadStatus getStatus()
        {
            var state = GamePad.GetState(_playerIndex);
            var status = new GamePadStatus();
            
            status.isConnected = state.IsConnected;
            status.buttons = new bool[buttonCodes.Length];
            for (var i = 0; i < buttonCodes.Length; i++)
            {
                status.buttons[i] = state.IsButtonDown((Buttons) buttonCodes[i]);
            }
            status.axes = new []
            {
                state.ThumbSticks.Left.X,
                state.ThumbSticks.Left.Y,
                state.ThumbSticks.Right.X,
                state.ThumbSticks.Right.Y,
                state.Triggers.Left,
                state.Triggers.Right
            };
            status.accelerometers = new Vector3[0];
            
            return status;
        }
        
        public void addListener(GamePadListener gpl)
        {
            _gamePadListeners.AddLast(gpl);
        }

        public void removeListener(GamePadListener gpl)
        {
            _gamePadListeners.Remove(gpl);
        }

        public GamePadType getGamePadType()
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
            return GamePadType.UNKNOWN_;
        }

        public bool wasConnected()
        {
            return _prevStatus.isConnected;
        }

        public bool isConnected()
        {
            return GamePad.GetState(_playerIndex).IsConnected;
        }

        public bool isPlayerIndicesSupported()
        {
            return true;
        }

        public int getPlayerIndex()
        {
            return _playerIndex;
        }

        public void setPlayerIndex(int i)
        {
            throw new NotSupportedException();
        }

        public bool isVibrateSupported()
        {
            var capabilities = GamePad.GetCapabilities(_playerIndex);
            return capabilities.HasRightVibrationMotor || capabilities.HasLeftVibrationMotor;
        }

        public bool isVibrating()
        {
            return _isVibrating;
        }

        public float getVibrationStrength()
        {
            return _vibrationStrength;
        }

        public void startVibration(float strength)
        {
            _isVibrating = true;
            _vibrationStrength = strength;
            GamePad.SetVibration(_playerIndex, strength, strength);
        }

        public void stopVibration()
        {
            _isVibrating = false;
            _vibrationStrength = 0;
            GamePad.SetVibration(_playerIndex, 0, 0);
        }

        public bool isButtonDown(int buttonCode)
        {
            return GamePad.GetState(_playerIndex).IsButtonDown((Buttons) buttonCode);
        }

        public bool isButtonUp(int buttonCode)
        {
            return GamePad.GetState(_playerIndex).IsButtonUp((Buttons) buttonCode);
        }

        public float getAxis(int axisCode)
        {
            var state = GamePad.GetState(_playerIndex);
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

        public bool isAccelerometerSupported()
        {
            return false;
        }
        
        public Vector3 getAccelerometer(int i)
        {
            throw new NotSupportedException();
        }

        public float getAccelerometerSensitivity()
        {
            throw new NotSupportedException();
        }

        public void setAccelerometerSensitivity(float f)
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
                        node.Value.onConnect(this);
                        node = node.Next;
                    }
                }
                else
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onDisconnect(this);
                        node = node.Next;
                    }
                }
            }
            
            for (int i = 0; i < _prevStatus.buttons.Length; i++)
            {
                if (_prevStatus.buttons[i] != currentStatus.buttons[i])
                {
                    if (currentStatus.buttons[i])
                    {
                        var node = _gamePadListeners.First;
                        while (node != null)
                        {
                            node.Value.onButtonDown(this, buttonCodes[i]);
                            node = node.Next;
                        }
                    }
                    else
                    {
                        var node = _gamePadListeners.First;
                        while (node != null)
                        {
                            node.Value.onButtonUp(this, buttonCodes[i]);
                            node = node.Next;
                        }
                    }
                }
            }
            
            for (int i = 0; i < currentStatus.axes.Length; i++)
            {
                if (_prevStatus.axes[i] != currentStatus.axes[i])
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onAxisChanged(this, i, currentStatus.axes[i]);
                        node = node.Next;
                    }
                }
            }
            
            for (int i = 0; i < currentStatus.accelerometers.Length; i++)
            {
                if (_prevStatus.accelerometers[i] != currentStatus.accelerometers[i])
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onAccelerometerChanged(this, i, currentStatus.accelerometers[i]);
                        node = node.Next;
                    }
                }
            }

            _prevStatus = currentStatus;
        }

        public Java.Lang.String getInstanceId()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName + "#" + _playerIndex;
        }

        public Java.Lang.String getModelInfo()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName;
        }
    }
}