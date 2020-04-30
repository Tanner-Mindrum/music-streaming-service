package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SongInfo {

    private ArrayList<Songs> songList;
    private Songs newSong;

    private String songName;
    private Double songLength;
    private String songID;
    private String artistName;
    private String albumName;
    private String termsName;

    private DFS myDfs;

    public SongInfo() {
        newSong = null;
        songList = new ArrayList<Songs>();
    }

    public SongInfo(DFS dfs) {
        newSong = null;
        songList = new ArrayList<Songs>();
        myDfs = dfs;
    }

    /**
     * Search for a song by matching the given parameter with a certain attribute of the song
     * @param name - The search parameter a user wants to find songs by
     * @return - A list of all found songs
     * @throws IOException
     * @throws ParseException
     */
    public String findSong(String name) throws IOException, ParseException, InterruptedException {
        return myDfs.search(name);
    }

    public String getSongID() {
        return songID;
    }
}