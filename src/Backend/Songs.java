package Backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Songs {

    private String songName;
    private Double songLength;
    private String artistName;
    private String albumName;
    private String songID;

    public Songs() {
        songName = "";
        songLength = 0.0;
        artistName = "";
        albumName = "";
        songID = "";
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        // C:\Users\Hunter\Documents\-Eclipse Workspace-\music-streaming-service\music.json
        // C://CECS 327//music-streaming-service//music.json
        JSONArray information = (JSONArray) parser.parse(new FileReader("C:\\Users\\Hunter\\Documents\\-Eclipse Workspace-\\music-streaming-service\\music.json"));

        for (Object info : information) {
            JSONObject songInfo = (JSONObject) info;

            Map release = ((Map) songInfo.get("release"));
            Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
            while (releaseItr.hasNext()) {
                Map.Entry data = releaseItr.next();
                if (data.getKey().equals("name")) {
                    System.out.println(data.getValue());
                }
            }
        }
    }

    // public String findSong(String songName) throws IOException, ParseException {
    //     JSONParser parser = new JSONParser();
    //     JSONArray information = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//music.json"));
    //     boolean match = false;

    //     for (Object info : information) {
    //         JSONObject entryInfo = (JSONObject) info;

    //         Map song = ((Map) entryInfo.get("song"));
    //         Iterator<Map.Entry> songItr = song.entrySet().iterator();
    //         while (songItr.hasNext()) {
    //             Map.Entry data = songItr.next();
    //             if (data.getKey().equals("title")) {
    //                 if ((data.getValue()).equals(songName)) {
    //                     match = true;
    //                     setSongName((String) data.getValue());
    //                 }
    //             }
    //             if (data.getKey().equals("duration")) {
    //                 if (match) {
    //                     setSongLength((String) data.getValue());
    //                 }
    //             }
    //             if (data.getKey().equals("id")) {
    //                 if (match) {
    //                     setSongID((String) data.getValue());
    //                 }
    //             }
    //         }

    //         Map artist = ((Map) entryInfo.get("artist"));
    //         Iterator<Map.Entry> artistItr = artist.entrySet().iterator();
    //         while (artistItr.hasNext()) {
    //             Map.Entry data = artistItr.next();
    //             if (data.getKey().equals("name")) {
    //                 // Check if artist names match when searching by artist
    //                 if (match) {
    //                     setArtistName((String) data.getValue());
    //                 }
    //             }
    //         }

    //         Map release = ((Map) entryInfo.get("release"));
    //         Iterator<Map.Entry> releaseItr = release.entrySet().iterator();
    //         while (releaseItr.hasNext()) {
    //             Map.Entry data = releaseItr.next();
    //             if (data.getKey().equals("name")) {
    //                 // Check if album name match when searching by album
    //                 if (match) {
    //                     setAlbumName((String) data.getValue());
    //                 }
    //             }
    //         }

    //         // Add object to arraylist
    //         // set match to false
    //     }

    //     // if arraylist is empty return n/a, else return array list
    //     return "N/A";
    // }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Double getSongLength() {
        return songLength;
    }

    public void setSongLength(Double songLength) {
        this.songLength = songLength;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }
}
