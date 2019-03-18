package iexport.parsing.itunes.keys;

import iexport.domain.builder.PlaylistBuilder;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PlaylistKeys
{

    private static Map<String, BiConsumer<PlaylistBuilder, Object>> handlers = new HashMap<>();

    public static BiConsumer<PlaylistBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    static
    {
        handlers.put("Master",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setMaster((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Master " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("Visible",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setVisible((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Visible " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("All Items",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setAllItems((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "All Items " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("Folder",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setFolder((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Folder " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        handlers.put("Music",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setMusic((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Music " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        handlers.put("Movies",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setMovies((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Movies " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("TV Shows",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setTvShows((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Shows " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );


        handlers.put("Audiobooks",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setAudibooks((Boolean) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Audiobooks " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        handlers.put("Playlist ID",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        playlistBuilder.setPlaylistId((Integer) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Playlist ID " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("Distinguished Kind",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        playlistBuilder.setDistinguishedKind((Integer) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Distinguished Kind " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("Playlist Persistent ID",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        playlistBuilder.setPlaylistPersistentId((String) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Playlist Persistent ID " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );
        handlers.put("Name",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        playlistBuilder.setName((String) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Name " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        handlers.put("Parent Persistent ID",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(String.class))
                    {
                        playlistBuilder.setParentPersistentId((String) value);
                    }
                    else
                    {
                        Logger.log(LogLevel.DEV_WARNING, "Parent Persistent ID " + value.toString() + " of unexpected type " + value.getClass().toString());
                    }
                }
        );

        // unhandled
        handlers.put("Smart Info", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

        // unhandled
        handlers.put("Smart Criteria", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

        // will be handled explicitly later
        handlers.put("Playlist Items", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

    }

    private PlaylistKeys ()
    {
    }
}
