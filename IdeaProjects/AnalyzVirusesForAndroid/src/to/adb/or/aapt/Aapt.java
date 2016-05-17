package to.adb.or.aapt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alex on 21.01.16.
 */
public class Aapt implements IAapt {
    private static final String LAUNCHABLE = "%s dump badging %s | grep %s";
    private static final String ACTIVITY = "launchable-activity";
    private static final String PACKAGE = "package";

    private String AAPT;

    public Aapt(String AAPT) {
        this.AAPT = AAPT;
    }

    private String getPath(String pathToFile, String param) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c",String.format(LAUNCHABLE, AAPT, pathToFile, param)})
                                .getInputStream()));
        String path = bufferedReader.readLine();
        path = path.substring(path.indexOf("'") + 1, path.length());
        path = path.substring(0, path.indexOf("'"));
        return path;
    }

    @Override
    public String getPathPackage(String pathToFile) throws IOException {
        return getPath(pathToFile, PACKAGE);
    }

    @Override
    public String getPathMainActivity(String pathToFile) throws IOException {
        return getPath(pathToFile, ACTIVITY);
    }
}