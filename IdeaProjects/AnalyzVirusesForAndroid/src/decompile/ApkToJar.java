package decompile;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import utils.AlertDialogUtils;

import java.io.IOException;
import java.util.concurrent.FutureTask;

/**
 * Created by alex on 22.05.16.
 */
public class ApkToJar extends Decompile {

    private static final String CHMOD = "chmod a+x /dex2jar-2.0";

    private static final String DEX_TO_JAR = "dex2jar-2.0/d2j-dex2jar.sh ";

    private static final String DECOMPILE_ERROR = "Ошибка декомпиляции";

    public ApkToJar() {
        try {
            Runtime.getRuntime().exec(CHMOD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeDecompile(String pathVirus) {
        FutureTask<Void> futureTask = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(DEX_TO_JAR + pathVirus);
                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    next.executeDecompile(pathVirus);
                } catch (IOException e) {
                    AlertDialogUtils.showDialog(DECOMPILE_ERROR, e.getMessage());
                    e.printStackTrace();
                }
            }
        }, null);
        new Thread(futureTask).start();
    }
}
