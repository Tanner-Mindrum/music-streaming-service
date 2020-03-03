package Backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Proxy {

    private Catalog catalog;
    private CommunicationModule comm;

    public Proxy(CommunicationModule cm) throws IOException, ParseException {
        catalog = new Catalog();
        comm = cm;
    }

    public JSONObject synchExecution(String methodName, Object... param) throws InterruptedException, java.text.ParseException, ParseException, IOException {
        HashMap<String, JSONObject> methods = catalog.getMethod();
        JSONObject remoteMethod = methods.get(methodName);

        Map objectInfo = ((Map) remoteMethod.get("param"));
        Iterator<Map.Entry> objectItr = objectInfo.entrySet().iterator();
        for (Object s : param) {
            while (objectItr.hasNext()){
                Map.Entry data = objectItr.next();
                data.setValue(s);
                break;
            }
        }

        String test = comm.receive(remoteMethod.toString());
        JSONParser parse = new JSONParser();
        JSONObject obj = (JSONObject) parse.parse(test);

        return obj;
    }
}
