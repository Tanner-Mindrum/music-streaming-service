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

    public SongInfo() {
        newSong = null;
        songList = new ArrayList<Songs>();
    }

    /**
     * Search for a song by matching the given parameter with a certain attribute of the song
     * @param name - The search parameter a user wants to find songs by
     * @return - A list of all found songs
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Songs> findSong(String name) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("music.json"));
        boolean match = false;
        boolean idFound = false;

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
                    else if ((((String) data.getValue()).toLowerCase()).equals(name)) {
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
                    else if ((((String) data.getValue()).toLowerCase()).equals(name)) {
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
                    else if ((((String) data.getValue()).toLowerCase()).equals(name)) {
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
                    else if ((((String) data.getValue()).toLowerCase()).equals(name.toLowerCase())) {
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
        return songList;
    }

    public String getSongID() {
        return songID;
    }

    public ArrayList<Songs> findSongByID(String name) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("music.json"));

        for (Object info : information) {
            JSONObject entryInfo = (JSONObject) info;
            newSong = new Songs();

            Map idSearch = ((Map) entryInfo.get("song"));
            Iterator<Map.Entry> idItr = idSearch.entrySet().iterator();
            while (idItr.hasNext()) {
                Map.Entry data = idItr.next();
                if (data.getKey().equals("id")) {
                    this.termsName = (String) data.getValue();
                    if ((((String) data.getValue()).toLowerCase()).equals(name.toLowerCase())) {
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
        }
        return songList;
    }
}