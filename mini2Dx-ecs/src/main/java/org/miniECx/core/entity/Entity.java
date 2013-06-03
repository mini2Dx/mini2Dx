/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.miniECx.core.entity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.miniECx.core.component.Component;

/**
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Entity {
	private UUID uuid;
	private Map<String, List> components;

	public Entity() {
		this(UUID.randomUUID());
	}

	public Entity(UUID uuid) {
		components = new ConcurrentHashMap<String, List>();
		this.uuid = uuid;
	}

	public void addComponent(Component component) {
		Class clazz = component.getClass();

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).add(component);

		for (Class interfaceClass : clazz.getInterfaces()) {
			key = getClassKey(interfaceClass);
			checkConsistency(key, interfaceClass);
			components.get(key).add(component);
		}
	}

	public <T> List<T> getComponents(Class<T> clazz) {
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		return components.get(key);
	}
	
	public void removeComponent(Component component) {
		Class clazz = component.getClass();

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).remove(component);
		
		for (Class interfaceClass : clazz.getInterfaces()) {
			key = getClassKey(interfaceClass);
			checkConsistency(key, interfaceClass);
			components.get(key).remove(component);
		}
	}
	
	public <T> void removeAllComponentsOfType(Class<T> clazz) {
		String key = getClassKey(clazz);
		components.remove(key);
		checkConsistency(key, clazz);
	}

	private <T> void checkConsistency(String key,
			Class<T> clazz) {
		if (!components.containsKey(key)) {
			components.put(key, new CopyOnWriteArrayList<T>());
		}
	}

	private String getClassKey(Class<?> clazz) {
		return clazz.getPackage() + "." + clazz.getSimpleName();
	}

	public UUID getUUID() {
		return uuid;
	}
}
