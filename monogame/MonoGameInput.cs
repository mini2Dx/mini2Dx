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
using System.Runtime.Remoting.Messaging;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;
using monogame.Input;
using org.mini2Dx.core;
using org.mini2Dx.core.input.nswitch;
using org.mini2Dx.core.input.ps4;
using org.mini2Dx.core.input.xbox;
using org.mini2Dx.gdx;
using Array = org.mini2Dx.gdx.utils.Array;
using GamePad = org.mini2Dx.core.input.GamePad;
using Math = System.Math;

namespace monogame
{
    public class MonoGameInput : org.mini2Dx.core.Input
    {
        private InputProcessor _inputProcessor;
        
        private MouseState _previousMouseState;
        private Keys[] _previousPressedKeys;
        private MonoGameGamePad[] _gamePads;
        private readonly Array _gamePadsArray;
        private MouseState _currentMouseState;
        private Keys[] _currentPressedKeys;

        public MonoGameInput()
        {
            _currentMouseState = _previousMouseState = Mouse.GetState();
            _currentPressedKeys = _previousPressedKeys = new Keys[0];
            _gamePads = new MonoGameGamePad[Microsoft.Xna.Framework.Input.GamePad.MaximumGamePadCount];
            for (var i = 0; i < _gamePads.Length; i++)
            {
                _gamePads[i] = new MonoGameGamePad(i);
            }
            _gamePadsArray = Array.with(_gamePads);
        }
        
        public void setInputProcessor(InputProcessor inputProcessor)
        {
            _inputProcessor = inputProcessor;
        }

        public Array getGamePads()
        {
            return _gamePadsArray;
        }

        public PS4GamePad newPS4GamePad(GamePad gamePad)
        {
            return new MonoGamePS4GamePad(gamePad);
        }

        public SwitchDualJoyConGamePad newSwitchDualJoyConGamePad(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public SwitchJoyConLGamePad newSwitchJoyConLGamePad(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public SwitchJoyConRGamePad newSwitchJoyConRGamePad(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public XboxGamePad newXboxGamePad(GamePad gamePad)
        {
            return new MonoGameXboxGamePad(gamePad);
        }

        public void update()
        {
            updateControllerInput();
            _previousMouseState = _currentMouseState;
            _previousPressedKeys = _currentPressedKeys;
            _currentMouseState = Mouse.GetState();
            _currentPressedKeys = Keyboard.GetState().GetPressedKeys();
            if (_inputProcessor != null)
            {
                updateMouseInput();
                updateKeyboardInput();
            }
        }

        private void updateControllerInput()
        {
            for (int i = 0; i < _gamePads.Length; i++)
            {
                if (_gamePads[i].wasConnected() || _gamePads[i].isConnected())
                {
                    _gamePads[i].update();
                }
            }
        }

        private static bool isAnyMouseButtonPressed(MouseState state)
        {
            return state.LeftButton == ButtonState.Pressed || 
                   state.MiddleButton == ButtonState.Pressed ||
                   state.RightButton == ButtonState.Pressed;
        }
        
        private void updateMouseInput()
        {

            if (_previousMouseState.X != _currentMouseState.X || _previousMouseState.Y != _currentMouseState.Y)
            {
                if (isAnyMouseButtonPressed(_currentMouseState))
                {
                    _inputProcessor.touchDragged(_currentMouseState.X, _currentMouseState.Y, 0);
                }
                else
                {
                    _inputProcessor.mouseMoved(_currentMouseState.X, _currentMouseState.Y);
                }
            }

            if (_previousMouseState.ScrollWheelValue != _currentMouseState.ScrollWheelValue)
            {
                _inputProcessor.scrolled(
                    Math.Sign(_currentMouseState.ScrollWheelValue - _previousMouseState.ScrollWheelValue));
            }

            if (_previousMouseState.LeftButton != _currentMouseState.LeftButton)
            {
                if (_currentMouseState.LeftButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.LEFT);
                }
                else
                {
                    _inputProcessor.touchUp(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.LEFT);
                }
            }

            if (_previousMouseState.MiddleButton != _currentMouseState.MiddleButton)
            {
                if (_currentMouseState.MiddleButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.MIDDLE);
                }
                else
                {
                    _inputProcessor.touchUp(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.MIDDLE);
                }
            }

            if (_previousMouseState.RightButton != _currentMouseState.RightButton)
            {
                if (_currentMouseState.RightButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.RIGHT);
                }
                else
                {
                    _inputProcessor.touchUp(_currentMouseState.X, _currentMouseState.Y, 0, org.mini2Dx.gdx.Input.Buttons.RIGHT);
                }
            }
        }

        //custom contains implementation because c# one is based on linq which is too slow
        private bool contains(Keys[] keys, Keys key)
        {
            for (int i = 0; i < keys.Length; i++)
            {
                if (keys[i] == key)
                {
                    return true;
                }
            }

            return false;
        }
        private void updateKeyboardInput()
        {
            for (int i = 0; i < _currentPressedKeys.Length; i++)
            {
                if (!contains(_previousPressedKeys, _currentPressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(_currentPressedKeys[i]);
                    if(monoGameKey != 0)
                    {
                        _inputProcessor.keyDown(monoGameKey);
                        var keyCharacter = monoGameKeyToChar(_currentPressedKeys[i]);
                        if (keyCharacter != 0)
                        {
                            _inputProcessor.keyTyped(keyCharacter);
                        }
                    }
                }
            }

            for (int i = 0; i < _previousPressedKeys.Length; i++)
            {
                if (!contains(_currentPressedKeys, _previousPressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(_previousPressedKeys[i]);
                    if (monoGameKey != 0)
                    {
                        _inputProcessor.keyUp(monoGameKey);
                    }
                }
            }
        }

        private char monoGameKeyToChar(Keys pressedKey)
        {
            if (pressedKey >= Keys.A && pressedKey <= Keys.Z)
            {
                return (char) (pressedKey - Keys.A + 'a');
            }
            if (pressedKey >= Keys.D0 && pressedKey <= Keys.D9)
            {
                return (char) (pressedKey - Keys.D0 + '0');
            }

            switch (pressedKey)
            {
                case Keys.Tab:
                    return '\t';
                case Keys.Space:
                    return ' ';
                case Keys.NumPad0:
                    return '0';
                case Keys.NumPad1:
                    return '1';
                case Keys.NumPad2:
                    return '2';
                case Keys.NumPad3:
                    return '3';
                case Keys.NumPad4:
                    return '4';
                case Keys.NumPad5:
                    return '5';
                case Keys.NumPad6:
                    return '6';
                case Keys.NumPad7:
                    return '7';
                case Keys.NumPad8:
                    return '8';
                case Keys.NumPad9:
                    return '9';
                case Keys.Multiply:
                    return '*';
                case Keys.Add:
                    return '+';
                case Keys.Separator:
                    return '.';
                case Keys.Subtract:
                    return '-';
                case Keys.Decimal:
                    return '.';
                case Keys.Divide:
                    return '/';
                case Keys.OemSemicolon:
                    return ';';
                case Keys.OemPlus:
                    return '+';
                case Keys.OemComma:
                    return ',';
                case Keys.OemMinus:
                    return '-';
                case Keys.OemPeriod:
                    return '.';
                case Keys.OemQuestion:
                    return '?';
                case Keys.OemTilde:
                    return '~';
                case Keys.OemOpenBrackets:
                    return '(';
                case Keys.OemPipe:
                    return '|';
                case Keys.OemCloseBrackets:
                    return ')';
                case Keys.OemQuotes:
                    return '"';
                case Keys.OemBackslash:
                    return '\\';
                default:
                    return '\0';
            }
        }

        private Keys gdxKeyToMonoGameKey(int key)
        {
            if (key >= org.mini2Dx.gdx.Input.Keys.A && key <= org.mini2Dx.gdx.Input.Keys.Z)
            {
                return key - org.mini2Dx.gdx.Input.Keys.A + Keys.A;
            }
            
            if (key >= org.mini2Dx.gdx.Input.Keys.F1 && key <= org.mini2Dx.gdx.Input.Keys.F12)
            {
                return key - org.mini2Dx.gdx.Input.Keys.F1 + Keys.F1;
            }

            if (key >= org.mini2Dx.gdx.Input.Keys.NUMPAD_0 && key <= org.mini2Dx.gdx.Input.Keys.NUMPAD_9)
            {
                return key - org.mini2Dx.gdx.Input.Keys.NUMPAD_0 + Keys.NumPad0;
            }
            if (key >= org.mini2Dx.gdx.Input.Keys.NUM_0 && key <= org.mini2Dx.gdx.Input.Keys.NUM_9)
            {
                return key - org.mini2Dx.gdx.Input.Keys.NUM_0 + Keys.D0;
            }

            switch (key)
            {
                case org.mini2Dx.gdx.Input.Keys.BACK:
                    return Keys.Back;
                case org.mini2Dx.gdx.Input.Keys.TAB:
                    return Keys.Tab;
                case org.mini2Dx.gdx.Input.Keys.ENTER:
                    return Keys.Enter;
                case org.mini2Dx.gdx.Input.Keys.ESCAPE:
                    return Keys.Escape;
                case org.mini2Dx.gdx.Input.Keys.SPACE:
                    return Keys.Space;
                case org.mini2Dx.gdx.Input.Keys.PAGE_UP:
                    return Keys.PageUp;
                case org.mini2Dx.gdx.Input.Keys.PAGE_DOWN:
                    return Keys.PageDown;
                case org.mini2Dx.gdx.Input.Keys.END:
                    return Keys.End;
                case org.mini2Dx.gdx.Input.Keys.HOME:
                    return Keys.Home;
                case org.mini2Dx.gdx.Input.Keys.LEFT:
                    return Keys.Left;
                case org.mini2Dx.gdx.Input.Keys.UP:
                    return Keys.Up;
                case org.mini2Dx.gdx.Input.Keys.RIGHT:
                    return Keys.Right;
                case org.mini2Dx.gdx.Input.Keys.DOWN:
                    return Keys.Down;
                case org.mini2Dx.gdx.Input.Keys.BUTTON_SELECT:
                    return Keys.Select;
                case org.mini2Dx.gdx.Input.Keys.INSERT:
                    return Keys.Insert;
                case org.mini2Dx.gdx.Input.Keys.DEL:
                    return Keys.Delete;
                case org.mini2Dx.gdx.Input.Keys.NUM:
                    return Keys.Multiply;
                case org.mini2Dx.gdx.Input.Keys.PLUS:
                    return Keys.Add;
                case org.mini2Dx.gdx.Input.Keys.MINUS:
                    return Keys.Subtract;
                case org.mini2Dx.gdx.Input.Keys.SLASH:
                    return Keys.Divide;
                case org.mini2Dx.gdx.Input.Keys.SHIFT_LEFT:
                    return Keys.LeftShift;
                case org.mini2Dx.gdx.Input.Keys.SHIFT_RIGHT:
                    return Keys.RightShift;
                case org.mini2Dx.gdx.Input.Keys.CONTROL_LEFT:
                    return Keys.LeftControl;
                case org.mini2Dx.gdx.Input.Keys.CONTROL_RIGHT:
                    return Keys.RightControl;
                case org.mini2Dx.gdx.Input.Keys.ALT_LEFT:
                    return Keys.LeftAlt;
                case org.mini2Dx.gdx.Input.Keys.ALT_RIGHT:
                    return Keys.RightAlt;
                case org.mini2Dx.gdx.Input.Keys.MUTE:
                    return Keys.VolumeMute;
                case org.mini2Dx.gdx.Input.Keys.VOLUME_UP:
                    return Keys.VolumeUp;
                case org.mini2Dx.gdx.Input.Keys.VOLUME_DOWN:
                    return Keys.VolumeDown;
                case org.mini2Dx.gdx.Input.Keys.MEDIA_NEXT:
                    return Keys.MediaNextTrack;
                case org.mini2Dx.gdx.Input.Keys.MEDIA_PREVIOUS:
                    return Keys.MediaPreviousTrack;
                case org.mini2Dx.gdx.Input.Keys.MEDIA_STOP:
                    return Keys.MediaStop;
                case org.mini2Dx.gdx.Input.Keys.MEDIA_PLAY_PAUSE:
                    return Keys.MediaPlayPause;
                case org.mini2Dx.gdx.Input.Keys.SEMICOLON:
                    return Keys.OemSemicolon;
                case org.mini2Dx.gdx.Input.Keys.COMMA:
                    return Keys.OemComma;
                case org.mini2Dx.gdx.Input.Keys.PERIOD:
                    return Keys.OemPeriod;
                case org.mini2Dx.gdx.Input.Keys.BACKSLASH:
                    return Keys.OemBackslash;
                default:
                    return 0;
            }
        }

        private int monoGameKeyToGdxKey(Keys key)
        {
            if (key >= Keys.A && key <= Keys.Z)
            {
                return key - Keys.A + org.mini2Dx.gdx.Input.Keys.A;
            }
            
            if (key >= Keys.F1 && key <= Keys.F12)
            {
                return key - Keys.F1 + org.mini2Dx.gdx.Input.Keys.F1;
            }

            if (key >= Keys.NumPad0 && key <= Keys.NumPad9)
            {
                return key - Keys.NumPad0 + org.mini2Dx.gdx.Input.Keys.NUMPAD_0;
            }
            if (key >= Keys.D0 && key <= Keys.D9)
            {
                return key - Keys.D0 + org.mini2Dx.gdx.Input.Keys.NUM_0;
            }
            
            switch (key)
            {
                case Keys.Back:
                    return org.mini2Dx.gdx.Input.Keys.BACK;
                case Keys.Tab:
                    return org.mini2Dx.gdx.Input.Keys.TAB;
                case Keys.Enter:
                    return org.mini2Dx.gdx.Input.Keys.ENTER;
                case Keys.Escape:
                    return org.mini2Dx.gdx.Input.Keys.ESCAPE;
                case Keys.Space:
                    return org.mini2Dx.gdx.Input.Keys.SPACE;
                case Keys.PageUp:
                    return org.mini2Dx.gdx.Input.Keys.PAGE_UP;
                case Keys.PageDown:
                    return org.mini2Dx.gdx.Input.Keys.PAGE_DOWN;
                case Keys.End:
                    return org.mini2Dx.gdx.Input.Keys.END;
                case Keys.Home:
                    return org.mini2Dx.gdx.Input.Keys.HOME;
                case Keys.Left:
                    return org.mini2Dx.gdx.Input.Keys.LEFT;
                case Keys.Up:
                    return org.mini2Dx.gdx.Input.Keys.UP;
                case Keys.Right:
                    return org.mini2Dx.gdx.Input.Keys.RIGHT;
                case Keys.Down:
                    return org.mini2Dx.gdx.Input.Keys.DOWN;
                case Keys.Select:
                    return org.mini2Dx.gdx.Input.Keys.BUTTON_SELECT;
                case Keys.Insert:
                    return org.mini2Dx.gdx.Input.Keys.INSERT;
                case Keys.Delete:
                    return org.mini2Dx.gdx.Input.Keys.DEL;
                case Keys.Multiply:
                    return org.mini2Dx.gdx.Input.Keys.NUM;
                case Keys.Add:
                    return org.mini2Dx.gdx.Input.Keys.PLUS;
                case Keys.Subtract:
                    return org.mini2Dx.gdx.Input.Keys.MINUS;
                case Keys.Divide:
                    return org.mini2Dx.gdx.Input.Keys.SLASH;
                case Keys.LeftShift:
                    return org.mini2Dx.gdx.Input.Keys.SHIFT_LEFT;
                case Keys.RightShift:
                    return org.mini2Dx.gdx.Input.Keys.SHIFT_RIGHT;
                case Keys.LeftControl:
                    return org.mini2Dx.gdx.Input.Keys.CONTROL_LEFT;
                case Keys.RightControl:
                    return org.mini2Dx.gdx.Input.Keys.CONTROL_RIGHT;
                case Keys.LeftAlt:
                    return org.mini2Dx.gdx.Input.Keys.ALT_LEFT;
                case Keys.RightAlt:
                    return org.mini2Dx.gdx.Input.Keys.ALT_RIGHT;
                case Keys.BrowserBack:
                    return org.mini2Dx.gdx.Input.Keys.BACK;
                case Keys.VolumeMute:
                    return org.mini2Dx.gdx.Input.Keys.MUTE;
                case Keys.VolumeDown:
                    return org.mini2Dx.gdx.Input.Keys.VOLUME_DOWN;
                case Keys.VolumeUp:
                    return org.mini2Dx.gdx.Input.Keys.VOLUME_UP;
                case Keys.MediaNextTrack: 
                    return org.mini2Dx.gdx.Input.Keys.MEDIA_NEXT;
                case Keys.MediaPreviousTrack:
                    return org.mini2Dx.gdx.Input.Keys.MEDIA_PREVIOUS;
                case Keys.MediaStop:
                    return org.mini2Dx.gdx.Input.Keys.MEDIA_STOP;
                case Keys.MediaPlayPause:
                    return org.mini2Dx.gdx.Input.Keys.MEDIA_PLAY_PAUSE;
                case Keys.OemSemicolon:
                    return org.mini2Dx.gdx.Input.Keys.SEMICOLON;
                case Keys.OemPlus:
                    return org.mini2Dx.gdx.Input.Keys.PLUS;
                case Keys.OemComma:
                    return org.mini2Dx.gdx.Input.Keys.COMMA;
                case Keys.OemMinus:
                    return org.mini2Dx.gdx.Input.Keys.MINUS;
                case Keys.OemPeriod:
                    return org.mini2Dx.gdx.Input.Keys.PERIOD;
                case Keys.OemBackslash:
                    return org.mini2Dx.gdx.Input.Keys.BACKSLASH;
                default:
                    return 0;
            }
        }
        
        public void setOnScreenKeyboardVisible(bool b)
        {
            if (Mdx.platform.isDesktop())
            {
                return;
            }
        }

        public int getX()
        {
            return _previousMouseState.X;
        }

        public int getY()
        {
            return _previousMouseState.Y;
        }

        public bool isKeyJustPressed(int i)
        {
            var monogameKey = gdxKeyToMonoGameKey(i);
            return contains(_currentPressedKeys, monogameKey) && !contains(_previousPressedKeys, monogameKey);
        }

        public bool justTouched()
        {
            return _previousMouseState.LeftButton == ButtonState.Released && _currentMouseState.LeftButton == ButtonState.Pressed ||
                   _previousMouseState.MiddleButton == ButtonState.Released && _currentMouseState.MiddleButton == ButtonState.Pressed ||
                   _previousMouseState.RightButton == ButtonState.Released && _currentMouseState.RightButton == ButtonState.Pressed;
        }
    }
}
