package iexport.parsing.itunes.keys;

import iexport.domain.builder.TrackBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.parsing.itunes.IExportParsingException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TrackKeys
{

    private static Map<String, BiConsumer<TrackBuilder, Object>> handlers = new HashMap<>();

    public static BiConsumer<TrackBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    static
    {
        handlers.put("Track ID",
                (TrackBuilder trackFactory, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        try
                        {
                            trackFactory.verifyTrackId((Integer) value);
                        }
                        catch (IExportParsingException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "TrackId " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Size " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Total Time " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Track Number " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Track Count " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Track Count " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Bit Rate " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sample Rate " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Play Count" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Skip Count" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Artwork Count" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "File Folder Count" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Library Folder Count" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Date Modified" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Release Date " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        // we use Play Date UTC
        handlers.put("Play Date",
                (TrackBuilder trackFactory, Object value)
                        -> {
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
                        Logger.log(LogLevel.DEV_WARNING, "Rating " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Album Rating " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Comments " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Equalizer " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Play Date UTC" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Date Added" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Skip Date" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Persistent ID" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Track Type" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Name" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Artist" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Album Artist" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Album" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Kind" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Location " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sort Name " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sort Album " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sort Artist " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sort Album Artist" + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Composer " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Sort Composer " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Genre " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Disk Count " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Disk Number " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "BPM " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Stop Time " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Start Time " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Volume Adjustment " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Rating Computed " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Disabled " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Compilation " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Album Rating Computed " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Loved " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Work " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Grouping " + value.toString() + " of unexpected type " + value.getClass().toString());
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
                        Logger.log(LogLevel.DEV_WARNING, "Boolean " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
    }

    private TrackKeys ()
    {
    }
}
