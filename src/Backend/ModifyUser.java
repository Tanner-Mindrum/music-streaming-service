package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModifyUser {

    private String username;
    private JSONObject userObject;

    public ModifyUser(String name) {
        this.username = name;
        userObject = null;
    }

    public boolean checkUserExists(String username) throws IOException, ParseException {

        // if user exists: return true
        // else return false
        JSONParser parser = new JSONParser();
        // C://CECS 327//music-streaming-service//user.json
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray) {
            JSONObject userInfoSearch = (JSONObject) info;

            // dupe for user
            Map release = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")) {
                    if (username.equals(data.getValue())) {
                        return true; // true means a duplicate has been found
                    }
                }
            }

        }
        return false;
    }

    // If this returns true, prompt them again, false: call constructor
    public String checkDuplicateUser(String email, String username) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray) {
            JSONObject userInfoSearch = (JSONObject) info;

            // dupe for user
            Map release = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("email")) {
                    if (email.equals(data.getValue())) {
                        return "email"; // true means a duplicate has been found
                    }
                }
                if (data.getKey().equals("username")) {
                    if (username.equals(data.getValue())) {
                        return "username"; // true means a duplicate has been found
                    }
                }
            }

        }
        return "okay";
    }

    // DELETE Playlists
    // Get songs in the playlist
    // Display playlists entirely
    // Create a new thread to play a song, stop the thread and restart it to play a new song
    // TODO: Check duplicate playlist names
    public void createPlaylist(String playlistName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray) {
            JSONObject userInfoSearch = (JSONObject) info;

            JSONArray playlists = (JSONArray) userInfoSearch.get("playlists");

            Map userInfo = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = userInfo.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")) {
                    if (username.equals(data.getValue())) {
                        Map playlistMap = new LinkedHashMap(2); // Creating sub-fields for playlists
                        playlistMap.put("name", playlistName); // temp line for default playlist
                        // Initialize an empty array
                        playlistMap.put("songs", new JSONArray());

                        // Insert into playlists array
                        //userInfoSearch.put("playlists", playlistMap);

                        playlists.add(playlistMap);

                    }
                }

            }




            // Writing playlist to JSON file
            PrintWriter fileWriter = new PrintWriter("user.json");
            fileWriter.write(userArray.toJSONString());

            fileWriter.flush();
            fileWriter.close();
        }



    }

    public void deletePlaylist(String playlistName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray){
            JSONObject userInfoSearch = (JSONObject) info;

            JSONArray playlists = (JSONArray) userInfoSearch.get("playlists");

            Map userInfo = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = userInfo.entrySet().iterator();
            while (releaseItr.hasNext()){
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")){
                    if (username.equals("username")){
                        // Delete user's playlist with passed in name:
                        // Iterate through the playlists and check the name
                        for (int i = 0; i < playlists.size(); i++){
                            JSONObject tempPlaylist = (JSONObject) playlists.get(i);
                            // If playlistName matches
                            if (playlistName.equals(tempPlaylist.get("name"))) {
                                // delete playlist
                                tempPlaylist.remove(tempPlaylist);
                            }
                        }
                    }
                }
            }
        }


    }
}
