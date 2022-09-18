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

package iexport.tasks.fileexport;

import iexport.settings.RawTaskSettings;
import iexport.settings.Settings;
import iexport.settings.TaskSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Settings for {@link ExportFilesTask}.
 */
@SuppressWarnings("ConstantConditions")
public class ExportFilesTaskSettings extends TaskSettings
{
    /**
     * Default settings for the exportFiles task
     */
    private static final Map<String, Object> EXPORT_FILES_DEFAULT_SETTINGS = new HashMap<>();

    /**
     * tasks.exportFiles.outputFolder
     * <p>
     * Folder at which the playlist files should be generated
     * Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
     * Note that backslashes ("\") need to be escaped as "\\".
     */
    private static final String SETTING_OUTPUT_FOLDER = "outputFolder";

    /**
     * Default value for tasks.exportFiles.outputFolder
     */
    private static final String SETTING_OUTPUT_FOLDER_DEFAULT_VALUE = "%USERPROFILE%\\Desktop\\iExport\\Files";

    /**
     * tasks.exportFiles.deleteFolder
     * <p>
     * If set to false, iExport will only proceed if outputFolder does not exist yet.
     * If set to true, the folder will be deleted if it already exists.
     */
    private static final String SETTING_DELETE_FOLDER = "deleteFolder";

    /**
     * Default value for tasks.exportFiles.deleteFolder
     */
    private static final Boolean SETTING_DELETE_FOLDER_DEFAULT_VALUE = false;

    /**
     * tasks.exportFiles.hierarchicalNames
     * <p>
     * Consider an iTunes folder "POP" containing a playlist "80s".
     * If set to true, the playlist will be exported to the folder "POP - 80s".
     * If set to false, the playlist will be exported to the folder "80s".
     * Note that if this is set to false,
     * name conflicts may arise if multiple playlists of the same name exist.
     */
    private static final String SETTING_HIERARCHICAL_NAMES = "hierarchicalNames";

    /**
     * Default value for tasks.exportFiles.hierarchicalNames
     */
    private static final Boolean SETTING_HIERARCHICAL_NAMES_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.onlyActualPlaylists
     * <p>
     * If set to true, we will only export actual playlists (but not folders).
     * If set to false, we will export all types of playlists including folders.
     */
    private static final String SETTING_ONLY_ACTUAL_PLAYLISTS = "onlyActualPlaylists";

    /**
     * Default value for tasks.exportFiles.onlyActualPlaylists
     */
    private static final Boolean SETTING_ONLY_ACTUAL_PLAYLISTS_DEFAULT_VALUE = true;


    /**
     * tasks.exportFiles.ignoreDistinguishedPlaylists
     * <p>
     * Set to true to not export distinguished playlists.
     * This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
     */
    private static final String SETTING_IGNORE_DISTINGUISHED_PLAYLISTS = "ignoreDistinguishedPlaylists";

    /**
     * Default value for tasks.exportFiles.ignoreDistinguishedPlaylists
     */
    private static final Boolean SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.ignorePlaylists
     * <p>
     * Specify playlist names that should be ignored, i.e. they will not be exported.
     * Note that if a folder is ignored, the playlists in it will still be exported.
     */
    private static final String SETTING_IGNORE_PLAYLISTS = "ignorePlaylists";

    /**
     * Default value for tasks.exportFiles.ignorePlaylists
     */
    private static final List<String> SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE = List.of(); // empty list

    /**
     * tasks.exportFiles.showContinuousProgress
     * <p>
     * Whether to show a continuously updating progress bar while exporting.
     */
    private static final String SETTING_SHOW_CONTINUOUS_PROGRESS = "showContinuousProgress";

    /**
     * Default value for tasks.exportFiles.showContinuousProgress
     */
    private static final Boolean SETTING_SHOW_CONTINUOUS_PROGRESS_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.toRootFolder
     * <p>
     * Specify playlist (via their name) that should not be exported into their own folder,
     * but into the root folder (tasks.exportFiles.outputFolder) instead.
     */
    private static final String SETTING_TO_ROOT_FOLDER = "toRootFolder";

    /**
     * Default value for tasks.exportFiles.toRootFolder
     */
    private static final List<String> SETTING_TO_ROOT_FOLDER_DEFAULT_VALUE = List.of(); // empty list

    /**
     * tasks.exportFiles.folderNumbering
     * <p>
     * Whether to number the folders.
     * If this is set to true, the folders will be called e.g. "1 POP", "2 ROCK", "3 ...".
     * If this is set to false, the folders will be called e..g. "POP", "ROCK", ...
     */
    private static final String SETTING_FOLDER_NUMBERING = "folderNumbering";

    /**
     * Default value for tasks.exportFiles.folderNumbering
     */
    private static final Boolean SETTING_FOLDER_NUMBERING_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.initialNumber
     * <p>
     * The number of the first folder, see tasks.exportFiles.folderNumbering.
     * This is useful for e.g. car radios that show the root folder as 1.
     */
    private static final String SETTING_INITIAL_NUMBER = "initialNumber";

    /**
     * Default value for tasks.exportFiles.initialNumber
     */
    private static final Integer SETTING_INITIAL_NUMBER_DEFAULT_VALUE = 1;

    /**
     * tasks.exportFiles.trackNumbering
     * <p>
     * If this is set to true, the file name for each track will be prefixes with "NUMBER - ".
     */
    private static final String SETTING_TRACK_NUMBERING = "trackNumbering";

    /**
     * Default value for tasks.exportFiles.trackNumbering
     */
    private static final Boolean SETTING_TRACK_NUMBERING_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.padTrackNumbers
     * <p>
     * If both this and tasks.exportFiles.trackNumbering are set to true,
     * the number at the beginning of each file name will be padded with leading zeros so that
     * the numbers for all tracks in that folder have the same length.
     * <p>
     * E.g. the first track in a playlist with 100+ tracks will be numbered with 001.
     */
    private static final String SETTING_PAD_TRACK_NUMBERS = "padTrackNumbers";

    /**
     * Default value for tasks.exportFiles.padTrackNumbers
     */
    private static final Boolean SETTING_PAD_TRACK_NUMBERS_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.padFolderNumbers
     * <p>
     * If both this and tasks.exportFiles.folderNumbering are set to true,
     * the number at the beginning of each folder  name will be padded with leading zeros so that
     * the numbers for all folder have the same length.
     * <p>
     * E.g. the first folder in a library with 10+ playlists 01.
     */
    private static final String SETTING_PAD_FOLDER_NUMBERS = "padFolderNumbers";

    /**
     * Default value for tasks.exportFiles.padFolderNumbers
     */
    private static final Boolean SETTING_PAD_FOLDER_NUMBERS_DEFAULT_VALUE = true;

    /**
     * tasks.exportFiles.normalize
     * <p>
     * If this is set to true, the folder and file names
     * will be normalized so that it only uses ASCII characters.
     * <p>
     * This may avoid compatibility problems with e.g. car radios.
     */
    private static final String SETTING_NORMALIZE = "normalize";

    /**
     * Default value for tasks.exportFiles.normalize
     */
    private static final Boolean SETTING_NORMALIZE_DEFAULT_VALUE = true;

    static
    {
        // Set default values.
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_IGNORE_PLAYLISTS, SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_OUTPUT_FOLDER, SETTING_OUTPUT_FOLDER_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_SHOW_CONTINUOUS_PROGRESS, SETTING_SHOW_CONTINUOUS_PROGRESS_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_ONLY_ACTUAL_PLAYLISTS, SETTING_ONLY_ACTUAL_PLAYLISTS_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_DELETE_FOLDER, SETTING_DELETE_FOLDER_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_HIERARCHICAL_NAMES, SETTING_HIERARCHICAL_NAMES_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_TO_ROOT_FOLDER, SETTING_TO_ROOT_FOLDER_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_FOLDER_NUMBERING, SETTING_FOLDER_NUMBERING_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_INITIAL_NUMBER, SETTING_INITIAL_NUMBER_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_TRACK_NUMBERING, SETTING_TRACK_NUMBERING_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_PAD_TRACK_NUMBERS, SETTING_PAD_TRACK_NUMBERS_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_NORMALIZE, SETTING_NORMALIZE_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_PAD_FOLDER_NUMBERS, SETTING_PAD_FOLDER_NUMBERS_DEFAULT_VALUE);
        EXPORT_FILES_DEFAULT_SETTINGS.put(SETTING_IGNORE_DISTINGUISHED_PLAYLISTS, SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE);
    }

    public ExportFilesTaskSettings (RawTaskSettings rawTaskSettings)
    {
        super(rawTaskSettings);
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(EXPORT_FILES_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    /**
     * @return tasks.exportFiles.ignorePlaylists
     */
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

    /**
     * @return tasks.exportFiles.toRootFolder
     */
    public List<String> getToRootFolder ()
    {
        String key = SETTING_TO_ROOT_FOLDER;
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

    /**
     * @return tasks.exportFiles.deleteFolder
     */
    public boolean getDeleteFolder ()
    {
        String key = SETTING_DELETE_FOLDER;
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
     * @return tasks.exportFiles.folderNumbering
     */
    public boolean getFolderNumbering ()
    {
        String key = SETTING_FOLDER_NUMBERING;
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
     * @return tasks.exportFiles.initialNumber
     */
    public int getInitialNumber ()
    {
        String key = SETTING_INITIAL_NUMBER;
        Object result = getValueFor(key);

        try
        {
            return (int) result;
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
     * @return tasks.exportFiles.showContinuousProgress
     */
    public boolean getShowContinuousProgress ()
    {
        String key = SETTING_SHOW_CONTINUOUS_PROGRESS;
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
     * @return tasks.exportFiles.onlyActualPlaylists
     */
    public boolean getOnlyActualPlaylists ()
    {
        String key = SETTING_ONLY_ACTUAL_PLAYLISTS;
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
     * @return tasks.exportFiles.ignoreDistinguishedPlaylists
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
     * @return tasks.exportFiles.hierarchicalNames
     */
    public boolean getHierarchicalNames ()
    {
        String key = SETTING_HIERARCHICAL_NAMES;
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
     * @return tasks.exportFiles.trackNumbering
     */
    public boolean getTrackNumbering ()
    {
        String key = SETTING_TRACK_NUMBERING;
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
     * @return tasks.exportFiles.padFolderNumbers
     */
    public boolean getPadFolderNumbers ()
    {
        String key = SETTING_PAD_FOLDER_NUMBERS;
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
     * @return tasks.exportFiles.padTrackNumbers
     */
    public boolean getPadTrackNumbers ()
    {
        String key = SETTING_PAD_TRACK_NUMBERS;
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
     * @return tasks.exportFiles.padTrackNumbers
     */
    public boolean getNormalize ()
    {
        String key = SETTING_NORMALIZE;
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
     * Also replace %USERPROFILE% using {@link Settings#applyUserProfileReplacement(String)}.
     *
     * @return tasks.exportFiles.outputFolder
     */
    public String getOutputFolder ()
    {
        String key = SETTING_OUTPUT_FOLDER;
        Object result = getValueFor(key);

        try
        {
            String resultString = (String) result;
            return Settings.applyUserProfileReplacement(resultString);
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a string, but got " + result.getClass().getSimpleName());
        }
    }

    protected Object getDefaultValueFor (String key)
    {
        return EXPORT_FILES_DEFAULT_SETTINGS.get(key);
    }


}


