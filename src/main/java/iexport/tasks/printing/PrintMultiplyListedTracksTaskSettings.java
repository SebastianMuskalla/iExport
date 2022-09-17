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

package iexport.tasks.printing;

import iexport.settings.RawTaskSettings;
import iexport.settings.TaskSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Setting for {@link PrintMultiplyListedTracksTask}.
 */
public class PrintMultiplyListedTracksTaskSettings extends TaskSettings
{

    /**
     * The default settings.
     */
    private static final Map<String, Object> PRINT_MULTIPLY_LISTED_TRACKS_DEFAULT_SETTINGS = new HashMap<>();

    /**
     * tasks.printMultiplyListedTracks.ignorePlaylists
     * -----------------------------------------------
     * Specify playlist names that should be ignored,
     * i.e. they will not be counted towards the number of playlists a track is in.
     */
    private static final String SETTING_IGNORE_PLAYLISTS = "ignorePlaylists";

    /**
     * Default value for tasks.printMultiplyListedTracks.ignorePlaylists
     */
    private static final List<String> SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE = List.of(); // empty list

    static
    {
        // Set default values.
        PRINT_MULTIPLY_LISTED_TRACKS_DEFAULT_SETTINGS.put(SETTING_IGNORE_PLAYLISTS, SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE);
    }

    public PrintMultiplyListedTracksTaskSettings (RawTaskSettings rawTaskSettings)
    {
        super(rawTaskSettings);
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(PRINT_MULTIPLY_LISTED_TRACKS_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    public List<String> getIgnorePlaylists ()
    {
        String key = SETTING_IGNORE_PLAYLISTS;
        Object result = getValueFor(key);

        try
        {
            // Type erasure yada yada
            @SuppressWarnings("unchecked")
            List<String> resultList = (List<String>) result;
            return resultList;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected an array of strings, but got " + result.getClass().getSimpleName());
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected an array of strings, but got null");
        }
    }

    protected Object getDefaultValueFor (String key)
    {
        return PRINT_MULTIPLY_LISTED_TRACKS_DEFAULT_SETTINGS.get(key);
    }

}
