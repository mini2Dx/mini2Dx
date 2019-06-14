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
using org.mini2Dx.gdx;

namespace monogame
{
    public class MonoGameInput : org.mini2Dx.core.Input
    {
        private InputProcessor _inputProcessor;
        
        private MouseState _previousMouseState;
        private Keys[] _previousPressedKeys;
        
        public MonoGameInput()
        {
            _previousMouseState = Mouse.GetState();
            _previousPressedKeys = new Keys[0];
        }
        
        public void setInputProcessor(InputProcessor inputProcessor)
        {
            _inputProcessor = inputProcessor;
        }

        public void update()
        {
            if (_inputProcessor == null)
            {
                return;
            }
            
            updateMouseInput();
            updateKeyboardInput();
        }

        private static bool isAnyMouseButtonPressed(MouseState state)
        {
            return state.LeftButton == ButtonState.Pressed || 
                   state.MiddleButton == ButtonState.Pressed ||
                   state.RightButton == ButtonState.Pressed;
        }
        
        private void updateMouseInput()
        {
            var currentState = Mouse.GetState();

            if (_previousMouseState.X != currentState.X || _previousMouseState.Y != currentState.Y)
            {
                if (isAnyMouseButtonPressed(currentState))
                {
                    _inputProcessor.touchDragged(currentState.X, currentState.Y, 0);
                }
                else
                {
                    _inputProcessor.mouseMoved(currentState.X, currentState.Y);
                }
            }

            if (_previousMouseState.ScrollWheelValue != currentState.ScrollWheelValue)
            {
                _inputProcessor.scrolled(
                    Math.Sign(currentState.ScrollWheelValue - _previousMouseState.ScrollWheelValue));
            }

            if (_previousMouseState.LeftButton != currentState.LeftButton)
            {
                if (currentState.LeftButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(currentState.X, currentState.Y, 0, Input.Buttons.LEFT);
                }
                else
                {
                    _inputProcessor.touchUp(currentState.X, currentState.Y, 0, Input.Buttons.LEFT);
                }
            }

            if (_previousMouseState.MiddleButton != currentState.MiddleButton)
            {
                if (currentState.MiddleButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(currentState.X, currentState.Y, 0, Input.Buttons.MIDDLE);
                }
                else
                {
                    _inputProcessor.touchUp(currentState.X, currentState.Y, 0, Input.Buttons.MIDDLE);
                }
            }

            if (_previousMouseState.RightButton != currentState.RightButton)
            {
                if (currentState.RightButton == ButtonState.Pressed)
                {
                    _inputProcessor.touchDown(currentState.X, currentState.Y, 0, Input.Buttons.RIGHT);
                }
                else
                {
                    _inputProcessor.touchUp(currentState.X, currentState.Y, 0, Input.Buttons.RIGHT);
                }
            }

            _previousMouseState = currentState;
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
            var pressedKeys = Keyboard.GetState().GetPressedKeys();
            for (int i = 0; i < pressedKeys.Length; i++)
            {
                if (!contains(_previousPressedKeys, pressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(pressedKeys[i]);
                    if(monoGameKey != 0)
                    {
                        _inputProcessor.keyDown(monoGameKey);
                        var keyCharacter = monoGameKeyToChar(pressedKeys[i]);
                        if (keyCharacter != 0)
                        {
                            _inputProcessor.keyTyped(keyCharacter);
                        }
                    }
                }
            }

            for (int i = 0; i < _previousPressedKeys.Length; i++)
            {
                if (!contains(pressedKeys, _previousPressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(_previousPressedKeys[i]);
                    if (monoGameKey != 0)
                    {
                        _inputProcessor.keyUp(monoGameKey);
                    }
                }
            }
            _previousPressedKeys = pressedKeys;
        }

        private char monoGameKeyToChar(Keys pressedKey)
        {
            if (pressedKey >= Keys.A && pressedKey <= Keys.Z)
            {
                return (char) (pressedKey - Keys.A + 'a');
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

        private int monoGameKeyToGdxKey(Keys pressedKey)
        {
            if (pressedKey >= Keys.A && pressedKey <= Keys.Z)
            {
                return pressedKey - Keys.A + Input.Keys.A;
            }
            
            if (pressedKey >= Keys.F1 && pressedKey <= Keys.F12)
            {
                return pressedKey - Keys.F1 + Input.Keys.F1;
            }

            if (pressedKey >= Keys.NumPad0 && pressedKey <= Keys.NumPad9)
            {
                return pressedKey - Keys.NumPad0 + Input.Keys.NUMPAD_0;
            }
            
            switch (pressedKey)
            {
                case Keys.Back:
                    return Input.Keys.BACK;
                case Keys.Tab:
                    return Input.Keys.TAB;
                case Keys.Enter:
                    return Input.Keys.ENTER;
                case Keys.Escape:
                    return Input.Keys.ESCAPE;
                case Keys.Space:
                    return Input.Keys.ESCAPE;
                case Keys.PageUp:
                    return Input.Keys.PAGE_UP;
                case Keys.PageDown:
                    return Input.Keys.PAGE_DOWN;
                case Keys.End:
                    return Input.Keys.END;
                case Keys.Home:
                    return Input.Keys.HOME;
                case Keys.Left:
                    return Input.Keys.LEFT;
                case Keys.Up:
                    return Input.Keys.UP;
                case Keys.Right:
                    return Input.Keys.RIGHT;
                case Keys.Down:
                    return Input.Keys.DOWN;
                case Keys.Select:
                    return Input.Keys.BUTTON_SELECT;
                case Keys.Insert:
                    return Input.Keys.INSERT;
                case Keys.Delete:
                    return Input.Keys.DEL;
                case Keys.Multiply:
                    return Input.Keys.NUM;
                case Keys.Add:
                    return Input.Keys.PLUS;
                case Keys.Subtract:
                    return Input.Keys.MINUS;
                case Keys.Divide:
                    return Input.Keys.SLASH;
                case Keys.LeftShift:
                    return Input.Keys.SHIFT_LEFT;
                case Keys.RightShift:
                    return Input.Keys.SHIFT_RIGHT;
                case Keys.LeftControl:
                    return Input.Keys.CONTROL_LEFT;
                case Keys.RightControl:
                    return Input.Keys.CONTROL_RIGHT;
                case Keys.LeftAlt:
                    return Input.Keys.ALT_LEFT;
                case Keys.RightAlt:
                    return Input.Keys.ALT_RIGHT;
                case Keys.BrowserBack:
                    return Input.Keys.BACK;
                case Keys.VolumeMute:
                    return Input.Keys.MUTE;
                case Keys.VolumeDown:
                    return Input.Keys.VOLUME_DOWN;
                case Keys.VolumeUp:
                    return Input.Keys.VOLUME_UP;
                case Keys.MediaNextTrack:
                    return Input.Keys.MEDIA_NEXT;
                case Keys.MediaPreviousTrack:
                    return Input.Keys.MEDIA_PREVIOUS;
                case Keys.MediaStop:
                    return Input.Keys.MEDIA_STOP;
                case Keys.MediaPlayPause:
                    return Input.Keys.MEDIA_PLAY_PAUSE;
                case Keys.OemSemicolon:
                    return Input.Keys.SEMICOLON;
                case Keys.OemPlus:
                    return Input.Keys.PLUS;
                case Keys.OemComma:
                    return Input.Keys.COMMA;
                case Keys.OemMinus:
                    return Input.Keys.MINUS;
                case Keys.OemPeriod:
                    return Input.Keys.PERIOD;
                case Keys.OemBackslash:
                    return Input.Keys.BACKSLASH;
                default:
                    return 0;
            }
        }
    }
}
