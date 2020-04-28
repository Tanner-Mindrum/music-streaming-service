package Backend;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.xml.bind.DatatypeConverter;

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
        chord.locateSuccessor(guid).put(guid, stream);
    }

    public void mv(String oldName, String newName) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        for (JsonElement jsonEle : files) {
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if ((fileObj.get("name").toString().replaceAll("^\"|\"$", "")).equals(oldName))
                fileObj.addProperty("name", newName);
        }
        InputStream stream = new ByteArrayInputStream(metadata.toString().getBytes());
        writeMetaData(stream);
    }

    public String ls() throws IOException {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        String finalFileList = "";
        for (JsonElement jsonEle : files) {
            JsonObject jsonObj = (JsonObject) jsonEle;
            finalFileList += jsonObj.getAsJsonObject("file").get("name").toString() + "\n";
        }
        return finalFileList;
    }

    
    public void touch(String fileName) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");

        JsonObject fileObj = new JsonObject();
        JsonObject mainFileObj = new JsonObject();
        fileObj.addProperty("name", fileName);
        fileObj.addProperty("numberOfPages", 0);
        fileObj.addProperty("pageSize", 0);
        fileObj.addProperty("size", 0);
        JsonArray pages = new JsonArray();
        fileObj.add("pages", pages);
        mainFileObj.add("file", fileObj);
        files.add(mainFileObj);

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
    
    public InputStream read(String fileName, int pageNumber) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        InputStream stream = null;
        for (JsonElement jsonEle : files) {
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if (fileObj.get("name").toString().replaceAll("^\"|\"$", "").equals(fileName)) {
                JsonArray pageList = fileObj.get("pages").getAsJsonArray();
                if (pageNumber == -1) pageNumber = pageList.size() - 1;
                for (JsonElement jsonElem : pageList) {
                    JsonObject pageObj = ((JsonObject) jsonElem).getAsJsonObject("page");
                    if (pageObj.get("number").toString().equals(Integer.toString(pageNumber))) {
                        long guid = Long.parseLong(pageObj.get("guid").toString());
                        stream = chord.locateSuccessor(guid).get(guid);
                    }
                }
            }
        }
        return stream;
    }

    public InputStream tail(String fileName) throws Exception {
        return read(fileName, -1);
    }

    public InputStream head(String fileName) throws Exception {
        return read(fileName, 1);
    }

    // file name, file name to append
    public void append(String filename, byte[] data) throws Exception {
        // TODO: append data to fileName. If it is needed, add a new page.
        // Let guid be the last page in Metadata.filename
        //ChordMessageInterface peer = chord.locateSuccessor(guid);
        //peer.put(guid, data);
        // Write Metadata
    }
    
}
