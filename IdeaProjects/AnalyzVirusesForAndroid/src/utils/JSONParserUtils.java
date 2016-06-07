package utils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

/**
 * Created by alex on 03.03.16.
 */
public class JSONParserUtils {

    private static final String ENCODING_ERROR_TITLE = "Ошибка преобразования строки";
    private static final String ENCODING_ERROR_CONTENT = "Ошибка кодировки";
    private static final String DATA = "data";
    private static final String REGEX_STRING_TO_PART = "\"";
    private static final String PATH = "path";
    private static final String REGEX_DROID_BOX = "DroidBox:";

    public static String createJsonFile(Process process) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String lineLog = null;
        StringBuilder lineJSON = new StringBuilder("{");
        FileWriter fileWriter = new FileWriter("logging.json");
        while ((lineLog = bufferedReader.readLine()) != null) {
            String[] json = lineLog.split(REGEX_DROID_BOX);
            if (json.length > 1) {
                String[] pasreArray = json[1].split(REGEX_STRING_TO_PART);
                lineJSON
                        .append(pasreArray[0])
                        .append(" ")
                        .append(pasreArray[1])
                        .append(" ")
                        .append(pasreArray[2])
                        .append(" ");
                for (int i = 3; i < pasreArray.length; i++) {
                    lineJSON.append(parseString(pasreArray, i));
                    i += 3;
                }
                lineJSON.append("\n");
            }
        }
        lineJSON.append("}");
        process.destroy();
        fileWriter.append(lineJSON.toString());
        fileWriter.close();
        return lineJSON.toString();
    }

    private static void addParse(String[] array, int num,
                                   StringBuilder stringBuilder, String value) {
        stringBuilder
                .append(array[num])
                .append(" ")
                .append(array[++num])
                .append(" ")
                .append(value)
                .append(array[num + 2])
                .append(" ");
    }

    private static String parseString(String[] array, int num) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPathOrData(array[num])) {
            addParse(array, num, stringBuilder, hexToString(array[num + 2]));
        } else {
            addParse(array, num, stringBuilder, array[num + 2]);
        }
        return stringBuilder.toString();
    }

    private static boolean isPathOrData(String value) {
        if (DATA.equals(value)) {
            return true;
        } else if (PATH.equals(value)) {
            return true;
        }
        return false;
    }

    private static String hexToString(String hex) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            byte[] buf = DatatypeConverter.parseHexBinary(hex);
            stringBuilder.append(new String(buf, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            AlertDialogUtils.showInformationDialog(ENCODING_ERROR_TITLE,
                    ENCODING_ERROR_CONTENT);
        }
        return stringBuilder.toString();
    }
}
