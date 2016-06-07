package proxy;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import utils.AlertDialogUtils;

/**
 * Created by alex on 07.06.16.
 */
public class TextAreaAppender extends WriterAppender {

    private static volatile TextArea textArea = null;

    private static final String LOGGING_ERROR = "Ошибка логгирования";

    public TextAreaAppender(TextArea textArea) {
        TextAreaAppender.textArea = textArea;
    }

    @Override
    public void append(LoggingEvent event) {
        final String message = this.layout.format(event);
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (textArea != null) {
                            if (textArea.getText().length() == 0) {
                                textArea.setText(message);
                            } else {
                                textArea.selectEnd();
                                textArea.insertText(textArea.getText().length(),
                                        message);
                            }
                        }
                    } catch (final Throwable t) {
                        AlertDialogUtils.showErrorDialog(LOGGING_ERROR, t.getMessage());
                    }
                }
            });
        } catch (final IllegalStateException e) {}
    }
}
