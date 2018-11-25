package iexport.domain.factories;

import iexport.domain.Track;

import javax.annotation.Nullable;
import java.util.Date;

public class ITunesLibraryFactory
{

    @Nullable
    private String applicationVersion;

    @Nullable
    private String persistentId;

    @Nullable
    private Date date;

    @Nullable
    private String musicFolder;

//    private final HashMap<String, Track> tracks = new HashMap<String, Track>();
//    private final HashMap<String, Playlist> all_playlists = new HashMap<>();

//    private TreeSet<Playlist> toplevel_playlists = new TreeSet<>(new PlaylistComparatorLeavesFirst()); // TODO

    public void setApplicationVersion (String applicationVersion)
    {
        this.applicationVersion = applicationVersion;
    }

    public void setPersistentId (String persistentId)
    {
        this.persistentId = persistentId;
    }

    public void setDate (Date date)
    {
        this.date = date;
    }

    public void setMusicFolder (String musicFolder)
    {
        this.musicFolder = musicFolder;
    }

    public void addTrack (Track track)
    {
    }
}
