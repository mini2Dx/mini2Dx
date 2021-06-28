package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.JvmPlatformUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class LibgdxPlatformUtils extends JvmPlatformUtils {
    @Override
    public void exit(boolean ignorePlatformRestrictions) {
        Gdx.app.exit();
    }

    @Override
    public String timestampToDateFormat(long millis, String format) {
        final Date date = new Date(millis);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }
}
