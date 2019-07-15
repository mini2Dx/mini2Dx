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
using org.mini2Dx.core.input;
using org.mini2Dx.core.input.ps4;
using org.mini2Dx.core.input.xbox;
using GamePad = Microsoft.Xna.Framework.Input.GamePad;
using GamePadType = org.mini2Dx.core.input.GamePadType;
using Vector3 = org.mini2Dx.gdx.math.Vector3;

namespace monogame.Input
{
    public class MonoGameGamePad : org.mini2Dx.core.input.GamePad
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
            public PovState[] povs;
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
            status.povs = new[]
            {
                getPov(0)
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

            for (int i = 0; i < PS4GamePad.ID.Length; i++)
            {
                if (displayName.Contains(PS4GamePad.ID[i]))
                {
                    return GamePadType.PS4;
                }
            }
            for (int i = 0; i < XboxGamePad.ID.Length; i++)
            {
                if(displayName.Contains(XboxGamePad.ID[i]))
                {
                    return GamePadType.XBOX;
                }
            }
            return GamePadType.UNKNOWN;
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

        public PovState getPov(int povCode)
        {
            if (povCode != 0)
            {
                throw new NotSupportedException();
            }

            var state = GamePad.GetState(povCode).DPad;
            PovState povState = PovState.CENTER;
            if (state.Down == ButtonState.Pressed)
            {
                povState = PovState.SOUTH;
            }

            if (state.Up == ButtonState.Pressed)
            {
                povState = PovState.NORTH;
            }

            if (state.Left == ButtonState.Pressed)
            {
                if (povState == PovState.NORTH)
                {
                    povState = PovState.NORTH_WEST;
                }
                else if (povState == PovState.SOUTH)
                {
                    povState = PovState.SOUTH_WEST;
                }
                else
                {
                    povState = PovState.WEST;
                }
            }

            if (state.Right == ButtonState.Pressed)
            {
                if (povState == PovState.NORTH)
                {
                    povState = PovState.NORTH_EAST;
                }
                else if (povState == PovState.SOUTH)
                {
                    povState = PovState.SOUTH_EAST;
                }
                else
                {
                    povState = PovState.EAST;
                }
            }

            return povState;
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
            
            for (int i = 0; i < currentStatus.povs.Length; i++)
            {
                if (_prevStatus.povs[i] != currentStatus.povs[i])
                {
                    var node = _gamePadListeners.First;
                    while (node != null)
                    {
                        node.Value.onPovChanged(this, i, currentStatus.povs[i]);
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

        public string getInstanceId()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName + "#" + _playerIndex;
        }

        public string getModelInfo()
        {
            return GamePad.GetCapabilities(_playerIndex).DisplayName;
        }
    }
}