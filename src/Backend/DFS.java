package Backend;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.IOUtils;

import javax.xml.bind.DatatypeConverter;

/* JSON Format

 {
    "metadata" :
    {
        file :  ~~ARRAY~~ MUSIC.json
        {
            name  : "music"
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
            JSONArray metadataArray  = new JSONArray();
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
        chord.print();
    }

    /**
     * Read a Metadata JSON object
     * @return JsonReader object (streaming JSON parser) which reads in Metadata as stream of tokens
     * @throws IOException - Communication errors, I/O errors
     */
    public JsonReader readMetaData() throws IOException {
        long guid = md5("Metadata");
        RemoteInputFileStream r = chord.locateSuccessor(guid).get(guid);
        r.connect();
        chord.print();
        return new JsonReader(new InputStreamReader(r, StandardCharsets.UTF_8));
    }

    /**
     * Write
     * @param
     * @throws IOException
     */
    public void writeMetaData(String metadata) throws IOException {
        long guid = md5("Metadata");
        chord.locateSuccessor(guid).put(guid, metadata);
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
        writeMetaData(metadata.toString());
    }

    public String ls() throws IOException {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        String finalFileList = "";
        for (JsonElement jsonEle : files) {
            JsonObject jsonObj = (JsonObject) jsonEle;
            finalFileList += jsonObj.getAsJsonObject("file").get("name").toString().replaceAll("^\"|\"$", "") + "\n";
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
        fileObj.addProperty("size", 0);
        JsonArray pages = new JsonArray();
        fileObj.add("pages", pages);
        mainFileObj.add("file", fileObj);
        files.add(mainFileObj);
        writeMetaData(metadata.toString());
    }

    public void delete(String fileName) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        int indx = 0;
        int count = 0;
        for (JsonElement jsonEle : files) {
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if ((fileObj.get("name").toString().replaceAll("^\"|\"$", "")).equals(fileName)) {
                indx = count;
                JsonArray pgs = (JsonArray) fileObj.get("pages");
                for (JsonElement jsonElem : pgs) {
                    JsonObject pageObj = ((JsonObject) jsonElem).getAsJsonObject("page");
                    chord.locateSuccessor(Long.parseLong(pageObj.get("guid").toString()))
                            .delete(Long.parseLong(pageObj.get("guid").toString()));
                }
                break;
            }
            count++;
        }
        if (count > -1) {
            files.remove(count);
             writeMetaData(metadata.toString());
        }
    }
    
    public byte[] read(String fileName, int pageNumber) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        RemoteInputFileStream stream = null;
        byte[] buff = null;
        for (JsonElement jsonEle : files) {
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if (fileObj.get("name").toString().replaceAll("^\"|\"$", "").equals(fileName)) {
                System.out.println("Reading file: " + fileObj.get("name"));
                JsonArray pageList = fileObj.get("pages").getAsJsonArray();
                if (pageNumber == -1) pageNumber = pageList.size();
                for (JsonElement jsonElem : pageList) {
                    JsonObject pageObj = ((JsonObject) jsonElem).getAsJsonObject("page");
                    if (pageObj.get("number").toString().equals(Integer.toString(pageNumber))) {
                        System.out.println("Reading page: " + pageObj.get("guid"));
                        long guid = Long.parseLong(pageObj.get("guid").toString());
                        stream = chord.locateSuccessor(guid).get(guid);
                        stream.connect();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        buff = new byte[stream.available()];
                        bos.write(buff, 0, stream.read(buff, 0, buff.length));
                        bos.flush();
                        stream.close();
                        break;
                    }
                }
            }
        }
        return buff;
    }

    public byte[] tail(String fileName) throws Exception {
        return read(fileName, -1);
    }

    public byte[] head(String fileName) throws Exception {
        return read(fileName, 1);
    }

    public void append(String filename, String fileToAppend) throws Exception {
        JsonObject metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        File f = new File(fileToAppend);
        Random rand = new Random();
        for (JsonElement jsonEle : files) {
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if ((fileObj.get("name").toString().replaceAll("^\"|\"$", "")).equals(filename)) {
                JsonArray pgs = (JsonArray) fileObj.get("pages");
                JsonObject pgObj = new JsonObject();
                JsonObject mainPgObj = new JsonObject();
                pgObj.addProperty("number", pgs.size()+1);
                long guid = md5(filename + rand.nextInt(1000000000));
                chord.locateSuccessor(guid).put(guid, new RemoteInputFileStream(fileToAppend, false));
                pgObj.addProperty("guid", guid);
                pgObj.addProperty("size", f.length()); // change later
                mainPgObj.add("page", pgObj);
                pgs.add(mainPgObj);
                int numOfPages = Integer.parseInt(fileObj.get("numberOfPages").toString());
                long size = Long.parseLong(fileObj.get("size").toString());
                numOfPages++;
                size += f.length();
                fileObj.addProperty("numberOfPages", numOfPages);
                fileObj.addProperty("size", size);
            }
        }
        writeMetaData(metadata.toString());
    }

    public String search(String name) throws InterruptedException {
        JsonObject metadata = null;
        String finalSongs = "";
        try {
            metadata = JsonParser.parseReader(readMetaData()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonArray files = (JsonArray) metadata.get("metadata");
        JsonObject fileObj = null;
        for (JsonElement jsonEle : files) {
            System.out.println("IN LOOP");
            fileObj = ((JsonObject) jsonEle).getAsJsonObject("file");
            if ((fileObj.get("name").toString().replaceAll("^\"|\"$", "")).equals("music")) {
                JsonArray pageList = fileObj.get("pages").getAsJsonArray();
                String[] s = new String[pageList.size()];
                Thread[] threads = new Thread[pageList.size()];
                for (int i = 0; i < pageList.size(); i++) {
                //for (JsonElement jsonElem : pageList) {
                    int finalI = i;
                    threads[i] = new Thread() {
                        public void run() {
                            System.out.println("IN THREAD");
                            JsonObject pageObj = ((JsonObject) pageList.get(finalI)).getAsJsonObject("page");
                            try {
                                long guid = Long.parseLong(pageObj.get("guid").toString());
                                s[finalI] = chord.locateSuccessor(guid).findSong(guid, name);
                            } catch (IOException | java.text.ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    };
                    threads[i].start();
                }
                for (int j =0; j < s.length; j++) {
                    threads[j].join();
                    finalSongs += s[j];
                }
            }
        }
        return finalSongs;
    }
}
