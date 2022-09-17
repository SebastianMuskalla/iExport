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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A record class for representing tracks in the iTunes library.
 * <p>
 * Example from iTunes Music Library.xml:
 * {@code
 * <key>16793</key>
 * <dict>
 * <key>Track ID</key><integer>16793</integer>
 * <key>Size</key><integer>8613801</integer>
 * <key>Total Time</key><integer>180432</integer>
 * <key>Track Number</key><integer>6</integer>
 * <key>Track Count</key><integer>6</integer>
 * <key>Year</key><integer>2022</integer>
 * <key>Date Modified</key><date>2022-06-17T20:49:41Z</date>
 * <key>Date Added</key><date>2022-06-17T20:48:49Z</date>
 * <key>Bit Rate</key><integer>320</integer>
 * <key>Sample Rate</key><integer>48000</integer>
 * <key>Artwork Count</key><integer>2</integer>
 * <key>Persistent ID</key><string>99E7235E519072C8</string>
 * <key>Track Type</key><string>File</string>
 * <key>File Folder Count</key><integer>-1</integer>
 * <key>Library Folder Count</key><integer>-1</integer>
 * <key>Name</key><string>The Burning</string>
 * <key>Artist</key><string>Sean Kolton</string>
 * <key>Album Artist</key><string>MechWarrior</string>
 * <key>Composer</key><string>Sean Kolton</string>
 * <key>Album</key><string>MechWarrior 5 Call to Arms</string>
 * <key>Kind</key><string>MPEG audio file</string>
 * <key>Sort Name</key><string>Burning</string>
 * <key>Location</key><string>file://localhost/E:/Audio/Music/M/MechWarrior%20-%202022%20MechWarrior%205%20Call%20to%20Arms/06%20-%20The%20Burningt.mp3</string>
 * </dict>
 * }
 *
 * @param trackId             the internal track ID.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Track ID</key><integer>2339</integer>}
 * @param year                the release year.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Year</key><integer>1999</integer>}
 * @param trackCount          how many tracks the disc this track is one contains.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Track Count</key><integer>33</integer>}
 * @param trackNumber         the number of the track on the disc.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Track Number</key><integer>20</integer>}
 * @param discNumber          the disc number this track is on.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Disc Number</key><integer>1</integer>}
 * @param discCount           the number of discs constituting the album the track is on.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Disc Count</key><integer>1</integer>}
 * @param totalTime           the running time of the track.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Total Time</key><integer>114991</integer>}
 * @param bitRate             bit rate in kb/s.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Bit Rate</key><integer>128</integer>}
 * @param sampleRate          the sample rate in Hz.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sample Rate</key><integer>44100</integer>}
 * @param size                the file size in bytes.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Size</key><integer>1941015</integer>}
 * @param rating              the track rating as a number from 0-100.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Rating</key><integer>40</integer>}
 * @param albumRating         the album rating as a number from 0-100.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Album Rating</key><integer>20</integer>}
 * @param bpm                 The bpm.
 * @param playCount           how often this track has been played.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Play Count</key><integer>40</integer>}
 * @param skipCount           how often this track was skipped.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Skip Count</key><integer>2</integer>}
 * @param startTime           ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Start Time</key><integer>5000</integer>}
 * @param stopTime            ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Volume Adjustment</key><integer>255</integer>}
 * @param volumeAdjustment    ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Disabled</key><true/>}
 * @param fileFolderCount     ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>File Folder Count</key><integer>-1</integer>}
 * @param libraryFolderCount  ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Library Folder Count</key><integer>-1</integer>}
 * @param artWorkCount        ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Artwork Count</key><integer>1</integer>}
 * @param playDate            when this track was last played.
 *                            <p>
 *                            Is this Unix-time?
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code  <key>Play Date</key><integer>3729804533</integer>}a
 * @param persistentId        iTunes-internal persistent ID.
 *                            <p>
 *                            We use this field for {@link #equals(Object)} and {@link #hashCode()}.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Persistent ID</key><string>9D376722713BF099</string>}
 * @param location            the location on the disk.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Location</key><string>file://localhost/C:/Audio/Music/A/Some%20Artist%20-%201999%20Album/1%20-%20Track%20Name.mp3</string>}
 * @param name                the track title.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Name</key><string>XYZ</string>}
 * @param sortName            track title used for sorting.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sort Name</key><string>XYZ</string>}
 * @param artist              the name of the artist.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Artist</key><string>XYZ</string>}
 * @param sortArtist          artist name used for sorting
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sort Artist</key><string>XYZ</string>}
 * @param album               the name of the album.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Album</key><string>XYZ</string>}
 * @param sortAlbum           album name used for sorting.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sort Album</key><string>XYZ</string>}
 * @param albumArtist         the name of the album artist.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Album Artist</key><string>XYZ</string>}
 * @param sortAlbumArtist     album artist name used for sorting
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sort Album Artist</key><string>XYZ</string>}
 * @param sortComposer        composer name used for sorting.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Sort Composer</key><string>XYZ</string>}
 * @param composer            name of the composer.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code     <key>Composer</key><string>XYZ</string>}
 * @param kind                the kind of file
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Kind</key><string>MPEG audio file</string>}
 * @param work                ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Work</key><string>XYZ/string>}
 * @param grouping            ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Grouping</key><string>Legend of Zelda: Link's Awakening</string>}
 * @param genre               ???
 * @param comments            ???
 * @param equalizer           ???
 * @param trackType           ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Track Type</key><string>File</string>}
 * @param ratingComputed      ???
 * @param albumRatingComputed ???
 * @param compilation         ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Compilation</key><true/>}
 * @param disabled            ???
 *                            <p>
 *                            example from iTunes Music Library.xml:
 *                            {@code <key>Rating Computed</key><true/>}
 * @param disliked            ???
 * @param loved               ???
 * @param dateAdded           the date at which the track was added to the library.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Date Added</key><date>2015-09-19T12:53:12Z</date>}
 * @param dateModified        the date modified.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Date Modified</key><date>2015-12-30T21:16:45Z</date>}
 * @param releaseDate         ???
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Release Date</key><date>2002-06-01T12:00:00Z</date>}
 * @param playDateUTC         when this track was lasted played.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Play Date UTC</key><date>2017-11-01T20:06:40Z</date>}
 * @param skipDate            when this track was last skipped.
 *                            <p>
 *                            Example from iTunes Music Library.xml:
 *                            {@code <key>Skip Date</key><date>2016-02-22T19:26:42Z</date>}
 * @param inPlaylists         the playlists this track is contained in.
 *                            <p>
 *                            This is NOT a property of an iTunes track that is parsed from the .xml file.
 */
public record Track
        (
                Integer trackId,
                Integer year,
                Integer trackCount,
                Integer trackNumber,
                Integer discNumber,
                Integer discCount,
                Integer totalTime,
                Integer bitRate,
                Integer sampleRate,
                Integer size,
                Integer rating,
                Integer albumRating,
                Integer bpm,
                Integer playCount,
                Integer skipCount,
                Integer startTime,
                Integer stopTime,
                Integer volumeAdjustment,
                Integer fileFolderCount,
                Integer libraryFolderCount,
                Integer artWorkCount,

                Long playDate,

                String persistentId,
                String location,
                String name,
                String sortName,
                String artist,
                String sortArtist,
                String album,
                String sortAlbum,
                String albumArtist,
                String sortAlbumArtist,
                String sortComposer,
                String composer,
                String kind,
                String work,
                String grouping,
                String genre,
                String comments,
                String equalizer,
                String trackType,

                Boolean ratingComputed,
                Boolean albumRatingComputed,
                Boolean compilation,
                Boolean disabled,
                Boolean disliked,
                Boolean loved,

                Date dateAdded,
                Date dateModified,
                Date releaseDate,
                Date playDateUTC,
                Date skipDate,

                Set<Playlist> inPlaylists
        )
{
    /**
     * Constructor that sets {@link #inPlaylists} to an empty {@link HashSet} and otherwise behaves as the canonical constructor.
     */
    public Track (Integer trackId,
                  String location,
                  String persistentId,
                  String name,
                  String artist,
                  String album,
                  String albumArtist,
                  String sortName,
                  String sortAlbumArtist,
                  String sortAlbum,
                  String composer,
                  String sortArtist,
                  Integer year,
                  Date dateAdded,
                  Integer discNumber,
                  Integer discCount,
                  Integer totalTime,
                  Integer trackNumber,
                  Date dateModified,
                  Integer bitRate,
                  Date skipDate,
                  Integer skipCount,
                  Integer sampleRate,
                  String sortComposer,
                  Long playDate,
                  Integer playCount,
                  Date playDateUTC,
                  Integer trackCount,
                  Integer artWorkCount,
                  Integer libraryFolderCount,
                  String kind,
                  Integer size,
                  Integer rating,
                  Integer albumRating,
                  String trackType,
                  Integer startTime,
                  Integer bpm,
                  Integer stopTime,
                  Integer fileFolderCount,
                  Integer volumeAdjustment,
                  Boolean disabled,
                  Boolean disliked,
                  Boolean ratingComputed,
                  Boolean albumRatingComputed,
                  Boolean compilation,
                  Boolean loved,
                  Date releaseDate,
                  String comments,
                  String equalizer,
                  String work,
                  String grouping,
                  String genre)
    {
        this(trackId,
                year, trackCount, trackNumber, discNumber, discCount, totalTime, bitRate, sampleRate, size, rating, albumRating, bpm, playCount, skipCount, startTime, stopTime, volumeAdjustment, fileFolderCount, libraryFolderCount, artWorkCount, playDate, persistentId, location, name, sortName, artist, sortArtist, album, sortAlbum, albumArtist, sortAlbumArtist, sortComposer, composer, kind, work, grouping, genre, comments, equalizer, trackType, ratingComputed, albumRatingComputed, compilation, disabled, disliked, loved, dateAdded, dateModified, releaseDate, playDateUTC, skipDate, new HashSet<>()
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

        Track track = (Track) o;

        return Objects.equals(persistentId, track.persistentId);
    }

    @Override
    public int hashCode ()
    {
        return persistentId != null ? persistentId.hashCode() : 0;
    }

    @Override
    public String toString ()
    {
        return ((artist != null) ? artist : "UNKNOWN ARTIST")
                + " - "
                + ((name != null) ? name : "UNKNOWN TITLE")
                + " {"
                + ((trackId != null) ? "trackId=" + trackId : "")
                + ((persistentId != null) ? ", persistentId=" + persistentId : "")
                + ((location != null) ? ", " + location : "")
                + '}';
    }

    /**
     * Set that the track is contained in the given playlist.
     * <p>
     * This method will be called by {@link Playlist#addTrack(Track)}
     *
     * @param playlist the playlist.
     */
    void setContainedInPlaylist (Playlist playlist)
    {
        inPlaylists.add(playlist);
    }

}

