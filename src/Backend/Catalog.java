package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Catalog {

    private JSONArray methods;
    private HashMap<String, JSONObject> methodMap;

    public Catalog () throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        methods = (JSONArray) parser.parse(new FileReader("Methods.json"));
        methodMap = new HashMap<String, JSONObject>();
        for (Object m : methods) {
            JSONObject entryInfo = (JSONObject) m;
            System.out.println(entryInfo.get("remoteMethod"));
            methodMap.put((String) entryInfo.get("remoteMethod"), entryInfo);
        }
    }

    public HashMap<String, JSONObject> getMethod() {
        return methodMap;
    }
}
