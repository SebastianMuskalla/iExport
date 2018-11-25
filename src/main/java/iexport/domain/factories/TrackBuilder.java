package iexport.domain.factories;

import iexport.parsing.itunes.LibraryParsingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @Nullable
    Integer trackNumber;
    /**
     * {@code <key>Total Time</key><integer>114991</integer>}
     */
    @Nullable
    Integer totalTime;
    /**
     * {@code <key>Year</key><integer>1999</integer>}
     */
    @Nullable
    Integer year;
    /**
     * {@code <key>Date Modified</key><date>2015-12-30T21:16:45Z</date>}
     */
    @Nullable
    Date dateModified;
    /**
     * {@code <key>Date Added</key><date>2015-09-19T12:53:12Z</date>}
     */
    @Nullable
    Date dateAdded;
    /**
     * {@code <key>Bit Rate</key><integer>128</integer>}
     */
    @Nullable
    Integer bitRate;
    /**
     * {@code <key>Track Count</key><integer>33</integer>}
     */
    @Nullable
    Integer trackCount;
    /**
     * {@code <key>Play Count</key><integer>40</integer>}
     */
    @Nullable
    Integer playCount;
    /**
     * {@code <key>Sample Rate</key><integer>44100</integer>}
     */
    @Nullable
    Integer sampleRate;
    /**
     * {@code <key>Play Date UTC</key><date>2017-11-01T20:06:40Z</date>}
     */
    @Nullable
    Date playDateUTC;
    /**
     * {@code <key>Skip Count</key><integer>2</integer>}
     */
    @Nullable
    Integer skipCount;
    /**
     * {@code <key>Skip Date</key><date>2016-02-22T19:26:42Z</date>}
     */
    @Nullable
    Date skipDate;
    /**
     * {@code <key>Artwork Count</key><integer>1</integer>}
     */
    @Nullable
    Integer artWorkCount;
    /**
     * {@code <key>Persistent ID</key><string>9D376722713BF099</string>}
     */
    @Nullable
    String persistentId;
    /**
     * {@code <key>Track Type</key><string>File</string>}
     */
    @Nullable
    String trackType;
    /**
     * {@code <key>File Folder Count</key><integer>-1</integer>}
     */
    @Nullable
    Integer fileFolderCount;
    /**
     * {@code <key>Library Folder Count</key><integer>-1</integer>}
     */
    @Nullable
    Integer libraryFolderCount;
    /**
     * {@code <key>Name</key><string>XYZ</string>}
     */
    @Nullable
    String name;
    /**
     * {@code <key>Artist</key><string>XYZ</string>}
     */
    @Nullable
    String artist;
    /**
     * {@code <key>Album Artist</key><string>XYZ</string>}
     */
    @Nullable
    String albumArtist;
    /**
     * {@code <key>Album</key><string>Digimon Adventure Uta to Ongaku Shuu Ver. 2</string>}
     */
    @Nullable
    String album;
    /**
     * {@code <key>Kind</key><string>MPEG audio file</string>}
     */
    @Nullable
    String kind;
    /**
     * {@code <key>Location</key><string>file://localhost/F:/Audio/Music/D/Digimon%20-%201999%20Adventure%20-%20OST%202/20%20-%20Tataki%20no%20toki.mp3</string>}
     */
    @Nullable
    String location;
    /**
     * {@code <key>Size</key><integer>1941015</integer>}
     */
    @Nullable
    private
    Integer size;


    @Nullable
    private Integer albumRating;

    @Nullable
    private Integer discNumber;

    @Nullable
    private Integer discCount;

    @Nullable
    private Integer bpm;

    @Nullable
    private Integer startTime;

    @Nullable
    private Integer stopTime;

    @Nullable
    private Integer volumeAdjustment;

    @Nullable
    private Boolean disabled;

    @Nullable
    private Boolean loved;

    @Nullable
    private Boolean ratingComputed;

    @Nullable
    private Boolean albumRatingComputed;

    @Nullable
    private Boolean compilation;

    @Nullable
    private Date releaseDate;

    @Nullable
    private String comments;

    @Nullable
    private String equalizer;

    @Nullable
    private String work;
    @Nullable
    private String grouping;
    @Nullable
    private String sortName;
    @Nullable
    private String sortArtist;
    @Nullable
    private String sortAlbumArtist;
    @Nullable
    private String sortAlbum;
    @Nullable
    private String Composer;
    @Nullable
    private String sortComposer;
    @Nullable
    private String Genre;

    public TrackBuilder (@Nonnull Integer trackId)
    {
        this.trackId = trackId;
    }

    public void setTrackCount (@Nullable Integer trackCount)
    {
        this.trackCount = trackCount;
    }

    public void setAlbumRating (@Nullable Integer albumRating)
    {
        this.albumRating = albumRating;
    }

    public void setDiscNumber (@Nullable Integer discNumber)
    {
        this.discNumber = discNumber;
    }

    public void setDiscCount (@Nullable Integer discCount)
    {
        this.discCount = discCount;
    }

    public void setBpm (@Nullable Integer bpm)
    {
        this.bpm = bpm;
    }

    public void setStartTime (@Nullable Integer startTime)
    {
        this.startTime = startTime;
    }

    public void setStopTime (@Nullable Integer stopTime)
    {
        this.stopTime = stopTime;
    }

    public void setVolumeAdjustment (@Nullable Integer volumeAdjustment)
    {
        this.volumeAdjustment = volumeAdjustment;
    }

    public void setDisabled (@Nullable Boolean disabled)
    {
        this.disabled = disabled;
    }

    public void setLoved (@Nullable Boolean loved)
    {
        this.loved = loved;
    }

    public void setRatingComputed (@Nullable Boolean ratingComputed)
    {
        this.ratingComputed = ratingComputed;
    }

    public void setAlbumRatingComputed (@Nullable Boolean albumRatingComputed)
    {
        this.albumRatingComputed = albumRatingComputed;
    }

    public void setReleaseDate (@Nullable Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setComments (@Nullable String comments)
    {
        this.comments = comments;
    }

    public void setEqualizer (@Nullable String equalizer)
    {
        this.equalizer = equalizer;
    }

    public void setWork (@Nullable String work)
    {
        this.work = work;
    }

    public void setGrouping (@Nullable String grouping)
    {
        this.grouping = grouping;
    }

    public void setSortName (@Nullable String sortName)
    {
        this.sortName = sortName;
    }

    public void setSortArtist (@Nullable String sortArtist)
    {
        this.sortArtist = sortArtist;
    }

    public void setSortAlbumArtist (@Nullable String sortAlbumArtist)
    {
        this.sortAlbumArtist = sortAlbumArtist;
    }

    public void setSortAlbum (@Nullable String sortAlbum)
    {
        this.sortAlbum = sortAlbum;
    }

    public void setComposer (@Nullable String composer)
    {
        Composer = composer;
    }

    public void setSortComposer (@Nullable String sortComposer)
    {
        this.sortComposer = sortComposer;
    }

    public void setGenre (@Nullable String genre)
    {
        Genre = genre;
    }

    public void verifyTrackId (@Nonnull Integer trackId)
            throws
            LibraryParsingException
    {
        if (!this.trackId.equals(trackId))
        {
            throw new LibraryParsingException("Track with Track ID key " + this.trackId + " has specified Track ID " + trackId + " in dictionary");
        }
    }

    public void setSize (@Nullable Integer size)
    {
        this.size = size;
    }

    public void setTrackNumber (@Nullable Integer trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    public void setTotalTime (@Nullable Integer totalTime)
    {
        this.totalTime = totalTime;
    }

    public void setYear (@Nullable Integer year)
    {
        this.year = year;
    }

    public void setDateModified (@Nullable Date dateModified)
    {
        this.dateModified = dateModified;
    }

    public void setDateAdded (@Nullable Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public void setBitRate (@Nullable Integer bitRate)
    {
        this.bitRate = bitRate;
    }

    public void setPlayCount (@Nullable Integer playCount)
    {
        this.playCount = playCount;
    }

    public void setSampleRate (@Nullable Integer sampleRate)
    {
        this.sampleRate = sampleRate;
    }

    public void setPlayDateUTC (@Nullable Date playDateUTC)
    {
        this.playDateUTC = playDateUTC;
    }

    public void setSkipCount (@Nullable Integer skipCount)
    {
        this.skipCount = skipCount;
    }

    public void setSkipDate (@Nullable Date skipDate)
    {
        this.skipDate = skipDate;
    }

    public void setArtWorkCount (@Nullable Integer artWorkCount)
    {
        this.artWorkCount = artWorkCount;
    }

    public void setPersistentId (@Nullable String persistentId)
    {
        this.persistentId = persistentId;
    }

    public void setTrackType (@Nullable String trackType)
    {
        this.trackType = trackType;
    }

    public void setFileFolderCount (@Nullable Integer fileFolderCount)
    {
        this.fileFolderCount = fileFolderCount;
    }

    public void setLibraryFolderCount (@Nullable Integer libraryFolderCount)
    {
        this.libraryFolderCount = libraryFolderCount;
    }

    public void setName (@Nullable String name)
    {
        this.name = name;
    }

    public void setArtist (@Nullable String artist)
    {
        this.artist = artist;
    }

    public void setAlbumArtist (@Nullable String albumArtist)
    {
        this.albumArtist = albumArtist;
    }

    public void setAlbum (@Nullable String album)
    {
        this.album = album;
    }

    public void setKind (@Nullable String kind)
    {
        this.kind = kind;
    }

    public void setLocation (@Nullable String location)
    {
        this.location = location;
    }

    public void setRating (@Nullable Integer rating)
    {
    }

    public void setCompilation (@Nullable Boolean compilation)
    {
        this.compilation = compilation;
    }
}
