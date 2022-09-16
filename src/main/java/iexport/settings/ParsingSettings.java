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
 * General settings for iExport that do not just affect parsing or a specific task.
 * <p>
 * These settings correspond to the dictionary under the key "parsing" in the root dictionary of the .yaml file.
 */
public class ParsingSettings extends SettingsImpl
{
    /**
     * Map holding the default settings for parsing
     */
    private static final Map<String, Object> PARSING_DEFAULT_SETTINGS = new HashMap<>();
    /**
     * Setting for the location of the {@code iTunes Music Library.xml} file
     */
    private static final String SETTING_XML_FILE_PATH = "xmlFilePath";

    /**
     * Default location of the {@code iTunes Music Library.xml} file:
     * {@code %USERPROFILE%/Music/iTunes/iTunes Music Library.xml}
     * (typically evaluates to {@code C:\Users\YourUserName/Music/iTunes/iTunes Music Library.xml}
     * after using {@link Settings#applyUserProfileReplacement(String)} to replace {@code %USERPROFILE%}).
     */
    private static final String SETTING_XML_FILE_PATH_DEFAULT_VALUE = "%USERPROFILE%/Music/iTunes/iTunes Music Library.xml";

    private static final String SETTING_IGNORE_EMPTY_PLAYLISTS = "ignoreEmptyPlaylists";
    private static final Boolean SETTING_IGNORE_EMPTY_PLAYLISTS_DEFAULT_VALUE = true;

    private static final String SETTING_IGNORE_NON_MUSIC_PLAYLISTS = "ignoreNonMusicPlaylists";
    private static final Boolean SETTING_IGNORE_NON_MUSIC_PLAYLISTS_DEFAULT_VALUE = true;

    private static final String SETTING_IGNORE_DISTINGUISHED_PLAYLISTS = "ignoreDistinguishedPlaylists";
    private static final Boolean SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE = false;

    private static final String SETTING_IGNORE_MASTER = "ignoreMaster";
    private static final Boolean SETTING_IGNORE_MASTER_DEFAULT_VALUE = true;

    private static final String SETTING_IGNORE_PLAYLISTS_BY_NAME = "ignorePlaylistsByName";
    private static final List<String> SETTING_IGNORE_PLAYLISTS_BY_NAME_DEFAULT_VALUE = List.of();

    static
    {
        PARSING_DEFAULT_SETTINGS.put(SETTING_XML_FILE_PATH, SETTING_XML_FILE_PATH_DEFAULT_VALUE);

        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_EMPTY_PLAYLISTS, SETTING_IGNORE_EMPTY_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_NON_MUSIC_PLAYLISTS, SETTING_IGNORE_NON_MUSIC_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_DISTINGUISHED_PLAYLISTS, SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_MASTER, SETTING_IGNORE_MASTER_DEFAULT_VALUE);
        PARSING_DEFAULT_SETTINGS.put(SETTING_IGNORE_PLAYLISTS_BY_NAME, SETTING_IGNORE_PLAYLISTS_BY_NAME_DEFAULT_VALUE);
    }

    public ParsingSettings (Map<String, Object> yamlParsingMap)
    {
        super(yamlParsingMap);
    }

    public ParsingSettings ()
    {
        super();
    }

    public String getSettingLibraryXmlFilePathString ()
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

    public boolean getSettingIgnoreEmptyPlaylists ()
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

    public boolean getSettingIgnoreNonMusicPlaylists ()
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

    public boolean getSettingIgnoreDistinguishedPlaylists ()
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

    public List<String> getSettingIgnorePlaylistNames ()
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

    public boolean getSettingsIgnoreMaster ()
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
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(PARSING_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    @Override
    public String getYamlPrefix ()
    {
        return "parsing.";
    }

    @Override
    protected Object getDefaultValueFor (String key)
    {
        return PARSING_DEFAULT_SETTINGS.get(key);
    }
}
