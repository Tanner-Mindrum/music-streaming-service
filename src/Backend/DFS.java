package Backend;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.stream.JsonReader;

/* JSON Format

 {
    "metadata" :
    {
        file :
        {
            name  : "File1"
            numberOfPages : "3"
            pageSize : "1024"
            size : "2291"
            page :
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


public class DFS
{
    int port;
    Chord  chord;
    
    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
                e.printStackTrace();
                
        }
        return 0;
    }
    
    
    
    public DFS(int port) throws Exception {
        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
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

    public void mv(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        // read file
        // Write Metadata
    }

    
    public String ls() throws IOException {
        String listOfFiles = "";
       // TODO: returns all the files in the Metadata
       // JsonParser jp = readMetaData();
        JsonReader reader = readMetaData();
        System.out.println(reader);
        return listOfFiles;
    }

    
    public void touch(String fileName) throws Exception
    {
         // TODO: Create the file fileName by adding a new entry to the Metadata
        // Write Metadata

    }
    public void delete(String fileName) throws Exception
    {
        // TODO: remove all the pages in the entry fileName in the Metadata and then the entry
        // for each page in Metadata.filename
        //     peer = chord.locateSuccessor(page.guid);
        //     peer.delete(page.guid)
        // delete Metadata.filename
        // Write Metadata

        
    }
    
    public Byte[] read(String fileName, int pageNumber) throws Exception
    {
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
