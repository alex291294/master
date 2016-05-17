package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import utils.AlertDialogUtils;
import utils.DialogMessage;
import utils.ListDangerousPermissions;
import virus.IVirusFileController;
import virus.VirusFileController;
import virus.VirusFileModel;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class Controller implements Initializable, EventHandler<WindowEvent> {

    private static final String VIRUS = "Choose virus file";
    private static final String ADB = "Choose ADB file";
    private static final String AAPT = "Choose AAPT file";
    private static final String EMULATOR = "Choose emulator file";

    private FileChooser fileChooser = new FileChooser();
    private Stage stage;

    @FXML
    private TextField pathToFile;

    @FXML
    private TextArea permissionFile;

    @FXML
    private TextField pathEmulator;

    @FXML
    private TextField nameEmulator;

    @FXML
    private TextField pathADB;

    @FXML
    private TextField pathAAPT;

    @FXML
    private TextArea logsArea;

    private IVirusFileController virusFileController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        virusFileController = new VirusFileController(new VirusFileModel());
        init();
    }

    private void init() {
        String pathTo = null;
        pathTo = virusFileController.getPathEmulator();
        if (pathTo != null) {
            pathEmulator.setText(pathTo);
        }
        pathTo = virusFileController.getPathAdb();
        if (pathTo != null) {
            pathADB.setText(pathTo);
        }
        pathTo = virusFileController.getPathAapt();
        if (pathTo != null) {
            pathAAPT.setText(pathTo);
        }
    }

    public void setStge(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(this);
    }

    @Override
    public void handle(WindowEvent event) {
        virusFileController.shutDown();
        System.exit(0);
    }

    private void initFileChooser(String title) {
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().clear();
    }

    public void chooseFile() throws IOException {
        initFileChooser(VIRUS);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("APK files", "*.apk"));
        File virusFile = fileChooser.showOpenDialog(pathToFile.getScene().getWindow());
        if (virusFile != null) {
            pathToFile.setText(virusFile.getAbsolutePath());
            virusFileController.updateFile(virusFile);
        }
    }

    public void chooseEmulator() {
        initFileChooser(EMULATOR);
        String pathEmulator = fileChooser.showOpenDialog(pathToFile.getScene().getWindow()).getAbsolutePath();
        if (pathEmulator != null) {
            this.pathEmulator.setText(pathEmulator);
            virusFileController.updatePathEmulator(pathEmulator);
        }
    }

    public void runEmulator() throws IOException {
        String nameEmulator = this.nameEmulator.getText();
        if (!nameEmulator.isEmpty()) {
            virusFileController.updateNameEmulator(nameEmulator);
        }
        virusFileController.runEmulator();
    }

    public void chooseADB() {
        initFileChooser(ADB);
        String pathADB = fileChooser.showOpenDialog(pathToFile.getScene().getWindow()).getAbsolutePath();
        if (pathADB != null) {
            this.pathADB.setText(pathADB);
            virusFileController.updatePathADB(pathADB);
        }
    }

    public void chooseAAPT() {
        initFileChooser(AAPT);
        String pathAAPT = fileChooser.showOpenDialog(pathToFile.getScene().getWindow()).getAbsolutePath();
        if (pathAAPT != null) {
            this.pathAAPT.setText(pathAAPT);
            virusFileController.updatePathAAPT(pathAAPT);
        }
    }

    public void installAPK() throws IOException, InterruptedException {
        if (!pathToFile.getText().isEmpty()) {
            virusFileController.installApp(pathToFile.getText());
        } else {
            AlertDialogUtils.showDialog(DialogMessage.ERROR_INSTALL_APK, DialogMessage.NOT_EXIST_PATH_TO_FILE);
        }
    }

    public void getPermissions() throws ParserConfigurationException, SAXException, IOException {
        Collection<String> permissions = virusFileController.scanVirusFileManifest();
        permissions = ListDangerousPermissions.checkOnDangerous(permissions);
        String allPermissions = "";
        for (String permission : permissions) {
            allPermissions += permission + "\n";
        }
        permissionFile.setText(allPermissions);
    }

    public void runApp() throws IOException, InterruptedException {
        virusFileController.runApp();
    }

    public void saveLogs() throws IOException, InterruptedException, ParseException, NoSuchFieldException, IllegalAccessException {
        logsArea.setText(virusFileController.saveLogs());
    }
}
