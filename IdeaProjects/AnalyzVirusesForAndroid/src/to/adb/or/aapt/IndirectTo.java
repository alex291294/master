package to.adb.or.aapt;

import utils.AlertDialogUtils;

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

    public void runApp(String pathToFile)  {
        if (!pathToFile.isEmpty()) {
            String pathPackage = aapt.getPathPackage(pathToFile);
            String pathActivity = aapt.getPathMainActivity(pathToFile);
            try {
                adb.runApp(pathPackage + "/" + pathActivity);
            } catch (IOException e) {
                AlertDialogUtils.showDialog(Aapt.RUN_APP_ERROR, e.getMessage());
            }
        }
    }

    public String logging() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        return adb.logging();
    }
}
