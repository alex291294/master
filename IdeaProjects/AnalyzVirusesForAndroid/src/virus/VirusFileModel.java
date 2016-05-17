package virus;

import java.io.File;

/**
 * Created by alex on 19.01.16.
 */
public class VirusFileModel implements IVirusFileModel {
    private File file;
    private String pathEmulator;
    private String nameEmulator;
    private String pathADB;
    private String pathAAPT;

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void setPathEmulator(String pathEmulator) {
        this.pathEmulator = pathEmulator;
    }

    @Override
    public void setNameEmulator(String nameEmulator) {
        this.nameEmulator = nameEmulator;
    }

    @Override
    public String getPathEmulator() {
        return pathEmulator;
    }

    @Override
    public String getNameEmulator() {
        return nameEmulator;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setPathADB(String pathADB) {
        this.pathADB = pathADB;
    }

    @Override
    public void setPathAAPT(String pathAAPT) {
        this.pathAAPT = pathAAPT;
    }

    @Override
    public String getPathADB() {
        return pathADB;
    }

    @Override
    public String getPathAAPT() {
        return pathAAPT;
    }
}
