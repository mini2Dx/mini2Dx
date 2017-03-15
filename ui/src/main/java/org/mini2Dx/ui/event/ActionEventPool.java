/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class ActionEventPool {
	private static final Queue<ActionEvent> pool = new LinkedList<ActionEvent>();
	
	public static ActionEvent allocate() {
		ActionEvent result = pool.poll();
		if(result == null) {
			result = new ActionEvent();
		}
		return result;
	}
	
	public static void release(ActionEvent event) {
		pool.offer(event);
	}
}
