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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * General settings for iExport that do not just affect parsing or a specific task.
 * <p>
 * These settings correspond to the dictionary under the key "parsing" in the root dictionary of the .yaml file.
 */
public class ParsingSettings extends Settings
{
    /**
     * Map holding the default settings for parsing
     */
    protected static final Map<String, Object> PARSING_DEFAULT_SETTINGS = new HashMap<>();
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

    static
    {
        PARSING_DEFAULT_SETTINGS.put(SETTING_XML_FILE_PATH, SETTING_XML_FILE_PATH_DEFAULT_VALUE);
    }

    public ParsingSettings (Map<String, Object> yamlParsingMap)
    {
        super(yamlParsingMap);
    }

    public ParsingSettings ()
    {
        super();
    }

    public String getLibraryXmlFilePathString ()
    {
        Object result = getValueFor(SETTING_XML_FILE_PATH);

        try
        {
            String resultString = (String) result;
            return Settings.applyUserProfileReplacement(resultString);
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass() + ": invalid entry for " + getYamlPath(SETTING_XML_FILE_PATH)
                    + ", expected a string, but got " + result.getClass());
        }
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(PARSING_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    @Override
    protected String getYamlPrefix ()
    {
        return "parsing.";
    }

    @Override
    Object getDefaultValueFor (String key)
    {
        return PARSING_DEFAULT_SETTINGS.get(key);
    }
}
