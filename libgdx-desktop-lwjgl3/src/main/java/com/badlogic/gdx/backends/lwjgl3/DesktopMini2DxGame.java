/*******************************************************************************
 * Copyright 2011 See LIBGDX_AUTHORS file.
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
package com.badlogic.gdx.backends.lwjgl3;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.audio.Mini2DxOpenALAudio;
import com.badlogic.gdx.backends.lwjgl3.audio.mock.MockAudio;
import com.badlogic.gdx.graphics.glutils.GLVersion;
import com.badlogic.gdx.utils.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.libgdx.desktop.Lwjgl3GameWrapper;
import org.mini2Dx.libgdx.desktop.Lwjgl3PlatformUtils;

import java.io.File;
import java.io.PrintStream;
import java.nio.IntBuffer;

public class DesktopMini2DxGame implements Application {
	private final Lwjgl3Mini2DxConfig config;
	private final Lwjgl3GameWrapper gameWrapper;
	final Array<Lwjgl3Mini2DxWindow> windows = new Array<Lwjgl3Mini2DxWindow>();
	private volatile Lwjgl3Mini2DxWindow currentWindow;
	private Audio audio;
	private final Files files;
	private final Net net;
	private final ObjectMap<String, Preferences> preferences = new ObjectMap<String, Preferences>();
	private final Lwjgl3Clipboard clipboard;
	private int logLevel = LOG_INFO;
	private ApplicationLogger applicationLogger;
	private volatile boolean running = true;
	private final Array<Runnable> runnables = new Array<Runnable>();
	private final Array<Runnable> executedRunnables = new Array<Runnable>();
	private final Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
	private static GLFWErrorCallback errorCallback;
	private static GLVersion glVersion;
	private static Callback glDebugCallback;

	private final Sync sync;

	static void initializeGlfw() {
		if (errorCallback == null) {
			Lwjgl3NativesLoader.load();
			errorCallback = GLFWErrorCallback.createPrint(System.err);
			GLFW.glfwSetErrorCallback(errorCallback);
			GLFW.glfwInitHint(GLFW.GLFW_JOYSTICK_HAT_BUTTONS, GLFW.GLFW_FALSE);
			if (!GLFW.glfwInit()) {
				throw new GdxRuntimeException("Unable to initialize GLFW");
			}
		}
	}

	public DesktopMini2DxGame(GameContainer gameContainer, Lwjgl3Mini2DxConfig config) {
		initializeGlfw();
		setApplicationLogger(new Lwjgl3ApplicationLogger());
		if (config.title == null) config.title = gameContainer.getClass().getSimpleName();
		this.config = config = Lwjgl3Mini2DxConfig.copy(config);
		Gdx.app = this;
		if (!config.disableAudio) {
			try {
				this.audio = Gdx.audio = new Mini2DxOpenALAudio(config.audioDeviceSimultaneousSources,
						config.audioDeviceBufferCount, config.audioDeviceBufferSize);
			} catch (Throwable t) {
				log("Lwjgl3Application", "Couldn't initialize audio, disabling audio", t);
				this.audio = Gdx.audio = new MockAudio();
			}
		} else {
			this.audio = Gdx.audio = new MockAudio();
		}
		this.files = Gdx.files = new Lwjgl3Files();
		this.net = Gdx.net = new Lwjgl3Net(config);
		this.clipboard = new Lwjgl3Mini2DxClipboard();

		this.sync = new Sync();

		gameWrapper = new Lwjgl3GameWrapper(gameContainer, config.gameIdentifier);

		Lwjgl3Mini2DxWindow window = createWindow(config,0);
		windows.add(window);
		try {
			loop();
			cleanupWindows();
		} catch(Throwable t) {
			if (t instanceof RuntimeException)
				throw (RuntimeException) t;
			else
				throw new GdxRuntimeException(t);
		} finally {
			cleanup();
		}
	}

	private void loop() {
		Lwjgl3PlatformUtils.GAME_THREAD_ID = Thread.currentThread().getId();

		Array<Lwjgl3Mini2DxWindow> closedWindows = new Array<Lwjgl3Mini2DxWindow>();

		while (running && windows.size > 0) {
			if(Mdx.platformUtils != null) {
				Mdx.platformUtils.markFrameBegin();
			}

			// FIXME put it on a separate thread
			if (audio instanceof Mini2DxOpenALAudio) {
				((Mini2DxOpenALAudio) audio).update();
			}

			boolean haveWindowsRendered = false;
			closedWindows.clear();
			for (Lwjgl3Mini2DxWindow window : windows) {
				window.makeCurrent();
				currentWindow = window;
				synchronized (lifecycleListeners) {
					haveWindowsRendered |= window.update(config);
				}
				if (window.shouldClose()) {
					closedWindows.add(window);
				}
			}
			GLFW.glfwPollEvents();

			boolean shouldRequestRendering;
			synchronized (runnables) {
				shouldRequestRendering = runnables.size > 0;
				executedRunnables.clear();
				executedRunnables.addAll(runnables);
				runnables.clear();
			}
			for (Runnable runnable : executedRunnables) {
				runnable.run();
			}
			if (shouldRequestRendering){
				// Must follow Runnables execution so changes done by Runnables are reflected
				// in the following render.
				for (Lwjgl3Mini2DxWindow window : windows) {
					if (!window.getGraphics().isContinuousRendering())
						window.requestRendering();
				}
			}

			for (Lwjgl3Mini2DxWindow closedWindow : closedWindows) {
				if (windows.size == 1) {
					// Lifecycle listener methods have to be called before ApplicationListener methods. The
					// application will be disposed when _all_ windows have been disposed, which is the case,
					// when there is only 1 window left, which is in the process of being disposed.
					for (int i = lifecycleListeners.size - 1; i >= 0; i--) {
						LifecycleListener l = lifecycleListeners.get(i);
						l.pause();
						l.dispose();
					}
					lifecycleListeners.clear();
				}
				closedWindow.dispose();

				windows.removeValue(closedWindow, false);
			}

			if(Mdx.platformUtils != null) {
				Mdx.platformUtils.markFrameEnd();
			}

			if (!haveWindowsRendered) {
				// Sleep a few milliseconds in case no rendering was requested
				// with continuous rendering disabled.
				try {
					Thread.sleep(1000 / config.idleFPS);
				} catch (InterruptedException e) {
					// ignore
				}
			} else if(config.foregroundFPS > 0) {
				sync.sync(config.foregroundFPS); // sleep as needed to meet the target framerate
			}
		}
	}

	private void cleanupWindows() {
		synchronized (lifecycleListeners) {
			for(LifecycleListener lifecycleListener : lifecycleListeners){
				lifecycleListener.pause();
				lifecycleListener.dispose();
			}
		}
		for (Lwjgl3Mini2DxWindow window : windows) {
			window.dispose();
		}
		windows.clear();
	}

	private void cleanup() {
		Lwjgl3Cursor.disposeSystemCursors();
		if (audio instanceof Mini2DxOpenALAudio) {
			((Mini2DxOpenALAudio) audio).dispose();
		}
		errorCallback.free();
		errorCallback = null;
		if (glDebugCallback != null) {
			glDebugCallback.free();
			glDebugCallback = null;
		}
		GLFW.glfwTerminate();
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return currentWindow.getListener();
	}

	@Override
	public Graphics getGraphics() {
		return currentWindow.getGraphics();
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public Input getInput() {
		return currentWindow.getInput();
	}

	@Override
	public Files getFiles() {
		return files;
	}

	@Override
	public Net getNet() {
		return net;
	}

	@Override
	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message);
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message, exception);
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message);
	}

	@Override
	public void log (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message, exception);
	}

	@Override
	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message);
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message, exception);
	}

	@Override
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel() {
		return logLevel;
	}

	@Override
	public void setApplicationLogger (ApplicationLogger applicationLogger) {
		this.applicationLogger = applicationLogger;
	}

	@Override
	public ApplicationLogger getApplicationLogger () {
		return applicationLogger;
	}

	@Override
	public ApplicationType getType() {
		return ApplicationType.Desktop;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public long getJavaHeap() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	@Override
	public long getNativeHeap() {
		return getJavaHeap();
	}

	@Override
	public Preferences getPreferences(String name) {
		if (preferences.containsKey(name)) {
			return preferences.get(name);
		} else {
			Preferences prefs = new Lwjgl3Preferences(
					new Lwjgl3FileHandle(new File(config.preferencesDirectory, name), config.preferencesFileType));
			preferences.put(name, prefs);
			return prefs;
		}
	}

	@Override
	public Clipboard getClipboard() {
		return clipboard;
	}

	@Override
	public void postRunnable(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
	}

	@Override
	public void exit() {
		running = false;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.removeValue(listener, true);
		}
	}

	/**
	 * Creates a new {@link Lwjgl3Window} using the provided game container and {@link Lwjgl3WindowConfiguration}.
	 *
	 * This function only just instantiates a {@link Lwjgl3Window} and returns immediately. The actual window creation
	 * is postponed with {@link Application#postRunnable(Runnable)} until after all existing windows are updated.
	 */
	public Lwjgl3Mini2DxWindow newWindow(Lwjgl3Mini2DxConfig config) {
		Lwjgl3Mini2DxConfig appConfig = Lwjgl3Mini2DxConfig.copy(this.config);
		appConfig.setWindowConfiguration(config);
		return createWindow(appConfig, windows.get(0).getWindowHandle());
	}

	private Lwjgl3Mini2DxWindow createWindow (final Lwjgl3Mini2DxConfig config, final long sharedContext) {
		final Lwjgl3Mini2DxWindow window = new Lwjgl3Mini2DxWindow(gameWrapper, config);
		if (sharedContext == 0) {
			// the main window is created immediately
			createWindow(window, config, sharedContext);
		} else {
			// creation of additional windows is deferred to avoid GL context trouble
			postRunnable(new Runnable() {
				public void run () {
					createWindow(window, config, sharedContext);
					windows.add(window);
				}
			});
		}
		return window;
	}

	void createWindow(Lwjgl3Mini2DxWindow window, Lwjgl3ApplicationConfiguration config, long sharedContext) {
		long windowHandle = createGlfwWindow(config, sharedContext);
		window.create(windowHandle);
		window.setVisible(config.initialVisible);

		for (int i = 0; i < 2; i++) {
			GL11.glClearColor(config.initialBackgroundColor.r, config.initialBackgroundColor.g, config.initialBackgroundColor.b,
					config.initialBackgroundColor.a);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GLFW.glfwSwapBuffers(windowHandle);
		}
	}

	static long createGlfwWindow(Lwjgl3ApplicationConfiguration config, long sharedContextWindow) {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, config.windowResizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, config.windowMaximized ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_AUTO_ICONIFY, config.autoIconify ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

		if(sharedContextWindow == 0) {
			GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, config.r);
			GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, config.g);
			GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, config.b);
			GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, config.a);
			GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, config.stencil);
			GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, config.depth);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, config.samples);
		}

		if (config.useGL30) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, config.gles30ContextMajorVersion);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, config.gles30ContextMinorVersion);
			if (SharedLibraryLoader.isMac) {
				// hints mandatory on OS X for GL 3.2+ context creation, but fail on Windows if the
				// WGL_ARB_create_context extension is not available
				// see: http://www.glfw.org/docs/latest/compat.html
				GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
				GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			}
		}

		if (config.transparentFramebuffer) {
			GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
		}

		if (config.debug) {
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
		}

		long windowHandle = 0;

		if(config.fullscreenMode != null) {
			GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, config.fullscreenMode.refreshRate);
			windowHandle = GLFW.glfwCreateWindow(config.fullscreenMode.width, config.fullscreenMode.height, config.title, config.fullscreenMode.getMonitor(), sharedContextWindow);
		} else {
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, config.windowDecorated? GLFW.GLFW_TRUE: GLFW.GLFW_FALSE);
			windowHandle = GLFW.glfwCreateWindow(config.windowWidth, config.windowHeight, config.title, 0, sharedContextWindow);
		}
		if (windowHandle == 0) {
			throw new GdxRuntimeException("Couldn't create window");
		}
		Lwjgl3Window.setSizeLimits(windowHandle, config.windowMinWidth, config.windowMinHeight, config.windowMaxWidth, config.windowMaxHeight);
		if (config.fullscreenMode == null) {
			if (config.windowX == -1 && config.windowY == -1) {
				int windowWidth = Math.max(config.windowWidth, config.windowMinWidth);
				int windowHeight = Math.max(config.windowHeight, config.windowMinHeight);
				if (config.windowMaxWidth > -1) windowWidth = Math.min(windowWidth, config.windowMaxWidth);
				if (config.windowMaxHeight > -1) windowHeight = Math.min(windowHeight, config.windowMaxHeight);

				long monitorHandle = GLFW.glfwGetPrimaryMonitor();
				if (config.windowMaximized && config.maximizedMonitor != null) {
					monitorHandle = config.maximizedMonitor.monitorHandle;
				}

				IntBuffer areaXPos = BufferUtils.createIntBuffer(1);
				IntBuffer areaYPos = BufferUtils.createIntBuffer(1);
				IntBuffer areaWidth = BufferUtils.createIntBuffer(1);
				IntBuffer areaHeight = BufferUtils.createIntBuffer(1);
				GLFW.glfwGetMonitorWorkarea(monitorHandle, areaXPos, areaYPos, areaWidth, areaHeight);

				GLFW.glfwSetWindowPos(windowHandle,
						areaXPos.get(0) + areaWidth.get(0) / 2 - windowWidth / 2,
						areaYPos.get(0) + areaHeight.get(0) / 2 - windowHeight / 2);
			} else {
				GLFW.glfwSetWindowPos(windowHandle, config.windowX, config.windowY);
			}

			if (config.windowMaximized) {
				GLFW.glfwMaximizeWindow(windowHandle);
			}
		}
		if (config.windowIconPaths != null) {
			Lwjgl3Window.setIcon(windowHandle, config.windowIconPaths, config.windowIconFileType);
		}
		GLFW.glfwMakeContextCurrent(windowHandle);
		GLFW.glfwSwapInterval(config.vSyncEnabled ? 1 : 0);
		GL.createCapabilities();

		initiateGL();
		if (!glVersion.isVersionEqualToOrHigher(2, 0))
			throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: "
					+ GL11.glGetString(GL11.GL_VERSION) + "\n" + glVersion.getDebugVersionString());

		if (!supportsFBO()) {
			throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: "
					+ GL11.glGetString(GL11.GL_VERSION) + ", FBO extension: false\n" + glVersion.getDebugVersionString());
		}

		if (config.debug) {
			glDebugCallback = GLUtil.setupDebugMessageCallback(config.debugStream);
			setGLDebugMessageControl(GLDebugMessageSeverity.NOTIFICATION, false);
		}

		return windowHandle;
	}

	private static void initiateGL () {
		String versionString = GL11.glGetString(GL11.GL_VERSION);
		String vendorString = GL11.glGetString(GL11.GL_VENDOR);
		String rendererString = GL11.glGetString(GL11.GL_RENDERER);
		glVersion = new GLVersion(Application.ApplicationType.Desktop, versionString, vendorString, rendererString);
	}

	private static boolean supportsFBO () {
		// FBO is in core since OpenGL 3.0, see https://www.opengl.org/wiki/Framebuffer_Object
		return glVersion.isVersionEqualToOrHigher(3, 0) || GLFW.glfwExtensionSupported("GL_EXT_framebuffer_object")
				|| GLFW.glfwExtensionSupported("GL_ARB_framebuffer_object");
	}

	public enum GLDebugMessageSeverity {
		HIGH(
				GL43.GL_DEBUG_SEVERITY_HIGH,
				KHRDebug.GL_DEBUG_SEVERITY_HIGH,
				ARBDebugOutput.GL_DEBUG_SEVERITY_HIGH_ARB,
				AMDDebugOutput.GL_DEBUG_SEVERITY_HIGH_AMD),
		MEDIUM(
				GL43.GL_DEBUG_SEVERITY_MEDIUM,
				KHRDebug.GL_DEBUG_SEVERITY_MEDIUM,
				ARBDebugOutput.GL_DEBUG_SEVERITY_MEDIUM_ARB,
				AMDDebugOutput.GL_DEBUG_SEVERITY_MEDIUM_AMD),
		LOW(
				GL43.GL_DEBUG_SEVERITY_LOW,
				KHRDebug.GL_DEBUG_SEVERITY_LOW,
				ARBDebugOutput.GL_DEBUG_SEVERITY_LOW_ARB,
				AMDDebugOutput.GL_DEBUG_SEVERITY_LOW_AMD),
		NOTIFICATION(
				GL43.GL_DEBUG_SEVERITY_NOTIFICATION,
				KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION,
				-1,
				-1);

		final int gl43, khr, arb, amd;

		GLDebugMessageSeverity(int gl43, int khr, int arb, int amd) {
			this.gl43 = gl43;
			this.khr = khr;
			this.arb = arb;
			this.amd = amd;
		}
	}

	/**
	 * Enables or disables GL debug messages for the specified severity level. Returns false if the severity
	 * level could not be set (e.g. the NOTIFICATION level is not supported by the ARB and AMD extensions).
	 *
	 * See {@link Lwjgl3ApplicationConfiguration#enableGLDebugOutput(boolean, PrintStream)}
	 */
	public static boolean setGLDebugMessageControl (GLDebugMessageSeverity severity, boolean enabled) {
		GLCapabilities caps = GL.getCapabilities();
		final int GL_DONT_CARE = 0x1100; // not defined anywhere yet

		if (caps.OpenGL43) {
			GL43.glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, severity.gl43, (IntBuffer) null, enabled);
			return true;
		}

		if (caps.GL_KHR_debug) {
			KHRDebug.glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, severity.khr, (IntBuffer) null, enabled);
			return true;
		}

		if (caps.GL_ARB_debug_output && severity.arb != -1) {
			ARBDebugOutput.glDebugMessageControlARB(GL_DONT_CARE, GL_DONT_CARE, severity.arb, (IntBuffer) null, enabled);
			return true;
		}

		if (caps.GL_AMD_debug_output && severity.amd != -1) {
			AMDDebugOutput.glDebugMessageEnableAMD(GL_DONT_CARE, severity.amd, (IntBuffer) null, enabled);
			return true;
		}

		return false;
	}

}
