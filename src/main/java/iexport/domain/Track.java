package iexport.domain;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Track
{
    /**
     * {@code <key>Track ID</key><integer>2339</integer>}
     */
    @Nonnull
    private final
    Integer trackId;
    /**
     * {@code <key>Track Number</key><integer>20</integer>}
     */
    @CheckForNull
    private final
    Integer trackNumber;
    /**
     * {@code <key>Total Time</key><integer>114991</integer>}
     */
    @CheckForNull
    private final
    Integer totalTime;
    /**
     * {@code <key>Year</key><integer>1999</integer>}
     */
    @CheckForNull
    private final
    Integer year;
    /**
     * {@code <key>Date Modified</key><date>2015-12-30T21:16:45Z</date>}
     */
    @CheckForNull
    private final
    Date dateModified;
    /**
     * {@code <key>Date Added</key><date>2015-09-19T12:53:12Z</date>}
     */
    @CheckForNull
    private final
    Date dateAdded;
    /**
     * {@code <key>Bit Rate</key><integer>128</integer>}
     */
    @CheckForNull
    private final
    Integer bitRate;
    /**
     * {@code <key>Sample Rate</key><integer>44100</integer>}
     */
    @CheckForNull
    private final
    Integer sampleRate;
    /**
     * {@code <key>Skip Date</key><date>2016-02-22T19:26:42Z</date>}
     */
    @CheckForNull
    private final
    Date skipDate;
    /**
     * {@code <key>Artwork Count</key><integer>1</integer>}
     */
    @CheckForNull
    private final
    Integer artWorkCount;
    /**
     * {@code <key>File Folder Count</key><integer>-1</integer>}
     */
    @CheckForNull
    private final
    Integer fileFolderCount;
    /**
     * {@code <key>Location</key><string>file://localhost/F:/Audio/Music/D/Digimon%20-%201999%20Adventure%20-%20OST%202/20%20-%20Tataki%20no%20toki.mp3</string>}
     */
    @CheckForNull
    private final
    String location;
    /**
     * {@code <key>Track Count</key><integer>33</integer>}
     */
    @CheckForNull
    private final
    Integer trackCount;
    /**
     * {@code <key>Play Count</key><integer>40</integer>}
     */
    @CheckForNull
    private final
    Integer playCount;
    /**
     * {@code <key>Play Date UTC</key><date>2017-11-01T20:06:40Z</date>}
     */
    @CheckForNull
    private final
    Date playDateUTC;
    /**
     * {@code <key>Skip Count</key><integer>2</integer>}
     */
    @CheckForNull
    private final
    Integer skipCount;
    /**
     * {@code <key>Persistent ID</key><string>9D376722713BF099</string>}
     */
    @CheckForNull
    private final
    String persistentId;
    /**
     * {@code <key>Track Type</key><string>File</string>}
     */
    @CheckForNull
    private final
    String trackType;
    /**
     * {@code <key>Library Folder Count</key><integer>-1</integer>}
     */
    @CheckForNull
    private final
    Integer libraryFolderCount;
    /**
     * {@code <key>Name</key><string>XYZ</string>}
     */
    @CheckForNull
    private final
    String name;
    /**
     * {@code <key>Artist</key><string>XYZ</string>}
     */
    @CheckForNull
    private final
    String artist;
    /**
     * {@code <key>Album Artist</key><string>XYZ</string>}
     */
    @CheckForNull
    private final
    String albumArtist;
    /**
     * {@code <key>Album</key><string>Digimon Adventure Uta to Ongaku Shuu Ver. 2</string>}
     */
    @CheckForNull
    private final
    String album;
    /**
     * {@code <key>Kind</key><string>MPEG audio file</string>}
     */
    @CheckForNull
    private final
    String kind;
    /**
     * {@code <key>Size</key><integer>1941015</integer>}
     */
    @CheckForNull
    private final
    Integer size;


    @CheckForNull
    private final Integer albumRating;

    @CheckForNull
    private final Integer discNumber;

    @CheckForNull
    private final Integer discCount;

    @CheckForNull
    private final Integer bpm;

    @CheckForNull
    private final Integer startTime;

    @CheckForNull
    private final Integer stopTime;

    @CheckForNull
    private final Integer volumeAdjustment;

    @CheckForNull
    private final Boolean disabled;

    @CheckForNull
    private final Boolean loved;

    @CheckForNull
    private final Boolean ratingComputed;

    @CheckForNull
    private final Boolean albumRatingComputed;

    @CheckForNull
    private final Boolean compilation;

    @CheckForNull
    private final Boolean disliked;

    @CheckForNull
    private final Date releaseDate;

    @CheckForNull
    private final String comments;

    @CheckForNull
    private final String equalizer;

    @CheckForNull
    private final String work;

    @CheckForNull
    private final String grouping;
    @CheckForNull
    private final String sortName;
    @CheckForNull
    private final String sortArtist;
    @CheckForNull
    private final String sortAlbumArtist;
    @CheckForNull
    private final String sortAlbum;
    @CheckForNull
    private final String composer;
    @CheckForNull
    private final String sortComposer;
    @CheckForNull
    private final String genre;

    @Nonnull
    private final Set<Playlist> inPlaylists;

    public Track (@Nonnull Integer trackId, @CheckForNull Integer trackNumber, @CheckForNull Integer totalTime, @CheckForNull Integer year, @CheckForNull Date dateModified, @CheckForNull Date dateAdded, @CheckForNull Integer bitRate, @CheckForNull Integer sampleRate, @CheckForNull Date skipDate, @CheckForNull Integer artWorkCount, @CheckForNull Integer fileFolderCount, @CheckForNull String location, @CheckForNull Integer trackCount, @CheckForNull Integer playCount, @CheckForNull Date playDateUTC, @CheckForNull Integer skipCount, @CheckForNull String persistentId, @CheckForNull String trackType, @CheckForNull Integer libraryFolderCount, @CheckForNull String name, @CheckForNull String artist, @CheckForNull String albumArtist, @CheckForNull String album, @CheckForNull String kind, @CheckForNull Integer size, @CheckForNull Integer albumRating, @CheckForNull Integer discNumber, @CheckForNull Integer discCount, @CheckForNull Integer bpm, @CheckForNull Integer startTime, @CheckForNull Integer stopTime, @CheckForNull Integer volumeAdjustment, @CheckForNull Boolean disabled, @CheckForNull Boolean loved, @CheckForNull Boolean ratingComputed, @CheckForNull Boolean albumRatingComputed, @CheckForNull Boolean compilation, @CheckForNull Boolean disliked, @CheckForNull Date releaseDate, @CheckForNull String comments, @CheckForNull String equalizer, @CheckForNull String work, @CheckForNull String grouping, @CheckForNull String sortName, @CheckForNull String sortArtist, @CheckForNull String sortAlbumArtist, @CheckForNull String sortAlbum, @CheckForNull String composer, @CheckForNull String sortComposer, @CheckForNull String genre)
    {
        this.trackId = trackId;
        this.trackNumber = trackNumber;
        this.totalTime = totalTime;
        this.year = year;
        this.dateModified = dateModified;
        this.dateAdded = dateAdded;
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
        this.skipDate = skipDate;
        this.artWorkCount = artWorkCount;
        this.fileFolderCount = fileFolderCount;
        this.location = location;
        this.trackCount = trackCount;
        this.playCount = playCount;
        this.playDateUTC = playDateUTC;
        this.skipCount = skipCount;
        this.persistentId = persistentId;
        this.trackType = trackType;
        this.libraryFolderCount = libraryFolderCount;
        this.name = name;
        this.artist = artist;
        this.albumArtist = albumArtist;
        this.album = album;
        this.kind = kind;
        this.size = size;
        this.albumRating = albumRating;
        this.discNumber = discNumber;
        this.discCount = discCount;
        this.bpm = bpm;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.volumeAdjustment = volumeAdjustment;
        this.disabled = disabled;
        this.loved = loved;
        this.ratingComputed = ratingComputed;
        this.albumRatingComputed = albumRatingComputed;
        this.compilation = compilation;
        this.disliked = disliked;
        this.releaseDate = releaseDate;
        this.comments = comments;
        this.equalizer = equalizer;
        this.work = work;
        this.grouping = grouping;
        this.sortName = sortName;
        this.sortArtist = sortArtist;
        this.sortAlbumArtist = sortAlbumArtist;
        this.sortAlbum = sortAlbum;
        this.composer = composer;
        this.sortComposer = sortComposer;
        this.genre = genre;
        this.inPlaylists = new HashSet<>();
    }

    @Nonnull
    public Integer getTrackId ()
    {
        return trackId;
    }

    @CheckForNull
    public Integer getTrackNumber ()
    {
        return trackNumber;
    }

    @CheckForNull
    public Integer getTotalTime ()
    {
        return totalTime;
    }

    @CheckForNull
    public Integer getYear ()
    {
        return year;
    }

    @CheckForNull
    public Date getDateModified ()
    {
        return dateModified;
    }

    @CheckForNull
    public Date getDateAdded ()
    {
        return dateAdded;
    }

    @CheckForNull
    public Integer getBitRate ()
    {
        return bitRate;
    }

    @CheckForNull
    public Integer getSampleRate ()
    {
        return sampleRate;
    }

    @CheckForNull
    public Date getSkipDate ()
    {
        return skipDate;
    }

    @CheckForNull
    public Integer getArtWorkCount ()
    {
        return artWorkCount;
    }

    @CheckForNull
    public Integer getFileFolderCount ()
    {
        return fileFolderCount;
    }

    @CheckForNull
    public String getLocation ()
    {
        return location;
    }

    @CheckForNull
    public Integer getTrackCount ()
    {
        return trackCount;
    }

    @CheckForNull
    public Integer getPlayCount ()
    {
        return playCount;
    }

    @CheckForNull
    public Date getPlayDateUTC ()
    {
        return playDateUTC;
    }

    @CheckForNull
    public Integer getSkipCount ()
    {
        return skipCount;
    }

    @CheckForNull
    public String getPersistentId ()
    {
        return persistentId;
    }

    @CheckForNull
    public String getTrackType ()
    {
        return trackType;
    }

    @CheckForNull
    public Integer getLibraryFolderCount ()
    {
        return libraryFolderCount;
    }

    @CheckForNull
    public String getName ()
    {
        return name;
    }

    @CheckForNull
    public String getArtist ()
    {
        return artist;
    }

    @CheckForNull
    public String getAlbumArtist ()
    {
        return albumArtist;
    }

    @CheckForNull
    public String getAlbum ()
    {
        return album;
    }

    @CheckForNull
    public String getKind ()
    {
        return kind;
    }

    @CheckForNull
    public Integer getSize ()
    {
        return size;
    }

    @CheckForNull
    public Integer getAlbumRating ()
    {
        return albumRating;
    }

    @CheckForNull
    public Integer getDiscNumber ()
    {
        return discNumber;
    }

    @CheckForNull
    public Integer getDiscCount ()
    {
        return discCount;
    }

    @CheckForNull
    public Integer getBpm ()
    {
        return bpm;
    }

    @CheckForNull
    public Integer getStartTime ()
    {
        return startTime;
    }

    @CheckForNull
    public Integer getStopTime ()
    {
        return stopTime;
    }

    @CheckForNull
    public Integer getVolumeAdjustment ()
    {
        return volumeAdjustment;
    }

    @CheckForNull
    public Boolean getDisabled ()
    {
        return disabled;
    }

    @CheckForNull
    public Boolean getLoved ()
    {
        return loved;
    }

    @CheckForNull
    public Boolean getRatingComputed ()
    {
        return ratingComputed;
    }

    @CheckForNull
    public Boolean getAlbumRatingComputed ()
    {
        return albumRatingComputed;
    }

    @CheckForNull
    public Boolean getCompilation ()
    {
        return compilation;
    }

    @CheckForNull
    public Boolean getDisliked ()
    {
        return disliked;
    }

    @CheckForNull
    public Date getReleaseDate ()
    {
        return releaseDate;
    }

    @CheckForNull
    public String getComments ()
    {
        return comments;
    }

    @CheckForNull
    public String getEqualizer ()
    {
        return equalizer;
    }

    @CheckForNull
    public String getWork ()
    {
        return work;
    }

    @CheckForNull
    public String getGrouping ()
    {
        return grouping;
    }

    @CheckForNull
    public String getSortName ()
    {
        return sortName;
    }

    @CheckForNull
    public String getSortArtist ()
    {
        return sortArtist;
    }

    @CheckForNull
    public String getSortAlbumArtist ()
    {
        return sortAlbumArtist;
    }

    @CheckForNull
    public String getSortAlbum ()
    {
        return sortAlbum;
    }

    @CheckForNull
    public String getComposer ()
    {
        return composer;
    }

    @CheckForNull
    public String getSortComposer ()
    {
        return sortComposer;
    }

    @CheckForNull
    public String getGenre ()
    {
        return genre;
    }

    public void setContainedInPlaylist (Playlist playlist)
    {
        inPlaylists.add(playlist);
    }
}
