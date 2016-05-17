package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alex on 03.03.16.
 */
public class JSONParserUtils {

    public static String createJsonFile(Process process) throws IOException, ParseException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String lineLog = null;
        StringBuilder lineJSON = new StringBuilder();
        boolean isWrittenFirstBracket = false;
        FileWriter fileWriter = new FileWriter("logging.json");
        while ((lineLog = bufferedReader.readLine()) != null) {
            String[] json = lineLog.split("\\{");
            if (json.length > 1) {
                for (int i = 1; i < json.length; i++) {
                    if (!isWrittenFirstBracket) {
                        lineJSON.append("{" + json[i]);
                        isWrittenFirstBracket = true;
                    } else {
                        if (i > 1) {
                            lineJSON.append("{");
                        }
                        lineJSON.append(json[i]);
                    }
                }
                lineJSON.delete(lineJSON.length() - 1, lineJSON.length());
                lineJSON.append("\n");
            }
        }
        JSONObject jsonObject= null;
        if (lineJSON.length() != 0) {
            lineJSON.append("}");
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(lineJSON.toString());
        }
        process.destroy();
        if (jsonObject != null) {
            fileWriter.append(jsonObject.toJSONString());
            fileWriter.close();
            return jsonObject.toJSONString();
        }
        return null;
    }
}
