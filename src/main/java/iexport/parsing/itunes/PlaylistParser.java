package iexport.parsing.itunes;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import iexport.domain.builder.LibraryBuilder;
import iexport.domain.builder.PlaylistBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.parsing.itunes.keys.PlaylistKeys;

/*
    <dict>
        <key>Master</key><true/>
        <key>Playlist ID</key><integer>12591</integer>
        <key>Playlist Persistent ID</key><string>AE64AF5F2638C280</string>
        <key>All Items</key><true/>
        <key>Visible</key><false/>
        <key>Name</key><string>Library</string>
        <key>Playlist Items</key>
        <array>
            <dict>
                <key>Track ID</key><integer>1723</integer>
            </dict>
            ...
        </array>

    </dict>

 */
public class PlaylistParser
{
    private final LibraryBuilder libraryBuilder;
    private final NSDictionary playlistDictionary;
    private final PlaylistBuilder playlistBuilder;

    public PlaylistParser (LibraryBuilder libraryBuilder, NSDictionary playlistDictionary)
    {
        this.libraryBuilder = libraryBuilder;
        this.playlistDictionary = playlistDictionary;
        this.playlistBuilder = new PlaylistBuilder();
    }

    public PlaylistBuilder parse ()
    {
        for (var keyValuePair : playlistDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();

            var handler = PlaylistKeys.getHandlerFor(key);

            if (handler != null)
            {
                handler.accept(playlistBuilder, value);
            }
            else
            {
                Logger.log(LogLevel.DEV_WARNING, "No handler for playlist key " + key + " with value " + value.toString());
            }
        }

        Object trackArrayObject = playlistDictionary.get("Playlist Items");

        if (trackArrayObject == null)
        {
            Logger.log(LogLevel.DEV_WARNING, "Playlist " + playlistBuilder + " has no track array - skipping it");
            return null;
        }

        NSArray trackArray;
        try
        {
            trackArray = (NSArray) trackArrayObject;
        }
        catch (ClassCastException e)
        {
            Logger.log(LogLevel.WARNING, "Playlist " + playlistBuilder + " has track array of unexpected type. Skipping playlist.");
            Logger.log(LogLevel.WARNING, 1, e.getMessage());
            return null;
        }

        for (var trackDictionaryObject : trackArray.getArray())
        {
            NSDictionary trackDictionary;
            try
            {
                trackDictionary = (NSDictionary) trackDictionaryObject;
            }
            catch (ClassCastException e)
            {
                Logger.log(LogLevel.WARNING, "Skipping unexpected track entry " + trackDictionaryObject);
                Logger.log(LogLevel.WARNING, 1, e.getMessage());
                continue;
            }

            if (trackDictionary.count() != 1)
            {
                Logger.log(LogLevel.WARNING, "Track dictionary " + trackDictionary + " has unexpected size " + trackDictionary.count() + ". Skipping it.");
                continue;
            }

            NSObject trackIdObject = trackDictionary.get("Track ID");

            if (trackIdObject == null)
            {
                Logger.log(LogLevel.WARNING, "Track dictionary " + trackDictionary + " does not contain key \"Track ID\". Skipping it.");
                continue;
            }

            try
            {
                Integer trackId = (Integer) trackIdObject.toJavaObject();
                playlistBuilder.addTrackWithId(trackId);
            }
            catch (ClassCastException e)
            {
                Logger.log(LogLevel.WARNING, "Key \"Track ID\" in track dictionary " + trackDictionary + " has unexpected value " + trackIdObject + ". Skipping it.");
                Logger.log(LogLevel.WARNING, 1, e.getMessage());
                continue;
            }
        }

        return playlistBuilder;
    }

}
