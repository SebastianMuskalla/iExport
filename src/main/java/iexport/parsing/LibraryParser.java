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

package iexport.parsing;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.parsing.builders.LibraryBuilder;
import iexport.parsing.builders.PlaylistBuilder;
import iexport.parsing.keys.LibraryKeys;
import iexport.parsing.sorting.PlaylistComparator;
import iexport.parsing.sorting.TrackComparator;
import iexport.settings.ParsingSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main class of the iExport parsing module.
 * <p>
 * It will take a (link to an) .xml file of type {@link File} a convert it into a {@link Library}.
 * <p>
 * It proceeds as follows:
 * <ol>
 *     <li> Create a {@link LibraryBuilder}.
 *     <li> Parse the metadata of the library (the keys whose values are not arrays or dictionaries)
 *     using the handlers from {@link LibraryKeys}
 *     and  set the fields of the {@link LibraryBuilder}.
 *     <li> Parse the "Tracks" dictionary of the library, obtaining a list of {@link Track}
 *     <li> Parse the "Playlists" array of the library, obtaining a list of {@link PlaylistBuilder}
 * <p>
 *          It now remains to construct the relationships between tracks and playlists and the relationship among playlists.
 *     <li> We resolve the parent-child relationships between the playlists.
 *     While doing so, we convert each {@link PlaylistBuilder} into an actual {@link Playlist}.
 *     <li> We turn the list of track ids of type {@link Integer} of each {@link PlaylistBuilder} into an actual list of {@link Track} objects
 *     <li> Finally, we sort the playlists and tracks using the comparators from {@link iexport.parsing.sorting}
 * </ol>
 */
public class LibraryParser
{
    /**
     * The file that should be parsed.
     */
    private final File libraryFile;

    /**
     * The settings that will be used for parsing.
     */
    private final ParsingSettings parsingSettings;

    /**
     * List of playlist persistent ids that should be ignored.
     */
    private final List<String> ignoredPlaylistPersistentIds = new ArrayList<>();

    /**
     * A map that takes a Playlist Persistent ID and returns the associated {@link PlaylistBuilder}.
     */
    private final Map<String, PlaylistBuilder> playlistsBuildersByPersistentId = new HashMap<>();

    /**
     * A map that takes a Playlist Persistent ID and returns the associated {@link Playlist}
     * once it has been constructed.
     */
    private final Map<String, Playlist> playlistsByPersistentId = new HashMap<>();

    /**
     * The builders for the playlists in this library.
     * <p>
     * {@link iexport.parsing.LibraryParser} will convert these builders into actual playlists.
     */
    private final List<PlaylistBuilder> playlistBuilders = new ArrayList<>();

    /**
     * A map that maps track ids to tracks.
     * <p>
     * Will be needed for parsing playlists.
     */
    private final Map<Integer, Track> tracksById = new HashMap<>();

    /**
     * The builder that will be used to construct the library.
     */
    private LibraryBuilder libraryBuilder;

    public LibraryParser (File libraryFile, ParsingSettings parsingSettings)
    {
        this.parsingSettings = parsingSettings;
        libraryBuilder = new LibraryBuilder();
        this.libraryFile = libraryFile;
    }

    /**
     * Parse the file as described in the documation of {@link LibraryParser}.
     *
     * @return the parsed library
     * @throws ITunesParsingException if parsing fails
     */
    public Library parse ()
            throws ITunesParsingException
    {

        // The dictionary that is at the root of the parsed file.
        NSDictionary rootDictionary = parseAndGetRootDictionary();

        // Parse the keys of the root dictionary itself that are not arrays or dictionaries.
        parseMetadata(rootDictionary);

        // Parse the "Tracks" dictionary.
        parseTracks(rootDictionary);

        // Parse the Playlists array.
        parsePlaylists(rootDictionary);

        // Set the parent-child relationships between the playlists,
        // in turn converting PlaylistBuilders into actual Playlists.
        processPlaylistBuilders();

        // Turn the track ids of the playlist builders into actual tracks.
        convertPlaylistTrackIdListToTrackList();

        // We can now build the library.
        Library library = libraryBuilder.build();

        // Reset this object in case someone uses it twice.
        reset();

        // Sort tracks and playlists.
        sortLibrary(library);

        return library;
    }

    /**
     * This method adds a track to a {@link LibraryBuilder}.
     * <p>
     * It will check that the library does not already contain a track with this Track ID
     *
     * @param track the track to add
     */
    void addTrackToLibrary (Track track)
    {
        // Check for a duplicate.
        Integer trackId = track.trackId();
        if (tracksById.get(trackId) != null)
        {
            Logging.getLogger().warning("Library already contains track with id " + trackId + "; Skipping new track.");
            Logging.getLogger().debug(1, "Old track:" + tracksById.get(trackId));
            Logging.getLogger().debug(1, "New track:" + track);
            return;
        }

        // No duplicate, we can add the track.
        tracksById.put(track.trackId(), track);
        libraryBuilder.getTracks().add(track);
    }

    /**
     * Resets the internal state of the parser in case somebody tries to use the same parser twice
     */
    private void reset ()
    {
        libraryBuilder = new LibraryBuilder();

        ignoredPlaylistPersistentIds.clear();
        playlistsBuildersByPersistentId.clear();
        playlistsByPersistentId.clear();
        playlistBuilders.clear();
        tracksById.clear();
    }

    /**
     * Populate {@link Playlist#tracks} by converting the list of track ids, {@link PlaylistBuilder#trackIds},
     * into a list of real {@link Track} objects using the tracks that have been parsed.
     */
    private void convertPlaylistTrackIdListToTrackList ()
    {
        for (Playlist playlist : libraryBuilder.getPlaylists())
        {
            for (Integer trackId : playlistsBuildersByPersistentId.get(playlist.playlistPersistentId()).getTrackIds())
            {
                Track track = tracksById.get(trackId);

                if (track == null)
                {
                    Logging.getLogger().warning("Playlist " + playlist + " should contain track with track id " + trackId + ", but this track does not exist; skipping it");
                    continue;
                }

                playlist.addTrack(track);
            }
        }
    }

    /**
     * Parse {@link #libraryFile} into a property list and then get the {@link NSDictionary}
     * at the root of the property list files.
     *
     * @return the dictionary at the root of the property list file
     * @throws ITunesParsingException if parsing fails in an non-recoverable way. Otherwise, we will just log a warning.
     */
    private NSDictionary parseAndGetRootDictionary ()
            throws ITunesParsingException
    {
        // Parse the file as a property list.
        NSObject propertyList;
        try
        {
            propertyList = PropertyListParser.parse(libraryFile);
        }
        catch (Exception e)
        {
            throw new ITunesParsingException(this.getClass().getSimpleName() + ": Parsing the library file as a property list has failed", e);
        }

        // Get the root dictionary.
        NSDictionary rootDictionary;
        try
        {
            rootDictionary = (NSDictionary) propertyList;
        }
        catch (ClassCastException e)
        {
            throw new ITunesParsingException(this.getClass().getSimpleName() + ": Expected a dictionary at the root of the library file, got " + propertyList.getClass() + " instead", e);
        }

        return rootDictionary;
    }

    /**
     * Parse the metadata of the library.
     * <p>
     * This method will parse the keys of the library in the .xml file (encoded in as {@link NSDictionary})
     * by calling the key handlers from {@link LibraryKeys} to set the fields of the {@link LibraryBuilder}.
     *
     * @param rootDictionary the root dictionary of the library file
     */
    private void parseMetadata (NSDictionary rootDictionary)
    {
        for (var keyValuePair : rootDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();

            if (value != null)
            {
                var handler = LibraryKeys.getHandlerFor(key);

                if (handler != null)
                {
                    // A handler for this key exists
                    handler.accept(libraryBuilder, value);
                }
                else
                {
                    Logging.getLogger().debug("No handler for library key \"" + key + "\" with value \"" + value + "\"");
                }
            }
        }
    }

    /**
     * Sort the playlists and tracks using the comparators from {@link iexport.parsing.sorting}
     * <p>
     * We sort all playlists ({@link Library#playlists}, {@link Library#playlistsAtTopLevel}, and {@link Playlist#children} of each playlist using {@link iexport.parsing.sorting.PlaylistComparator}.
     * <p>
     * We sort all tracks ({@link Library#tracks} and {@link Playlist#tracks} of each playlist using {@link iexport.parsing.sorting.TrackComparator}.
     *
     * @param library the library that should be sorted
     */
    private void sortLibrary (Library library)
    {
        var playlistComparator = new PlaylistComparator();
        var trackComparator = new TrackComparator();

        library.playlistsAtTopLevel().sort(playlistComparator);
        library.playlists().sort(playlistComparator);
        library.tracks().sort(trackComparator);

        for (Playlist playlist : library.playlists())
        {
            playlist.tracks().sort(trackComparator);
            playlist.children().sort(playlistComparator);
        }
    }

    /**
     * We need to set the parent-child relationships of the playlists.
     * <p>
     * We need to populate the following fields of {@link PlaylistBuilder}:
     *     <ul>
     *         <li> {@link PlaylistBuilder#parent} - the parent playlist (if available)
     *         <li> {@link PlaylistBuilder#depth} - the depth from the root playlist
     *     </ul>
     * <p>
     * After these fields have been set, we can turn each {@link PlaylistBuilder} into a {@link Playlist} object.
     * We then add this object to the list of playlists, and either set is as a top-level playlist or add it to its parent as a child playlist.
     * <p>
     * In order to the identify the parent, we have to go through the list of PlaylistBuilders multiple times.
     * We first find the PlaylistBuilders that have no parent (i.e. builders for top-level playlists) and construct the corresponding {@link Playlist}.
     * Then we find the PlaylistBuilders that have already constructed Playlists as their parents, and so on.
     * <p>
     * Within finitely many iterations (at most the number of playlists squared), we should be able to resolve all dependencies.
     */
    private void processPlaylistBuilders ()
    {

        int iterationCount = 0;

        int numberOfPlaylists = playlistBuilders.size();

        // The maximum numbers of iterations we will do in order to avoid an infinite loop.
        // In the worst case, the tree is a single sequence.
        // In this case, we will need to go through the full workList (of size numberOfPlaylists) to build one Playlist,
        // i.e. we need numberOfPlaylists * numberOfPlaylists iterations to construct all playlists.
        int maximumNumberOfIterations = numberOfPlaylists * numberOfPlaylists + numberOfPlaylists + 1;

        // Copy all playlist builders into a work list.
        List<PlaylistBuilder> workList = new ArrayList<>(playlistBuilders);

        while (!workList.isEmpty())
        {
            iterationCount++;

            // We have exceeded the maximum number of iterations without resolving all dependencies.
            if (iterationCount > maximumNumberOfIterations)
            {
                Logging.getLogger().error("Failed to resolve dependencies among playlists.");
                Logging.getLogger().debug("Playlists with unresolved dependencies:");

                for (PlaylistBuilder remaining : workList)
                {
                    Logging.getLogger().debug(1, remaining.toString());
                }
                Logging.getLogger().error("Continuing with the libraries that could be processed.");
                return;
            }

            // Take and remove the first item from the work list.
            PlaylistBuilder playlistBuilder = workList.get(0);
            workList.remove(playlistBuilder);

            // Check if we should ignore this playlist.
            if (shouldBeIgnored(playlistBuilder))
            {
                if (playlistBuilder.getPlaylistPersistentId() != null)
                {
                    Logging.getLogger().debug("Adding " + playlistBuilder.getPlaylistPersistentId() + " to the list of ignored playlists.");
                    ignoredPlaylistPersistentIds.add(playlistBuilder.getPlaylistPersistentId());
                }
                continue;
            }

            // We want to compute the parent playlist and the depth.
            Playlist parent = null;
            int depth;

            // Check if this list has a parent.
            String parentPersistentId = playlistBuilder.getParentPersistentId();
            if (parentPersistentId != null)
            {
                // Check if we should ignore the parent playlist.
                if (ignoredPlaylistPersistentIds.contains(parentPersistentId))
                {
                    Logging.getLogger().debug("Ignoring " + playlistBuilder
                            + " because its parent with persistent id " + parentPersistentId + " has been ignored.");

                    if (playlistBuilder.getPlaylistPersistentId() != null)
                    {
                        Logging.getLogger().debug("dding " + playlistBuilder.getPlaylistPersistentId()
                                + " to the list of ignored playlists.");
                        ignoredPlaylistPersistentIds.add(playlistBuilder.getPlaylistPersistentId());
                    }

                    continue;
                }

                // Check if we have already constructed this parent playlist.
                parent = playlistsByPersistentId.get(parentPersistentId);
                if (parent == null)
                {
                    // The parent has not been constructed.
                    // Either this is because it not yet been processed, or it does not exist at all.
                    if (playlistsBuildersByPersistentId.get(parentPersistentId) == null)
                    {
                        // The playlist with the specified Parent Persistent ID does not exist.
                        // Remove from work list and issue a warning.
                        Logging.getLogger().warning("Playlist " + playlistBuilder + " specifies parent playlist with Persistent ID " + parentPersistentId + ", but no such playlist exists");
                    }
                    else
                    {
                        // The parent playlist exists, but has not been processed yet
                        // Simply add the current playlist back to the end of the work list.
                        // simply requeue this playlist for later construction, and skip it for now
                        workList.add(playlistBuilder);
                    }
                    continue;
                }
            }

            // Either this playlist has no parent (i.e. parent is null), or we have found it successfully
            playlistBuilder.setParent(parent);

            if (parent == null)
            {
                // this playlist has no parent - it is a top-level playlist
                depth = 0;
            }
            else // parent != null
            {
                // we can compute the depth
                depth = parent.depth() + 1;
            }
            playlistBuilder.setDepth(depth);

            // we can now finally turn the PlaylistBuilder into a real Playlist object
            Playlist playlist = playlistBuilder.build();

            // do some additional maintenance
            libraryBuilder.getPlaylists().add(playlist);
            playlistsByPersistentId.put(playlist.playlistPersistentId(), playlist);

            if (parent == null)
            {
                // this playlist has no parent - it is a top-level playlist
                libraryBuilder.getPlaylistsAtTopLevel().add(playlist);
            }
            else // parent != null
            {
                // this playlist has a parent, and we should add it as a child playlist
                parent.addChild(playlist);
            }

            // compute the ancestry
            if (parent != null)
            {
                playlist.ancestry().addAll(parent.ancestry());
            }
            playlist.ancestry().add(playlist);

        } // while !workList.isEmpty()

        Logging.getLogger().debug("Finished resolving all playlist dependencies within " + iterationCount + " iterations.");
    }

    /**
     * Parse the tracks in the library from the dictionary with the key "Tracks".
     *
     * @param rootDictionary the root dictionary of the library file
     */
    private void parseTracks (NSDictionary rootDictionary)
    {
        // get the object for the key "Tracks"
        Object tracksObject = rootDictionary.get("Tracks");
        if (tracksObject == null)
        {
            Logging.getLogger().warning("Library " + libraryBuilder + " has no Tracks dictionary.");
            return;
        }

        // this should be a dictionary, convert it
        NSDictionary tracksDictionary;
        try
        {
            tracksDictionary = (NSDictionary) tracksObject;
        }
        catch (ClassCastException e)
        {
            Logging.getLogger().warning("Library " + libraryBuilder + " has Tracks dictionary of unexpected type " + tracksObject.getClass() + ", expected NSDictionary; skipping it");
            return;
        }

        /*
         * each entry in the dictionary will be of the shape
         * <key>2177</key>
         * <dict>
         *     <key>Track ID</key><integer>2177</integer>
         *	   <key>Size</key><integer>8234772</integer>
         *     // other track data
         * </dict>
         *
         */
        for (var trackIdTrackDictionaryPair : tracksDictionary.entrySet())
        {
            // extract the Track ID from the key of the pair, and convert it to an integer
            String trackIdKey = trackIdTrackDictionaryPair.getKey();
            int trackId;
            try
            {
                trackId = Integer.parseInt(trackIdKey);
            }
            catch (Exception e)
            {
                Logging.getLogger().warning("Track with key Track ID  \"" + trackIdKey + "\" is of unexpected type " + tracksObject.getClass() + ", expected an integer; skipping it");
                continue;
            }

            // try to convert the value as a dictionary
            NSDictionary trackDictionary;
            try
            {
                trackDictionary = (NSDictionary) trackIdTrackDictionaryPair.getValue();
            }
            catch (ClassCastException e)
            {
                Logging.getLogger().warning("Track with id \"" + trackId + "\" has track dictionary of unexpected type " + tracksObject.getClass() + ", expected NSDictionary; skipping it");
                continue;
            }

            // we can now invoke the track parser to turned the trackDictionary into a track
            TrackParser trackParser = new TrackParser(trackDictionary);
            Track track = trackParser.parse();

            /*
             * we should verify that the two Track IDs match
             * - the Track ID from the key of the "Tracks" dictionary of the library
             * - the Track ID from the key "Track ID" of the track dictionary
             */
            if (!track.trackId().equals(trackId))
            {
                Logging.getLogger().warning("For track " + track + ", Track ID  " + trackId + " from key does not match internal Track ID " + track.trackId() + "; skipping it");
                continue;
            }

            addTrackToLibrary(track);
        }
    }

    /**
     * Parse the playlists in the library from the array with the key "Playlists".
     *
     * @param rootDictionary the root dictionary of the library file
     */
    private void parsePlaylists (NSDictionary rootDictionary)
    {
        // get the object for the key "Tracks"
        Object playlistsObject = rootDictionary.get("Playlists");
        if (playlistsObject == null)
        {
            Logging.getLogger().warning("Library " + libraryBuilder + " has no Playlists array.");
            return;
        }

        // this should be an array, convert it
        NSArray playlistsArray;
        try
        {
            playlistsArray = (NSArray) playlistsObject;
        }
        catch (ClassCastException e)
        {
            Logging.getLogger().warning("Library " + libraryBuilder + " has Playlists array of unexpected type " + playlistsObject.getClass() + ", expected NSArray.");
            return;
        }

        // we can now parse each playlist inside the array
        for (var playlistObject : playlistsArray.getArray())
        {
            // each playlist should be a dictionary
            NSDictionary playlistDictionary;
            try
            {
                playlistDictionary = (NSDictionary) playlistObject;
            }
            catch (ClassCastException e)
            {
                Logging.getLogger().warning("Playlist dictionary " + playlistObject + " is of unexpected type " + playlistObject.getClass().getSimpleName() + ", expected NSDictionary; skipping it");
                continue;
            }

            // we can now the convert this dictionary into a PlaylistBuilder
            PlaylistParser playlistParser = new PlaylistParser(playlistDictionary);
            PlaylistBuilder playlistBuilder = playlistParser.parse();

            // we add it to the library builder object
            playlistBuilders.add(playlistBuilder);

            // converting it into an actual Playlist will be done later
            // to this end, we will need the persistent id of the playlist
            if (playlistBuilder.getPlaylistPersistentId() == null)
            {
                Logging.getLogger().warning("Playlist " + playlistBuilder + " has no Playlist Persistent ID, resolving dependencies for this playlist will likely fail.");
                continue;
            }

            playlistsBuildersByPersistentId.put(playlistBuilder.getPlaylistPersistentId(), playlistBuilder);
        }
    }

    /**
     * Check if the playlist associated to the given builder should be ignored.
     *
     * @param builder the builder
     * @return true iff it should be ignored
     */
    private boolean shouldBeIgnored (PlaylistBuilder builder)
    {
        // We have already determined that this playlist should be ignored
        if (builder.getPlaylistPersistentId() != null && ignoredPlaylistPersistentIds.contains(builder.getPlaylistPersistentId()))
        {
            return true;
        }

        // If parsing.ignoreEmptyPlaylists is set to true, ignore playlists with empty track list
        if (parsingSettings.getSettingIgnoreEmptyPlaylists())
        {
            if (builder.getTrackIds() == null || builder.getTrackIds().size() == 0)
            {
                Logging.getLogger().debug("Ignoring empty playlist " + builder + " because parsing.ignoreEmptyPlaylists is set");
                return true;
            }
        }

        // If parsing.ignoreNonMusicPlaylists is set to true,
        // ignore playlists that have audiobooks, tvShows, or movies set to true
        if (parsingSettings.getSettingIgnoreNonMusicPlaylists())
        {
            if ((builder.getAudiobooks() != null && builder.getAudiobooks())
                    || (builder.getMovies() != null && builder.getMovies())
                    || (builder.getTvShows() != null && builder.getTvShows()))
            {
                Logging.getLogger().debug("Ignoring special playlist " + builder + " because parsing.ignoreNonMusicPlaylists is set");
                return true;
            }
        }

        // If parsing.ignoreDistinguishedPlaylists is set to true,
        // ignore playlists that have a distinguishedKind set
        // ("Downloaded", ...)
        if (parsingSettings.getSettingIgnoreDistinguishedPlaylists())
        {
            if (builder.getDistinguishedKind() != null)
            {
                Logging.getLogger().debug("Ignoring distinguished playlist " + builder + " because parsing.ignoreNonMusicPlaylists is set");
                return true;
            }
        }

        // If parsing.ignoreMaster is set to true,
        // ignore playlists that have the master flag
        // (playlist "Library")
        if (parsingSettings.getSettingsIgnoreMaster())
        {
            if (builder.getMaster() != null && builder.getMaster())
            {
                Logging.getLogger().debug("Ignoring master playlist " + builder + " because parsing.ignoreNonMusicPlaylists is set");
                return true;
            }
        }

        // Ignore playlists whose name is in parsing.ignorePlaylistsByName
        if (builder.getName() != null && parsingSettings.getSettingIgnorePlaylistNames().contains(builder.getName()))
        {
            Logging.getLogger().debug("Ignoring playlist " + builder + " because its name is in parsing.ignorePlaylistsByName " + "(" + parsingSettings.getSettingIgnorePlaylistNames() + ")");
            return true;
        }

        return false;
    }

}
