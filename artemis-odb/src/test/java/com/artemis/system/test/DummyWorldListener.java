/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.artemis.system.test;

import com.artemis.MdxWorld;
import com.artemis.listener.WorldListener;
import junit.framework.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DummyWorldListener implements WorldListener {
    private Set<Integer> entitiesCreated = new HashSet<Integer>();
    private Set<Integer> entitiesDeleted = new HashSet<Integer>();

    @Override
    public void afterEntityCreated(MdxWorld world, int entityId) {
        entitiesCreated.add(entityId);
    }

    @Override
    public void beforeEntityDeleted(MdxWorld world, int entityId) {
        entitiesDeleted.add(entityId);
    }

    public void assertEntityCreated(int entityId) {
        Assert.assertEquals(true, entitiesCreated.contains(entityId));
    }

    public void assertEntityNotCreated(int entityId) {
        Assert.assertEquals(false, entitiesCreated.contains(entityId));
    }

    public void assertEntityDeleted(int entityId) {
        Assert.assertEquals(true, entitiesDeleted.contains(entityId));
    }

    public void assertEntityNotDeleted(int entityId) {
        Assert.assertEquals(false, entitiesDeleted.contains(entityId));
    }
}
