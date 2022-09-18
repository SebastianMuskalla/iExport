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
import iexport.parsing.builders.PlaylistBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The keys needed to parse playlists.
 * <p>
 * These are needed to populate the fields of {@link PlaylistBuilder}.
 * <p>
 * This class is fully static and cannot be instantiated.
 */
public class PlaylistKeys
{

    /**
     * A map that maps keys from the .xml file to the appropriate handler.
     */
    private static final Map<String, BiConsumer<PlaylistBuilder, Object>> handlers = new HashMap<>();

    /**
     * Get the handler for the specified key.
     * <p>
     * Each handler is of type {@code BiConsumer<PlaylistBuilder, Object>}, i.e. it provides a method that takes a {@link PlaylistBuilder} and parsed value of type {@code Object} and assigns the parsed value to the {@link PlaylistBuilder} if the types match.
     *
     * @param key the key
     * @return the handler
     */
    public static BiConsumer<PlaylistBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    /**
     * Logs a warning if a parsed value has an unexpected type and cannot be set.
     *
     * @param key            The key
     * @param value          The value
     * @param unexpectedType The type of the parsed value
     * @param expectedType   The expected type of the parsed value
     */
    @SuppressWarnings("rawtypes")
    private static void logUnexpectedType (String key, String value, Class unexpectedType, Class expectedType)
    {
        Logging.getLogger().warning("Key \"" + key + "\" with value \"" + value + "\" is of unexpected type \"" + unexpectedType.getSimpleName() + "\" expected \"" + expectedType.getSimpleName() + "\"");
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
                        logUnexpectedType("Master", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("Visible", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("All Items", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("Folder", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("Music", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("Movies", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("TV Shows", value.toString(), value.getClass(), Boolean.class);
                    }
                }
        );

        handlers.put("Audiobooks",
                (PlaylistBuilder playlistBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Boolean.class))
                    {
                        playlistBuilder.setAudiobooks((Boolean) value);
                    }
                    else
                    {
                        logUnexpectedType("Audiobooks", value.toString(), value.getClass(), Boolean.class);
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
                        logUnexpectedType("Playlist ID", value.toString(), value.getClass(), Integer.class);
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
                        logUnexpectedType("Distinguished Kind", value.toString(), value.getClass(), Integer.class);
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
                        logUnexpectedType("Playlist Persistent ID", value.toString(), value.getClass(), String.class);
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
                        logUnexpectedType("Name", value.toString(), value.getClass(), String.class);
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
                        logUnexpectedType("Parent Persistent ID", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        // We cannot handle the raw data for "Smart Info".
        handlers.put("Smart Info", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

        // We cannot handle the raw data for "Smart Criteria".
        handlers.put("Smart Criteria", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

        // Will be handled explicitly later.
        handlers.put("Playlist Items", (PlaylistBuilder playlistBuilder, Object value) -> {
        });

    }

    /**
     * This class should not be instantiated.
     */
    private PlaylistKeys ()
    {
    }
}
