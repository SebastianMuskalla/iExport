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

import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A parser for converting a settings .yaml file into a {@link SettingsTriple} object containing
 * <ul>
 *     <li> {@link GeneralSettings}, the generating settings for iExport
 *     <li> {@link ParsingSettings}, the settings for parsing the library
 *     <li> for each task the corresponding {@link RawTaskSettings}
 * </ol>
 * <p>
 * This class relies on {@link org.snakeyaml.engine.v2.api.Load} and the other classes from {@link org.snakeyaml.engine.v2}
 * in order to parse the .yaml file into Java objects.
 */
public class SettingsParser
{
    /**
     * The key of the root dictionary that contains the settings for parsing.
     */
    private static final String SETTINGS_KEY_PARSING = "parsing";
    /**
     * The key of the root dictionary that contains the settings for each task (as a dictionary).
     */
    private static final String SETTINGS_KEY_TASKS = "tasks";

    /**
     * The location of the .yaml file, e.g. {@code %USERPROFILE%/Desktop/iExportSettings.yaml}.
     */
    private final String pathToYamlFileAsString;

    /**
     * Constract a {@code SettingsParser} for the specified file location
     *
     * @param pathToYamlFileAsString the file location
     */
    public SettingsParser (String pathToYamlFileAsString)
    {
        this.pathToYamlFileAsString = pathToYamlFileAsString;
    }

    /**
     * Try to parse the .yaml file
     *
     * @return the parsed settings
     * @throws SettingsParsingException if parsing fails in a non-recoverable way
     */
    public SettingsTriple parse ()
            throws SettingsParsingException
    {
        // We try to replace %USERPROFILE% in pathToYamlFileAsString
        // by the value of USERPROFILE from the environment, e.g. C:\Users\YourUserName
        // If this fails, we get the unmodified string back
        String pathToYamlFileAsStringPreprocessed = Settings.applyUserProfileReplacement(pathToYamlFileAsString);

        // Convert the path as string into a java path
        Path pathToYamlFile = FileSystems.getDefault().getPath(pathToYamlFileAsStringPreprocessed);

        // Read the contents of that file into a string
        String yamlFileContent;
        try
        {
            yamlFileContent = Files.readString(pathToYamlFile);
        }
        catch (IOException e)
        {
            throw new SettingsParsingException("Opening the file \"" + pathToYamlFile + "\" failed", e);
        }

        // Initialize the yaml parser with its default settings
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);

        // Now we can actually do the parsing
        Object yamlObject = load.loadFromString(yamlFileContent);

        // The root object of the .yaml file should be a dictionary (= Map<String,Object>)
        if (!(yamlObject instanceof Map))
        {
            throw new SettingsParsingException("Expected the root object of the .yaml file to be a dictionary with strings as keys, found  " + yamlObject + " of type " + yamlObject.getClass() + " instead");
        }

        // Due to type erasure at runtime, we can only check that yamlObject is Map,
        // but not whether it is actually a Map<String,Object>
        @SuppressWarnings("unchecked")
        Map<String, Object> yamlRootMap = (Map<String, Object>) yamlObject;

        // We remove the "parsings" key from the map and store the result
        Object yamlParsingObject = yamlRootMap.remove(SETTINGS_KEY_PARSING);

        // We remove the tasks key from the map and store the result
        Object yamlTasksObject = yamlRootMap.remove(SETTINGS_KEY_TASKS);

        // The remaining settings are the top-level settings
        GeneralSettings generalSettings = new GeneralSettings(yamlRootMap);

        // Generate the parsingSettings from the value for the key "parsing"
        ParsingSettings parsingSettings = parseParsingSettings(yamlParsingObject);

        // Generate the taskSettings for each task from the value for the key "tasks"
        Map<String, RawTaskSettings> taskSettingsMap = parseTasksSettings(yamlTasksObject);

        return new SettingsTriple(generalSettings, parsingSettings, taskSettingsMap);
    }

    /**
     * Parse the dictionary at the "parsing" key of the .yaml file into a map of {@link ParsingSettings}.
     *
     * @param yamlParsingObject the java representation of the dictionary for the key "parsing"
     * @return the parsed settings
     * @throws SettingsParsingException if parsing fails in a non-recoverable way
     */
    private ParsingSettings parseParsingSettings (Object yamlParsingObject)
            throws SettingsParsingException
    {
        ParsingSettings parsingSettings;
        if (yamlParsingObject == null)
        {
            // "parsing" settings is unspecified, use default settings
            parsingSettings = new ParsingSettings();
        }
        else
        {
            // Same thing with type erasure as before
            if (!(yamlParsingObject instanceof Map))
            {
                throw new SettingsParsingException("Expected the value for the key " + SETTINGS_KEY_PARSING + " in the root dictionary to be a dictionary with strings as keys, found  " + yamlParsingObject + " of type " + yamlParsingObject.getClass() + " instead");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> yamlParsingMap = (Map<String, Object>) yamlParsingObject;
            parsingSettings = new ParsingSettings(yamlParsingMap);
        }
        return parsingSettings;
    }

    /**
     * Parse the dictionary at the "tasks" key of the .yaml file into a map of {@link RawTaskSettings}.
     *
     * @param yamlTasksObject the java representation of the dictionary for the key "tasks"
     * @return the parsed map of settings
     * @throws SettingsParsingException if parsing fails in a non-recoverable way
     */
    private Map<String, RawTaskSettings> parseTasksSettings (Object yamlTasksObject)
            throws SettingsParsingException
    {

        if (yamlTasksObject == null)
        {
            // If "tasks" settings is unspecified, just use an empty map
            return new HashMap<>();
        }

        Map<String, RawTaskSettings> taskSettingsMap = new HashMap<>();

        // Same thing with type erasure as before
        if (!(yamlTasksObject instanceof Map<?, ?> yamlTasksMap))
        {
            throw new SettingsParsingException("Expected the value for the key " + SETTINGS_KEY_TASKS + "key to be a a dictionary with strings as keys, found  " + yamlTasksObject + " of type " + yamlTasksObject.getClass() + " instead");
        }

        // Go through all keys and construct the corresponding task settings
        for (var entry : yamlTasksMap.entrySet())
        {
            // The keys of the dictionary should be strings containing the task name
            Object taskNameObject = entry.getKey();

            if (!(taskNameObject instanceof String taskName))
            {
                throw new SettingsParsingException("Expected they keys inside the \"tasks\" dictionary to be strings, found " + taskNameObject + " of type " + taskNameObject.getClass() + " instead");
            }

            // Transform the value for each such key into TaskSettings
            RawTaskSettings taskSettings = parseTaskSettings(taskName, entry.getValue());

            taskSettingsMap.put(taskName, taskSettings);
        }

        return taskSettingsMap;
    }

    /**
     * Parse an entry of the dictionary for  the "tasks" key of the .yaml file into a {@link RawTaskSettings}.
     * <p>
     * e.g. the dictionary at the key "tasks.printLibrary" should be parsed into {@link RawTaskSettings} for {@link LibraryPrinter}
     *
     * @param taskName      the name of the task (from the key)
     * @param taskMapObject the java object for the key "tasks.taskName"
     * @return the parsed task settings
     * @throws SettingsParsingException if parsing fails in a non-recoverable way
     */
    private RawTaskSettings parseTaskSettings (String taskName, Object taskMapObject)
            throws SettingsParsingException
    {
        // The settings for the key"tasks.taskName" should be a dictionary (i.e. a Map<String,Object>)
        if (!(taskMapObject instanceof Map))
        {
            throw new SettingsParsingException("Expected they values for the keys inside the \"tasks\" dictionary to be maps with strings as keys, found " + taskMapObject + " of type " + taskMapObject.getClass() + " instead");
        }

        // Type erasure again
        @SuppressWarnings("unchecked")
        Map<String, Object> taskMap = (Map<String, Object>) taskMapObject;

        return new RawTaskSettings(taskName, taskMap);
    }

}
