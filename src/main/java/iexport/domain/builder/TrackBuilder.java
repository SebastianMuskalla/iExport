package iexport.domain.builder;

import iexport.domain.Track;
import iexport.parsing.itunes.IExportParsingException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Date;

public class TrackBuilder
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
    private
    Integer trackNumber;
    /**
     * {@code <key>Total Time</key><integer>114991</integer>}
     */
    @CheckForNull
    private
    Integer totalTime;
    /**
     * {@code <key>Year</key><integer>1999</integer>}
     */
    @CheckForNull
    private
    Integer year;
    /**
     * {@code <key>Date Modified</key><date>2015-12-30T21:16:45Z</date>}
     */
    @CheckForNull
    private
    Date dateModified;
    /**
     * {@code <key>Date Added</key><date>2015-09-19T12:53:12Z</date>}
     */
    @CheckForNull
    private
    Date dateAdded;
    /**
     * {@code <key>Bit Rate</key><integer>128</integer>}
     */
    @CheckForNull
    private
    Integer bitRate;
    /**
     * {@code <key>Sample Rate</key><integer>44100</integer>}
     */
    @CheckForNull
    private
    Integer sampleRate;
    /**
     * {@code <key>Skip Date</key><date>2016-02-22T19:26:42Z</date>}
     */
    @CheckForNull
    private
    Date skipDate;
    /**
     * {@code <key>Artwork Count</key><integer>1</integer>}
     */
    @CheckForNull
    private
    Integer artWorkCount;
    /**
     * {@code <key>File Folder Count</key><integer>-1</integer>}
     */
    @CheckForNull
    private
    Integer fileFolderCount;
    /**
     * {@code <key>Location</key><string>file://localhost/F:/Audio/Music/D/Digimon%20-%201999%20Adventure%20-%20OST%202/20%20-%20Tataki%20no%20toki.mp3</string>}
     */
    @CheckForNull
    private
    String location;
    /**
     * {@code <key>Track Count</key><integer>33</integer>}
     */
    @CheckForNull
    private
    Integer trackCount;
    /**
     * {@code <key>Play Count</key><integer>40</integer>}
     */
    @CheckForNull
    private
    Integer playCount;
    /**
     * {@code <key>Play Date UTC</key><date>2017-11-01T20:06:40Z</date>}
     */
    @CheckForNull
    private
    Date playDateUTC;
    /**
     * {@code <key>Skip Count</key><integer>2</integer>}
     */
    @CheckForNull
    private
    Integer skipCount;
    /**
     * {@code <key>Persistent ID</key><string>9D376722713BF099</string>}
     */
    @CheckForNull
    private
    String persistentId;
    /**
     * {@code <key>Track Type</key><string>File</string>}
     */
    @CheckForNull
    private
    String trackType;
    /**
     * {@code <key>Library Folder Count</key><integer>-1</integer>}
     */
    @CheckForNull
    private
    Integer libraryFolderCount;
    /**
     * {@code <key>Name</key><string>XYZ</string>}
     */
    @CheckForNull
    private
    String name;
    /**
     * {@code <key>Artist</key><string>XYZ</string>}
     */
    @CheckForNull
    private
    String artist;
    /**
     * {@code <key>Album Artist</key><string>XYZ</string>}
     */
    @CheckForNull
    private
    String albumArtist;
    /**
     * {@code <key>Album</key><string>Digimon Adventure Uta to Ongaku Shuu Ver. 2</string>}
     */
    @CheckForNull
    private
    String album;
    /**
     * {@code <key>Kind</key><string>MPEG audio file</string>}
     */
    @CheckForNull
    private
    String kind;
    /**
     * {@code <key>Size</key><integer>1941015</integer>}
     */
    @CheckForNull
    private
    Integer size;


    @CheckForNull
    private Integer albumRating;

    @CheckForNull
    private Integer discNumber;

    @CheckForNull
    private Integer discCount;

    @CheckForNull
    private Integer bpm;

    @CheckForNull
    private Integer startTime;

    @CheckForNull
    private Integer stopTime;

    @CheckForNull
    private Integer volumeAdjustment;

    @CheckForNull
    private Boolean disabled;

    @CheckForNull
    private Boolean loved;

    @CheckForNull
    private Boolean ratingComputed;

    @CheckForNull
    private Boolean albumRatingComputed;

    @CheckForNull
    private Boolean compilation;

    @CheckForNull
    private Boolean disliked;

    @CheckForNull
    private Date releaseDate;

    @CheckForNull
    private String comments;

    @CheckForNull
    private String equalizer;

    @CheckForNull
    private String work;

    @CheckForNull
    private String grouping;
    @CheckForNull
    private String sortName;
    @CheckForNull
    private String sortArtist;
    @CheckForNull
    private String sortAlbumArtist;
    @CheckForNull
    private String sortAlbum;
    @CheckForNull
    private String composer;
    @CheckForNull
    private String sortComposer;
    @CheckForNull
    private String genre;

    private Track track;

    public TrackBuilder (@Nonnull Integer trackId)
    {
        this.trackId = trackId;
    }

    public void setDisliked (@CheckForNull Boolean disliked)
    {
        this.disliked = disliked;
    }

    public void setTrackCount (@CheckForNull Integer trackCount)
    {
        this.trackCount = trackCount;
    }

    public void setAlbumRating (@CheckForNull Integer albumRating)
    {
        this.albumRating = albumRating;
    }

    public void setDiscNumber (@CheckForNull Integer discNumber)
    {
        this.discNumber = discNumber;
    }

    public void setDiscCount (@CheckForNull Integer discCount)
    {
        this.discCount = discCount;
    }

    public void setBpm (@CheckForNull Integer bpm)
    {
        this.bpm = bpm;
    }

    public void setStartTime (@CheckForNull Integer startTime)
    {
        this.startTime = startTime;
    }

    public void setStopTime (@CheckForNull Integer stopTime)
    {
        this.stopTime = stopTime;
    }

    public void setVolumeAdjustment (@CheckForNull Integer volumeAdjustment)
    {
        this.volumeAdjustment = volumeAdjustment;
    }

    public void setDisabled (@CheckForNull Boolean disabled)
    {
        this.disabled = disabled;
    }

    public void setLoved (@CheckForNull Boolean loved)
    {
        this.loved = loved;
    }

    public void setRatingComputed (@CheckForNull Boolean ratingComputed)
    {
        this.ratingComputed = ratingComputed;
    }

    public void setAlbumRatingComputed (@CheckForNull Boolean albumRatingComputed)
    {
        this.albumRatingComputed = albumRatingComputed;
    }

    public void setReleaseDate (@CheckForNull Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setComments (@CheckForNull String comments)
    {
        this.comments = comments;
    }

    public void setEqualizer (@CheckForNull String equalizer)
    {
        this.equalizer = equalizer;
    }

    public void setWork (@CheckForNull String work)
    {
        this.work = work;
    }

    public void setGrouping (@CheckForNull String grouping)
    {
        this.grouping = grouping;
    }

    public void setSortName (@CheckForNull String sortName)
    {
        this.sortName = sortName;
    }

    public void setSortArtist (@CheckForNull String sortArtist)
    {
        this.sortArtist = sortArtist;
    }

    public void setSortAlbumArtist (@CheckForNull String sortAlbumArtist)
    {
        this.sortAlbumArtist = sortAlbumArtist;
    }

    public void setSortAlbum (@CheckForNull String sortAlbum)
    {
        this.sortAlbum = sortAlbum;
    }

    public void setComposer (@CheckForNull String composer)
    {
        this.composer = composer;
    }

    public void setSortComposer (@CheckForNull String sortComposer)
    {
        this.sortComposer = sortComposer;
    }

    public void setGenre (@CheckForNull String genre)
    {
        this.genre = genre;
    }

    public void verifyTrackId (@Nonnull Integer trackId)
            throws
            IExportParsingException
    {
        if (!this.trackId.equals(trackId))
        {
            throw new IExportParsingException("Track with Track ID key " + this.trackId + " has specified Track ID " + trackId + " in dictionary");
        }
    }

    public void setSize (@CheckForNull Integer size)
    {
        this.size = size;
    }

    public void setTrackNumber (@CheckForNull Integer trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    public void setTotalTime (@CheckForNull Integer totalTime)
    {
        this.totalTime = totalTime;
    }

    public void setYear (@CheckForNull Integer year)
    {
        this.year = year;
    }

    public void setDateModified (@CheckForNull Date dateModified)
    {
        this.dateModified = dateModified;
    }

    public void setDateAdded (@CheckForNull Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public void setBitRate (@CheckForNull Integer bitRate)
    {
        this.bitRate = bitRate;
    }

    public void setPlayCount (@CheckForNull Integer playCount)
    {
        this.playCount = playCount;
    }

    public void setSampleRate (@CheckForNull Integer sampleRate)
    {
        this.sampleRate = sampleRate;
    }

    public void setPlayDateUTC (@CheckForNull Date playDateUTC)
    {
        this.playDateUTC = playDateUTC;
    }

    public void setSkipCount (@CheckForNull Integer skipCount)
    {
        this.skipCount = skipCount;
    }

    public void setSkipDate (@CheckForNull Date skipDate)
    {
        this.skipDate = skipDate;
    }

    public void setArtWorkCount (@CheckForNull Integer artWorkCount)
    {
        this.artWorkCount = artWorkCount;
    }

    public void setPersistentId (@CheckForNull String persistentId)
    {
        this.persistentId = persistentId;
    }

    public void setTrackType (@CheckForNull String trackType)
    {
        this.trackType = trackType;
    }

    public void setFileFolderCount (@CheckForNull Integer fileFolderCount)
    {
        this.fileFolderCount = fileFolderCount;
    }

    public void setLibraryFolderCount (@CheckForNull Integer libraryFolderCount)
    {
        this.libraryFolderCount = libraryFolderCount;
    }

    public void setName (@CheckForNull String name)
    {
        this.name = name;
    }

    public void setArtist (@CheckForNull String artist)
    {
        this.artist = artist;
    }

    public void setAlbumArtist (@CheckForNull String albumArtist)
    {
        this.albumArtist = albumArtist;
    }

    public void setAlbum (@CheckForNull String album)
    {
        this.album = album;
    }

    public void setKind (@CheckForNull String kind)
    {
        this.kind = kind;
    }

    public void setLocation (@CheckForNull String location)
    {
        this.location = location;
    }

    public void setRating (@CheckForNull Integer rating)
    {
    }

    public void setCompilation (@CheckForNull Boolean compilation)
    {
        this.compilation = compilation;
    }

    public Track create ()
    {
        if (track != null)
        {
            return track;
        }
        track = new Track(trackId, trackNumber, totalTime, year, dateModified, dateAdded, bitRate, sampleRate, skipDate, artWorkCount, fileFolderCount, location, trackCount, playCount, playDateUTC, skipCount, persistentId, trackType, libraryFolderCount, name, artist, albumArtist, album, kind, size, albumRating, discNumber, discCount, bpm, startTime, stopTime, volumeAdjustment, disabled, loved, ratingComputed, albumRatingComputed, compilation, disliked, releaseDate, comments, equalizer, work, grouping, sortName, sortArtist, sortAlbumArtist, sortAlbum, composer, sortComposer, genre);
        return track;
    }
}
