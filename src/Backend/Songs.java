package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Songs {

    private String title;

    public Songs() {
        title = "";
    }

    public String getTitle() {
        return title;
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//music.json"));

        for (Object info : information) {
            JSONObject songInfo = (JSONObject) info;

            Map release = ((Map) songInfo.get("release"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("name")) {
                    System.out.println(data.getValue());
                }
            }
        }
    }
}
