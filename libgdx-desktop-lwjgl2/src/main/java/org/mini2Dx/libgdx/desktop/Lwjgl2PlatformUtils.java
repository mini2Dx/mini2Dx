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
package org.mini2Dx.libgdx.desktop;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Lwjgl2PlatformUtils extends LibgdxPlatformUtils {
	@Override
	public boolean isGameThread() {
		try {
			return Display.isCurrent();
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void enablePerformanceMode() {
		//NOOP on PC
	}

	@Override
	public void cancelPerformanceMode() {
		//NOOP on PC
	}
}