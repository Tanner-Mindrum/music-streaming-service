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
    private String termsName;

    public Songs() {
        songName = "";
        songLength = 0.0;
        artistName = "";
        albumName = "";
        songID = "";
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray information = (JSONArray) parser.parse(new FileReader("C:\\CECS 327\\music-streaming-service\\music.json"));

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

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }
}
