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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.Logger;

public class LibgdxLogger implements Logger {
	@Override
	public void info(String tag, String message) {
		Gdx.app.log(tag, message);
	}

	@Override
	public void debug(String tag, String message) {
		Gdx.app.debug(tag, message);
	}

	@Override
	public void error(String tag, String message) {
		Gdx.app.error(tag, message);
	}

	@Override
	public void error(String tag, String message, Exception e) {
		Gdx.app.error(tag, message, e);
	}

	@Override
	public void setLoglevel(int loglevel) {
		Gdx.app.setLogLevel(loglevel);
	}
}
