/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.event.params.EventTriggerParams;

/**
 *
 */
public class ActionEvent {	
	private Actionable source;
	private EventTrigger eventTrigger;
	private EventTriggerParams eventTriggerParams;
	
	public void set(Actionable source, EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		this.source = source;
		this.eventTrigger = eventTrigger;
		this.eventTriggerParams = eventTriggerParams;
	}

	public Actionable getSource() {
		return source;
	}

	public EventTrigger getEventTrigger() {
		return eventTrigger;
	}

	public EventTriggerParams getEventTriggerParams() {
		return eventTriggerParams;
	}
}
