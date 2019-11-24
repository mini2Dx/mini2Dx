/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.*;

/**
 * Utility class for layout of elements based on pixel coordinates
 */
public class PixelLayoutUtils {
	private static final PixelLayoutDependencyTree ALIGN_DEPENDENCY_TREE = new PixelLayoutDependencyTree();
	private static final PixelLayoutDependencyTree SIZE_DEPENDENCY_TREE = new PixelLayoutDependencyTree();

	public static void update(float delta) {
		SIZE_DEPENDENCY_TREE.update(delta);
		ALIGN_DEPENDENCY_TREE.update(delta);
	}

	public static boolean isOperationsComplete() {
		if(!SIZE_DEPENDENCY_TREE.isEmpty()) {
			return false;
		}
		if(!ALIGN_DEPENDENCY_TREE.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Shrinks the width and height for a {@link ParentUiElement} based on its children
	 * @param parentUiElement The {@link ParentUiElement} to set
	 */
	public static void shrinkToContents(final ParentUiElement parentUiElement, final boolean recursive, final Runnable callback) {
		switch(UiContainer.getState()) {
		case LAYOUT:
		case UPDATE:
			deferShrinkToContentsUntilUpdate(parentUiElement, recursive, callback);
			return;
		case NOOP:
		case RENDER:
			break;
		}

		if(!parentUiElement.isInitialised()) {
			deferShrinkToContentsUntilUpdate(parentUiElement, recursive, callback);
			return;
		}

		if(parentUiElement.getFlexLayout() != null) {
			if(recursive) {
				for(int i = 0; i < parentUiElement.getTotalChildren(); i++) {
					final UiElement child = parentUiElement.getChild(i);
					if(!(child instanceof ParentUiElement)) {
						continue;
					}
					final ParentUiElement nestedTree = (ParentUiElement) child;
					nestedTree.shrinkToContents(true, new Runnable() {
						@Override
						public void run() {
							shrinkToContents(parentUiElement, false, callback);
						}
					});
				}
			} else {
				if (callback != null) {
					parentUiElement.deferUntilLayout(callback);
				}
			}
			return;
		}
		if(parentUiElement instanceof TabView) {
			if(recursive) {
				final TabView tabView = (TabView) parentUiElement;
				for(int i = 0; i < tabView.getTotalTabs(); i++) {
					final Tab tab = tabView.getTab(i);
					for(int j = 0; j < tab.getTotalChildren(); j++) {
						final UiElement child = tab.getChild(i);
						if(!(child instanceof ParentUiElement)) {
							continue;
						}
						final ParentUiElement nestedTree = (ParentUiElement) child;
						nestedTree.shrinkToContents(true, new Runnable() {
							@Override
							public void run() {
								shrinkToContents(parentUiElement, false, callback);
							}
						});
					}
				}
			} else {
				if(callback != null) {
					parentUiElement.deferUntilLayout(callback);
				}
			}
			return;
		}

		if(recursive) {
			boolean matchedParent = false;
			for(int i = 0; i < parentUiElement.getTotalChildren(); i++) {
				final UiElement child = parentUiElement.getChild(i);
				if(!(child instanceof ParentUiElement)) {
					continue;
				}
				final ParentUiElement nestedTree = (ParentUiElement) child;
				nestedTree.shrinkToContents(true, new Runnable() {
					@Override
					public void run() {
						shrinkToContents(parentUiElement, false, callback);
					}
				});
				matchedParent = true;
			}

			if(matchedParent) {
				return;
			}
		}

		float maxX = 0f;
		float maxY = 0f;
		for(int i = 0; i < parentUiElement.getTotalChildren(); i++) {
			final UiElement child = parentUiElement.getChild(i);
			if(!child.isInitialLayoutOccurred()) {
				child.deferUntilLayout(new Runnable() {
					@Override
					public void run() {
						shrinkToContents(parentUiElement, recursive, callback);
					}
				});
				return;
			}
			if(!child.isInitialUpdateOccurred()) {
				child.deferUntilUpdate(new Runnable() {
					@Override
					public void run() {
						shrinkToContents(parentUiElement, recursive, callback);
					}
				});
				return;
			}
			if(child.isRenderNodeDirty()) {
				child.deferUntilLayout(new Runnable() {
					@Override
					public void run() {
						shrinkToContents(parentUiElement, recursive, callback);
					}
				});
				return;
			}
			maxX = Math.max(maxX, child.getX() + child.getWidth());
			maxY = Math.max(maxY, child.getY() + child.getHeight());
		}
		parentUiElement.setContentWidth(maxX);
		parentUiElement.setContentHeight(maxY);

		if(callback != null) {
			parentUiElement.deferUntilLayout(callback);
		}
	}

	private static void deferShrinkToContentsUntilLayout(final ParentUiElement parentUiElement, final boolean recursive, final Runnable callback) {
		if(parentUiElement.getTotalChildren() > 0) {
			parentUiElement.setRenderNodeDirty();
			final UiElement lastChild = parentUiElement.get(parentUiElement.getTotalChildren() - 1);
			lastChild.deferUntilLayout(new Runnable() {
				@Override
				public void run() {
					shrinkToContents(parentUiElement, recursive, callback);
				}
			});
			return;
		}
		parentUiElement.deferUntilLayout(new Runnable() {
			@Override
			public void run() {
				shrinkToContents(parentUiElement, recursive, callback);
			}
		});
	}

	private static void deferShrinkToContentsUntilUpdate(final ParentUiElement parentUiElement, final boolean recursive, final Runnable callback) {
		if(parentUiElement.getTotalChildren() > 0) {
			parentUiElement.get(parentUiElement.getTotalChildren() - 1).deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					shrinkToContents(parentUiElement, recursive, callback);
				}
			});
			return;
		}
		parentUiElement.deferUntilUpdate(new Runnable() {
			@Override
			public void run() {
				shrinkToContents(parentUiElement, recursive, callback);
			}
		});
	}

	/**
	 * Aligns the edges of a {@link UiElement} to the edges of another element.
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the right-side of this element to the left side of the align element.
	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	{@link HorizontalAlignment#RIGHT} aligns the left-side of this element to the right-side of the align element.
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the bottom-side of this element to the top-side of the align element.
	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	{@link VerticalAlignment#BOTTOM} aligns the top-side of this element to the bottom-side of the align element.
	 */
	public static void alignEdgeToEdge(final UiElement element, final UiElement alignToElement, final HorizontalAlignment horizontalAlignment, final VerticalAlignment verticalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, alignToElement, new Runnable() {
			@Override
			public void run() {
				final float x,y;
				switch (horizontalAlignment) {
				default:
				case LEFT:
					x = MathUtils.round(alignToElement.getX() - element.getWidth());
					break;
				case CENTER:
					x = MathUtils.round(alignToElement.getX() + (alignToElement.getWidth() * 0.5f) - (element.getWidth() * 0.5f));
					break;
				case RIGHT:
					x = MathUtils.round(alignToElement.getX() + alignToElement.getWidth());
					break;
				}
				switch (verticalAlignment) {
				default:
				case TOP:
					y = MathUtils.round(alignToElement.getY() - element.getHeight());
					break;
				case MIDDLE:
					y = MathUtils.round(alignToElement.getY() + (alignToElement.getHeight() * 0.5f) - (element.getHeight() * 0.5f));
					break;
				case BOTTOM:
					y = MathUtils.round(alignToElement.getY() + alignToElement.getHeight());
					break;
				}

				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
							flexUiElement.getFlexLayout(), x, y));
				} else {
					element.setXY(x, y);
				}
			}
		});
	}

	/**
	 * Aligns the right edge of a {@link UiElement} to the left edge of another element
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the top-side of this element to the top-side of the align element.
	 * 	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	 * 	{@link VerticalAlignment#BOTTOM} aligns the bottom-side of this element to the bottom-side of the align element.
	 */
	public static void alignLeftOf(final UiElement element, final UiElement alignToElement, final VerticalAlignment verticalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, alignToElement, new Runnable() {
			@Override
			public void run() {

			}
		});

		if (!element.isInitialised()) {
			element.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignLeftOf(element, alignToElement, verticalAlignment);
				}
			});
			return;
		}
		if (!alignToElement.isInitialised()) {
			alignToElement.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignLeftOf(element, alignToElement, verticalAlignment);
				}
			});
			return;
		}

		switch(UiContainer.getState()) {
		case LAYOUT:
		case UPDATE:
			alignToElement.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignLeftOf(element, alignToElement, verticalAlignment);
				}
			});
			break;
		case NOOP:
		case RENDER:
			final float x,y;
			switch (verticalAlignment) {
			default:
			case TOP:
				y = MathUtils.round(alignToElement.getY());
				break;
			case MIDDLE:
				y = MathUtils.round(alignToElement.getY() + (alignToElement.getHeight() * 0.5f) - (element.getHeight() * 0.5f));
				break;
			case BOTTOM:
				y = MathUtils.round(alignToElement.getY() + alignToElement.getHeight() - element.getHeight());
				break;
			}

			x = MathUtils.round(alignToElement.getX() - element.getWidth());

			if(element.isFlexLayout()) {
				FlexUiElement flexUiElement = (FlexUiElement) element;
				flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
						flexUiElement.getFlexLayout(), x, y));
			} else {
				element.setXY(x, y);
			}
			break;
		}
	}

	/**
	 * Aligns the left edge of this element to the right edge of another element
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param verticalAlignment {@link VerticalAlignment#TOP} aligns the top-side of this element to the top-side of the align element.
	 * 	 * 	{@link VerticalAlignment#MIDDLE} aligns the middle of this element to the middle of the align element.
	 * 	 * 	{@link VerticalAlignment#BOTTOM} aligns the bottom-side of this element to the bottom-side of the align element.
	 */
	public static void alignRightOf(final UiElement element, final UiElement alignToElement, final VerticalAlignment verticalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, alignToElement, new Runnable() {
			@Override
			public void run() {

			}
		});

		if (!element.isInitialised()) {
			element.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignRightOf(element, alignToElement, verticalAlignment);
				}
			});
			return;
		}
		if (!alignToElement.isInitialised()) {
			alignToElement.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignRightOf(element, alignToElement, verticalAlignment);
				}
			});
			return;
		}

		switch(UiContainer.getState()) {
		case LAYOUT:
		case UPDATE:
			alignToElement.deferUntilUpdate(new Runnable() {
				@Override
				public void run() {
					alignRightOf(element, alignToElement, verticalAlignment);
				}
			});
			break;
		case NOOP:
		case RENDER:
			final float x, y;
			switch (verticalAlignment) {
			default:
			case TOP:
				y = MathUtils.round(alignToElement.getY());
				break;
			case MIDDLE:
				y = MathUtils.round(alignToElement.getY() + (alignToElement.getHeight() * 0.5f) - (element.getHeight() * 0.5f));
				break;
			case BOTTOM:
				y = MathUtils.round(alignToElement.getY() + alignToElement.getHeight() - element.getHeight());
				break;
			}
			x = MathUtils.round(alignToElement.getX() + alignToElement.getWidth());

			if(element.isFlexLayout()) {
				FlexUiElement flexUiElement = (FlexUiElement) element;
				flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
						flexUiElement.getFlexLayout(), x, y));
			} else {
				element.setXY(x, y);
			}
			break;
		}
	}

	private static int alignCount = 0;

	/**
	 * Aligns the top edge of a {@link UiElement} to the bottom of another element
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the left-side of this element to the left side of the align element.
	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	{@link HorizontalAlignment#RIGHT} aligns the right-side of this element to the right-side of the align element.
	 */
	public static void alignBelow(final UiElement element, final UiElement alignToElement, final HorizontalAlignment horizontalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, alignToElement, new Runnable() {
			@Override
			public void run() {
				final float x,y;

				switch (horizontalAlignment) {
				default:
				case LEFT:
					x = MathUtils.round(alignToElement.getX());
					break;
				case CENTER:
					x = MathUtils.round(alignToElement.getX() + (alignToElement.getWidth() * 0.5f) - (element.getWidth() * 0.5f));
					break;
				case RIGHT:
					x = MathUtils.round(alignToElement.getX() + alignToElement.getWidth() - element.getWidth());
					break;
				}
				y = MathUtils.round(alignToElement.getY() + alignToElement.getHeight());

				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
							flexUiElement.getFlexLayout(), x, y));
				} else {
					element.setXY(x, y);
				}
			}
		});
	}

	/**
	 * Aligns the bottom edge of a {@link UiElement} to the top of another element
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param alignToElement The {@link UiElement} to align with. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment {@link HorizontalAlignment#LEFT} aligns the left-side of this element to the left side of the align element.
	 * 	 * 	{@link HorizontalAlignment#CENTER} aligns the center of this element to the center of the align element.
	 * 	 * 	{@link HorizontalAlignment#RIGHT} aligns the right-side of this element to the right-side of the align element.
	 */
	public static void alignAbove(final UiElement element, final UiElement alignToElement, final HorizontalAlignment horizontalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, alignToElement, new Runnable() {
			@Override
			public void run() {
				final float x,y;
				switch (horizontalAlignment) {
				default:
				case LEFT:
					x = MathUtils.round(alignToElement.getX());
					break;
				case CENTER:
					x = MathUtils.round(alignToElement.getX() + (alignToElement.getWidth() * 0.5f) - (element.getWidth() * 0.5f));
					break;
				case RIGHT:
					x = MathUtils.round(alignToElement.getX() + alignToElement.getWidth() - element.getWidth());
					break;
				}
				y = MathUtils.round(alignToElement.getY() - element.getHeight());

				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
							flexUiElement.getFlexLayout(), x, y));
				} else {
					element.setXY(x, y);
				}
			}
		});
	}

	/**
	 * Snaps the top-left of a {@link UiElement} to the top-left corner of another {@link UiElement}
	 * @param element The {@link UiElement} to perform alignment on
	 * @param snapToElement The {@link UiElement} to snap to. Note: This can also be the {@link UiContainer}
	 */
	public static void snapTo(final UiElement element, UiElement snapToElement) {
		snapTo(element, snapToElement, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
	}

	/**
	 * Snaps the edge of a {@link UiElement} to the edge of another element.
	 *
	 * @param element The {@link UiElement} to perform alignment on
	 * @param snapToElement The {@link UiElement} to snap to. Note: This can also be the {@link UiContainer}
	 * @param horizontalAlignment The {@link HorizontalAlignment} of this element within the area of the align element
	 * @param verticalAlignment The {@link VerticalAlignment} of this element within the area of the align element
	 */
	public static void snapTo(final UiElement element, final UiElement snapToElement, final HorizontalAlignment horizontalAlignment, final VerticalAlignment verticalAlignment) {
		ALIGN_DEPENDENCY_TREE.queue(element, snapToElement, new Runnable() {
			@Override
			public void run() {
				final float x, y;
				switch (horizontalAlignment) {
				default:
				case LEFT:
					x = MathUtils.round(snapToElement.getX());
					break;
				case CENTER:
					x = MathUtils.round(snapToElement.getX() + (snapToElement.getWidth() * 0.5f) - (element.getWidth() * 0.5f));
					break;
				case RIGHT:
					x = MathUtils.round(snapToElement.getX() + snapToElement.getWidth() - element.getWidth());
					break;
				}
				switch (verticalAlignment) {
				default:
				case TOP:
					y = MathUtils.round(snapToElement.getY());
					break;
				case MIDDLE:
					y = MathUtils.round(snapToElement.getY() + (snapToElement.getHeight() * 0.5f) - (element.getHeight() * 0.5f));
					break;
				case BOTTOM:
					y = MathUtils.round(snapToElement.getY() + snapToElement.getHeight() - element.getHeight());
					break;
				}
				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setXY(
							flexUiElement.getFlexLayout(), x, y));
				} else {
					element.setXY(x, y);
				}
			}
		});
	}

	/**
	 * Sets the width of one {@link UiElement} to match the width of another element
	 * @param element The {@link UiElement} to set the width on
	 * @param matchElement The {@link UiElement} to get the width of
	 */
	public static void setWidthToWidth(final UiElement element, final UiElement matchElement) {
		SIZE_DEPENDENCY_TREE.queue(element, matchElement, new Runnable() {
			@Override
			public void run() {
				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setWidth(
							flexUiElement.getFlexLayout(), matchElement.getWidth()));
				} else {
					element.setWidth(matchElement.getWidth());
				}
			}
		});
	}

	/**
	 * Sets the width of one {@link UiElement} to match the content width of another element. Note: content width = (width - margin - padding)
	 * @param element The {@link UiElement} to set the width on
	 * @param matchElement The {@link UiElement} to get the content width of
	 */
	public static void setWidthToContentWidth(final UiElement element, final UiElement matchElement) {
		SIZE_DEPENDENCY_TREE.queue(element, matchElement, new Runnable() {
			@Override
			public void run() {
				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setWidth(
							flexUiElement.getFlexLayout(), matchElement.getContentWidth()));
				} else {
					element.setWidth(matchElement.getContentWidth());
				}
			}
		});
	}

	/**
	 * Sets the height of one {@link UiElement} to match the height of another element
	 * @param element The {@link UiElement} to set the height on
	 * @param matchElement The {@link UiElement} to get the height of
	 */
	public static void setHeightToHeight(final UiElement element, final UiElement matchElement) {
		SIZE_DEPENDENCY_TREE.queue(element, matchElement, new Runnable() {
			@Override
			public void run() {
				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setHeight(
							flexUiElement.getFlexLayout(), matchElement.getHeight()));
				} else {
					element.setHeight(matchElement.getHeight());
				}
			}
		});
	}

	/**
	 * Sets the height of one {@link UiElement} to match the content height of another element. Note: content height = (height - margin - padding)
	 * @param element The {@link UiElement} to set the height on
	 * @param matchElement The {@link UiElement} to get the content height of
	 */
	public static void setHeightToContentHeight(final UiElement element, final UiElement matchElement) {
		SIZE_DEPENDENCY_TREE.queue(element, matchElement, new Runnable() {
			@Override
			public void run() {
				if(element.isFlexLayout()) {
					FlexUiElement flexUiElement = (FlexUiElement) element;
					flexUiElement.setFlexLayout(FlexLayoutRuleset.setHeight(
							flexUiElement.getFlexLayout(), matchElement.getContentHeight()));
				} else {
					element.setHeight(matchElement.getContentHeight());
				}
			}
		});
	}

	public static void resizeScrollContentHeightToContents(final ScrollBox scrollBox) {
		if(scrollBox.getTotalChildren() == 0) {
			scrollBox.setScrollContentHeight(0f);
			return;
		}
		ALIGN_DEPENDENCY_TREE.queue(scrollBox, scrollBox.getChild(scrollBox.getTotalChildren() - 1), new Runnable() {
			@Override
			public void run() {
				float maxY = 0f;
				for(int i = 0; i < scrollBox.getTotalChildren(); i++) {
					final UiElement uiElement = scrollBox.getChild(i);
					if(uiElement == null) {
						continue;
					}
					if(uiElement.getVisibility().equals(Visibility.HIDDEN)) {
						continue;
					}
					maxY = Math.max(maxY, uiElement.getY() + uiElement.getHeight());
				}
				scrollBox.setScrollContentHeight(maxY);
			}
		});
	}
}
