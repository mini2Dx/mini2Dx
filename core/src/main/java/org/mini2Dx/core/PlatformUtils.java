package org.mini2Dx.core;

public interface PlatformUtils {

    /**
     * Exits the game
     * @param ignorePlatformRestrictions exit even if platform rules don't permit exiting programmatically. Should be
     *                                   used only for debug purposes and always set to false for release builds.
     */
    public void exit(boolean ignorePlatformRestrictions);
}
