/*
 * Copyright 2014-2022 Sebastian Muskalla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package iexport.parsing.builders;

import iexport.itunes.Playlist;
import iexport.itunes.Track;

import java.util.LinkedList;
import java.util.List;

/**
 * A mutable builder class for building records of type {@link iexport.itunes.Playlist}.
 * <p>
 * The fields correspond to the fields of {@link iexport.itunes.Playlist}, see {@link iexport.itunes.Playlist} for the documentation.
 */
public class PlaylistBuilder
{
    /**
     * The list of track ids of the tracks in the playlist.
     * <p>
     * This can be parsed from the .xml file.
     * <p>
     * {@link iexport.parsing.LibraryParser} will later be convert this into an actual list of {@link Track} objects.
     */
    private final List<Integer> trackIds;

    private Integer depth;
    private Integer playlistId;
    private Integer distinguishedKind;
    private String name;
    private String playlistPersistentId;
    private String parentPersistentId;
    private Boolean allItems;
    private Boolean master;
    private Boolean visible;
    private Boolean folder;
    private Boolean music;
    private Boolean movies;
    private Boolean tvShows;
    private Boolean audiobooks;
    private Playlist parent;

    public PlaylistBuilder ()
    {
        this.trackIds = new LinkedList<>();
    }

    public void addTrackId (Integer trackId)
    {
        trackIds.add(trackId);
    }

    public List<Integer> getTrackIds ()
    {
        return trackIds;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPlaylistPersistentId ()
    {
        return playlistPersistentId;
    }

    public void setPlaylistPersistentId (String playlistPersistentId)
    {
        this.playlistPersistentId = playlistPersistentId;
    }

    public void setMaster (Boolean master)
    {
        this.master = master;
    }

    public void setVisible (Boolean visible)
    {
        this.visible = visible;
    }

    public void setAllItems (Boolean allItems)
    {
        this.allItems = allItems;
    }

    public void setPlaylistId (Integer playlistId)
    {
        this.playlistId = playlistId;
    }

    public void setDistinguishedKind (Integer distinguishedKind)
    {
        this.distinguishedKind = distinguishedKind;
    }

    public void setFolder (Boolean folder)
    {
        this.folder = folder;
    }

    public void setMusic (Boolean music)
    {
        this.music = music;
    }

    public void setMovies (Boolean movies)
    {
        this.movies = movies;
    }

    public void setTvShows (Boolean tvShows)
    {
        this.tvShows = tvShows;
    }

    public void setAudibooks (Boolean audiobooks)
    {
        this.audiobooks = audiobooks;
    }

    public String getParentPersistentId ()
    {
        return parentPersistentId;
    }

    public void setParentPersistentId (String parentPersistentId)
    {
        this.parentPersistentId = parentPersistentId;
    }

    /**
     * Build an immutable {@link Playlist}.
     *
     * @return the playlist
     */
    public Playlist build ()
    {
        return new Playlist(playlistId,
                depth,
                distinguishedKind,
                name,
                playlistPersistentId,
                parentPersistentId,
                visible,
                allItems,
                folder,
                master,
                music,
                movies,
                tvShows,
                audiobooks,
                parent
        );
    }

    public void setDepth (Integer depth)
    {
        this.depth = depth;
    }

    public void setParent (Playlist parent)
    {
        this.parent = parent;
    }

    @Override
    public String toString ()
    {
        return "PlaylistBuilder{" +
                "#trackIds=" + trackIds.size() +
                ", depth=" + depth +
                ", playlistId=" + playlistId +
                ", distinguishedKind=" + distinguishedKind +
                ", name='" + name + '\'' +
                ", playlistPersistentId='" + playlistPersistentId + '\'' +
                ", parentPersistentId='" + parentPersistentId + '\'' +
                ", allItems=" + allItems +
                ", master=" + master +
                ", visible=" + visible +
                ", folder=" + folder +
                ", music=" + music +
                ", movies=" + movies +
                ", tvShows=" + tvShows +
                ", audiobooks=" + audiobooks +
                ", parent=" + parent +
                '}';
    }
}
