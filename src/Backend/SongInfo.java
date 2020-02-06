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

    public SongInfo() {
        newSong = null;
        songList = new ArrayList<Songs>();
    }

    public ArrayList<Songs> findSong(String name) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("C://CECS 327//music-streaming-service//music.json"));
        boolean match = false;

        for (Object info : information) {
            JSONObject entryInfo = (JSONObject) info;
            newSong = new Songs();

            Map song = ((Map) entryInfo.get("song"));
            Iterator<Map.Entry> songItr = song.entrySet().iterator();
            while (songItr.hasNext()) {
                Map.Entry data = songItr.next();
                if (data.getKey().equals("title")) {
                    this.songName = (String) data.getValue();
                    if ((((String) data.getValue()).toLowerCase()).equals(name)) {
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
                    else if ((((String) data.getValue()).toLowerCase()).equals(name)) {
                        newSong.setSongName(songName);
                        newSong.setSongLength(songLength);
                        newSong.setSongID(songID);
                        newSong.setArtistName(artistName);
                        newSong.setAlbumName(albumName);
                        songList.add(newSong);
                    }
                    // Check if artist names match when searching by artist
                }
            }

            // When we find a song match, we only display that one song, so we break
            if (match) {
                songList.add(newSong);
                break;
            }
        }
        return songList;
    }

    public String getSongID() {
        return songID;
    }

}