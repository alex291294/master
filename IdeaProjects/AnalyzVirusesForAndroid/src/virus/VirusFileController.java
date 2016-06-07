package virus;

import emulator.Emulator;
import emulator.EmulatorRun;
import emulator.IEmulator;
import execute.InstallApk;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import preferences.UserPreferences;
import to.adb.or.aapt.Aapt;
import to.adb.or.aapt.Adb;
import to.adb.or.aapt.IndirectTo;
import utils.AlertDialogUtils;
import utils.DialogMessage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by alex on 19.01.16.
 */
public class VirusFileController implements IVirusFileController {

    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    private static final String PARSING_ERROR = "Ошибка при парсинге ";
    private static final String FILE_NOT_FOUND = "Файл \"%s\" отсутствует";

    IVirusFileModel virusFileModel;
    IEmulator emulatorRun;
    IndirectTo indirectTo;
    UserPreferences userPreferences;

    public VirusFileController(IVirusFileModel virusFileModel) {
        this.virusFileModel = virusFileModel;
        userPreferences = new UserPreferences();
        String pathTo = userPreferences.getPathEmulator();
        if (pathTo != null) {
            virusFileModel.setPathEmulator(pathTo);
        }
        pathTo = userPreferences.getPathAdb();
        if (pathTo != null) {
            virusFileModel.setPathADB(pathTo);
        }
        pathTo = userPreferences.getPathAapt();
        if (pathTo != null) {
            virusFileModel.setPathAAPT(pathTo);
        }
    }

    @Override
    public String getPathEmulator() {
        return virusFileModel.getPathEmulator();
    }

    @Override
    public String getPathAdb() {
        return virusFileModel.getPathADB();
    }

    @Override
    public String getPathAapt() {
        return virusFileModel.getPathAAPT();
    }

    @Override
    public void updateFile(File file) {
        virusFileModel.setFile(file);
    }

    @Override
    public void updatePathEmulator(String pathEmultor) {
        virusFileModel.setPathEmulator(pathEmultor);
        userPreferences.setPathEmulator(pathEmultor);
    }

    @Override
    public void updateNameEmulator(String nameEmulator) {
        virusFileModel.setNameEmulator(nameEmulator);
    }

    @Override
    public void updatePathADB(String pathADB) {
        virusFileModel.setPathADB(pathADB);
        userPreferences.setPathAdb(pathADB);
    }

    @Override
    public void updatePathAAPT(String pathAAPT) {
        virusFileModel.setPathAAPT(pathAAPT);
        userPreferences.setPathAapt(pathAAPT);
    }

    @Override
    public Collection<String> scanVirusFileManifest() {
        Collection<String> permissionsCollection = new ArrayList<String>();
        try {
            if (virusFileModel.getFile() == null) {
                throw new IOException();
            }
            ZipFile zipFile = new ZipFile(virusFileModel.getFile());
            ZipEntry file = zipFile.getEntry(ANDROID_MANIFEST);
            InputStream is = zipFile.getInputStream(file);
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            String xml = AndroidXMLDecompress.decompressXML(buf);
            FileWriter fileWriter = new FileWriter(file.getName());
            fileWriter.append(xml);
            fileWriter.close();
            File manifestFile = new File(ANDROID_MANIFEST);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(manifestFile);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("uses-permission");
            for (int i = 0; i < nodeList.getLength(); i++) {
                permissionsCollection.add(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
            }
        } catch (IOException e) {
            AlertDialogUtils.showErrorDialog(PARSING_ERROR + ANDROID_MANIFEST,
                    String.format(FILE_NOT_FOUND, virusFileModel.getFile()));
        }
        catch (Exception e) {
            AlertDialogUtils.showDialog(PARSING_ERROR + ANDROID_MANIFEST,
                    e.getMessage());
        }
        return permissionsCollection;
    }

    @Override
    public void installApp(String installPackageName) {
        InstallApk installApk = new InstallApk(virusFileModel.getPathADB());
        installApk.installPackage(installPackageName);
    }

    @Override
    public void runApp() throws IOException, InterruptedException {
        if (!virusFileModel.getPathAAPT().isEmpty()
                && !virusFileModel.getPathADB().isEmpty()) {
            indirectTo = new IndirectTo(new Aapt(virusFileModel.getPathAAPT()),
                    new Adb(virusFileModel.getPathADB()));
            if (virusFileModel.getFile() != null) {
                indirectTo.runApp(virusFileModel.getFile().getAbsolutePath());
            } else {
                AlertDialogUtils.showDialog(String.format(FILE_NOT_FOUND,
                        virusFileModel.getFile()), "");
            }
        } else {
            AlertDialogUtils.showDialog(DialogMessage.RUN_APP_ERROR, DialogMessage.NOT_EXIST_AATP_OR_ADB);
        }
    }

    @Override
    public void runEmulator() throws IOException {
        if (checkParamsEmu()) {
            emulatorRun = new EmulatorRun(new Emulator(virusFileModel.getPathEmulator(),
                    virusFileModel.getNameEmulator()));
            emulatorRun.execute();
        }
    }

    private boolean checkParamsEmu() {
        if (virusFileModel.getNameEmulator() == null) {
            AlertDialogUtils.showDialog(DialogMessage.RUN_EMU_ERROR, DialogMessage.NOT_EXIST_NAME_EMU);
            return false;
        } else if (virusFileModel.getPathEmulator() == null) {
            AlertDialogUtils.showDialog(DialogMessage.RUN_EMU_ERROR, DialogMessage.NOT_EXIST_PATH_EMU);
            return false;
        }
        return true;
    }

    @Override
    public String saveLogs() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        if (indirectTo == null) {
            indirectTo = new IndirectTo(new Aapt(virusFileModel.getPathAAPT()),
                    new Adb(virusFileModel.getPathADB()));
        }
        return indirectTo.logging();
    }
}
