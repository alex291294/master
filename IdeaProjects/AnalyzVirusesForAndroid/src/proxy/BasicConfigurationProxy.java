package proxy;

import javafx.scene.control.TextArea;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Created by alex on 07.06.16.
 */
public class BasicConfigurationProxy extends BasicConfigurator {

    public static void configure(TextArea textArea) {
        Logger root = Logger.getRootLogger();
        TextAreaAppender textAreaAppender = new TextAreaAppender(textArea);
        textAreaAppender.setLayout(new PatternLayout("%r [%t] %p %c %x - %m%n"));
        root.addAppender(textAreaAppender);
    }
}
