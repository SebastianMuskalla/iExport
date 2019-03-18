package iexport.parsing.itunes;

import com.dd.plist.NSDictionary;
import iexport.domain.Track;
import iexport.domain.builder.LibraryBuilder;
import iexport.domain.builder.TrackBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.parsing.itunes.keys.TrackKeys;

/*
<key>3339</key>
		<dict>
			<key>Track ID</key><integer>3339</integer>
			<key>Size</key><integer>2479882</integer>
			<key>Total Time</key><integer>153469</integer>
			<key>Track Number</key><integer>23</integer>
			<key>Track Count</key><integer>27</integer>
			<key>Year</key><integer>1993</integer>
			<key>Date Modified</key><date>2015-12-30T21:16:52Z</date>
			<key>Date Added</key><date>2015-09-19T12:53:20Z</date>
			<key>Bit Rate</key><integer>128</integer>
			<key>Sample Rate</key><integer>44100</integer>
			<key>Play Count</key><integer>9</integer>
			<key>Play Date</key><integer>3592752019</integer>
			<key>Play Date UTC</key><date>2017-11-05T17:40:19Z</date>
			<key>Artwork Count</key><integer>1</integer>
			<key>Persistent ID</key><string>48B2D6F5D9066F36</string>
			<key>Track Type</key><string>File</string>
			<key>File Folder Count</key><integer>-1</integer>
			<key>Library Folder Count</key><integer>-1</integer>
			<key>Name</key><string>Last castle</string>
			<key>Artist</key><string>FF Mystic Quest</string>
			<key>Album Artist</key><string>FF Mystic Quest</string>
			<key>Album</key><string>FFMQ OST</string>
			<key>Kind</key><string>MPEG audio file</string>
			<key>Location</key><string>file://localhost/F:/Audio/Music/F/Final%20Fantasy%20-%201993%20Mystic%20Quest/23%20-%20Last%20castle.mp3</string>
		</dict>
 */

final class TrackParser
{

    private final LibraryBuilder iTunesLibraryFactory;
    private final NSDictionary trackDictionary;
    private final TrackBuilder trackBuilder;

    public TrackParser (LibraryBuilder iTunesLibraryFactory, String trackIdString, NSDictionary trackDictionary)
            throws
            IExportParsingException
    {
        this.iTunesLibraryFactory = iTunesLibraryFactory;
        this.trackDictionary = trackDictionary;

        Integer trackId;
        try
        {
            trackId = Integer.parseInt(trackIdString);
        }
        catch (Exception e)
        {
            throw new IExportParsingException("Track ID " + trackIdString + " not convertible to int", e);
        }
        trackBuilder = new TrackBuilder(trackId);
    }

    protected Track parse ()
    {
        for (var keyValuePair : trackDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();

            var handler = TrackKeys.getHandlerFor(key);

            if (handler != null)
            {
                handler.accept(trackBuilder, value);
            }
            else
            {
                Logger.log(LogLevel.DEV_WARNING, "No handler for key " + key + " with value " + value.toString());
            }
        }

        return trackBuilder.create();
    }
}
