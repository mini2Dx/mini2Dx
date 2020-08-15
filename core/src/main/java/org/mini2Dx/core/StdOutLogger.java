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
package org.mini2Dx.core;

public class StdOutLogger implements Logger {

    private int logLevel = Logger.LOG_INFO;
    @Override
    public void info(String tag, String message) {
        if (logLevel <= Logger.LOG_INFO){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (logLevel <= Logger.LOG_DEBUG){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void error(String tag, String message) {
        if (logLevel <= Logger.LOG_ERROR){
            System.out.println("[" + tag + "] " + message);
        }
    }

    @Override
    public void error(String tag, String message, Exception e) {
        if (logLevel <= Logger.LOG_ERROR){
            System.err.println("[" + tag + "] " + message + " " + e.toString());
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void setLoglevel(int loglevel) {
        this.logLevel = loglevel;
    }
}
