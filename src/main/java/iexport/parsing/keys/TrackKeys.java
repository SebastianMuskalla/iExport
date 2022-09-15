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

package iexport.parsing.keys;

import iexport.logging.Logging;
import iexport.parsing.TrackParser;
import iexport.parsing.builders.TrackBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The keys needed to parse tracks.
 * <p>
 * These are needed to populate the fields of {@link TrackBuilder}.
 * <p>
 * This class is fully static and cannot be instantiated.
 */
public class TrackKeys
{
    /**
     * A map that maps keys from the .xml file to the appropriate handler.
     */
    private static final Map<String, BiConsumer<TrackBuilder, Object>> handlers = new HashMap<>();

    /**
     * Get the handler for the specified key.
     * <p>
     * Each handler is of type {@code BiConsumer<TrackBuilder, Object>}, i.e. it provides a method that takes a {@link TrackBuilder} and parsed value of type {@code Object} and assigns the parsed value to the {@link TrackBuilder} if the types match.
     *
     * @param key the key.
     * @return the handler.
     */
    public static BiConsumer<TrackBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    /**
     * Logs a warning if a parsed value has an unexpected type and cannot be set.
     *
     * @param key            The key.
     * @param value          The value.
     * @param unexpectedType The type of the parsed value.
     * @param expectedType   The expected type of the parsed value.
     */
    @SuppressWarnings("rawtypes")
    private static void logUnexpectedType (String key, String value, Class unexpectedType, Class expectedType)
    {
        Logging.getLogger().info(TrackParser.class + ": key \"" + key + "\" with value \"" + value + "\" is of unexpected type \"" + unexpectedType.toString() + "\" expected \"" + expectedType + "\"");
    }

    static
    {
        handlers.put("Track ID",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setTrackId((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Track ID", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Size",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setSize((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Size", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Total Time",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setTotalTime((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Total Time", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Track Number",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setTrackNumber((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Track Number", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Track Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setTrackCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Track Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Year",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setYear((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Year", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Bit Rate",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setBitRate((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Bit Rate", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Sample Rate",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setSampleRate((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Sample Rate", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Play Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setPlayCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Play Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Skip Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setSkipCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Skip Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Artwork Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setArtWorkCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Artwork Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("File Folder Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setFileFolderCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("File Folder Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Library Folder Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setLibraryFolderCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Library Folder Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Date Modified",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        trackFactory.setDateModified((Date) value);
                    }
                    else
                    {
                        logUnexpectedType("Date Modified", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

        handlers.put("Release Date",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        trackFactory.setReleaseDate((Date) value);
                    }
                    else
                    {
                        logUnexpectedType("Release Date", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

        handlers.put("Rating",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setRating((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Rating", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Play Date",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Long.class))
                    {
                        trackFactory.setPlayDate((Long) value);
                    }
                    else
                    {
                        logUnexpectedType("Play Date", value.toString(), value.getClass(), Long.class);
                    }
                }
        );

        handlers.put("Album Rating",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setAlbumRating((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Album Rating", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Comments",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setComments((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Comments", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Equalizer",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setEqualizer((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Equalizer", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Play Date UTC",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        trackFactory.setPlayDateUTC((Date) value);
                    }
                    else
                    {
                        logUnexpectedType("Play Date UTC", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

        handlers.put("Date Added",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        trackFactory.setDateAdded((Date) value);
                    }
                    else
                    {
                        logUnexpectedType("Date Added", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

        handlers.put("Skip Date",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        trackFactory.setSkipDate((Date) value);
                    }
                    else
                    {
                        logUnexpectedType("Skip Date", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

        handlers.put("Persistent ID",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setPersistentId((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Persistent ID", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Track Type",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setTrackType((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Track Type", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Name",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setName((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Name", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Artist",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setArtist((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Artist", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Album Artist",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setAlbumArtist((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Album Artist", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Album",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setAlbum((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Album", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Kind",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setKind((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Kind", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Location",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setLocation((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Location", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Sort Name",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setSortName((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Sort Name", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Sort Album",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setSortAlbum((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Sort Album", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Sort Artist",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setSortArtist((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Sort Artist", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Sort Album Artist",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setSortAlbumArtist((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Sort Album Artist", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Composer",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setComposer((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Composer", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Sort Composer",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setSortComposer((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Sort Composer", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Genre",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setGenre((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Genre", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Disc Count",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setDiscCount((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Disc Count", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Disc Number",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setDiscNumber((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Disc Number", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("BPM",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setBpm((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("BPM", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Stop Time",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setStopTime((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Stop Time", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );


        handlers.put("Start Time",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setStartTime((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Start Time", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );


        handlers.put("Volume Adjustment",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        trackFactory.setVolumeAdjustment((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Volume Adjustment", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Rating Computed",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setRatingComputed((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Rating Computed", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );

        handlers.put("Disabled",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setDisabled((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Disabled", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );

        handlers.put("Compilation",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setCompilation((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Compilation", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );

        handlers.put("Album Rating Computed",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setAlbumRatingComputed((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Album Rating Computed", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );


        handlers.put("Loved",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setLoved((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Loved Computed", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );

        handlers.put("Work",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setWork((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Work", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Grouping",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        trackFactory.setGrouping((String) value);
                    }
                    else
                    {
                        logUnexpectedType("Grouping", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Disliked",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        trackFactory.setDisliked((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Disliked", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );
    }

    private TrackKeys ()
    {
    }
}
