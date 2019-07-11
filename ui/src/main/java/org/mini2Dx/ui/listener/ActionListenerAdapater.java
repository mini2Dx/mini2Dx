package org.mini2Dx.ui.listener;

import org.mini2Dx.ui.event.ActionEvent;

/**
 * Adapter class that implements {@link ActionListener}. All methods are
 * no-op and can be overridden individually.
 *
 * @author Caidan Williams
 */
public class ActionListenerAdapater implements ActionListener {
    @Override
    public void onActionBegin(ActionEvent event) {}

    @Override
    public void onActionEnd(ActionEvent event) {}
}
