package virus;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by alex on 19.01.16.
 */
public interface IVirusFileController {
    void updateFile(File file);
    void updatePathEmulator(String pathEmultor);
    void updateNameEmulator(String nameEmulator);
    void updatePathADB(String pathADB);
    void updatePathAAPT(String pathAAPT);
    String getPathEmulator();
    String getPathAdb();
    String getPathAapt();
    Collection<String> scanVirusFileManifest() throws IOException, ParserConfigurationException, SAXException;
    void installApp(String installPackageName);
    void runApp() throws IOException, InterruptedException;
    void shutDown();
    void runEmulator() throws IOException;
    void saveLogs() throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException;
}
