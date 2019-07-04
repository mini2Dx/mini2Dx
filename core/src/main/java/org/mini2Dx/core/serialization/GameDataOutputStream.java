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
package org.mini2Dx.core.serialization;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extends {@link DataOutputStream} so that we can listen for a close event
 */
public class GameDataOutputStream extends DataOutputStream {
	private CloseListener closeListener;

	/**
	 * Creates a new data output stream to write data to the specified
	 * underlying output stream. The counter <code>written</code> is
	 * set to zero.
	 *
	 * @param out the underlying output stream, to be saved for later
	 *            use.
	 * @see FilterOutputStream#out
	 */
	public GameDataOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void close() throws IOException {
		super.close();
		if(closeListener != null) {
			closeListener.onClose();
		}
	}

	public void setCloseListener(CloseListener closeListener) {
		this.closeListener = closeListener;
	}

	public interface CloseListener {

		public void onClose();
	}
}
