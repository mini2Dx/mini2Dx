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
using monogame.Input;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Input;
using Org.Mini2Dx.Core.Input.Nswitch;
using Org.Mini2Dx.Core.Input.Ps4;
using Org.Mini2Dx.Core.Input.Xbox;
using Org.Mini2Dx.Gdx;
using Array = Org.Mini2Dx.Gdx.Utils.Array;
using GamePad = Org.Mini2Dx.Core.Input.GamePad;
using Math = System.Math;
using String = Java.Lang.String;

namespace monogame
{
    public class MonoGameInput : global::Java.Lang.Object, Org.Mini2Dx.Core._Input
    {
        private InputProcessor inputProcessor;
        private GamePadConnectionListener gamePadConnectionListener;

        private MouseState previousMouseState;
        private Keys[] previousPressedKeys;
        private MonoGameGamePad[] gamePads;
        private readonly Array gamePadsArray;
        private MouseState currentMouseState;
        private Keys[] currentPressedKeys;

        public MonoGameInput()
        {
            currentMouseState = previousMouseState = Mouse.GetState();
            currentPressedKeys = previousPressedKeys = new Keys[0];
            gamePads = new MonoGameGamePad[Microsoft.Xna.Framework.Input.GamePad.MaximumGamePadCount];
            for (var i = 0; i < gamePads.Length; i++)
            {
                gamePads[i] = new MonoGameGamePad(i);
            }
            gamePadsArray = Array.with_7D7B1E36(gamePads);
        }

        public void setInputProcessor_8E738C44(InputProcessor inputProcessor)
        {
            this.inputProcessor = inputProcessor;
        }

        public void setGamePadConnectionListener_39881AFC(GamePadConnectionListener listener, bool notifyExisting)
        {
            gamePadConnectionListener = listener;

            if(!notifyExisting)
            {
                return;
            }
            for(int i = 0; i < gamePads.Length; i++)
            {
                if(gamePads[i] == null)
                {
                    continue;
                }
                if(gamePads[i].isConnected_FBE0B2A4())
                {
                    listener.onConnect_238EC38A(gamePads[i]);
                }
            }
        }

        public Array getGamePads_9BE31B41()
        {
            return gamePadsArray;
        }

        public PS4GamePad newPS4GamePad_D12C0E63(GamePad gamePad)
        {
            return new MonoGamePS4GamePad(gamePad);
        }

        public SwitchDualJoyConGamePad newSwitchDualJoyConGamePad_B4A1FE67(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public SwitchJoyConLGamePad newSwitchJoyConLGamePad_DC6EEB37(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public SwitchJoyConRGamePad newSwitchJoyConRGamePad_663BA2E9(GamePad gamePad)
        {
            throw new NotImplementedException();
        }

        public XboxGamePad newXboxGamePad_1507DC33(GamePad gamePad)
        {
            return new MonoGameXboxGamePad(gamePad);
        }

        public void update()
        {
            try
            {
                updateControllerInput();
                previousMouseState = currentMouseState;
                previousPressedKeys = currentPressedKeys;
                currentMouseState = Mouse.GetState();
                currentPressedKeys = Keyboard.GetState().GetPressedKeys();
                if (inputProcessor != null)
                {
                    updateMouseInput();
                    updateKeyboardInput();
                }
            }
            catch(Exception e)
            {
                if(Mdx.log_ != null)
                {
                    Mdx.log_.error_159F0320("MonoGameInput", e.Message);
                }
            }
        }

        private void updateControllerInput()
        {
            for (int i = 0; i < gamePads.Length; i++)
            {
                if (gamePads[i] == null)
                {
                    continue;
                }
                if (!gamePads[i].wasConnected() && gamePads[i].isConnected_FBE0B2A4())
                {
                    if(gamePadConnectionListener != null)
                    {
                        gamePadConnectionListener.onConnect_238EC38A(gamePads[i]);
                    }
                }
                else if (gamePads[i].wasConnected() && !gamePads[i].isConnected_FBE0B2A4())
                {
                    if (gamePadConnectionListener != null)
                    {
                        gamePadConnectionListener.onDisconnect_238EC38A(gamePads[i]);
                    }
                }
                if (gamePads[i].wasConnected() || gamePads[i].isConnected_FBE0B2A4())
                {
                    gamePads[i].update();
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

            if (previousMouseState.X != currentMouseState.X || previousMouseState.Y != currentMouseState.Y)
            {
                if (isAnyMouseButtonPressed(currentMouseState))
                {
                    inputProcessor.touchDragged_F8B7DE3F(currentMouseState.X, currentMouseState.Y, 0);
                }
                else
                {
                    inputProcessor.mouseMoved_1E4D20DC(currentMouseState.X, currentMouseState.Y);
                }
            }

            if (previousMouseState.ScrollWheelValue != currentMouseState.ScrollWheelValue)
            {
                inputProcessor.scrolled_1548FAA4(0,
                    -Math.Sign(currentMouseState.ScrollWheelValue - previousMouseState.ScrollWheelValue));
            }

            if (previousMouseState.LeftButton != currentMouseState.LeftButton)
            {
                if (currentMouseState.LeftButton == ButtonState.Pressed)
                {
                    inputProcessor.touchDown_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.LEFT_);
                }
                else
                {
                    inputProcessor.touchUp_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.LEFT_);
                }
            }

            if (previousMouseState.MiddleButton != currentMouseState.MiddleButton)
            {
                if (currentMouseState.MiddleButton == ButtonState.Pressed)
                {
                    inputProcessor.touchDown_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.MIDDLE_);
                }
                else
                {
                    inputProcessor.touchUp_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.MIDDLE_);
                }
            }

            if (previousMouseState.RightButton != currentMouseState.RightButton)
            {
                if (currentMouseState.RightButton == ButtonState.Pressed)
                {
                    inputProcessor.touchDown_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.RIGHT_);
                }
                else
                {
                    inputProcessor.touchUp_A890D1B4(currentMouseState.X, currentMouseState.Y, 0, Org.Mini2Dx.Gdx.Input_n_Buttons.RIGHT_);
                }
            }
        }

        //custom contains implementation because c# one is based on linq which is too slow
        private static bool contains(Keys[] keys, Keys key)
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
            for (int i = 0; i < currentPressedKeys.Length; i++)
            {
                if (!contains(previousPressedKeys, currentPressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(currentPressedKeys[i]);
                    if(monoGameKey != 0)
                    {
                        inputProcessor.keyDown_4118CD17(monoGameKey);
                        var keyCharacter = monoGameKeyToChar(currentPressedKeys[i]);
                        if (keyCharacter != 0)
                        {
                            inputProcessor.keyTyped_0E996675(keyCharacter);
                        }
                    }
                }
            }

            for (int i = 0; i < previousPressedKeys.Length; i++)
            {
                if (!contains(currentPressedKeys, previousPressedKeys[i]))
                {
                    var monoGameKey = monoGameKeyToGdxKey(previousPressedKeys[i]);
                    if (monoGameKey != 0)
                    {
                        inputProcessor.keyUp_4118CD17(monoGameKey);
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
            if (key >= Org.Mini2Dx.Gdx.Input_n_Keys.A_ && key <= Org.Mini2Dx.Gdx.Input_n_Keys.Z_)
            {
                return key - Org.Mini2Dx.Gdx.Input_n_Keys.A_ + Keys.A;
            }

            if (key >= Org.Mini2Dx.Gdx.Input_n_Keys.F1_ && key <= Org.Mini2Dx.Gdx.Input_n_Keys.F12_)
            {
                return key - Org.Mini2Dx.Gdx.Input_n_Keys.F1_ + Keys.F1;
            }

            if (key >= Org.Mini2Dx.Gdx.Input_n_Keys.NUMPAD_0_ && key <= Org.Mini2Dx.Gdx.Input_n_Keys.NUMPAD_9_)
            {
                return key - Org.Mini2Dx.Gdx.Input_n_Keys.NUMPAD_0_ + Keys.NumPad0;
            }
            if (key >= Org.Mini2Dx.Gdx.Input_n_Keys.NUM_0_ && key <= Org.Mini2Dx.Gdx.Input_n_Keys.NUM_9_)
            {
                return key - Org.Mini2Dx.Gdx.Input_n_Keys.NUM_0_ + Keys.D0;
            }

            switch (key)
            {
                case Org.Mini2Dx.Gdx.Input_n_Keys.BACK_:
                    return Keys.Back;
                case Org.Mini2Dx.Gdx.Input_n_Keys.TAB_:
                    return Keys.Tab;
                case Org.Mini2Dx.Gdx.Input_n_Keys.ENTER_:
                    return Keys.Enter;
                case Org.Mini2Dx.Gdx.Input_n_Keys.ESCAPE_:
                    return Keys.Escape;
                case Org.Mini2Dx.Gdx.Input_n_Keys.SPACE_:
                    return Keys.Space;
                case Org.Mini2Dx.Gdx.Input_n_Keys.PAGE_UP_:
                    return Keys.PageUp;
                case Org.Mini2Dx.Gdx.Input_n_Keys.PAGE_DOWN_:
                    return Keys.PageDown;
                case Org.Mini2Dx.Gdx.Input_n_Keys.END_:
                    return Keys.End;
                case Org.Mini2Dx.Gdx.Input_n_Keys.HOME_:
                    return Keys.Home;
                case Org.Mini2Dx.Gdx.Input_n_Keys.LEFT_:
                    return Keys.Left;
                case Org.Mini2Dx.Gdx.Input_n_Keys.UP_:
                    return Keys.Up;
                case Org.Mini2Dx.Gdx.Input_n_Keys.RIGHT_:
                    return Keys.Right;
                case Org.Mini2Dx.Gdx.Input_n_Keys.DOWN_:
                    return Keys.Down;
                case Org.Mini2Dx.Gdx.Input_n_Keys.BUTTON_SELECT_:
                    return Keys.Select;
                case Org.Mini2Dx.Gdx.Input_n_Keys.INSERT_:
                    return Keys.Insert;
                case Org.Mini2Dx.Gdx.Input_n_Keys.DEL_:
                    return Keys.Delete;
                case Org.Mini2Dx.Gdx.Input_n_Keys.NUM_:
                    return Keys.Multiply;
                case Org.Mini2Dx.Gdx.Input_n_Keys.PLUS_:
                    return Keys.Add;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MINUS_:
                    return Keys.Subtract;
                case Org.Mini2Dx.Gdx.Input_n_Keys.SLASH_:
                    return Keys.Divide;
                case Org.Mini2Dx.Gdx.Input_n_Keys.SHIFT_LEFT_:
                    return Keys.LeftShift;
                case Org.Mini2Dx.Gdx.Input_n_Keys.SHIFT_RIGHT_:
                    return Keys.RightShift;
                case Org.Mini2Dx.Gdx.Input_n_Keys.CONTROL_LEFT_:
                    return Keys.LeftControl;
                case Org.Mini2Dx.Gdx.Input_n_Keys.CONTROL_RIGHT_:
                    return Keys.RightControl;
                case Org.Mini2Dx.Gdx.Input_n_Keys.ALT_LEFT_:
                    return Keys.LeftAlt;
                case Org.Mini2Dx.Gdx.Input_n_Keys.ALT_RIGHT_:
                    return Keys.RightAlt;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MUTE_:
                    return Keys.VolumeMute;
                case Org.Mini2Dx.Gdx.Input_n_Keys.VOLUME_UP_:
                    return Keys.VolumeUp;
                case Org.Mini2Dx.Gdx.Input_n_Keys.VOLUME_DOWN_:
                    return Keys.VolumeDown;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_NEXT_:
                    return Keys.MediaNextTrack;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_PREVIOUS_:
                    return Keys.MediaPreviousTrack;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_STOP_:
                    return Keys.MediaStop;
                case Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_PLAY_PAUSE_:
                    return Keys.MediaPlayPause;
                case Org.Mini2Dx.Gdx.Input_n_Keys.SEMICOLON_:
                    return Keys.OemSemicolon;
                case Org.Mini2Dx.Gdx.Input_n_Keys.COMMA_:
                    return Keys.OemComma;
                case Org.Mini2Dx.Gdx.Input_n_Keys.PERIOD_:
                    return Keys.OemPeriod;
                case Org.Mini2Dx.Gdx.Input_n_Keys.BACKSLASH_:
                    return Keys.OemBackslash;
                default:
                    return 0;
            }
        }

        private int monoGameKeyToGdxKey(Keys key)
        {
            if (key >= Keys.A && key <= Keys.Z)
            {
                return key - Keys.A + Org.Mini2Dx.Gdx.Input_n_Keys.A_;
            }

            if (key >= Keys.F1 && key <= Keys.F12)
            {
                return key - Keys.F1 + Org.Mini2Dx.Gdx.Input_n_Keys.F1_;
            }

            if (key >= Keys.NumPad0 && key <= Keys.NumPad9)
            {
                return key - Keys.NumPad0 + Org.Mini2Dx.Gdx.Input_n_Keys.NUMPAD_0_;
            }
            if (key >= Keys.D0 && key <= Keys.D9)
            {
                return key - Keys.D0 + Org.Mini2Dx.Gdx.Input_n_Keys.NUM_0_;
            }

            switch (key)
            {
                case Keys.Back:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.BACK_;
                case Keys.Tab:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.TAB_;
                case Keys.Enter:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.ENTER_;
                case Keys.Escape:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.ESCAPE_;
                case Keys.Space:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.SPACE_;
                case Keys.PageUp:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.PAGE_UP_;
                case Keys.PageDown:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.PAGE_DOWN_;
                case Keys.End:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.END_;
                case Keys.Home:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.HOME_;
                case Keys.Left:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.LEFT_;
                case Keys.Up:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.UP_;
                case Keys.Right:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.RIGHT_;
                case Keys.Down:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.DOWN_;
                case Keys.Select:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.BUTTON_SELECT_;
                case Keys.Insert:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.INSERT_;
                case Keys.Delete:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.DEL_;
                case Keys.Multiply:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.NUM_;
                case Keys.Add:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.PLUS_;
                case Keys.Subtract:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MINUS_;
                case Keys.Divide:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.SLASH_;
                case Keys.LeftShift:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.SHIFT_LEFT_;
                case Keys.RightShift:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.SHIFT_RIGHT_;
                case Keys.LeftControl:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.CONTROL_LEFT_;
                case Keys.RightControl:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.CONTROL_RIGHT_;
                case Keys.LeftAlt:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.ALT_LEFT_;
                case Keys.RightAlt:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.ALT_RIGHT_;
                case Keys.BrowserBack:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.BACK_;
                case Keys.VolumeMute:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MUTE_;
                case Keys.VolumeDown:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.VOLUME_DOWN_;
                case Keys.VolumeUp:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.VOLUME_UP_;
                case Keys.MediaNextTrack:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_NEXT_;
                case Keys.MediaPreviousTrack:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_PREVIOUS_;
                case Keys.MediaStop:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_STOP_;
                case Keys.MediaPlayPause:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MEDIA_PLAY_PAUSE_;
                case Keys.OemSemicolon:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.SEMICOLON_;
                case Keys.OemPlus:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.PLUS_;
                case Keys.OemComma:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.COMMA_;
                case Keys.OemMinus:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.MINUS_;
                case Keys.OemPeriod:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.PERIOD_;
                case Keys.OemBackslash:
                    return Org.Mini2Dx.Gdx.Input_n_Keys.BACKSLASH_;
                default:
                    return 0;
            }
        }

        public void setOnScreenKeyboardVisible_AA5A2C66(bool b)
        {
            if (Mdx.platform_.isDesktop_FBE0B2A4())
            {
                return;
            }
        }

        public int getX_0EE0D08D()
        {
            return previousMouseState.X;
        }

        public int getY_0EE0D08D()
        {
            return previousMouseState.Y;
        }

        public bool hasClipboardContents_FBE0B2A4()
        {
            return false;
        }

        public bool isClipboardSupported_FBE0B2A4()
        {
            return false;
        }

        public void setClipboardContents_56DB2ED6(String arg0)
        {
        }

        public String getClipboardContents_E605312C()
        {
            return null;
        }

        public bool isKeyJustPressed_4118CD17(int keyCode)
        {
            var monogameKey = gdxKeyToMonoGameKey(keyCode);
            return contains(currentPressedKeys, monogameKey) && !contains(previousPressedKeys, monogameKey);
        }

        public bool isKeyDown_4118CD17(int keyCode)
        {
            return contains(currentPressedKeys, gdxKeyToMonoGameKey(keyCode));
        }

        public bool isKeyUp_4118CD17(int keyCode)
        {
            return !isKeyDown_4118CD17(keyCode);
        }

        public bool justTouched_FBE0B2A4()
        {
            return previousMouseState.LeftButton == ButtonState.Released && currentMouseState.LeftButton == ButtonState.Pressed ||
                   previousMouseState.MiddleButton == ButtonState.Released && currentMouseState.MiddleButton == ButtonState.Pressed ||
                   previousMouseState.RightButton == ButtonState.Released && currentMouseState.RightButton == ButtonState.Pressed;
        }
    }
}
