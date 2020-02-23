package Backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Proxy {

        public Proxy () {}

        public JSONObject synchExecution(String getFileSize, String valueOf) throws IOException, ParseException {
                System.out.println("hello");
                JSONParser parser = new JSONParser();
                return (JSONObject) parser.parse(new FileReader("getFileSize.json"));
        }

        public JSONObject synchExecution(String getSongChunk, String fileName, String fileName1, int fragment) throws IOException, ParseException {
                JSONParser parser = new JSONParser();
                return (JSONObject) parser.parse(new FileReader("getSongChunk.json"));
        }

}
