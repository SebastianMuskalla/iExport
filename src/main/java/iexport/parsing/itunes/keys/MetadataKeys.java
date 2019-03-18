package iexport.parsing.itunes.keys;

import iexport.domain.builder.LibraryBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MetadataKeys
{

    public static final String MUSIC_FOLDER = "Music Folder";
    private static Map<String, BiConsumer<LibraryBuilder, Object>> handlers = new HashMap<>();

    public static BiConsumer<LibraryBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    private static void unexpectedType (String key, String value, Class unexpectedType)
    {
        Logger.log(LogLevel.DEV_WARNING, key + " " + value + " of unexpected type " + unexpectedType.toString());
    }

    static
    {

        /*
         * {@code <key>Application Version</key><string>12.9.1.4</string>}
         */
        handlers.put("Application Version",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        libraryBuilder.setApplicationVersion((String) value);
                    }
                    else
                    {
                        unexpectedType("Application Version", value.toString(), value.getClass());
                    }
                }
        );

        /*
         * {@code <key>Date</key><date>2018-11-25T16:12:51Z</date>}
         */
        handlers.put("Date",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Date.class))
                    {
                        libraryBuilder.setDate((Date) value);
                    }
                    else
                    {
                        unexpectedType("Date", value.toString(), value.getClass());
                    }
                }
        );


        /*
         * {@code <key>Library Persistent ID</key><string>B2D95F48C25993B6</string>}
         */
        handlers.put("Library Persistent ID",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        libraryBuilder.setPersistentId((String) value);
                    }
                    else
                    {
                        unexpectedType("Library Persistent ID", value.toString(), value.getClass());
                    }
                }
        );

        /*
         * {@code <key>Music Folder</key><string>file://localhost/C:/Users/XXX/Music/iTunes/iTunes%20Media/</string>}
         */
        handlers.put("Music Folder",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        libraryBuilder.setMusicFolder((String) value);
                    }
                    else
                    {
                        unexpectedType("Music Folder", value.toString(), value.getClass());
                    }
                }
        );

        // will be handled explicitly later
        handlers.put("Playlists", (LibraryBuilder libraryBuilder, Object value) -> {
        });

    }

    private MetadataKeys ()
    {
    }

}
