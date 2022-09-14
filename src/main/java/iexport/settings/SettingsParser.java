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

public class SettingsParser
{
    private static final String SETTINGS_KEY_PARSING = "parsing";
    private static final String SETTINGS_KEY_TASKS = "tasks";

    private final String pathToYamlFileAsString;

    public SettingsParser (String pathToYamlFileAsString)
    {
        this.pathToYamlFileAsString = pathToYamlFileAsString;
    }

    public SettingsTriple parse ()
            throws SettingsParsingException
    {
        // We try to replace %USERPROFILE% in pathToYamlFileAsString
        // by the value of USERPROFILE from the environment, e.g. C:\Users\YourUserName
        String pathToYamlFileAsStringPreprocessed = applyUserProfileReplacement(pathToYamlFileAsString);

        Path pathToYamlFile = FileSystems.getDefault().getPath(pathToYamlFileAsStringPreprocessed);
        String yamlFileContent;
        try
        {
            yamlFileContent = Files.readString(pathToYamlFile);
        }
        catch (IOException e)
        {
            throw new SettingsParsingException("Opening the file \"" + pathToYamlFile + "\" failed", e);
        }

        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);
        Object yamlObject = load.loadFromString(yamlFileContent);

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
        Map<String, TaskSettings> taskSettingsMap = parseTasksSettings(yamlTasksObject);
        
        return new SettingsTriple(generalSettings, parsingSettings, taskSettingsMap);
    }

    private Map<String, TaskSettings> parseTasksSettings (Object yamlTasksObject)
            throws SettingsParsingException
    {

        if (yamlTasksObject == null)
        {
            // If "tasks" settings is unspecified, just use an empty map
            return new HashMap<>();
        }

        Map<String, TaskSettings> taskSettingsMap = new HashMap<>();

        // Same thing with type erasure as before
        if (!(yamlTasksObject instanceof Map))
        {
            throw new SettingsParsingException("Expected the value for the key " + SETTINGS_KEY_TASKS + "key to be a a dictionary with strings as keys, found  " + yamlTasksObject + " of type " + yamlTasksObject.getClass() + " instead");
        }

        Map<?, ?> yamlTasksMap = (Map<?, ?>) yamlTasksObject;

        // Go through all keys and construct the corresponding task settings
        for (var entry : yamlTasksMap.entrySet())
        {
            Object taskNameObject = entry.getKey();

            if (!(taskNameObject instanceof String))
            {
                throw new SettingsParsingException("Expected they keys inside the \"tasks\" dictionary to be strings, found " + taskNameObject + " of type " + taskNameObject.getClass() + " instead");
            }

            String taskName = (String) taskNameObject;

            TaskSettings taskSettings = parseTaskSettings(entry.getValue());

            taskSettingsMap.put(taskName, taskSettings);
        }

        return taskSettingsMap;
    }

    private TaskSettings parseTaskSettings (Object taskMapObject)
            throws SettingsParsingException
    {
        if (!(taskMapObject instanceof Map))
        {
            throw new SettingsParsingException("Expected they values for the keys inside the \"tasks\" dictionary to be maps with strings as keys, found " + taskMapObject + " of type " + taskMapObject.getClass() + " instead");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> taskMap = (Map<String, Object>) taskMapObject;

        return new TaskSettings(taskMap);
    }

    private ParsingSettings parseParsingSettings (Object yamlParsingObject)
            throws SettingsParsingException
    {
        ParsingSettings parsingSettings;
        if (yamlParsingObject == null)
        {
            // TODO
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

    private String applyUserProfileReplacement (String pathToYamlFileAsString)
    {
        final String userProfileEnvironmentVariable = "USERPROFILE";
        final String userProfilePlaceholder = "%USERPROFILE%";

        try
        {
            String userProfileValue = System.getenv(userProfileEnvironmentVariable);
            if (userProfileValue != null)
            {
                pathToYamlFileAsString = pathToYamlFileAsString.replace(userProfilePlaceholder, userProfileValue);
            }
        }
        catch (Exception e)
        {
            System.out.println("Applying Replacement of " + userProfilePlaceholder + " failed");
            System.out.println(e.getMessage());
        }
        return pathToYamlFileAsString;
    }


}
