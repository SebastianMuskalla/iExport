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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A record class for representing playlists.
 * <p>
 * Example from iTunes Music Library.xml:
 * {@code
 * <dict>
 * <key>Playlist ID</key><integer>53074</integer>
 * <key>Playlist Persistent ID</key><string>1CBCD3C1D85440D2</string>
 * <key>All Items</key><true/>
 * <key>Name</key><string>VOICE</string>
 * <key>Playlist Items</key>
 * <array>
 * <dict>
 * <key>Track ID</key><integer>15197</integer>
 * </dict>
 * <dict>
 * <key>Track ID</key><integer>15199</integer>
 * </dict>
 * </array>
 * </dict>
 * }
 *
 * @param playlistId           the iTunes-internal playlist ID
 *                             <p>
 *                             we use this field for {@link #equals(Object)} and {@link #hashCode()}
 * @param depth                the depth of the playlist,
 *                             i.e. this value is 0 if the playlist has no parent or it is its parent's depth plus one.
 * @param distinguishedKind    ???
 * @param name                 the name of the playlist
 * @param playlistPersistentId the iTunes-internal persistent playlist ID
 * @param parentPersistentId   the persistent ID of the parent playlist
 * @param visible              ???
 * @param allItems             ???
 * @param folder               is this playlist a folder?
 * @param master               is this the master playlist?
 * @param music                is this the music play list?
 * @param movies               is this the movies play list?
 * @param tvShows              is this the TV shows play list?
 * @param audiobooks           is this the audiobooks play list?
 * @param parent               the parent playlist
 * @param ancestry             the list of ancestors of this playlist, i.e.
 *                             the first entry of ancestry is a top-level playlist with no parent,
 *                             the penultimate entry of ancestry is the parent of this playlist,
 *                             the last entry of ancestry is this playlist itself
 * @param tracks               the list of tracks contained in the playlist
 * @param children             the list of child playlists
 */
public record Playlist
        (
                Integer playlistId,
                int depth,
                Integer distinguishedKind,

                String name,
                String playlistPersistentId,
                String parentPersistentId,

                Boolean visible,
                Boolean allItems,
                Boolean folder,
                Boolean master,
                Boolean music,
                Boolean movies,
                Boolean tvShows,
                Boolean audiobooks,

                Playlist parent,

                List<Playlist> ancestry,

                List<Track> tracks,

                List<Playlist> children

        )
{
    /**
     * Constructor that sets {@link #tracks} amd {@link #children} to an empty {@link ArrayList} each
     * and otherwise behaves as the canonical constructor.
     */
    public Playlist (Integer playlistId,
                     Integer depth,
                     Integer distinguishedKind,
                     String name,
                     String playlistPersistentId,
                     String parentPersistentId,
                     Boolean visible, Boolean allItems,
                     Boolean folder,
                     Boolean master,
                     Boolean music,
                     Boolean movies,
                     Boolean tvShows,
                     Boolean audiobooks,
                     Playlist parent
    )
    {
        this(playlistId,
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
                parent,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Playlist playlist = (Playlist) o;

        return Objects.equals(playlistId, playlist.playlistId);
    }

    @Override
    public int hashCode ()
    {
        return playlistId != null ? playlistId.hashCode() : 0;
    }

    @Override
    public String toString ()
    {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", depth=" + depth +
                ", #tracks=" + tracks.size() +
                ", #children=" + children.size() +
                ", playlistId=" + playlistId +
                ", distinguishedKind=" + distinguishedKind +
                ", playlistPersistentId='" + playlistPersistentId + '\'' +
                ", parentPersistentId='" + parentPersistentId + '\'' +
                ", visible=" + visible +
                ", allItems=" + allItems +
                ", folder=" + folder +
                ", master=" + master +
                ", music=" + music +
                ", movies=" + movies +
                ", tvShows=" + tvShows +
                ", audiobooks=" + audiobooks +
                ", parent=" + (parent == null ? "null" : parent.name) +
                // ", ancestry=" + ancestry +
                '}';
    }

    /**
     * @return the number of tracks contained in the playlist.
     */
    public int getNumberOfTracks ()
    {
        return tracks.size();
    }

    /**
     * At a child playlist to this playlist.
     *
     * @param playlist the child playlist to add.
     */
    public void addChild (Playlist playlist)
    {
        children.add(playlist);
    }

    /**
     * Adds the given track to this playlist.
     *
     * @param track the track to add.
     */
    public void addTrack (Track track)
    {
        tracks.add(track);
        track.setContainedInPlaylist(this);
    }
}
