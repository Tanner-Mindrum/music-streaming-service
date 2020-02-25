package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Proxy {

        private Catalog catalog;

        public Proxy() throws IOException, ParseException {
                catalog = new Catalog();
        }

        public JSONObject synchExecution(String method, Object... param) throws InterruptedException {
                JSONArray methods = catalog.getMethod();
                for (Object m : methods) {
                        JSONObject entryInfo = (JSONObject) m;
                        Map rMethod = ((Map) entryInfo.get("remoteMethod"));
                        Iterator<Map.Entry> rMethodItr = rMethod.entrySet().iterator();
                        while (rMethodItr.hasNext()) {
                                Map.Entry data = rMethodItr.next();
                                if (data.getKey().equals("remoteMethod")) {
                                        method = (String) data.getValue();
                                        if (method.equals((String) data.getValue())) {

                                        }
                                }
                        }
                }

                for (Object s : param) {
                }

                Thread thread = new Thread();
                thread.join();
        }

}
