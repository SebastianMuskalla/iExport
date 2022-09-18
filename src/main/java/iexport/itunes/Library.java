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

package iexport.itunes;

import java.util.Date;
import java.util.List;

/**
 * A record class for representing the iTunes library.
 *
 * @param majorVersion        {@code <key>Major Version</key><integer>1</integer>}
 * @param minorVersion        {@code <<key>Minor Version</key><integer>1</integer>}
 * @param features            {@code <key>Features</key><integer>5</integer>}
 * @param persistentId        the iTunes-internal persistent id for this playlist
 *                            <p>
 *                            {@code <key>Library Persistent ID</key><string>B2D94F38C25993B6</string>}
 * @param applicationVersion  {@code <key>Application Version</key><string>12.12.4.1</string>}
 * @param musicFolder         the location of the music folder managed by iTunes
 *                            <p>
 *                            {@code <key>Music Folder</key><string>file://localhost/C:/Users/REDACTED/Music/iTunes/iTunes%20Media/</string>}
 * @param date                {@code <key>Date</key><date>2022-09-11T01:20:10Z</date>}
 * @param tracks              the tracks contained in this playlist.
 * @param playlists           all playlists contained in this library.
 *                            <p>
 *                            This includes all child playlists.
 * @param playlistsAtTopLevel the top-level playlists in this library
 */
public record Library
        (
                Integer majorVersion,
                Integer minorVersion,
                Integer features,

                String persistentId,
                String applicationVersion,
                String musicFolder,

                Date date,

                List<Track> tracks,

                List<Playlist> playlists,
                List<Playlist> playlistsAtTopLevel
        )
{
    
    @Override
    public String toString ()
    {
        return "Library {" +
                "majorVersion=" + majorVersion +
                ", minorVersion=" + minorVersion +
                ", features=" + features +
                ", persistentId='" + persistentId + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", musicFolder='" + musicFolder + '\'' +
                ", date=" + date +
                ", #tracks=" + tracks.size() +
                ", #playlists=" + playlists.size() +
                ", #playlistsAtTopLevel=" + playlistsAtTopLevel.size() +
                '}';
    }
}

