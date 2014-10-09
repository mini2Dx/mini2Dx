/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.core.data;

import org.mini2Dx.core.data.annotation.Field;

/**
 * Common interface for reading/writing game data, e.g. game saves, preferences,
 * etc.
 * 
 * The location to save data in is automatically determined by the game
 * identifier and the current platform. All files/directories specified by the
 * developer will be automatically placed inside the pre-determined data
 * location.
 * 
 * Note: Objects must use {@link Field} annotations to be
 * serialized/deserialized properly
 * 
 * @author Thomas Cashman
 */
public interface Data {
    /**
     * Converts XML from a file into an object. Note the object must use the
     * mini2Dx data annotations.
     * 
     * @param clazz
     *            The object type to convert the XML into
     * @param filepath
     *            The path to the XML file. This will be resolved as a path
     *            within the game data location.
     * @return The resulting object
     * @throws Exception
     *             Thrown if the XML is invalid, the file does not exist or the
     *             game data location cannot be accessed.
     */
    public <T> T readXml(Class<T> clazz, String... filepath) throws Exception;

    /**
     * Writes an object as XML to a file. Note the object must use the mini2Dx
     * data annotations.
     * 
     * @param object
     *            The object to be written to the file
     * @param filepath
     *            The path to the XML file. This will be resolved as a path
     *            within the game data location.
     * @throws Exception
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public <T> void writeXml(T object, String... filepath) throws Exception;

    /**
     * Converts JSON from a file into an object. Note the object must use the
     * mini2Dx data annotations.
     * 
     * @param clazz
     *            The object type to convert the JSON into
     * @param filepath
     *            The path to the JSON file. This will be resolved as a path
     *            within the game data location.
     * @return The resulting object
     * @throws Exception
     *             Thrown if the XML is invalid, the file does not exist or the
     *             game data location cannot be accessed.
     */
    public <T> T readJson(Class<T> clazz, String... filepath) throws Exception;

    /**
     * Writes an object as JSON to a file. Note the object must use the mini2Dx
     * data annotations.
     * 
     * @param object
     *            The object to be written to the file
     * @param filepath
     *            The path to the JSON file. This will be resolved as a path
     *            within the game data location.
     * @throws Exception
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public <T> void writeJson(T object, String... filepath) throws Exception;

    /**
     * Checks if the file exists in the game data location
     * 
     * @param filepath
     *            The path to the file within the game data location
     * @return True if the file exists
     * @throws Exception
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasFile(String... filepath) throws Exception;

    /**
     * Checks if the directory exists in the game data location
     * 
     * @param path
     *            The path to the directory within the game data location
     * @return True if the file exists
     * @throws Exception
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasDirectory(String... path) throws Exception;

    /**
     * Creates a directory within in the game data location
     * 
     * @param path
     *            The path to the directory within the game data location
     * @throws Exception
     *             Thrown if the game data location cannot be accessed or the
     *             directory could not be created
     */
    public void createDirectory(String... path) throws Exception;

    /**
     * Wipes all data within the game data location
     * 
     * @throws Exception
     *             Thrown if the game data location cannot be accessed or the
     *             data could not be wiped
     */
    public void wipe() throws Exception;
}
