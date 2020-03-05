package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {


    private String email;
    private String username;
    private String password;
    private String dob;
    private int id;

    public User() {
        email = "";
        username = "";
        dob = "";
        id = 0;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String email, String username, String password, String month, String day, String year) {
        // Set fields
        this.email = email;
        this.username = username;
        this.password = password;
        this.dob = month + "." + day + "." + year;


    }

    public String addUserToDatabase(String email, String username, String password, String month, String day, String year) throws IOException, ParseException {
        // Creating JSONArray for the array of users
        JSONParser parser = new JSONParser();
        // Tanner's path: C://CECS 327//music-streaming-service//user.json
        JSONArray userArray = (JSONArray) parser.parse(new FileReader("user.json"));
        // Creating the JSON Object
        JSONObject userObject = new JSONObject();

        String dob = month + "." + day + "." + year;

        // Creating map for userInfo
        Map userInfo = new LinkedHashMap(4);

        // Inserting data into JSONObject
        userInfo.put("email", email);
        userInfo.put("username", username);
        userInfo.put("password", password);
        this.id = userArray.size() + 1;
        userInfo.put("id", this.id);
        userInfo.put("dob", dob);

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

        return "";
    }

    public int getID() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }
}
