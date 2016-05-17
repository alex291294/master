package to.adb.or.aapt;

import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by alex on 21.01.16.
 */
public class IndirectTo {
    private IAapt aapt;
    private IAdb adb;

    public IndirectTo(IAapt aapt, IAdb adb) {
        this.aapt = aapt;
        this.adb = adb;
    }

    public void runApp(String pathToFile) throws IOException, InterruptedException {
        String pathPackage = aapt.getPathPackage(pathToFile);
        String pathActivity = aapt.getPathMainActivity(pathToFile);
        adb.runApp(pathPackage + "/" + pathActivity);
    }

    public void stopEmulator() throws IOException {
        adb.stopEmulator();
    }

    public void logging() throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException {
        adb.logging();
    }
}
