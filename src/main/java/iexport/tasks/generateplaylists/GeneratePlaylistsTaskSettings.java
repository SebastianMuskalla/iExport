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

package iexport.tasks.generateplaylists;

import iexport.settings.RawTaskSettings;
import iexport.settings.Settings;
import iexport.settings.TaskSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@SuppressWarnings("ConstantConditions")
public class GeneratePlaylistsTaskSettings extends TaskSettings
{

    /**
     * Default settings for the generatePlaylists task
     */
    private static final Map<String, Object> GENERATE_PLAYLISTS_DEFAULT_SETTINGS = new HashMap<>();

    /**
     * tasks.generatePlaylists.outputFolder
     * <p>
     * Folder at which the playlist files should be generated
     * Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
     * Note that backslashes ("\") need to be escaped as "\\".
     */
    private static final String SETTING_OUTPUT_FOLDER = "outputFolder";

    /**
     * Default value for tasks.generatePlaylists.outputFolder
     */
    private static final String SETTING_OUTPUT_FOLDER_DEFAULT_VALUE = "%USERPROFILE%\\Desktop\\iExport\\Playlists";

    /**
     * tasks.generatePlaylists.deleteFolder
     * <p>
     * If set to false, iExport will only proceed if outputFolder does not exist yet.
     * If set to true, the folder will be deleted if it already exists.
     */
    private static final String SETTING_DELETE_FOLDER = "deleteFolder";

    /**
     * Default value for tasks.generatePlaylists.deleteFolder
     */
    private static final Boolean SETTING_DELETE_FOLDER_DEFAULT_VALUE = false;

    /**
     * tasks.generatePlaylists.organizeInFolders
     * <p>
     * If set to true, the playlists will be organized in folders.
     * e.g. an iTunes folder "POP" containing a playlist "80s" will lead to the file "POP/80s.m3u"
     * If set to false, all playlists will be exported into the same folder.
     * Note that file name conflicts may arise if multiple playlists of the same name exist.
     */
    private static final String SETTING_ORGANIZE_IN_FOLDERS = "organizeInFolders";

    /**
     * Default value for tasks.generatePlaylists.organizeInFolders
     */
    private static final Boolean SETTING_ORGANIZE_IN_FOLDERS_DEFAULT_VALUE = true;

    /**
     * tasks.generatePlaylists.hierarchicalNames
     * <p>
     * Consider an iTunes folder "POP" containing a playlist "80s".
     * If set to false, the playlist file will be called "80s.m3u".
     * If set to true, the playlist file will be called "POP - 80s.m3u".
     * This setting may avoid naming conflicts if organizeInFolders is set to false.
     */
    private static final String SETTING_HIERARCHICAL_NAMES = "hierarchicalNames";

    /**
     * Default value for tasks.generatePlaylists.hierarchicalNames
     */
    private static final Boolean SETTING_HIERARCHICAL_NAMES_DEFAULT_VALUE = true;

    /**
     * tasks.generatePlaylists.onlyActualPlaylists
     * <p>
     * If set to true, we will only export actual playlists (but not folders).
     * If set to false, we will export all types of playlists including folders.
     */
    private static final String SETTING_ONLY_ACTUAL_PLAYLISTS = "onlyActualPlaylists";

    /**
     * Default value for tasks.generatePlaylists.onlyActualPlaylists
     */
    private static final Boolean SETTING_ONLY_ACTUAL_PLAYLISTS_DEFAULT_VALUE = false;

    /**
     * tasks.generatePlaylists.ignoreDistinguishedPlaylists
     * <p>
     * Set to true to not export distinguished playlists.
     * This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
     */
    private static final String SETTING_IGNORE_DISTINGUISHED_PLAYLISTS = "ignoreDistinguishedPlaylists";

    /**
     * Default value for tasks.generatePlaylists.ignoreDistinguishedPlaylists
     */
    private static final Boolean SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE = false;


    /**
     * tasks.generatePlaylists.playlistExtension
     * <p>
     * The extension (e.g. ".m3u", ".m3u8") of the generated playlist files.
     * We recommend using ".m3u8" to signal that the paths inside the playlist may contain unicode characters.
     */
    private static final String SETTING_PLAYLIST_EXTENSION = "playlistExtension";

    /**
     * Default value for tasks.generatePlaylists.playlistExtension
     */
    private static final String SETTING_PLAYLIST_EXTENSION_DEFAULT_VALUE = ".m3u8";

    /**
     * tasks.generatePlaylists.useRelativePaths
     * <p>
     * If set to false, the generated playlist files will contain absolute paths to the tracks.
     * If set to false, the generated playlist files will contain relative paths.
     * <p>
     * Consider a track stored at "C:\Music\Tracks\Track.mp3"
     * and assume we export the playlists to "C:\Music\Playlists\" (outputFolder).
     * If relative paths are enabled, a playlist containing that track will
     * contain "..\Tracks\Track.mp3" instead of "C:\Music\Tracks\Track.mp3".
     * <p>
     * Advantage: The whole folder containing tracks and playlists can be copied to
     * another device (e.g. an android phone) and it will still work.
     * <p>
     * Disadvantage: If the folder containing the playlists is moved,
     * they will stop working.
     */
    private static final String SETTING_USE_RELATIVE_PATHS = "useRelativePaths";

    /**
     * Default value for tasks.generatePlaylists.useRelativePaths
     */
    private static final Boolean SETTING_USE_RELATIVE_PATHS_DEFAULT_VALUE = false;

    /**
     * tasks.generatePlaylists.warnSquareBrackets
     * <p>
     * If set to true, iExport will print a warning if a file path inside a playlist
     * contains square brackets ('[' or ']').
     * This is useful because some players require these to be escaped (e.g. VLC)
     * while others cannot deal with escaped brackets (e.g. Poweramp).
     */
    private static final String SETTING_WARN_SQUARE_BRACKETS = "warnSquareBrackets";

    /**
     * Default value for tasks.generatePlaylists.warnSquareBrackets
     */
    private static final Boolean SETTING_WARN_SQUARE_BRACKETS_DEFAULT_VALUE = true;

    /**
     * tasks.generatePlaylists.ignorePlaylists
     * <p>
     * Specify playlist names that should be ignored, i.e. they will not be exported.
     * Note that if a folder is ignored, the playlists in it will still be exported.
     */
    private static final String SETTING_IGNORE_PLAYLISTS = "ignorePlaylists";

    /**
     * Default value for tasks.generatePlaylists.ignorePlaylists
     */
    private static final List<String> SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE = List.of(); // empty list

    /**
     * tasks.generatePlaylists.showContinuousProgress
     * <p>
     * Whether to show a continuously updating progress bar while exporting.
     */
    private static final String SETTING_SHOW_CONTINUOUS_PROGRESS = "showContinuousProgress";

    /**
     * Default value for tasks.generatePlaylists.showContinuousProgress
     */
    private static final Boolean SETTING_SHOW_CONTINUOUS_PROGRESS_DEFAULT_VALUE = true;

    /**
     * tasks.generatePlaylists.slashAsSeparator
     * <p>
     * If set to false, use Backslash ('\', Windows notation) in file paths inside the playlist files.
     * If set to true, use Slash ('/', Unix/Linux/Mac notation) in file paths inside the playlist files.
     */
    private static final String SETTING_SLASH_AS_SEPARATOR = "slashAsSeparator";

    /**
     * Default value for tasks.generatePlaylists.slashAsSeparator
     */
    private static final Boolean SETTING_SLASH_AS_SEPARATOR_DEFAULT_VALUE = false;

    /**
     * tasks.generatePlaylists.trackVerification
     * <p>
     * If set to true, we verify for each track that the corresponding file actually exists.
     * This may substantially slow down this task.
     */
    private static final String SETTING_TRACK_VERIFICATION = "trackVerification";

    /**
     * Default value for tasks.generatePlaylists.trackVerification
     */
    private static final Boolean SETTING_TRACK_VERIFICATION_DEFAULT_VALUE = true;

    static
    {
        // Set default values.
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_IGNORE_PLAYLISTS, SETTING_IGNORE_PLAYLISTS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_OUTPUT_FOLDER, SETTING_OUTPUT_FOLDER_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_SHOW_CONTINUOUS_PROGRESS, SETTING_SHOW_CONTINUOUS_PROGRESS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_SLASH_AS_SEPARATOR, SETTING_SLASH_AS_SEPARATOR_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_WARN_SQUARE_BRACKETS, SETTING_WARN_SQUARE_BRACKETS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_USE_RELATIVE_PATHS, SETTING_USE_RELATIVE_PATHS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_PLAYLIST_EXTENSION, SETTING_PLAYLIST_EXTENSION_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_ONLY_ACTUAL_PLAYLISTS, SETTING_ONLY_ACTUAL_PLAYLISTS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_DELETE_FOLDER, SETTING_DELETE_FOLDER_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_ORGANIZE_IN_FOLDERS, SETTING_ORGANIZE_IN_FOLDERS_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_HIERARCHICAL_NAMES, SETTING_HIERARCHICAL_NAMES_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_TRACK_VERIFICATION, SETTING_TRACK_VERIFICATION_DEFAULT_VALUE);
        GENERATE_PLAYLISTS_DEFAULT_SETTINGS.put(SETTING_IGNORE_DISTINGUISHED_PLAYLISTS, SETTING_IGNORE_DISTINGUISHED_PLAYLISTS_DEFAULT_VALUE);
    }

    public GeneratePlaylistsTaskSettings (RawTaskSettings rawTaskSettings)
    {
        super(rawTaskSettings);
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(GENERATE_PLAYLISTS_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    /**
     * @return tasks.generatePlaylists.ignorePlaylists
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
     * @return tasks.generatePlaylists.deleteFolder
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
     * @return tasks.generatePlaylists.trackVerification
     */
    public boolean getTrackVerification ()
    {
        String key = SETTING_TRACK_VERIFICATION;
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
     * @return tasks.generatePlaylists.showContinuousProgress
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
     * @return tasks.generatePlaylists.slashAsSeparator
     */
    public boolean getSlashAsSeparator ()
    {
        String key = SETTING_SLASH_AS_SEPARATOR;
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
     * @return tasks.generatePlaylists.warnSquareBrackets
     */
    public boolean getWarnSquareBrackets ()
    {
        String key = SETTING_WARN_SQUARE_BRACKETS;
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
     * @return tasks.generatePlaylists.useRelativePaths
     */
    public boolean getUseRelativePaths ()
    {
        String key = SETTING_USE_RELATIVE_PATHS;
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
     * @return tasks.generatePlaylists.onlyActualPlaylists
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
     * @return tasks.generatePlaylists.hierarchicalNames
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
     * @return tasks.generatePlaylists.organizeInFolders
     */
    public boolean getOrganizeInFolders ()
    {
        String key = SETTING_ORGANIZE_IN_FOLDERS;
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
     * @return tasks.generatePlaylists.ignoreDistinguishedPlaylists
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
     * Also replace %USERPROFILE% using {@link Settings#applyUserProfileReplacement(String)}
     *
     * @return tasks.generatePlaylists.outputFolder
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

    /**
     * @return tasks.generatePlaylists.playlistExtension
     */
    public String getPlaylistExtension ()
    {
        String key = SETTING_PLAYLIST_EXTENSION;
        Object result = getValueFor(key);

        try
        {
            return (String) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(key)
                    + ", expected a string, but got " + result.getClass().getSimpleName());
        }
    }

    protected Object getDefaultValueFor (String key)
    {
        return GENERATE_PLAYLISTS_DEFAULT_SETTINGS.get(key);
    }

}


