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
import iexport.logging.Logging;
import iexport.parsing.builders.PlaylistBuilder;
import iexport.parsing.keys.PlaylistKeys;

/**
 * A class for converting a parsed {@link NSDictionary} from the .xml file into a {@link PlaylistBuilder}.
 * In order to turn this {@link PlaylistBuilder} into a {@link iexport.itunes.Playlist}, a few steps are missing:
 * <ul>
 *     <li>{@code parentPersistentId} of {@link PlaylistBuilder} needs to be converted into a reference to the parent {@link iexport.itunes.Playlist}
 *     <li>the {@code trackIds} list of {@link PlaylistBuilder} needs to be converted into a list of the tracks as {@link iexport.itunes.Track} objects
 *     <li> the {@code depth} of the playlist needs to be computed
 *     <li> {@code children}, the list of child playlists of the playlist needs to be set.
 * </ul>
 * These steps will be performed by {@link LibraryParser}.
 */
public class PlaylistParser
{
    /**
     * The parsed JSON data.
     */
    private final NSDictionary playlistDictionary;

    /**
     * The builder that will be used to construct the playlist.
     */
    private PlaylistBuilder playlistBuilder;

    public PlaylistParser (NSDictionary playlistDictionary)
    {
        this.playlistDictionary = playlistDictionary;
        this.playlistBuilder = new PlaylistBuilder();
    }

    /**
     * Create a {@link PlaylistBuilder} representing the parsed playlist
     * <p>
     * In order to be able to be turned into an immutable {@link iexport.itunes.Playlist},
     * additional steps will need to be performed by {@link LibraryParser}.
     *
     * @return the playlist builder
     */
    public PlaylistBuilder parse ()
    {
        // parse the keys
        parseKeys();

        // parse the Tracks array
        parsePlaylistItems();

        // reset the PlaylistBuilder in case someone makes the mistake of using this method twice
        PlaylistBuilder result = playlistBuilder;
        playlistBuilder = new PlaylistBuilder();
        return result;
    }

    /**
     * Parse the keys of the playlist in the .xml file (encoded in as {@link NSDictionary})
     * by calling the key handlers from {@link PlaylistKeys} to set the fields of the {@link PlaylistBuilder}.
     */
    private void parseKeys ()
    {
        // for each each key, call the corresponding handler
        for (var keyValuePair : playlistDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();

            var handler = PlaylistKeys.getHandlerFor(key);

            if (handler != null)
            {
                // a handler for this key exists
                handler.accept(playlistBuilder, value);
            }
            else
            {
                // no handler for this key exists
                Logging.getLogger().info(PlaylistParser.class + ": No handler for key \"" + key + "\" with value \"" + value.toString() + "\"");
            }
        }

    }

    /**
     * Parse the "Playlist Items" array in the .xml file to detect the tracks of the playlist.
     * by calling the key handlers from {@link PlaylistKeys} to set the fields of the {@link PlaylistBuilder}.
     */
    private void parsePlaylistItems ()
    {
        // get the object for the key "Playlist Items"
        Object playlistItemsArrayObject = playlistDictionary.get("Playlist Items");

        if (playlistItemsArrayObject == null)
        {
//            Logger.log(LogLevel.DEV_WARNING, this.getClass() + ": Playlist " + playlistBuilder.toString() + " has no track array.");
            // TODO
            return;
        }

        // this should be an <array>, try to convert it
        NSArray playlistItemsArray;
        try
        {
            playlistItemsArray = (NSArray) playlistItemsArrayObject;
        }
        catch (ClassCastException e)
        {
            Logging.getLogger().important(this.getClass() + ": Playlist " + playlistBuilder.toString() + " has track array of unexpected type " + playlistItemsArrayObject.getClass() + ", skipping it");
            return;
        }

        /*
         * each entry in the array will be a dictionary with a single entry,
         * e.g.
         * <dict>
         *     <key>Track ID</key><integer>9171</integer>
         * </dict>
         */
        for (var trackDictionaryObject : playlistItemsArray.getArray())
        {
            // try to convert it into a dictionary object
            NSDictionary trackDictionary;
            try
            {
                trackDictionary = (NSDictionary) trackDictionaryObject;
            }
            catch (ClassCastException e)
            {
                Logging.getLogger().important(this.getClass() + ": Track array of playlist " + playlistBuilder + " contains entry of unexpected type " + trackDictionaryObject.getClass() + "; skipping it");
                continue;
            }

            // each dictionary should just have a single key-value pair inside it
            if (trackDictionary.count() != 1)
            {
                Logging.getLogger().important(this.getClass() + ": Dictionary " + trackDictionary + " inside track array of playlist " + playlistBuilder + " has unexpected size " + trackDictionary.count() + ", expected size 1; skipping it.");
                continue;
            }

            // the key inside the dictionary should be "Track ID
            NSObject trackIdObject = trackDictionary.get("Track ID");
            if (trackIdObject == null)
            {
                Logging.getLogger().message(this.getClass() + ": Dictionary " + trackDictionary + " inside track array of playlist " + playlistBuilder + " does not contain the key \"Track ID\"; skipping it.");
                continue;
            }

            Integer trackId;
            // the value for the key "Track ID" should be an integer
            try
            {
                trackId = (Integer) trackIdObject.toJavaObject();
            }
            catch (ClassCastException e)
            {
                Logging.getLogger().important(": Value " + trackIdObject.toJavaObject() + " inside track array of playlist " + playlistBuilder + " has unexpected type " + trackIdObject.getClass() + ", expected an integer; skipping it.");
                continue;
            }

            // we can now finally add the track id to the playlist builder
            playlistBuilder.addTrackId(trackId);
        }
    }

}
