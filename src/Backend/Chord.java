package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;
import java.io.*;


public class Chord extends java.rmi.server.UnicastRemoteObject implements ChordMessageInterface
{
    public static final int M = 2;

    // SONG INFO FIELDS
    private ArrayList<Songs> songList;
    private Songs newSong;

    private String songName;
    private Double songLength;
    private String songID;
    private String artistName;
    private String albumName;
    private String termsName;

    Registry registry;    // rmi registry for lookup the remote objects.
    ChordMessageInterface successor;
    ChordMessageInterface predecessor;
    ChordMessageInterface[] finger;
    int nextFinger;
    long guid;   		// GUID (i)


    public Boolean isKeyInSemiCloseInterval(long key, long key1, long key2)
    {
       if (key1 < key2)
           return (key > key1 && key <= key2);
      else
          return (key > key1 || key <= key2);
    }

    public Boolean isKeyInOpenInterval(long key, long key1, long key2)
    {
      if (key1 < key2)
          return (key > key1 && key < key2);
      else
          return (key > key1 || key < key2);
    }


    public void put(long guidObject, InputStream stream) throws RemoteException {
      try {
          String fileName = "./"+guid+"/repository/" + guidObject;
          FileOutputStream output = new FileOutputStream(fileName);
          while (stream.available() > 0)
              output.write(stream.read());
          output.close();
      }
      catch (IOException e) {
          System.out.println(e);
      }
    }


    public InputStream get(long guidObject) throws RemoteException {
        FileStream file = null;
        try {
             file = new FileStream("./"+guid+"/repository/" + guidObject);
        } catch (IOException e)
        {
            throw(new RemoteException("File does not exists"));
        }
        return file;
    }

    public void delete(long guidObject) throws RemoteException {
        File file = new File("./"+guid+"/repository/" + guidObject);
        file.delete();
    }

    public long getId() throws RemoteException {
        return guid;
    }
    public boolean isAlive() throws RemoteException {
	    return true;
    }

    public ChordMessageInterface getPredecessor() throws RemoteException {
	    return predecessor;
    }

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

    public ChordMessageInterface closestPrecedingNode(long key) throws RemoteException {
        // todo
        if(key != guid) {
            int i = M - 1;
            while (i >= 0) {
                try{

                    if(isKeyInSemiCloseInterval(finger[i].getId(), guid, key)) {
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

    public void stabilize() {
      try {
          if (successor != null)
          {
              ChordMessageInterface x = successor.getPredecessor();

              if (x != null && x.getId() != this.getId() && isKeyInOpenInterval(x.getId(), this.getId(), successor.getId()))
              {
                  successor = x;
              }
              if (successor.getId() != getId())
              {
                  successor.notify(this);
              }
          }
      } catch(RemoteException | NullPointerException e1) {
          findingNextSuccessor();

      }
    }

    public void notify(ChordMessageInterface j) throws RemoteException {
         if (predecessor == null || (predecessor != null
                    && isKeyInOpenInterval(j.getId(), predecessor.getId(), guid)))
             predecessor = j;
            try {
                File folder = new File("./"+guid+"/repository/");
                File[] files = folder.listFiles();
                for (File file : files) {
                    long guidObject = Long.valueOf(file.getName());
                    if(guidObject < predecessor.getId() && predecessor.getId() < guid) {
                        predecessor.put(guidObject, new FileStream(file.getPath()));
                        file.delete();
                    }
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                //happens sometimes when a new file is added during foreach loop
            } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void fixFingers() {

        long id= guid;
        try {
            long nextId = this.getId() + 1<< (nextFinger+1);
            finger[nextFinger] = locateSuccessor(nextId);

            if (finger[nextFinger].getId() == guid)
                finger[nextFinger] = null;
            else
                nextFinger = (nextFinger + 1) % M;
        }
        catch(RemoteException | NullPointerException e){
            e.printStackTrace();
        }
    }

    public void checkPredecessor() {
      try {
          if (predecessor != null && !predecessor.isAlive())
              predecessor = null;
      }
      catch(RemoteException e)
      {
          predecessor = null;
//           e.printStackTrace();
      }
    }

    public Chord(int port, long guid) throws RemoteException {
        int j;
	    finger = new ChordMessageInterface[M];
        for (j=0;j<M; j++){
	       finger[j] = null;
     	}
        this.guid = guid;

        predecessor = null;
        successor = this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
	    @Override
	    public void run() {
            stabilize();
            fixFingers();
            checkPredecessor();
            }
        }, 500, 500);
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

    void Print()
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
                    if (finger != null)
                        System.out.println("Finger "+ i + " " + finger[i].getId());
                } catch(NullPointerException e)
                {
                    finger[i] = null;
                }
            }
        }
        catch(RemoteException e){
	       System.out.println("Cannot retrive id");
        }
    }

    public String findSong(long guid, String name) throws IOException, ParseException {
        System.out.println("BEING CALLED");
        byte[] buff = null;
        InputStream stream = get(guid);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buff = new byte[stream.available()];
        bos.write(buff, 0, stream.read(buff, 0, buff.length));
        bos.flush();
        stream.close();

        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new String(buff));  // search method in chord, create method search in dfs return string
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
