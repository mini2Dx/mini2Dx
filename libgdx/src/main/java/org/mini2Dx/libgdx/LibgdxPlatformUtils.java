package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.PlatformUtils;

public class LibgdxPlatformUtils implements PlatformUtils {
    @Override
    public void exit(boolean ignorePlatformRestrictions) {
        Gdx.app.exit();
    }
}
