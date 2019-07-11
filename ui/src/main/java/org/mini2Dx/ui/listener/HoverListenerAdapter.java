package org.mini2Dx.ui.listener;

import org.mini2Dx.ui.element.Hoverable;

/**
 * Adapter class that implements {@link HoverListener}. All methods are
 * no-op and can be overridden individually.
 *
 * @author Caidan Williams
 */
public class HoverListenerAdapter implements HoverListener {
    @Override
    public void onHoverBegin(Hoverable source) {}

    @Override
    public void onHoverEnd(Hoverable source) {}
}
