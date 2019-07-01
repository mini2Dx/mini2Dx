package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.ui.element.UiElement;

public class PixelLayoutDependencyTree {
	private final Array<UiElement> queuedElements = new Array<UiElement>();
	private final ObjectMap<String, UiElement> dependsOn = new ObjectMap<String, UiElement>();
	private final ObjectMap<String, Runnable> operations = new ObjectMap<String, Runnable>();

	public void queue(final UiElement element, final UiElement dependsOnElement, final Runnable operation) {
		queuedElements.add(element);
		operations.put(element.getId(), operation);

		this.dependsOn.put(element.getId(), dependsOnElement);
	}

	public void update(float delta) {
		for(int i = queuedElements.size - 1; i >= 0; i--) {
			if(!process(queuedElements.get(i))) {
				return;
			}
		}
	}

	private boolean hasDependency(final UiElement element) {
		if(!dependsOn.containsKey(element.getId())) {
			return false;
		}
		final UiElement dependency = dependsOn.get(element.getId());
		if(dependsOn.containsKey(dependency.getId())) {
			return true;
		}
		return !dependency.isInitialised();
	}

	/**
	 * Process operations for a {@link UiElement}
	 * @param element
	 * @return True if processing can continue
	 */
	private boolean process(final UiElement element) {
		if(!element.isInitialised()) {
			return true;
		}
		if(hasDependency(element)) {
			return true;
		}
		dependsOn.remove(element.getId());
		queuedElements.removeValue(element, false);

		if(!operations.containsKey(element.getId())) {
			return true;
		}

		final Runnable runnable = operations.remove(element.getId());
		runnable.run();
		return !element.isFlexLayout();
	}

	public boolean isEmpty() {
		return queuedElements.size == 0;
	}
}
