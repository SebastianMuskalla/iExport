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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A mutable builder class for building a record of type {@link iexport.itunes.Library}.
 * <p>
 * The fields correspond to the fields of {@link iexport.itunes.Library}, see {@link iexport.itunes.Library} for the documentation.
 */
public class LibraryBuilder
{
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

    private Integer majorVersion;
    private Integer minorVersion;
    private Integer features;

    private String persistentId;
    private String applicationVersion;
    private String musicFolder;

    private Date date;

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

    public List<Playlist> getPlaylists ()
    {
        return playlists;
    }

    public List<Track> getTracks ()
    {
        return tracks;
    }

    public List<Playlist> getPlaylistsAtTopLevel ()
    {
        return playlistsAtTopLevel;
    }
}
