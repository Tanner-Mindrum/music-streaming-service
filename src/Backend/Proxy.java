package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Proxy {

    private Catalog catalog;

    public Proxy() throws IOException, ParseException {
        catalog = new Catalog();
    }

    public JSONObject synchExecution(String methodName, Object... param) throws InterruptedException {
        HashMap<String, JSONObject> methods = catalog.getMethod();
        JSONObject remoteMethod = methods.get(methodName);

        for (Object s : param) {
        }

        Thread thread = new Thread();
        thread.join();

        return null;
    }

}
