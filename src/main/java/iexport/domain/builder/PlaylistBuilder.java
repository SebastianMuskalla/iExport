package iexport.domain.builder;

import javax.annotation.CheckForNull;
import java.util.LinkedList;
import java.util.List;

public class PlaylistBuilder
{
    private final List<Integer> tracks;

    @CheckForNull
    private String name;
    @CheckForNull
    private String playlistPersistentId;
    @CheckForNull
    private Boolean master;
    @CheckForNull
    private Boolean visible;
    @CheckForNull
    private Boolean allItems;
    @CheckForNull
    private Integer playlistId;
    @CheckForNull
    private Integer distinguishedKind;
    @CheckForNull
    private Boolean folder;
    @CheckForNull
    private Boolean music;
    @CheckForNull
    private Boolean movies;
    @CheckForNull
    private Boolean tvShows;
    @CheckForNull
    private Boolean audibooks;
    @CheckForNull
    private String parentPersistentId;


    public PlaylistBuilder ()
    {
        this.tracks = new LinkedList<>();
    }

    public void addTrackWithId (Integer trackId)
    {
        tracks.add(trackId);
    }

    public List<Integer> getTracks ()
    {
        return tracks;
    }

    @CheckForNull
    public String getName ()
    {
        return name;
    }

    public void setName (@CheckForNull String name)
    {
        this.name = name;
    }

    @CheckForNull
    public String getPlaylistPersistentId ()
    {
        return playlistPersistentId;
    }

    public void setPlaylistPersistentId (@CheckForNull String playlistPersistentId)
    {
        this.playlistPersistentId = playlistPersistentId;
    }

    @CheckForNull
    public Boolean getMaster ()
    {
        return master;
    }

    public void setMaster (@CheckForNull Boolean master)
    {
        this.master = master;
    }

    @CheckForNull
    public Boolean getVisible ()
    {
        return visible;
    }

    public void setVisible (@CheckForNull Boolean visible)
    {
        this.visible = visible;
    }

    @CheckForNull
    public Boolean getAllItems ()
    {
        return allItems;
    }

    public void setAllItems (@CheckForNull Boolean allItems)
    {
        this.allItems = allItems;
    }

    @CheckForNull
    public Integer getPlaylistId ()
    {
        return playlistId;
    }

    public void setPlaylistId (@CheckForNull Integer playlistId)
    {
        this.playlistId = playlistId;
    }

    @CheckForNull
    public Integer getDistinguishedKind ()
    {
        return distinguishedKind;
    }

    public void setDistinguishedKind (@CheckForNull Integer distinguishedKind)
    {
        this.distinguishedKind = distinguishedKind;
    }

    @CheckForNull
    public Boolean getFolder ()
    {
        return folder;
    }

    public void setFolder (@CheckForNull Boolean folder)
    {
        this.folder = folder;
    }

    @CheckForNull
    public Boolean getMusic ()
    {
        return music;
    }

    public void setMusic (@CheckForNull Boolean music)
    {
        this.music = music;
    }

    @CheckForNull
    public Boolean getMovies ()
    {
        return movies;
    }

    public void setMovies (@CheckForNull Boolean movies)
    {
        this.movies = movies;
    }

    @CheckForNull
    public Boolean getTvShows ()
    {
        return tvShows;
    }

    public void setTvShows (@CheckForNull Boolean tvShows)
    {
        this.tvShows = tvShows;
    }

    @CheckForNull
    public Boolean getAudibooks ()
    {
        return audibooks;
    }

    public void setAudibooks (@CheckForNull Boolean audibooks)
    {
        this.audibooks = audibooks;
    }

    @CheckForNull
    public String getParentPersistentId ()
    {
        return parentPersistentId;
    }

    public void setParentPersistentId (@CheckForNull String parentPersistentId)
    {
        this.parentPersistentId = parentPersistentId;
    }

    @Override
    public String toString ()
    {
        return "PlaylistBuilder{" +
//                "tracks=" + tracks +
                ", name='" + name + '\'' +
                ", playlistPersistentId='" + playlistPersistentId + '\'' +
                ", master=" + master +
                ", visible=" + visible +
                ", allItems=" + allItems +
                ", playlistId=" + playlistId +
                ", distinguishedKind=" + distinguishedKind +
                ", folder=" + folder +
                ", music=" + music +
                ", movies=" + movies +
                ", tvShows=" + tvShows +
                ", audibooks=" + audibooks +
                ", parentPersistentId='" + parentPersistentId + '\'' +
                '}';
    }
}
