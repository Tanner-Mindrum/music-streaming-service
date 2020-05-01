package Backend; /**
 * Chord implements Chord P2P
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   03-3-2019
 */

import Backend.ChordMessageInterface;
import Backend.RemoteInputFileStream;
import Backend.Songs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.IOUtils;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;
import java.io.*;



/**
 * Chord extends from UnicastRemoteObject to support RMI.
 * It implements the ChordMessageInterface
 *
 */
public class Chord extends java.rmi.server.UnicastRemoteObject implements ChordMessageInterface
{
    // Numbers of fingers
    public static final int M = 2;

    // rmi registry for lookup the remote objects.
    Registry registry;
    // Successor peeer
    ChordMessageInterface successor;
    // Predecessor peeer
    ChordMessageInterface predecessor;
    // array of fingers
    ChordMessageInterface[] finger;
    // it is used to keep the fingers updated
    int nextFinger;
    // GUID
    long guid;
    // path prefix
    String prefix;

    private ArrayList<Songs> songList;
    private Songs newSong;

    private String songName;
    private Double songLength;
    private String songID;
    private String artistName;
    private String albumName;
    private String termsName;



    /**
     * Constructor of the Chord.
     * <p>
     * The function is used to debug if the ring is correctly formed
     * </p>
     *
     * @param  port it is the port where it listen. If the port is being used
     * for another process, it throw RemoteException.
     * @param guid the global unique id of the peer.
     */
    public Chord(int port, long guid) throws RemoteException {
        int j;
        // Initialize the variables
        prefix = "./" + guid + "/repository/";
        finger = new ChordMessageInterface[M];
        for (j=0;j<M; j++){
            finger[j] = null;
        }
        this.guid = guid;
        nextFinger = 0;
        predecessor = null;
        successor = this;
        Timer timer = new Timer();
        // It sets the timer to self stabilize the chord when nodes leave or join
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                stabilize();
                fixFingers();
                checkSuccessor();
                checkPredecessor();
            }
        }, 1000, 1000);   // Every second
        try{
            // create the registry and bind the name and object.
            System.out.println(guid + " is starting RMI at port="+port);
            registry = LocateRegistry.createRegistry( port );
            registry.rebind("Chord", this);
        }
        catch(RemoteException e){
            throw e;
        }
    }



    /**
     * return true if the key is in the open interval (key1, key2)
     */
    public Boolean isKeyInOpenInterval(long key, long key1, long key2)
    {
        if (key1 < key2)
            return (key > key1 && key < key2);
        else
            return (key > key1 || key < key2);
    }


    /**
     *return true if the key is in the semi-open interval (key1, key2]
     */
    public Boolean isKeyInSemiCloseInterval(long key, long key1, long key2)
    {
        return isKeyInOpenInterval(key, key1, key2) || key == key2;
    }

    /**
     *  put a file in the repository
     * @param guidObject  GUID of the object to store
     * @param stream  File to store
     */
    public void put(long guidObject, RemoteInputFileStream stream) throws RemoteException {
        stream.connect();
        try {
            String fileName = prefix + guidObject;
            FileOutputStream output = new FileOutputStream(fileName);
            while (stream.available() > 0)
                output.write(stream.read());
            output.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     *  put a text in guidObject
     * @param guidObject  GUID of the object to store
     * @param text text to store
     */
    public void put(long guidObject, String text) throws RemoteException {
        try {
            String fileName = prefix + guidObject;
            FileOutputStream output = new FileOutputStream(fileName);
            output.write(text.getBytes());
            output.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    /**
     * return guidObject
     * @param guidObject  GUID of the object to return
     */
    public RemoteInputFileStream get(long guidObject) throws RemoteException {
        RemoteInputFileStream file = null;
        try {
            file = new RemoteInputFileStream(prefix + guidObject);
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return file;
    }


    /**
     * return len bytes of guidObject from offset
     * @param guidObject  GUID of the object to return
     */
    public byte[] get(long guidObject, long offset, int len) throws RemoteException {
        byte[] buf = new byte[len];

        try {
            File file = new File(prefix + guidObject);
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.skip(offset);
            inputStream.read(buf);
            inputStream.close();
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return buf;
    }


    /**
     * deletes a file with guidObject from the repository
     * @param guidObject  GUID of the object to delete
     */
    public void delete(long guidObject) throws RemoteException {
        File file = new File(prefix + guidObject);
        file.delete();
    }

    /**
     * returns the id of the peer
     */
    public long getId() throws RemoteException {
        return guid;
    }

    /**
     * It is used to detect that the peer is still alive
     * <p>
     * return true
     */
    public boolean isAlive() throws RemoteException {
        return true;
    }

    /**
     * return the predecessor
     * <p>
     * return the Chord Interface of the predecessor
     */
    public ChordMessageInterface getPredecessor() throws RemoteException {
        return predecessor;
    }

    /**
     * locates the successor of key
     * <p>
     * @param key
     * return the Chord Interface of the successor of key
     */
    public ChordMessageInterface locateSuccessor(long key) throws RemoteException {
        if (key == guid)
            throw new IllegalArgumentException("Key must be distinct that  " + guid);
        if (successor.getId() != guid)
        {
            if (isKeyInSemiCloseInterval(key, guid, successor.getId()))
                return successor;
            ChordMessageInterface j = closestPrecedingNode(key);

            if (j == null)
                return null;
            return j.locateSuccessor(key);
        }
        return successor;
    }

    /**
     * Returns the closest preceding node for the key
     * <p>
     * @param key
     * return the Chord Interface of the closet preceding node
     */
    public ChordMessageInterface closestPrecedingNode(long key) throws RemoteException {
        if(key != guid) {
            int i = M - 1;
            while (i >= 0) {
                try{

                    // It verifies from the largest interval
                    if(finger[i] != null && isKeyInSemiCloseInterval(finger[i].getId(), guid, key)) {
                        if(finger[i].getId() != key)
                            return finger[i];
                        else {
                            return successor;
                        }
                    }
                }
                catch(Exception e)
                {
                    // Skip ;
                }
                i--;
            }
        }
        return successor;
    }

    /**
     * It joins the ring in the peer (ip,port). The peer must exist
     * <p>
     * @param ip of the peer
     * @param port of the peer
     */
    public void joinRing(String ip, int port)  throws RemoteException {
        try{
            System.out.println("Get Registry to joining ring");
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ChordMessageInterface chord = (ChordMessageInterface)(registry.lookup("Chord"));
            predecessor = null;
            successor = chord.locateSuccessor(this.getId());
            System.out.println("Joining ring");
        }
        catch(RemoteException | NotBoundException e){
            successor = this;
        }
    }

    /**
     * It joins the ring in the peer ChordMessageInterface. The peer must exist
     * <p>
     * @param s is the successor
     */
    public void joinRing(ChordMessageInterface s)  throws RemoteException {
        predecessor = null;
        successor = s;
    }

    /**
     *  If the successor fails, it tries to handle the failure using the
     * first finger available
     * <p>
     */
    public void findingNextSuccessor()
    {
        int i;
        successor = this;
        for (i = 0;  i< M; i++)
        {
            try
            {
                if (finger[i].isAlive())
                {
                    successor = finger[i];
                }
            }
            catch(RemoteException | NullPointerException e)
            {
                finger[i] = null;
            }
        }
    }

    /**
     * Stabilizes the chord
     * <p>
     * It verifies if the peer is in the right interval. If it is not
     * in the interval, it corrects the interval
     * This method executed by the timer.
     */
    public void stabilize() {
        try {
            if (successor != null)
            {
                ChordMessageInterface x = successor.getPredecessor();

                // It verifies if the predecessor is in the correct interval
                // x.getId() != this.getId() is used for for the trivial case
                // where only one peer exists
                if (x != null && x.getId() != this.getId() && isKeyInOpenInterval(x.getId(), this.getId(), successor.getId()))
                {
                    successor = x;
                }
                // The if statament is to handle the trivial case where only
                // one peer exists
                if (successor.getId() != getId())
                {
                    // We notified the successor that there It verifies if the predecessor is in the correct interval
                    successor.notify(this);
                }
            }
        } catch(RemoteException | NullPointerException e1) {
            findingNextSuccessor();

        }
    }

    /**
     * A node notifies that it has a new predecessor j. It also moves all the
     * files that the predecessor must handle
     * </p>
     * @param j the new predecessor
     */
    public void notify(ChordMessageInterface j) throws RemoteException {
        if (predecessor == null || (predecessor != null
                && isKeyInOpenInterval(j.getId(), predecessor.getId(), guid)))
            predecessor = j;
        try {
            File folder = new File(prefix);
            // It reads all the files in repository
            File[] files = folder.listFiles();

            for (File file : files) {
                try{
                    long guidObject = Long.valueOf(file.getName());
                    // If the guidObject is less than the new predecessor
                    if(!isKeyInSemiCloseInterval(guidObject, j.getId(), getId())) {
                        predecessor.put(guidObject, new RemoteInputFileStream(file.getPath(), true));

                    }
                }
                catch (NumberFormatException e)
                {
                    //skip;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //happens sometimes when a new file is added during the loop
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Fixes the fingers
     * <p>
     * Every time that is executed is fixing the Finger nextFinger.
     * This method executed by the timer.
     */
    public void fixFingers() {

        long id= guid;

        try {
            if (successor != null && successor.getId() != getId())
            {
                // The finger is at distance 2^(nextFinger) of this.getId()
                // We use a shift to the left to perform the operation
                long nextId = this.getId() + (1<< (nextFinger+1));
                finger[nextFinger] = locateSuccessor(nextId);

                // The same process cannot be a finger
                if (finger[nextFinger].getId() == guid)
                    finger[nextFinger] = null;
                else
                    nextFinger = (nextFinger + 1) % M;
            }
            else{
                if (successor != null)
                {
                    finger[nextFinger] = null;
                    nextFinger = (nextFinger + 1) % M;
                }
            }
        }
        catch(RemoteException | NullPointerException e){
            //System.out.println(e.message());
            e.printStackTrace();
        }
    }

    /**
     * It checks if the predecessor is still alive.
     * <p>
     * It checks if the predecessor is still alive. If the predecessor
     * is not present it sets its predecessor to null. This method executed
     * by the timer.
     */
    public void checkPredecessor() {
        try {
            if (predecessor != null && !predecessor.isAlive())
                predecessor = null;
        }
        catch(RemoteException e)
        {
            predecessor = null;
        }
    }

    public String getString(long guid) throws RemoteException {
        //byte[] buff = null;
        //        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        buff = new byte[stream.available()];
//        bos.write(buff, 0, stream.read(buff, 0, buff.length));
//        bos.flush();
//        stream.close();
        String str = "";
        try {
            File file = new File(prefix + guid);
            FileInputStream inputStream = new FileInputStream(file);
            // input stream to string
            str = inputStream.toString();
            inputStream.close();
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return str;
    }

    /**
     * It checks if the successor is still alive.
     * <p>
     * It checks if the successor is still alive. If the succesor
     * is not present it joins its successor. This method executed
     * by the timer.
     */
    public void checkSuccessor() {
        try {
            successor.isAlive();
        }
        catch(Exception e)
        {
            successor = null;
        }


        if (successor == null)
        {
            try{
                for (int i=1; i<M; i++)
                {
                    joinRing(finger[i]);
                    break;
                }
            }catch(Exception e)
            {

            }
        }
    }

    /**
     * Leaves the ring. It move all files to the successor
     */
    public void leave()  {
        if (predecessor == null) return;
        try
        {
            ChordMessageInterface suc = successor;
            if (suc != null && suc.getId() != getId())
            {
                successor = null;
                predecessor.joinRing(suc);
                predecessor = null;

                try {
                    File folder = new File(prefix);
                    // It reads all the files in repository
                    File[] files = folder.listFiles();

                    for (File file : files) {
                        try{
                            long guidObject = Long.valueOf(file.getName());
                            suc.put(guidObject, new RemoteInputFileStream(file.getPath()));
                            //file.delete();
                        }
                        catch (Exception   e)
                        {
                            //skip;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //happens sometimes when a new file is added during the loop
                }
            }
        }
        catch (RemoteException e)
        {

        }
    }

    /**
     * Prints the successor, predecessor and fingers if they are not null.
     * <p>
     * The function is used to debug if the ring is correctly formed
     * </p>
     */
    void print()
    {
        int i;
        try {
            if (successor != null)
                System.out.println("successor "+ successor.getId());
            if (predecessor != null)
                System.out.println("predecessor "+ predecessor.getId());
            for (i=0; i<M; i++)
            {
                try {
                    if (finger[i] != null)
                        System.out.println("Finger "+ i + " " + finger[i].getId());
                } catch(NullPointerException e)
                {
                    System.out.println("Cannot retrive id of the finger " + i);
                }
            }
        }
        catch(RemoteException e){
            System.out.println("Cannot retrive id of successor or predecessor");
        }
    }

    public String findSong(long guid, String name) throws IOException, RemoteException {

        String stream = getString(guid);

        JSONParser parse = new JSONParser();
        JSONArray information = null;  // search method in chord, create method search in dfs return string
        try {
            information = (JSONArray) parse.parse(stream);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean match = false;
        boolean idFound = false;
        songList = new ArrayList<>();

        //System.out.println("BEING CALLED");

        for (Object info : information) {
            JSONObject entryInfo = (JSONObject) info;
            newSong = new Songs();

            Map song = ((Map) entryInfo.get("song"));
            Iterator<Map.Entry> songItr = song.entrySet().iterator();
            while (songItr.hasNext()) {
                Map.Entry data = songItr.next();
                if (data.getKey().equals("title")) {
                    this.songName = (String) data.getValue();
                    if ((((String) data.getValue()).toLowerCase()).contains(name)) {
                        match = true;
                        newSong.setSongName((String) data.getValue());
                    }
                }
                if (data.getKey().equals("duration")) {
                    assert data.getValue() instanceof Double;
                    this.songLength = (Double) data.getValue();
                    if (match) {
                        newSong.setSongLength((Double) data.getValue());
                    }
                }
                if (data.getKey().equals("id")) {
                    assert data.getValue() instanceof String;
                    this.songID = (String) data.getValue();
                    if (match) {
                        newSong.setSongID((String) data.getValue());
                    }
                }
                if (data.getKey().equals("terms")) {
                    assert data.getValue() instanceof String;
                    this.songID = (String) data.getValue();
                    if (match) {
                        newSong.setTermsName((String) data.getValue());
                    }
                }
            }

            Map release = ((Map) entryInfo.get("release"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("name")) {
                    // Check if album name match when searching by album
                    this.albumName = (String) data.getValue();
                    if (match) {
                        newSong.setAlbumName((String) data.getValue());
                    }
                    else if ((((String) data.getValue()).toLowerCase()).contains(name)) {
                        newSong.setSongName(songName);
                        newSong.setSongLength(songLength);
                        newSong.setSongID(songID);
                        newSong.setArtistName(artistName);
                        newSong.setAlbumName(albumName);
                        songList.add(newSong);
                    }
                }
            }

            Map artist = ((Map) entryInfo.get("artist"));
            Iterator<Map.Entry> artistItr = artist.entrySet().iterator();
            while (artistItr.hasNext()) {
                Map.Entry data = artistItr.next();
                if (data.getKey().equals("name")) {
                    this.artistName = (String) data.getValue();
                    if (match) {
                        newSong.setArtistName((String) data.getValue());
                    }
                    else if ((((String) data.getValue()).toLowerCase()).contains(name)) {
                        newSong.setSongName(songName);
                        newSong.setSongLength(songLength);
                        newSong.setSongID(songID);
                        newSong.setArtistName(artistName);
                        newSong.setAlbumName(albumName);
                        songList.add(newSong);
                    }
                }
            }

            Map artist2 = ((Map) entryInfo.get("artist"));
            Iterator<Map.Entry> artistItr2 = artist2.entrySet().iterator();
            while (artistItr2.hasNext()) {
                Map.Entry data = artistItr2.next();
                if (data.getKey().equals("terms")) {
                    this.termsName = (String) data.getValue();
                    if (match) {
                        newSong.setTermsName((String) data.getValue());
                    }
                    else if ((((String) data.getValue()).toLowerCase()).contains(name)) {
                        newSong.setSongName(songName);
                        newSong.setSongLength(songLength);
                        newSong.setSongID(songID);
                        newSong.setArtistName(artistName);
                        newSong.setAlbumName(albumName);
                        newSong.setTermsName(termsName);
                        songList.add(newSong);
                    }
                }
            }

            Map idSearch = ((Map) entryInfo.get("song"));
            Iterator<Map.Entry> idItr = idSearch.entrySet().iterator();
            while (idItr.hasNext()) {
                Map.Entry data = idItr.next();
                if (data.getKey().equals("id")) {
                    this.termsName = (String) data.getValue();
                    if (match) {
                        newSong.setSongID((String) data.getValue());
                    }
                    else if ((((String) data.getValue()).toLowerCase()).contains(name.toLowerCase())) {
                        idFound = true;
                        newSong.setSongName(songName);
                        newSong.setSongLength(songLength);
                        newSong.setSongID(songID);
                        newSong.setArtistName(artistName);
                        newSong.setAlbumName(albumName);
                        newSong.setTermsName(termsName);
                        songList.add(newSong);
                    }
                }
            }

            // When we find a song match, we only display that one song, so we break
            if (match) {
                songList.add(newSong);
                break;
            }
            else if (idFound) { break; }
        }

        ArrayList<String> songInfo = new ArrayList<>();
        for (Songs s : songList) {
            songInfo.add(s.getSongName() + " | " + s.getArtistName() + " | " + s.getAlbumName() + ":" + s.getSongID());
        }
        String songNamesAsString = String.join(",, ", songInfo);
        System.out.println("SONG NAMES: " + songNamesAsString);


        //return songList;
        return songNamesAsString;
    }
}
