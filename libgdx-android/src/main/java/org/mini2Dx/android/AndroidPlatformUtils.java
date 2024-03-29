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
package org.mini2Dx.android;

import android.content.Context;
import android.os.Build;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;

public class AndroidPlatformUtils extends LibgdxPlatformUtils {

    @Override
    public boolean isGameThread() {
        return Thread.currentThread().getName().startsWith("GLThread");
    }

    @Override
    public void enablePerformanceMode() {
        //TODO: Add when API 33 is release
//        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            android.app.GameManager gameManager = Context.getSystemService(android.app.GameManager.class);
//            gameManager.setGameMode(new android.app.GameState(true, android.app.GameState.MODE_UNKNOWN));
//        }
    }

    @Override
    public void cancelPerformanceMode() {
        //TODO: Add when API 33 is release
//        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            android.app.GameManager gameManager = Context.getSystemService(android.app.GameManager.class);
//            gameManager.setGameMode(new android.app.GameState(false, android.app.GameState.MODE_UNKNOWN));
//        }
    }
}
