package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModifyUser {

    private String username;
    private JSONObject userObject;

    // Constructor with username parameter to find user in JSON
    public ModifyUser(String name) {
        this.username = name;
        userObject = null;
    }

    /**
     * Finds if a user exists in the JSON file when given a username and password
     * @param username - String username
     * @param password - String password
     * @return - true if the username and corresponding password is found
     * @throws IOException
     * @throws ParseException
     */
    public boolean checkUserExists(String username, String password) throws IOException, ParseException {
        boolean usernameFound = false;
        boolean passwordFound = false;

        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray) {
            JSONObject userInfoSearch = (JSONObject) info;

            // Check if the username exists in the JSON
            Map release = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")) {
                    if (username.equals(data.getValue())) {
                        usernameFound = true;
                    }
                }
            }

            // If username is found, then check if a corresponding password matches with password parameter
            Map passwordCheck = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> passwordCheckItr = passwordCheck.entrySet().iterator();
            while (passwordCheckItr.hasNext()) {
                Map.Entry data = passwordCheckItr.next();
                if (usernameFound && data.getKey().equals("password")) {
                    if (password.equals(data.getValue())) {
                        passwordFound = true;
                    }
                }
            }
        }
        return usernameFound && passwordFound;
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

    public ArrayList<String> getPlaylists() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));
        ArrayList<String> playlistNames = new ArrayList<>(); // add to this arraylist

        // For each user:
        for (Object info : userArray){
            JSONObject userInfoSearch = (JSONObject) info;

            Map userInfo = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = userInfo.entrySet().iterator();
            while (releaseItr.hasNext()){
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")){
                    if (username.equals(data.getValue())){
                        JSONArray playlistArray = (JSONArray) userInfoSearch.get("playlists");
                        // Try to iterate the JSONArray of playlists
                        for (int i = 0; i <playlistArray.size(); i++){
                            JSONObject tempPlaylist = (JSONObject) playlistArray.get(i);
                            playlistNames.add(tempPlaylist.get("name").toString());
                            //System.out.println(username + "'s playlist: " + tempPlaylist.get("name"));
                        }
                    }
                }
            }

        }
        return playlistNames;
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
    // TODO Check if working
    public void deletePlaylist(String playlistName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));

        for (Object info : userArray){
            JSONObject userInfoSearch = (JSONObject) info;


            Map userInfo = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = userInfo.entrySet().iterator();
            while (releaseItr.hasNext()){
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")){
                    if (username.equals(data.getValue())){
                        // Delete user's playlist with passed in name:
                        // Iterate through the playlists and check the name
                        JSONArray playlists = (JSONArray) userInfoSearch.get("playlists");
                        for (int i = 0; i < playlists.size(); i++){
                            JSONObject tempPlaylist = (JSONObject) playlists.get(i);
                            // If playlistName matches
                            if (playlistName.equals(tempPlaylist.get("name").toString())) {
                                // delete playlist from JSON
                                playlists.remove(i);
                            }
                        }
                    }
                }
            }
            // Writing non-deleted playlists? to JSON file
            PrintWriter fileWriter = new PrintWriter("user.json");
            fileWriter.write(userArray.toJSONString());

            fileWriter.flush();
            fileWriter.close();
        }
    }
}
