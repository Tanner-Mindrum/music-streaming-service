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
    private Dispatcher dispatcher;
    private SongDispatcher sd;

    public Proxy(Dispatcher d) throws IOException, ParseException {
        catalog = new Catalog();
        sd = new SongDispatcher();
        dispatcher = d;
    }

    public JSONObject synchExecution(String methodName, Object... param) throws InterruptedException, java.text.ParseException, ParseException, IOException {
        HashMap<String, JSONObject> methods = catalog.getMethod();
        JSONObject remoteMethod = methods.get(methodName);

        Map objectInfo = ((Map) remoteMethod.get("param"));
        Iterator<Map.Entry> objectItr = objectInfo.entrySet().iterator();
        for (Object s : param) {
            System.out.println(s);
            while (objectItr.hasNext()){
                Map.Entry data = objectItr.next();
                data.setValue(s);
                break;
            }
        }

        String test = dispatcher.dispatch(methodName);

        // return the ret
        JSONParser parse = new JSONParser();
        JSONObject obj = (JSONObject) parse.parse(test);

        System.out.println(obj);

        return obj;
    }
}
