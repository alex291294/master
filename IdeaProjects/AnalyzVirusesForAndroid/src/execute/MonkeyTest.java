package execute;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 19.01.16.
 */
public class MonkeyTest {

    private static final long TIMEOUT = 5000;
    private static final String BACKEND = "backend";
    private static final String ADB_LOCATION = "adbLocation";

    private String ADB;
    private IChimpDevice device;
    private ChimpChat chimpchat;
    private static volatile MonkeyTest monkeyTest;

    public static MonkeyTest getInstance(String ADB) {
        MonkeyTest localMonkeyTest = monkeyTest;
        if (localMonkeyTest == null) {
            synchronized (MonkeyTest.class) {
                localMonkeyTest = monkeyTest;
                if (localMonkeyTest == null) {
                    monkeyTest = localMonkeyTest = new MonkeyTest(ADB);
                }
            }
        }
        return localMonkeyTest;
    }

    private MonkeyTest(String ADB) {
        this.ADB = ADB;
    }

    private void init() {
        if (chimpchat == null && device == null) {
            Map<String, String> options = new HashMap<String, String>();
            options.put(BACKEND, "adb");
            options.put(ADB_LOCATION, ADB);
            chimpchat = ChimpChat.getInstance(options);
            device = chimpchat.waitForConnection(TIMEOUT, ".*");
        }
    }

    public void installPackage(String installPackage) {
        init();
        device.installPackage(installPackage);
    }

    public void shutDownMonkey() {
        if (chimpchat != null) {
            chimpchat.shutdown();
        }
    }
}
