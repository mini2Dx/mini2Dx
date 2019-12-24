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
package org.mini2Dx.ui.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InMemoryFileHandler extends AbstractFileHandle {
    private byte[] data;

    public InMemoryFileHandler(String contents) {
        this(contents.getBytes(UTF_8));
    }

    public InMemoryFileHandler(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(data);
    }
}
