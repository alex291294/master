package sample;

import decompile.DecompileExecutor;
import decompile.IDecompile;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.xml.sax.SAXException;
import proxy.ProxyServer;
import utils.AlertDialogUtils;
import utils.DialogMessage;
import virus.IVirusFileController;
import virus.VirusFileController;
import virus.VirusFileModel;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Scanner;

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

    @FXML
    private Tab decompileTab;

    @FXML
    private TreeView<File> decompileTree;

    @FXML
    private TextArea decompileCode;

    @FXML
    private TextArea logProxy;

    private IVirusFileController virusFileController;

    private IDecompile decompileExecutor;

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

    public void getPermissions() {
        Collection<String> permissions = virusFileController.scanVirusFileManifest();
        String allPermissions = "";
        for (String permission : permissions) {
            allPermissions += permission + "\n";
        }
        permissionFile.setText(allPermissions);
    }

    public void runApp() throws IOException, InterruptedException {
        virusFileController.runApp();
    }

    public void saveLogs() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        logsArea.setText(virusFileController.saveLogs());
    }

    private TreeItem<File> createTree(String nameFile, TreeItem<File> treeItem) {
        File file = new File(nameFile);
        treeItem = new TreeItem<>(file);
        for (File file1 : file.listFiles()) {
            TreeItem<File> treeItem1 = null;
            if (file1.isDirectory()) {
                treeItem1 = createTree(file1.getPath(), treeItem1);
            } else {
                treeItem1 = new TreeItem<>(file1);
            }
            treeItem.getChildren().add(treeItem1);
        }
        return treeItem;
    }

    public void showTree() {
        decompileExecutor.jarToJava();
        if (!pathToFile.getText().isEmpty()) {
            String[] path = pathToFile.getText().split("/");
            String nameFile = path[path.length - 1].split(".apk")[0];
            nameFile += "-dex";
            TreeItem<File> rootItem = null;
            rootItem = createTree(nameFile, rootItem);
            decompileTree.setRoot(rootItem);
        }
    }

    public void showDecompileCode() {
        TreeItem<File> treeItem = decompileTree.getSelectionModel().getSelectedItem();
        if (!treeItem.getValue().isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Scanner scanner = new Scanner(treeItem.getValue());
                while (scanner.hasNext()) {
                    stringBuilder.append(scanner.nextLine()).append("\n");
                }
                decompileCode.setText(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectDecompile() {
        if (decompileTab.isSelected()) {
            decompileExecutor = new DecompileExecutor(pathToFile.getText());
            decompileExecutor.apkToJar();
        }
    }

    public void startProxy() {
        ProxyServer.getInstance().runServer(logProxy);
    }

    public void stopProxy() {
        ProxyServer.getInstance().stopServer();
    }
}
