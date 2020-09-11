/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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

import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.serialization.JsonSerializer;
import org.mini2Dx.core.serialization.XmlSerializer;

/**
 * Mini2Dx environment class<br>
 * <br>
 * Note: Everything is public static to allow cross-platform reference setting. These variables should not be modified.
 */
public class Mdx {
    /**
     * {@link Audio} API
     */
    public static Audio audio;

    /**
     * Object pool for {@link org.mini2Dx.core.collision.CollisionObject} classes
     */
    public static final Collisions collisions = new Collisions();

    /**
     * {@link DependencyInjection} API
     */
    public static DependencyInjection di;

    /**
     * {@link TaskExecutor} API
     */
    public static TaskExecutor executor;

    /**
     * {@link Files} API
     */
    public static Files files;

    /**
     * {@link Fonts} API
     */
    public static Fonts fonts;

    /**
     * This game's unique identifier for app stores
     */
    public static String gameIdentifier;

    /**
     * Object pool for {@link Geometry} classes
     */
    public static final Geometry geom = new Geometry();

    /**
     * {@link GraphicsUtils} API
     */
    public static GraphicsUtils graphics;

    /**
     * {@link Graphics} API - should not be called
     */
    public static Graphics graphicsContext;

    /**
     * {@link Input} API
     */
    public static Input input;

    /**
     * {@link Locks} API
     */
    public static Locks locks;

    /**
     * JSON Serialization API
     */
    public static JsonSerializer json;

    /**
     * {@link Logger} API
     */
    public static Logger log;

    /**
     * Returns the current {@link Platform}
     */
    public static Platform platform;

    /**
     * API for reading/writing {@link PlayerData}
     */
    public static PlayerData playerData;

    /**
     * API for reflection
     */
    public static Reflection reflect;

    /**
     * Returns the current {@link ApiRuntime}
     */
    public static ApiRuntime runtime;

    /**
     * Returns the {@link TimestepMode} specified at launch
     */
    public static TimestepMode timestepMode = TimestepMode.DEFAULT;

    /**
     * XML serialization API
     */
    public static XmlSerializer xml;

    /**
     * Platform utilities API
     */
    public static PlatformUtils platformUtils;
}
