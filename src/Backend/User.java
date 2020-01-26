package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
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

    public User(String email, String userName, String month, String day, String year) throws IOException {
        // Creating JSONArray for the array of users
        JSONArray userArray = new JSONArray();

        // Creating the JSON Object
        JSONObject userObject1 = new JSONObject();

        // Creating map for userInfo
        Map userInfo = new LinkedHashMap(4);
        // Inserting data into JSONObject
        userInfo.put("email", email);
        userInfo.put("userName", userName);
        userInfo.put("id", 1);
        String dateOfBirth = month + "/" + day + "/" + year;
        userInfo.put("dob", dateOfBirth);

        // Inserting userInfo to the user JSON object
        userObject1.put("info", userInfo);


        // Initialize user with no playlists
        // Creating JSONArray for the user's playlists
        JSONArray playlists = new JSONArray();


        userObject1.put("playlists", playlists);


        // Adding the user object to the array
        userArray.add(userObject1);


        // writing JSON to file
        PrintWriter fileWriter = new PrintWriter("user.json");
        fileWriter.write(userArray.toJSONString());

        fileWriter.flush();
        fileWriter.close();


        // User 2 to test the Array of JSON objects
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



}
