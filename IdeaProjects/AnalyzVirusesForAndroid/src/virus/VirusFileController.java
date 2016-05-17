package virus;

import emulator.Emulator;
import emulator.EmulatorRun;
import emulator.IEmulator;
import execute.MonkeyTest;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import preferences.UserPreferences;
import to.adb.or.aapt.*;
import utils.AlertDialogUtils;
import utils.DialogMessage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by alex on 19.01.16.
 */
public class VirusFileController implements IVirusFileController {
    IVirusFileModel virusFileModel;
    IEmulator emulatorRun;
    MonkeyTest monkeyTest;
    IndirectTo indirectTo;
    UserPreferences userPreferences;

    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";

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
    public Collection<String> scanVirusFileManifest() throws IOException, ParserConfigurationException, SAXException {
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
        Collection<String> permissionsCollection = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            permissionsCollection.add(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
        }
        return permissionsCollection;
    }

    @Override
    public void installApp(String installPackageName) {
        monkeyTest = MonkeyTest.getInstance(virusFileModel.getPathADB());
        monkeyTest.installPackage(installPackageName);
    }

    @Override
    public void runApp() throws IOException, InterruptedException {
        if (!virusFileModel.getPathAAPT().isEmpty() && !virusFileModel.getPathADB().isEmpty()) {
            indirectTo = new IndirectTo(new Aapt(virusFileModel.getPathAAPT()),
                    new Adb(virusFileModel.getPathADB()));
            indirectTo.runApp(virusFileModel.getFile().getAbsolutePath());
        } else {
            AlertDialogUtils.showDialog(DialogMessage.RUN_APP_ERROR, DialogMessage.NOT_EXIST_AATP_OR_ADB);
        }
    }

    @Override
    public void shutDown() {
        monkeyTest = MonkeyTest.getInstance(virusFileModel.getPathADB());
        monkeyTest.shutDownMonkey();
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
    public String saveLogs() throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException {
        return indirectTo.logging();
    }
}
