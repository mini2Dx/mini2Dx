/**
 * Copyright (c) 2014, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.util;

/**
 * Determines the desktop's operating system
 * @see {@link http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/}
 * @author Thomas Cashman
 */
public class OsDetector {
	private static final String OS = System.getProperty("os.name").toLowerCase();
	private static Os os;
	
	public static Os getOs() {
		if(os == null) {
			os = determineOs();
		}
		return os;
	}
	
	private static Os determineOs() {
		if(OS.indexOf("win") >= 0) {
			return Os.WINDOWS;
		}
		if(OS.indexOf("mac") >= 0) {
			return Os.MAC;
		}
		if(OS.indexOf("nix") >= 0) {
			return Os.UNIX;
		}
		if(OS.indexOf("nux") >= 0) {
			return Os.UNIX;
		}
		if(OS.indexOf("aix") >= 0) {
			return Os.UNIX;
		}
		return Os.UNKNOWN;
	}
	
	public static boolean isWindows() {
		return getOs() == Os.WINDOWS;
	}
 
	public static boolean isMac() {
		return getOs() == Os.MAC;
	}
 
	public static boolean isUnix() {
		return getOs() == Os.UNIX;
	}
}
