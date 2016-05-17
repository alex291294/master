package to.adb.or.aapt;

import java.io.IOException;

/**
 * Created by alex on 21.01.16.
 */
public interface IAapt {
    String getPathPackage(String pathPackage) throws IOException;
    String getPathMainActivity(String pathPackage) throws IOException;
}
