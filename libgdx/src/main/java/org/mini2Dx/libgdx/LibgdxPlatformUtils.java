package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.JvmPlatformUtils;

public abstract class LibgdxPlatformUtils extends JvmPlatformUtils {
    @Override
    public void exit(boolean ignorePlatformRestrictions) {
        Gdx.app.exit();
    }
}
