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
import iexport.parsing.LibraryParser;
import iexport.parsing.builders.LibraryBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The keys needed to parse the library.
 * <p>
 * These are needed to populate the fields of {@link LibraryBuilder}.
 * <p>
 * This class is fully static and cannot be instantiated.
 */
public class LibraryKeys
{

    /**
     * A map that maps keys from the .xml file to the appropriate handler.
     */
    private static final Map<String, BiConsumer<LibraryBuilder, Object>> handlers = new HashMap<>();

    /**
     * Get the handler for the specified key.
     * <p>
     * Each handler is of type {@code BiConsumer<LibraryBuilder, Object>}, i.e. it provides a method that takes a {@link LibraryBuilder} and parsed value of type {@code Object} and assigns the parsed value to the {@link LibraryBuilder} if the types match.
     *
     * @param key the key.
     * @return the handler.
     */
    public static BiConsumer<LibraryBuilder, Object> getHandlerFor (String key)
    {
        return handlers.get(key);
    }

    /**
     * Logs a warning if a parsed value has an unexpected type and cannot be set.
     *
     * @param key            The key.
     * @param value          The value.
     * @param unexpectedType The type of the parsed value.
     * @param expectedType   The expected type of the parsed value.
     */
    @SuppressWarnings("rawtypes")
    private static void logUnexpectedType (String key, String value, Class unexpectedType, Class expectedType)
    {
        Logging.getLogger().message(LibraryParser.class + ": key \"" + key + "\" with value \"" + value + "\" is of unexpected type \"" + unexpectedType.toString() + "\" expected \"" + expectedType + "\"");
    }

    static
    {

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
                        logUnexpectedType("Application Version", value.toString(), value.getClass(), String.class);
                    }
                }
        );


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
                        logUnexpectedType("Date", value.toString(), value.getClass(), Date.class);
                    }
                }
        );

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
                        logUnexpectedType("Library Persistent ID", value.toString(), value.getClass(), String.class);
                    }
                }
        );

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
                        logUnexpectedType("Music Folder", value.toString(), value.getClass(), String.class);
                    }
                }
        );

        handlers.put("Major Version",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        libraryBuilder.setMajorVersion((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Major Version", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Minor Version",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        libraryBuilder.setMinorVersion((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Minor Version", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        handlers.put("Features",
                (LibraryBuilder libraryBuilder, Object value)
                        ->
                {
                    if (value.getClass().equals(Integer.class))
                    {
                        libraryBuilder.setFeatures((Integer) value);
                    }
                    else
                    {
                        logUnexpectedType("Features", value.toString(), value.getClass(), Integer.class);
                    }
                }
        );

        // will be handled explicitly later
        handlers.put("Playlists", (LibraryBuilder libraryBuilder, Object value) -> {
        });

        // will be handled explicitly later
        handlers.put("Tracks", (LibraryBuilder libraryBuilder, Object value) -> {
        });

    }

    private LibraryKeys ()
    {
    }

}
