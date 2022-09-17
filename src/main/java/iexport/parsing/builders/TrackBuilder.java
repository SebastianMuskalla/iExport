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

import iexport.itunes.Track;

import java.util.Date;

/**
 * A mutable builder class for building records of type {@link Track}.
 * <p>
 * The fields correspond to the fields of {@link Track}, see {@link Track} for the documentation.
 */
public class TrackBuilder
{
    private Integer trackId;
    private Integer trackNumber;
    private Integer totalTime;
    private Integer year;
    private Integer bitRate;
    private Integer sampleRate;
    private Integer artWorkCount;
    private Integer fileFolderCount;
    private Integer trackCount;
    private Integer playCount;
    private Integer skipCount;
    private Integer libraryFolderCount;
    private Integer size;
    private Integer rating;
    private Integer albumRating;
    private Integer discNumber;
    private Integer discCount;
    private Integer bpm;
    private Integer startTime;
    private Integer stopTime;
    private Integer volumeAdjustment;

    private Long playDate;

    private String location;
    private String persistentId;
    private String trackType;
    private String name;
    private String artist;
    private String albumArtist;
    private String album;
    private String kind;
    private String comments;
    private String equalizer;
    private String work;
    private String grouping;
    private String sortName;
    private String sortArtist;
    private String sortAlbumArtist;
    private String sortAlbum;
    private String composer;
    private String sortComposer;
    private String genre;

    private Date dateModified;
    private Date dateAdded;
    private Date skipDate;
    private Date playDateUTC;
    private Date releaseDate;

    private Boolean disabled;
    private Boolean loved;
    private Boolean ratingComputed;
    private Boolean albumRatingComputed;
    private Boolean compilation;
    private Boolean disliked;

    public TrackBuilder ()
    {
    }

    public void setTrackId (Integer value)
    {
        trackId = value;
    }

    public void setDisliked (Boolean disliked)
    {
        this.disliked = disliked;
    }

    public void setTrackCount (Integer trackCount)
    {
        this.trackCount = trackCount;
    }

    public void setAlbumRating (Integer albumRating)
    {
        this.albumRating = albumRating;
    }

    public void setDiscNumber (Integer discNumber)
    {
        this.discNumber = discNumber;
    }

    public void setDiscCount (Integer discCount)
    {
        this.discCount = discCount;
    }

    public void setBpm (Integer bpm)
    {
        this.bpm = bpm;
    }

    public void setStartTime (Integer startTime)
    {
        this.startTime = startTime;
    }

    public void setStopTime (Integer stopTime)
    {
        this.stopTime = stopTime;
    }

    public void setVolumeAdjustment (Integer volumeAdjustment)
    {
        this.volumeAdjustment = volumeAdjustment;
    }

    public void setDisabled (Boolean disabled)
    {
        this.disabled = disabled;
    }

    public void setLoved (Boolean loved)
    {
        this.loved = loved;
    }

    public void setRatingComputed (Boolean ratingComputed)
    {
        this.ratingComputed = ratingComputed;
    }

    public void setAlbumRatingComputed (Boolean albumRatingComputed)
    {
        this.albumRatingComputed = albumRatingComputed;
    }

    public void setReleaseDate (Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setComments (String comments)
    {
        this.comments = comments;
    }

    public void setEqualizer (String equalizer)
    {
        this.equalizer = equalizer;
    }

    public void setWork (String work)
    {
        this.work = work;
    }

    public void setGrouping (String grouping)
    {
        this.grouping = grouping;
    }

    public void setSortName (String sortName)
    {
        this.sortName = sortName;
    }

    public void setSortArtist (String sortArtist)
    {
        this.sortArtist = sortArtist;
    }

    public void setSortAlbumArtist (String sortAlbumArtist)
    {
        this.sortAlbumArtist = sortAlbumArtist;
    }

    public void setSortAlbum (String sortAlbum)
    {
        this.sortAlbum = sortAlbum;
    }

    public void setComposer (String composer)
    {
        this.composer = composer;
    }

    public void setSortComposer (String sortComposer)
    {
        this.sortComposer = sortComposer;
    }

    public void setGenre (String genre)
    {
        this.genre = genre;
    }

    public void setSize (Integer size)
    {
        this.size = size;
    }

    public void setTrackNumber (Integer trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    public void setTotalTime (Integer totalTime)
    {
        this.totalTime = totalTime;
    }

    public void setYear (Integer year)
    {
        this.year = year;
    }

    public void setDateModified (Date dateModified)
    {
        this.dateModified = dateModified;
    }

    public void setDateAdded (Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public void setBitRate (Integer bitRate)
    {
        this.bitRate = bitRate;
    }

    public void setPlayCount (Integer playCount)
    {
        this.playCount = playCount;
    }

    public void setSampleRate (Integer sampleRate)
    {
        this.sampleRate = sampleRate;
    }

    public void setPlayDateUTC (Date playDateUTC)
    {
        this.playDateUTC = playDateUTC;
    }

    public void setSkipCount (Integer skipCount)
    {
        this.skipCount = skipCount;
    }

    public void setSkipDate (Date skipDate)
    {
        this.skipDate = skipDate;
    }

    public void setArtWorkCount (Integer artWorkCount)
    {
        this.artWorkCount = artWorkCount;
    }

    public void setPersistentId (String persistentId)
    {
        this.persistentId = persistentId;
    }

    public void setTrackType (String trackType)
    {
        this.trackType = trackType;
    }

    public void setFileFolderCount (Integer fileFolderCount)
    {
        this.fileFolderCount = fileFolderCount;
    }

    public void setLibraryFolderCount (Integer libraryFolderCount)
    {
        this.libraryFolderCount = libraryFolderCount;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public void setArtist (String artist)
    {
        this.artist = artist;
    }

    public void setAlbumArtist (String albumArtist)
    {
        this.albumArtist = albumArtist;
    }

    public void setPlayDate (Long playDate)
    {
        this.playDate = playDate;
    }

    public void setAlbum (String album)
    {
        this.album = album;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public void setRating (Integer rating)
    {
        this.rating = rating;
    }

    public void setCompilation (Boolean compilation)
    {
        this.compilation = compilation;
    }

    /**
     * Build an immutable {@link Track}.
     *
     * @return the track
     */
    public Track build ()
    {
        return new Track(trackId,
                location,
                persistentId,
                name,
                artist,
                album,
                albumArtist,
                sortName,
                sortAlbumArtist,
                sortAlbum,
                composer,
                sortArtist,
                year,
                dateAdded,
                discNumber,
                discCount,
                totalTime,
                trackNumber,
                dateModified,
                bitRate,
                skipDate,
                skipCount,
                sampleRate,
                sortComposer,
                playDate,
                playCount,
                playDateUTC,
                trackCount,
                artWorkCount,
                libraryFolderCount,
                kind,
                size,
                rating,
                albumRating,
                trackType,
                startTime,
                bpm,
                stopTime,
                fileFolderCount,
                volumeAdjustment,
                disabled,
                disliked,
                ratingComputed,
                albumRatingComputed,
                compilation,
                loved,
                releaseDate,
                comments,
                equalizer,
                work,
                grouping,
                genre);
    }

    @Override
    public String toString ()
    {
        return "TrackBuilder for "
                + ((artist != null) ? artist : "UNKNOWN ARTIST")
                + " - "
                + ((name != null) ? name : "UNKNOWN TITLE")
                + " {"
                + ((trackId != null) ? "trackId=" + trackId : "")
                + ((persistentId != null) ? ", persistentId=" + persistentId : "")
                + '}';
    }
    
}
