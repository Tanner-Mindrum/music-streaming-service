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

    public SongInfo() {
        newSong = new Songs();
        songList = new ArrayList<Songs>();

    }
    public String findSong(String songName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//music.json"));
        boolean match = false;

        for (Object info : information) {
            JSONObject entryInfo = (JSONObject) info;

            Map song = ((Map) entryInfo.get("song"));
            Iterator<Map.Entry> songItr = song.entrySet().iterator();
            while (songItr.hasNext()) {
                Map.Entry data = songItr.next();
                if (data.getKey().equals("title")) {
                    if ((data.getValue()).equals(songName)) {
                        match = true;
                        newSong.setSongName((String) data.getValue());
                    }
                }
                if (data.getKey().equals("duration")) {
                    if (match) {
                        newSong.setSongLength((String) data.getValue());
                    }
                }
                if (data.getKey().equals("id")) {
                    if (match) {
                        newSong.setSongID((String) data.getValue());
                    }
                }
            }

            Map artist = ((Map) entryInfo.get("artist"));
            Iterator<Map.Entry> artistItr = artist.entrySet().iterator();
            while (artistItr.hasNext()) {
                Map.Entry data = artistItr.next();
                if (data.getKey().equals("name")) {
                    // Check if artist names match when searching by artist
                    if (match) {
                        newSong.setArtistName((String) data.getValue());
                    }
                }
            }

            Map release = ((Map) entryInfo.get("release"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("name")) {
                    // Check if album name match when searching by album
                    if (match) {
                        newSong.setAlbumName((String) data.getValue());
                    }
                }
            }
            //This part needs to be modified
            if (match) {
                songList.add(newSong);
            }
            match = false;
            // Add object to arraylist
            // set match to false
        }

        // if arraylist is empty return n/a, else return array list
        return "N/A";
    }

}