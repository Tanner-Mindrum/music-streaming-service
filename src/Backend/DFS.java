package Backend;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/* JSON Format

 {
    "metadata" :
    {
        file :  ~~ARRAY~~ MUSIC.json
        {
            name  : "File1"
            numberOfPages : "3"
            pageSize : "1024"
            size : "2291"
            page :  ~~ARRAY~~  MUSIC1.json []
            {
                number : "1"
                guid   : "22412"
                size   : "1024"
            }
            page :
            {
                number : "2" // []
                guid   : "46312"
                size   : "1024"
            }
            page :
            {
                number : "3"
                guid   : "93719"
                size   : "243"
            }
        }
    }
}
 */


public class DFS {
    int port;
    Chord  chord;
    
    private long md5(String objectName) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e) {
                e.printStackTrace();
                
        }
        return 0;
    }

    public DFS(int port) throws Exception {
        long guid = md5("" + port);
        this.port = port;
        this.chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
        File metadataFile = new File(guid+"/repository/"+md5("Metadata"));
        if (!metadataFile.exists()) {  // Check if metadata exists
            // Create new metadata
            JSONArray metadataArray = new JSONArray();
            JSONObject metadata = new JSONObject();
            metadata.put("metadata", metadataArray);
            PrintWriter pw = new PrintWriter(metadataFile);
            pw.write(metadata.toJSONString());
            pw.flush();
            pw.close();
        }
    }
    
    public void join(String Ip, int port) throws Exception {
        chord.joinRing(Ip, port);
        chord.Print();
    }

    /**
     * Read a Metadata JSON object
     * @return JsonReader object (streaming JSON parser) which reads in Metadata as stream of tokens
     * @throws IOException - Communication errors, I/O errors
     */
    public JsonReader readMetaData() throws IOException {
        long guid = md5("Metadata");
        return new JsonReader(new InputStreamReader(chord.locateSuccessor(guid).get(guid), StandardCharsets.UTF_8));
    }

    /**
     * Write
     * @param stream
     * @throws IOException
     */
    public void writeMetaData(InputStream stream) throws IOException {
        long guid = md5("Metadata");
        System.out.println(guid);
        chord.locateSuccessor(guid).put(guid, stream);
    }

    public void mv(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        // read file
        // Write Metadata
    }


    public String ls() throws IOException {
        String listOfFiles = "";
       // TODO: returns all the files in the Metadata
        JsonObject obj = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        return listOfFiles;
    }

    
    public void touch(String fileName) throws Exception {
         // TODO: Create the file fileName by adding a new entry to the Metadata
        // Write Metadata
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");

        JsonObject fileObj = new JsonObject();
        fileObj.addProperty("name", fileName);
        fileObj.addProperty("numberOfPages", 0);
        fileObj.addProperty("pageSize", 0);
        fileObj.addProperty("size", 0);
        JsonArray pages = new JsonArray();
        fileObj.add("page", pages);
        files.add(fileObj);
        InputStream stream = new ByteArrayInputStream(metadata.toString().getBytes());
        writeMetaData(stream);

    }
    public void delete(String fileName) throws Exception {
        // TODO: remove all the pages in the entry fileName in the Metadata and then the entry
        // for each page in Metadata.filename
        //     peer = chord.locateSuccessor(page.guid);
        //     peer.delete(page.guid)
        // delete Metadata.filename
        // Write Metadata

        
    }
    
    public Byte[] read(String fileName, int pageNumber) throws Exception {
        // TODO: read pageNumber from fileName
        return null;
    }

    public Byte[] tail(String fileName) throws Exception
    {
        // TODO: return the last page of the fileName
        return null;
    }
    public Byte[] head(String fileName) throws Exception
    {
        // TODO: return the first page of the fileName
        return null;
    }
    public void append(String filename, Byte[] data) throws Exception
    {
        // TODO: append data to fileName. If it is needed, add a new page.
        // Let guid be the last page in Metadata.filename
        //ChordMessageInterface peer = chord.locateSuccessor(guid);
        //peer.put(guid, data);
        // Write Metadata
    }
    
}
