/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.element;

import org.mini2Dx.core.exception.MdxException;

/**
 *
 */
public class Tab extends Row {
	private String title = "";
	
	public Tab() {
		this(null);
	}
	
	public Tab(String id) {
		this(id, "");
	}
	
	public Tab(String id, String title) {
		super(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title == null) {
			return;
		}
		if(title.equals(this.title)) {
			return;
		}
		this.title = title;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	@Override
	public void setZIndex(int zIndex) {
		throw new MdxException("Tabs cannot change Z index");
	}
	
	@Override
	public void setVisibility(Visibility visibility) {
		throw new MdxException("Tab visibility is managed by " + TabView.class.getSimpleName());
	}
	
	void activateTab() {
		setVisibility(Visibility.VISIBLE);
	}
	
	void deactivateTab() {
		setVisibility(Visibility.HIDDEN);
	}
}
