package org.mini2Dx.ui.listener;

import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

/**
 * Adapter class that implements {@link UiInputSourceListener}. All methods are
 * no-op and can be overridden individually.
 *
 * @author Caidan Williams
 */
public class UiInputSourceListenerAdapter implements UiInputSourceListener {
    @Override
    public void inputSourceChanged(UiContainer container, InputSource oldInputSource, InputSource newInputSource) {}

    @Override
    public void gamePadTypeChanged(UiContainer container, GamePadType oldGamePadType, GamePadType newGamePadType) {}
}
