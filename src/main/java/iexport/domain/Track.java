package iexport.domain;

public class Track
{

    private int id = -1;
    private String name = null;
    private String artist = null;
    private String track_id = null;
    private String album = null;
    private int total_time = -1;
    private String location = null;
    private long size = 0;
    private Integer year = null;
    private String album_artist = null;
    private Integer track_number = null;

    /**
     * Creates a new instance of TrackType
     */
    public Track ()
    {
    }

    public Track (Track obj)
    {
        setId(obj.getId());
        setName(obj.getName());
        setArtist(obj.getArtist());
        setTotalTime(obj.getTotalTime());
        setFileLocation(obj.getFileLocation());
        setTrackId(obj.getTrackId());
        setAlbum(obj.getAlbum());
        setSize(obj.getSize());
        setYear(obj.getYear());
        setAlbumArtist(obj.getAlbumArtist());
        setTrackNumber(obj.getTrackNumber());
    }

    public int getId ()
    {
        return id;
    }

    public void setId (int value)
    {
        id = value;
    }

    @Override
    public String toString ()
    {
        return "Track{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", track_id='" + track_id + '\'' +
                ", album='" + album + '\'' +
                //", total_time=" + total_time +
                //", location='" + location + '\'' +
                //", size=" + size +
                ", year=" + year +
                ", album_artist='" + album_artist + '\'' +
                ", track_number=" + track_number +
                '}';
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String value)
    {
        name = value;
    }

    public String getArtist ()
    {
        return artist;
    }

    public void setArtist (String value)
    {
        artist = value;
    }

    public String getTrackId ()
    {
        return track_id;
    }

    public void setTrackId (String value)
    {
        track_id = value;
    }

    public String getAlbum ()
    {
        return album;
    }

    public void setAlbum (String value)
    {
        album = value;
    }

    public long getSize ()
    {
        return size;
    }

    public void setSize (long value)
    {
        size = value;
    }

    public int getTotalTime ()
    {
        return total_time;
    }

    public void setTotalTime (int value)
    {
        total_time = value;
    }

    public String getFileLocation ()
    {
        return location;
    }

    public void setFileLocation (String value)
    {
        location = value;
    }

    public Integer getYear ()
    {
        return year;
    }

    public void setYear (Integer year)
    {
        this.year = year;
    }

    public String getAlbumArtist ()
    {
        return album_artist;
    }

    public void setAlbumArtist (String album_artist)
    {
        this.album_artist = album_artist;
    }

    public Integer getTrackNumber ()
    {
        return track_number;
    }

    public void setTrackNumber (Integer track_number)
    {
        this.track_number = track_number;
    }
}
