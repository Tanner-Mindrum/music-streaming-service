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
    private Dispatcher d;
    private SongDispatcher sd;

    public Proxy() throws IOException, ParseException {
        catalog = new Catalog();
        sd = new SongDispatcher();
        d = new Dispatcher();
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
        System.out.println(methodName);
        d.registerObject(sd, "SongServices");
        String test = d.dispatch(methodName);
        System.out.println(test);


        return remoteMethod;
    }

}
