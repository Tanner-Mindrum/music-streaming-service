package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModifyUser {

    private String username;
    private JSONObject userObject;
    private SongInfo songInfo;

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
            usernameFound = false;

            // Check if the username exists in the JSON
            Map user = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> userItr = user.entrySet().iterator();
            while (userItr.hasNext()) {
                Map.Entry data = userItr.next();
                if (data.getKey().equals("username")) {
                    if (username.equals(data.getValue())) {
                        usernameFound = true;
                    }
                }
                if (usernameFound && data.getKey().equals("password")) {
                    if (password.equals(data.getValue())) {
                        passwordFound = true;
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
            if (usernameFound && passwordFound) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a user already exists with the email or username given
     * @param email
     * @param username
     * @return
     * @throws IOException
     * @throws ParseException
     */
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

    /**
     * Gets the playlists to udpdate the display
     * @return
     * @throws IOException
     * @throws ParseException
     */
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


    /**
     * Creates a new playlist with a specified name for the user
     * @param playlistName
     * @throws IOException
     * @throws ParseException
     */
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

    /**
     * Deletes the selected playlist
     * @param playlistName
     * @throws IOException
     * @throws ParseException
     */
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

    /**
     * Adds a selected song to a playlist
     * @param song
     * @param playlistName
     * @throws IOException
     * @throws ParseException
     */
    public void addToPlaylist(Songs song, String playlistName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));
        boolean duplicate = false;

        for (Object info : userArray) {
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
                                // Add song to playlist
                                JSONArray songs = (JSONArray) tempPlaylist.get("songs");
                                for (int j = 0; j < songs.size(); j++) {
                                    if (song.getSongID().equals(songs.get(j))) {
                                        duplicate = true;
                                        break;
                                    }
                                }
                                if (!duplicate) {
                                    songs.add(song.getSongID());
                                }
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

    /**
     * Gets the songs from a playlist to update the display
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Songs> getSongs(String playlistName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));
        JSONArray songArray = (JSONArray) parser.parse(new FileReader("music.json"));
        ArrayList<Songs> songObjs = new ArrayList<>(); // add to this arraylist
        JSONArray songs = new JSONArray();
        boolean found = false;

        // For each user:
        for (Object info : userArray) {
            JSONObject userInfoSearch = (JSONObject) info;

            Map userInfo = ((Map) userInfoSearch.get("info"));
            Iterator<Map.Entry> releaseItr = userInfo.entrySet().iterator();
            while (releaseItr.hasNext() && !found) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("username")){
                    if (username.equals(data.getValue())){
                        JSONArray playlistArray = (JSONArray) userInfoSearch.get("playlists");
                        // Try to iterate the JSONArray of playlists
                        for (int i = 0; i < playlistArray.size(); i++){
                            JSONObject tempPlaylist = (JSONObject) playlistArray.get(i);
                            // Iterate through songs in playlist
                            if (playlistName.equals(tempPlaylist.get("name").toString())){
                                // Display songs in playlist
                                songs = (JSONArray) tempPlaylist.get("songs");
                                found = true;
                                break;
                            }
                       }
                    }
                }
            }
            if (found) {
                break;
            }
        }

        SongInfo songInfo;
        // Go through songs array
        for (Object song : songs) {
            songInfo = new SongInfo();
            // Search json for ID match
            songObjs.addAll(songInfo.findSong(song.toString()));
        }

        return songObjs;
    }

    // No duplicate Playlist names


}
