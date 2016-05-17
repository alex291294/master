package virus;

import java.io.File;

/**
 * Created by alex on 19.01.16.
 */
public interface IVirusFileModel {
    void setFile(File file);
    void setPathEmulator(String pathEmulator);
    void setNameEmulator(String nameEmulator);
    void setPathADB(String pathADB);
    void setPathAAPT(String pathAAPT);
    String getPathEmulator();
    String getNameEmulator();
    File getFile();
    String getPathADB();
    String getPathAAPT();
}
