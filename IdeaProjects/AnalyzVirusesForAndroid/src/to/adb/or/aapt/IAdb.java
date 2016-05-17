package to.adb.or.aapt;

import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by alex on 21.01.16.
 */
public interface IAdb {
    void stopEmulator() throws IOException;
    void runApp(String pathPackage) throws IOException;
    String logging() throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException;
}
