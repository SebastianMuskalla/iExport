package iexport.parsing.itunes;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import iexport.domain.ITunesLibrary;
import iexport.domain.Track;
import iexport.domain.factories.ITunesLibraryFactory;
import iexport.parsing.itunes.keys.MetadataKeys;

import java.io.File;
import java.util.Date;

public class ITunesLibraryParser
{

    private final File libraryFile;
    private final ITunesLibraryFactory iTunesLibraryFactory;

    public ITunesLibraryParser (File libraryFile)
    {
        iTunesLibraryFactory = new ITunesLibraryFactory();
        this.libraryFile = libraryFile;
    }

    public ITunesLibrary parse ()
            throws
            LibraryParsingException
    {

        NSObject propertyList = null;
        try
        {
            propertyList = PropertyListParser.parse(libraryFile);
        }
        catch (Exception e)
        {
            throw new LibraryParsingException(e);
        }
        NSDictionary rootDictionary = (NSDictionary) propertyList;

        parseMetadata(rootDictionary);

        parseTracks(rootDictionary);

        return null;
    }

    private void parseMetadata (NSDictionary rootDictionary)
    {

        if (rootDictionary.containsKey(MetadataKeys.APPLICATION_VERSION))
        {
            NSObject valueNSObject = rootDictionary.get(MetadataKeys.APPLICATION_VERSION);
            Object valueObject = valueNSObject.toJavaObject();
            if (valueObject.getClass().equals(String.class))
            {
                String value = (String) valueObject;
                iTunesLibraryFactory.setApplicationVersion(value);
            }
        }

        if (rootDictionary.containsKey(MetadataKeys.DATE))
        {
            NSObject valueNSObject = rootDictionary.get(MetadataKeys.DATE);
            Object valueObject = valueNSObject.toJavaObject();
            if (valueObject.getClass().equals(Date.class))
            {
                Date value = (Date) valueObject;
                iTunesLibraryFactory.setDate(value);
            }
        }

        if (rootDictionary.containsKey(MetadataKeys.LIBRARY_PERSISTENT_ID))
        {
            NSObject valueNSObject = rootDictionary.get(MetadataKeys.LIBRARY_PERSISTENT_ID);
            Object valueObject = valueNSObject.toJavaObject();
            if (valueObject.getClass().equals(String.class))
            {
                String value = (String) valueObject;
                iTunesLibraryFactory.setPersistentId(value);
            }
        }

        if (rootDictionary.containsKey(MetadataKeys.MUSIC_FOLDER))
        {
            NSObject valueNSObject = rootDictionary.get(MetadataKeys.MUSIC_FOLDER);
            Object valueObject = valueNSObject.toJavaObject();
            if (valueObject.getClass().equals(String.class))
            {
                String value = (String) valueObject;
                iTunesLibraryFactory.setMusicFolder(value);
            }
        }
    }

    private void parseTracks (NSDictionary rootDictionary)
            throws
            LibraryParsingException
    {

        NSDictionary tracksDictionary = (NSDictionary) rootDictionary.get("Tracks");

        for (var trackKeyDictPair : tracksDictionary.entrySet())
        {
            String trackIdString = trackKeyDictPair.getKey();
            NSDictionary trackDictionary = (NSDictionary) trackKeyDictPair.getValue();
            ITunesTrackParser trackParser = new ITunesTrackParser(iTunesLibraryFactory, trackIdString, trackDictionary);
            Track track = trackParser.parse();
            iTunesLibraryFactory.addTrack(track);
        }
    }

}
