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

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;

import java.util.*;

/**
 * A mutable builder class for building a record of type {@link iexport.itunes.Library}.
 * <p>
 * Most fields correspond to the fields of {@link iexport.itunes.Library, see {@link iexport.itunes.Library for the documentation.
 * <p>
 * Some additional fields will be needed for {@link iexport.parsing.LibraryParser}
 */
public class LibraryBuilder
{
    /**
     * A map that maps track ids to tracks.
     * <p>
     * Will be needed for parsing playlists.
     */
    private final Map<Integer, Track> tracksById = new HashMap<>();

    /**
     * The list of tracks in this library.
     */
    private final List<Track> tracks = new ArrayList<>();

    /**
     * The list of playlists in this library (that have already been constructed).
     */
    private final List<Playlist> playlists = new LinkedList<>();

    /**
     * The list of playlists that are top level, i.e. they have no parent playlist.
     */
    private final List<Playlist> playlistsAtTopLevel = new LinkedList<>();

    /**
     * The builders for the playlists in this library.
     * <p>
     * {@link iexport.parsing.LibraryParser} will convert these builders into actual playlists.
     */
    private final List<PlaylistBuilder> playlistBuilders = new ArrayList<>();

    /**
     * A map that takes a Playlist Persistent ID and returns the associated {@link PlaylistBuilder}
     */
    private final Map<String, PlaylistBuilder> playlistsBuildersByPersistentId = new HashMap<>();

    /**
     * A map that takes a Playlist Persistent ID and returns the associated {@link Playlist} once it has been constructed
     */
    private final Map<String, Playlist> playlistsByPersistentId = new HashMap<>();

    private Integer majorVersion;
    private Integer minorVersion;
    private Integer features;
    private String persistentId;
    private String applicationVersion;
    private String musicFolder;
    private Date date;

    public List<Track> getTracks ()
    {
        return tracks;
    }

    public Map<Integer, Track> getTracksById ()
    {
        return tracksById;
    }

    public void setMajorVersion (Integer majorVersion)
    {
        this.majorVersion = majorVersion;
    }

    public void setMinorVersion (Integer minorVersion)
    {
        this.minorVersion = minorVersion;
    }

    public void setFeatures (Integer features)
    {
        this.features = features;
    }

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

    /**
     * Build an immutable {@link Library}.
     *
     * @return the library
     */
    public Library build ()
    {
        return new Library(majorVersion,
                minorVersion,
                features,
                persistentId,
                applicationVersion,
                musicFolder,
                date,
                tracks,
                playlists,
                playlistsAtTopLevel);
    }

    public List<PlaylistBuilder> getPlaylistBuilders ()
    {
        return playlistBuilders;
    }

    public Map<String, Playlist> getPlaylistsByPersistentId ()
    {
        return playlistsByPersistentId;
    }

    public Map<String, PlaylistBuilder> getPlaylistsBuildersByPersistentId ()
    {
        return playlistsBuildersByPersistentId;
    }

    public List<Playlist> getPlaylistsAtTopLevel ()
    {
        return playlistsAtTopLevel;
    }

    public List<Playlist> getPlaylists ()
    {
        return playlists;
    }
}
