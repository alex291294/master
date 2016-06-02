package decompile;

import com.android.utils.FileUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import jd.core.Decompiler;
import jd.core.DecompilerException;
import utils.AlertDialogUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by alex on 22.05.16.
 */
public class Decompile {

    private static final String CHMOD = "chmod a+x /dex2jar-2.0";
    private static final String DEX_TO_JAR = "dex2jar-2.0/d2j-dex2jar.sh ";
    private static final String DECOMPILE_ERROR = "Ошибка декомпиляции";
    private static final String DECOMPILE_PROCESS = "Выполнение декомпиляции";
    private static final String DECOMPILE_FINISHED = "Декомпиляция выполнена";
    private static final String DECOMPILE_SUCCESSED = "Успешное выполнение декомпиляции";

    private String pathToVirus;

    public Decompile(String pathToVirus) {
        this.pathToVirus = pathToVirus;
        try {
            Runtime.getRuntime().exec(CHMOD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void apkToJar() {
        Alert alert = AlertDialogUtils.showProcessDialog(DECOMPILE_PROCESS);
        alert.setOnShown(event -> {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Process process = Runtime.getRuntime().exec(DEX_TO_JAR + pathToVirus);
                        try {
                            process.waitFor();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialogUtils.showInformationDialog(DECOMPILE_FINISHED,
                                            DECOMPILE_SUCCESSED);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        AlertDialogUtils.showDialog(DECOMPILE_ERROR, e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            new Thread(task).start();
        });
        alert.show();
    }

    public void jarToJava() {
        File files[] = new File(".").listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        try {
            String folder = files[0].getName().split(".jar")[0];
            File file = new File(folder);
            if (file.exists()) {
                FileUtils.deleteFolder(file);
            }
            new Decompiler().decompile(files[0].getAbsolutePath(),
                    folder);
            files[0].delete();
        } catch (DecompilerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
