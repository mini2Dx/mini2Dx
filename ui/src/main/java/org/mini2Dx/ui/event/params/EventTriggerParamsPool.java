/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event.params;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class EventTriggerParamsPool {
	private static final Queue<MouseEventTriggerParams> mouseParams = new LinkedList<MouseEventTriggerParams>();
	private static final Queue<KeyboardEventTriggerParams> keyboardParams = new LinkedList<KeyboardEventTriggerParams>();
	private static final Queue<ControllerEventTriggerParams> controllerParams = new LinkedList<ControllerEventTriggerParams>();
	
	public static MouseEventTriggerParams allocateMouseParams() {
		MouseEventTriggerParams result = mouseParams.poll();
		if(result == null) {
			result = new MouseEventTriggerParams();
		}
		return result;
	}
	
	public static void release(MouseEventTriggerParams params) {
		mouseParams.offer(params);
	}
	
	public static KeyboardEventTriggerParams allocateKeyboardParams() {
		KeyboardEventTriggerParams result = keyboardParams.poll();
		if(result == null) {
			result = new KeyboardEventTriggerParams();
		}
		return result;
	}
	
	public static void release(KeyboardEventTriggerParams params) {
		keyboardParams.offer(params);
	}
	
	public static ControllerEventTriggerParams allocateControllerParams() {
		ControllerEventTriggerParams result = controllerParams.poll();
		if(result == null) {
			result = new ControllerEventTriggerParams();
		}
		return result;
	}
	
	public static void release(ControllerEventTriggerParams params) {
		controllerParams.offer(params);
	}
}
