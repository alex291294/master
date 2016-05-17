package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by alex on 20.01.16.
 */
public class AlertDialogUtils {
    private static Alert alert;
    private static final String LOGGING = "Логгирование";
    private static final String LOGGING_STOP = "Ведется сбор логгов";
    private static final String STOP = "Остановить";

    public static void showDialog(String headerText, String contextText) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(headerText);
        alert.getDialogPane().setContent(new Label(contextText));
        alert.showAndWait();
    }

    public static String showLoggingDialog(Process process) throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LOGGING);
        alert.setHeaderText(LOGGING_STOP);
        alert.setGraphic(new ImageView(AlertDialogUtils.class.getResource("progress.gif").toExternalForm()));
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
}
