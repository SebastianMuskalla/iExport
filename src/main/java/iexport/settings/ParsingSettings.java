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

package iexport.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Settings for iExport that affect parsing the library.
 * <p>
 * These settings correspond to the dictionary under the key "parsing" in the root dictionary of the .yaml file.
 */
@SuppressWarnings("ConstantConditions")
public class ParsingSettings extends SettingsImpl
{
    /**
     * Map holding the default settings for parsing
     */
    private static final Map<String, Object> PARSING_DEFAULT_SETTINGS = new HashMap<>();

    /**
     * parsing.xmlFilePath
     * <p>
     * Path to "iTunes Music Library.xml".
     * Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
     */
    private static final String SETTING_XML_FILE_PATH = "xmlFilePath";

    /**
     * Default value for "parsing.xmlFilePath".
     */
    private static final String SETTING_XML_FILE_PATH_DEFAULT_VALUE = "%USERPROFILE%\\Music\\iTunes\\iTunes Music Library.xml";

    /**
     * parsing.ignoreEmptyPlaylists
     * <p>
     * Set to true to ignore playlists that contain no tracks
     */
    private static final String SETTING_IGNORE_EMPTY_PLAYLISTS = "ignoreEmptyPlaylists";

    /**
     * Default value for "parsing.ignoreEmptyPlaylists".
     */
    private static final Boolean SETTING_IGNORE_EMPTY_PLAYLISTS_DEFAULT_VALUE = true;

    /**
     * parsing.ignoreNonMusicPlaylists
     * <p>
     * Set to true to ignore playlists for movies, tvShows, audiobooks.
     */
    private static final String SETTING_IGNORE_NON_MUSIC_PLAYLISTS = "ignoreNonMusicPlaylists";

    /**
     * Default value for "parsing.ignoreNonMusicPlaylists".
     */
    private static final Boolean SETTING_IGNORE_NON_MUSIC_PLAYLISTS_DEFAULT_VALUE = true;

    /**
     * parsing.ignoreDistinguishedPlaylists
     * <p>
     * Set to true to ignore distinguished playlists.
     * This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
     */
    private static final String SETTING_IGNORE_DISTINGUISHED_PLAYLISTS = "ignoreDistinguishedPlaylists";

    /**
     * Default value for "parsing.ignoreDistinguishedPlaylists".
     */
    private static final Boolean SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE = false;

    /**
     * parsing.ignoreMaster
     * <p>
     * Set to true to ignore the playlist "Library" with the "master" flag
     */
    private static final String SETTING_IGNORE_MASTER = "ignoreMaster";

    /**
     * Default value for "parsing.ignoreMaster".
     */
    private static final Boolean SETTING_IGNORE_MASTER_DEFAULT_VALUE = true;

    /**
     * parsing.ignorePlaylistsByName
     * <p>
     * Specify an array of playlist names that should be ignored
     */
    private static final String SETTING_IGNORE_PLAYLISTS_BY_NAME = "ignorePlaylistsByName";

    /**
     * Default value for "parsing.ignorePlaylistsByName".
     */
    private static final List<String> SETTING_IGNORE_PLAYLISTS_BY_NAME_DEFAULT_VALUE = List.of(); // empty List

    static
    {
        // Set the default values.
        PARSING_DEFAULT_SETTINGS.put(SETTING_XML_FILE_PATH, SETTING_XML_FILE_PATH_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_EMPTY_PLAYLISTS, SETTING_IGNORE_EMPTY_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_NON_MUSIC_PLAYLISTS, SETTING_IGNORE_NON_MUSIC_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_DISTINGUISHED_PLAYLISTS, SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_MASTER, SETTING_IGNORE_MASTER_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_PLAYLISTS_BY_NAME, SETTING_IGNORE_PLAYLISTS_BY_NAME_DEFAULT_VALUE);
    }

    /**
     * Construct parsing settings from user-specified values.
     *
     * @param yamlParsingMap the parsed user-specified values.
     */
    public ParsingSettings (Map<String, Object> yamlParsingMap)
    {
        super(yamlParsingMap);
    }

    /**
     * Construct parsing settings with the default values.
     */
    public ParsingSettings ()
    {
        super();
    }

    /**
     * @return parsing.xmlFilePath
     */
    public String getXmlFilePathString ()
    {
        Object result = getValueFor(SETTING_XML_FILE_PATH);

        try
        {
            String resultString = (String) result;
            return Settings.applyUserProfileReplacement(resultString);
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(SETTING_XML_FILE_PATH)
                    + ", expected a string, but got " + result.getClass().getSimpleName());
        }
    }

    /**
     * @return parsing.ignoreEmptyPlaylists
     */
    public boolean getIgnoreEmptyPlaylists ()
    {
        String key = SETTING_IGNORE_EMPTY_PLAYLISTS;
        Object result = getValueFor(key);

        try
        {
            return (boolean) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got " + result.getClass().getSimpleName());
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got null");
        }
    }

    /**
     * @return parsing.ignoreNonMusicPlaylists
     */
    public boolean getIgnoreNonMusicPlaylists ()
    {
        String key = SETTING_IGNORE_NON_MUSIC_PLAYLISTS;
        Object result = getValueFor(key);

        try
        {
            return (boolean) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got " + result.getClass().getSimpleName());
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got null");
        }
    }

    /**
     * @return parsing.ignoreDistinguishedPlaylists
     */
    public boolean getIgnoreDistinguishedPlaylists ()
    {
        String key = SETTING_IGNORE_DISTINGUISHED_PLAYLISTS;
        Object result = getValueFor(key);

        try
        {
            return (boolean) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got " + result.getClass().getSimpleName());
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got null");
        }
    }

    /**
     * @return parsing.ignorePlaylistsByName
     */
    public List<String> getIgnorePlaylistNames ()
    {
        String key = SETTING_IGNORE_PLAYLISTS_BY_NAME;
        Object result = getValueFor(key);

        try
        {
            // Type erasure
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


    /**
     * @return parsing.ignoreMaster
     */
    public boolean getIgnoreMaster ()
    {
        String key = SETTING_IGNORE_MASTER;
        Object result = getValueFor(key);

        try
        {
            return (boolean) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got " + result.getClass().getSimpleName());
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a boolean, but got null");
        }
    }

    @Override
    public String getYamlPrefix ()
    {
        return "parsing";
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(PARSING_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    @Override
    protected Object getDefaultValueFor (String key)
    {
        return PARSING_DEFAULT_SETTINGS.get(key);
    }
}
