package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by alex on 20.01.16.
 */
public class AlertDialogUtils {

    private static final String ERROR = "Ошибка";
    private static final String LOGGING = "Логгирование";
    private static final String LOGGING_STOP = "Ведется сбор логгов";
    private static final String STOP = "Остановить";
    private static final String CLOSE = "Свернуть";
    private static final String WAIT_EXECUTE_OPERATION = "Ожидайте выполнение операции";

    public static void showDialog(String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR);
        alert.setHeaderText(headerText);
        alert.getDialogPane().setContent(new Label(contextText));
        alert.showAndWait();
    }

    public static String showLoggingDialog(Process process) throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LOGGING);
        alert.setHeaderText(LOGGING_STOP);
        alert.setGraphic(new ImageView(AlertDialogUtils.class
                .getResource("progress.gif").toExternalForm()));
        ButtonType stop = new ButtonType(STOP);
        alert.getButtonTypes().setAll(stop);
        Optional<ButtonType> click = alert.showAndWait();
        if (click.get() == stop) {
            Class<?> pid  = process.getClass();
            Field field = pid.getDeclaredField("pid");
            field.setAccessible(true);
            Runtime.getRuntime().exec("kill -2 " + field.getInt(process));
            return JSONParserUtils.createJsonFile(process);
        }
        return null;
    }

    public static Alert showProcessDialog(String headerText) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(WAIT_EXECUTE_OPERATION);
        alert.setHeaderText(headerText);
        alert.setGraphic(new ImageView(AlertDialogUtils.class
                .getResource("progress.gif").toExternalForm()));
        ButtonType close = new ButtonType(CLOSE);
        alert.getButtonTypes().setAll(close);
        return alert;
    }

    private static void showDialog(String title, String headerText,
                                   Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void showInformationDialog(String title, String headerText) {
        showDialog(title, headerText, Alert.AlertType.INFORMATION);
    }

    public static void showErrorDialog(String title, String headerText) {
        showDialog(title, headerText, Alert.AlertType.ERROR);
    }
}
