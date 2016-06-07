package to.adb.or.aapt;

import utils.AlertDialogUtils;

import java.io.IOException;

/**
 * Created by alex on 21.01.16.
 */
public class Adb implements IAdb {
    private static final String RUN_APP = "%s shell am start -n %s";
    private static final String STOP_EMU = "%s -s emulator-5554 emu kill";
    private static final String LOGCAT = " logcat DroidBox:W dalvikvm:W ActivityManager:I";
    private static final String LOGCAT_CLEAR = " logcat -c";

    private String ADB;

    public Adb(String ADB) {
        this.ADB = ADB;
    }

    @Override
    public void stopEmulator() throws IOException {
        Runtime.getRuntime().exec(String.format(STOP_EMU, ADB));
    }

    @Override
    public void runApp(String pathPackage) throws IOException {
        Runtime.getRuntime().exec(String.format(RUN_APP, ADB, pathPackage));
    }

    @Override
    public String logging() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        Runtime.getRuntime().exec(ADB + LOGCAT_CLEAR);
        Process process = Runtime.getRuntime().exec(ADB + LOGCAT);
        return AlertDialogUtils.showLoggingDialog(process);
    }
}
