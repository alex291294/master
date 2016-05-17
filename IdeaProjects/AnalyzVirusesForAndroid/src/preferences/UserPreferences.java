package preferences;

import java.util.prefs.Preferences;

/**
 * Created by alex on 28.02.16.
 */
public class UserPreferences {

    private static final String USER_PREFERENCES = "user_preferences";
    private static final String PATH_EMULATOR = "path_emulator";
    private static final String PATH_ADB = "path_adb";
    private static final String PATH_AAPT = "path_aapt";

    private Preferences userPreferences;

    public UserPreferences() {
        userPreferences = Preferences.userRoot().node(USER_PREFERENCES);
    }

    public void setPathEmulator(String pathEmulator) {
        userPreferences.put(PATH_EMULATOR, pathEmulator);
    }

    public void setPathAdb(String pathAdb) {
        userPreferences.put(PATH_ADB, pathAdb);
    }

    public void setPathAapt(String pathAapt) {
        userPreferences.put(PATH_AAPT, pathAapt);
    }

    public String getPathEmulator() {
        return userPreferences.get(PATH_EMULATOR, null);
    }

    public String getPathAdb() {
        return userPreferences.get(PATH_ADB, null);
    }

    public String getPathAapt() {
        return userPreferences.get(PATH_AAPT, null);
    }
}
