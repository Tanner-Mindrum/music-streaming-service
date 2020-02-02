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

public class User {

    private String userName;

    public User() {
        userName = "";
    }

    public User(String name) {
        userName = name;
    }

    public User(String email, String username, String month, String day, String year) throws IOException, ParseException {
        // Creating JSONArray for the array of users
        JSONParser parser = new JSONParser();
        // Tanner's path: C://CECS 327//music-streaming-service//user.json
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("C:\\CECS 327\\music-streaming-service\\user.json"));
        // Creating the JSON Object
        JSONObject userObject = new JSONObject();

        // Creating map for userInfo
        Map userInfo = new LinkedHashMap(4);
        // Inserting data into JSONObject
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("id", userArray.size() + 1);
        String dateOfBirth = month + " - " + day + " - " + year;
        System.out.println(dateOfBirth);
        userInfo.put("dob", dateOfBirth);

        // Inserting userInfo to the user JSON object
        userObject.put("info", userInfo);


        // Initialize user with no playlists
        // Creating JSONArray for the user's playlists
        JSONArray playlists = new JSONArray();


        userObject.put("playlists", playlists);


        // Adding the user object to the array
        userArray.add(userObject);


        // writing JSON to file
        PrintWriter fileWriter = new PrintWriter("user.json");
        fileWriter.write(userArray.toJSONString());

        fileWriter.flush();
        fileWriter.close();
    }

    public boolean checkUserExists(String username) throws IOException, ParseException {

        // if user exists: return true
        // else return false
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//user.json"));

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
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//user.json"));

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

    public void createPlaylist(String playlistName){

        Map playlistMap = new LinkedHashMap(2); // Creating subfields for playlists
        playlistMap.put("name", playlistName); // temp line for default playlist
        // Initialize an empty array
        JSONArray playlistSongs = new JSONArray();
        playlistMap.put("songs", playlistSongs);

        // TODO : Add created playlist map to the user's playlist array
        //playlists.add(playlistMap);
    }

//    public ArrayList<String> getPlaylistNames() throws IOException, ParseException {
//        ArrayList<String> playListNames = new ArrayList<>();
//
//        JSONParser parser = new JSONParser();
//        JSONArray information = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//user.json"));
//        for (Object info : information) {
//            JSONObject entryInfo = (JSONObject) info;
//
//            Map release = ((Map) entryInfo.get("release"));
//            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
//            while (releaseItr.hasNext()) {
//                Map.Entry data = releaseItr.next();
//                if (data.getKey().equals("name")) {
//
//                }
//            }
//
//        }
//
//    }



}
