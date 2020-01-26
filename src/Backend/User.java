package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

public class User {

    private String userName;

    public User() {
        userName = "";
    }

    public User(String name) {
        userName = name;
    }

    public static void main(String[] args) throws IOException {
        // Creating JSONArray for the array of users
        JSONArray jsonArray = new JSONArray();

        // Creating the JSON Object
        JSONObject userObject1 = new JSONObject();

        //Map map = new LinkedHashMap(); // Don't need this unless we have subfields

        // Inserting data into JSONObject
        userObject1.put("userName", "DynamicHunter");
        userObject1.put("email", "huntertigerdavis@gmail.com");
        userObject1.put("id", 1);
        userObject1.put("dob", "11/10/1998");

        // Adding the user object to the array
        jsonArray.add(userObject1);

        // Creating the JSON Object
        JSONObject userObject2 = new JSONObject();

        //Map map = new LinkedHashMap(); // Don't need this unless we have subfields

        // Inserting data into JSONObject
        userObject2.put("userName", "GingaT");
        userObject2.put("email", "tannermindrum@gmail.com");
        userObject2.put("id", 2);
        userObject2.put("dob", "12/20/1997");

        // Adding the user object to the array
        jsonArray.add(userObject2);

        // writing JSON to file
        PrintWriter fileWriter = new PrintWriter("user.json");
        fileWriter.write(jsonArray.toJSONString());

        fileWriter.flush();
        fileWriter.close();


        // User 2 to test the Array of JSON objects


    }

}
