package decompile;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utils.AlertDialogUtils;

import java.util.Optional;

/**
 * Created by alex on 22.05.16.
 */
public abstract class Decompile {

    static final String DECOMPILE_PROCESS = "Выполнение декомпиляции";
    static final String DECOMPILE_FINISHED = "Декомпиляция выполнена";
    static final String DECOMPILE_SUCCESSED = "Успешное выполнение декомпиляции";

    protected Decompile next;

    public void setNext(Decompile next) {
        this.next = next;
    }

    public void decompile(String pathVirus) {
        Alert alert = AlertDialogUtils.showProcessDialog(DECOMPILE_PROCESS);
        alert.setOnShown(event -> {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    executeDecompile(pathVirus);
                    return null;
                }
            };
            new Thread(task).start();
        });
        alert.show();
    }

    public abstract void executeDecompile(String pathVirus);
}
