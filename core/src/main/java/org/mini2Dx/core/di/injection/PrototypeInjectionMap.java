/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.di.injection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.mini2Dx.core.di.bean.PrototypeBean;

/**
 * An implementation of {@link Map} that produces prototypes when get() is called
 */
public class PrototypeInjectionMap implements Map<String, Object> {
	private Map<String, Object> prototypes;
	
	public PrototypeInjectionMap(Map<String, Object> prototypes) {
		this.prototypes = prototypes;
	}

	@Override
	public int size() {
		return prototypes.size();
	}

	@Override
	public boolean isEmpty() {
		return prototypes.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return prototypes.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return prototypes.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		Object prototype = prototypes.get(key);
		
		try {
			return PrototypeBean.duplicate(prototype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Object put(String key, Object value) {
		return prototypes.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return prototypes.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		prototypes.putAll(m);
	}

	@Override
	public void clear() {
		prototypes.clear();
	}

	@Override
	public Set<String> keySet() {
		return prototypes.keySet();
	}

	@Override
	public Collection<Object> values() {
		return prototypes.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return prototypes.entrySet();
	}
}
