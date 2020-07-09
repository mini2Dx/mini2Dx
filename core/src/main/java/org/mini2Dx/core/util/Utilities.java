/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utilities {

    public static int STREAM_COPY_BUFFER_SIZE = 8192;

    private Utilities() {
    }

    /**
     * Reads all bytes from an input stream and writes them to an output stream.
     */
    public static long streamCopy(InputStream source, OutputStream sink) throws IOException {
        long nread = 0L;
        byte[] buf = new byte[STREAM_COPY_BUFFER_SIZE];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
        }
        return nread;
    }
}
