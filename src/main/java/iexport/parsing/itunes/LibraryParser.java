package iexport.parsing.itunes;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import iexport.domain.Library;
import iexport.domain.Track;
import iexport.domain.builder.LibraryBuilder;
import iexport.domain.builder.PlaylistBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.parsing.itunes.keys.MetadataKeys;

import java.io.File;


public class LibraryParser
{

    private final File libraryFile;
    private final LibraryBuilder libraryBuilder;

    public LibraryParser (File libraryFile)
    {
        libraryBuilder = new LibraryBuilder();
        this.libraryFile = libraryFile;
    }

    public Library parse ()
            throws
            IExportParsingException
    {

        NSObject propertyList = null;
        try
        {
            propertyList = PropertyListParser.parse(libraryFile);
        }
        catch (Exception e)
        {
            throw new IExportParsingException(e);
        }
        NSDictionary rootDictionary = (NSDictionary) propertyList;

        parseMetadata(rootDictionary);

        parseTracks(rootDictionary);

        parsePlaylists(rootDictionary);

        return libraryBuilder.construct();
    }


    private void parseMetadata (NSDictionary rootDictionary)
    {
        for (var keyValuePair : rootDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();


            var handler = MetadataKeys.getHandlerFor(key);

            if (handler != null)
            {
                handler.accept(libraryBuilder, value);
            }
            else
            {
                Logger.log(LogLevel.DEV_WARNING, "No handler for library key " + key + " with value " + value.toString());
            }
        }
    }

    private void parseTracks (NSDictionary rootDictionary)
            throws
            IExportParsingException
    {

        NSDictionary tracksDictionary = (NSDictionary) rootDictionary.get("Tracks");

        for (var trackKeyDictPair : tracksDictionary.entrySet())
        {
            String trackIdString = trackKeyDictPair.getKey();
            NSDictionary trackDictionary = (NSDictionary) trackKeyDictPair.getValue();
            TrackParser trackParser = new TrackParser(libraryBuilder, trackIdString, trackDictionary);
            Track track = trackParser.parse();
            libraryBuilder.addTrack(track);
        }
    }


    private void parsePlaylists (NSDictionary rootDictionary)
    {
        NSArray playlistsArray = (NSArray) rootDictionary.get("Playlists");

        for (var playlistObject : playlistsArray.getArray())
        {
            NSDictionary playlistDictionary = (NSDictionary) playlistObject;
            PlaylistParser playlistParser = new PlaylistParser(libraryBuilder, playlistDictionary);
            PlaylistBuilder playlistBuilder = playlistParser.parse();
            libraryBuilder.addPlaylist(playlistBuilder);
        }
    }

}
