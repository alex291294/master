package to.adb.or.aapt;

import java.io.IOException;

/**
 * Created by alex on 21.01.16.
 */
public interface IAapt {
    String RUN_APP_ERROR = "Ошибка запуска приложения";
    String getPathPackage(String pathPackage);
    String getPathMainActivity(String pathPackage);
}
