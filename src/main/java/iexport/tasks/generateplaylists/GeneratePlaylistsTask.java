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

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;
import iexport.utils.FolderDeleter;
import iexport.utils.ProgressPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An advanced task that exports iTunes playlists as .m3u / .m3ua playlist files.
 * <p>
 * For each playlist in iTunes, this task will create a folder inside tasks.generatePlaylists.outputFolder
 * containing a playlist file that contains the paths of the tracks inside the playlist.
 */
public class GeneratePlaylistsTask extends Task
{
    /**
     * Pattern that matches square brackets ('[' and ']').
     */
    private static final String SQUARE_BRACKETS = "\\[|\\]";

    /**
     * Pattern that matches square brackets ('[' and ']').
     */
    private static final Pattern SQUARE_BRACKETS_PATTERN = Pattern.compile(SQUARE_BRACKETS);
    /**
     * Printer for a progress bar.
     */
    private ProgressPrinter progressPrinter;

    /**
     * Playlists that have been processed.
     */
    private int playlistsProcessed = 0;

    /**
     * The settings used for this task.
     */
    private GeneratePlaylistsTaskSettings settings;

    @Override
    public String getTaskName ()
    {
        return "generatePlaylists";
    }

    @Override
    public String getDescription ()
    {
        return "export playlists as .m3u files";
    }

    @Override
    public void initialize (Library library, RawTaskSettings rawTaskSettings)
    {
        super.initialize(library, rawTaskSettings);

        // Convert the RawTaskSettings into settings for this type of task.
        settings = new GeneratePlaylistsTaskSettings(rawTaskSettings);
    }


    @Override
    public void reportProblems ()
    {
        // Check that this task has been initialized.
        super.reportProblems();

        // Settings should now be non-null.
        if (settings == null)
        {
            throw new RuntimeException("Settings have not been initialized for Task " + getTaskName());
        }

        // Report if we are using default settings.
        if (settings.isDefault())
        {
            Logging.getLogger().warning("No settings for task " + getTaskName() + " have been specified in the .yaml file, using all default settings from now on");
        }
        else
        {
            // Report settings that are specified in the .yaml file, but not actually used by this task.
            for (String key : settings.unusedSettings())
            {
                Logging.getLogger().warning("Setting for key \"" + settings.getYamlPath(key) + "\""
                        + " specified in .yaml file, but it is not used by iExport");
            }
        }
    }

    @Override
    public void run ()
    {
        // Prepare the output folder
        prepareOutputFolder();

        // Check which playlists have to be processed (i.e. they are not ignored).
        List<Playlist> playlistsToProcess = library.playlists().stream().filter(Predicate.not(this::isIgnored)).toList();

        // Total number of playlists to process.
        int totalNumberOfPlaylists = playlistsToProcess.size();

        progressPrinter = new ProgressPrinter(totalNumberOfPlaylists);

        Logging.getLogger().message("Exporting " + totalNumberOfPlaylists + " playlists.");

        // Export each playlist
        playlistsToProcess.forEach(this::exportPlaylist);
    }

    /**
     * Prepare the output folder,
     * i.e. whether it exists,
     * delete it if tasks.generatePlaylists.deleteFolder is set,
     * then recreate it.
     */
    private void prepareOutputFolder ()
    {
        // Get the location of the output folder
        String outputFolderPathAsString = settings.getOutputFolder();
        Path outputFolderPath = Paths.get(outputFolderPathAsString);

        if (Files.exists(outputFolderPath))
        {
            // The folder already exists
            if (!settings.getDeleteFolder())
            {
                throw new RuntimeException("The specified output folder " + outputFolderPathAsString + " already exists. Delete the folder or set tasks.generatePlaylists.deleteFolder to true.");
            }
            else
            {
                // Delete the folder
                Logging.getLogger().message("Folder " + outputFolderPathAsString + " exists and tasks.generatePlaylists.deleteFolder is set to true, deleting it.");

                try
                {
                    FolderDeleter.recursiveDelete(outputFolderPath);
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Deleting the folder " + outputFolderPathAsString + "failed", e);
                }
            }

            // Now the folder definitely does not exist and we can delete it
            try
            {
                Logging.getLogger().debug("Creating empty folder " + outputFolderPathAsString + ".");

                Files.createDirectories(outputFolderPath);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Creating the folder " + outputFolderPathAsString + "failed ", e);
            }
        }
    }

    /**
     * Export the specified playlist into an .m3u file inside the output folder.
     *
     * @param playlist the playlist to export
     */
    private void exportPlaylist (Playlist playlist)
    {
        if (settings.getShowContinuousProgress())
        {
            progressPrinter.update(playlistsProcessed, "Generating " + playlist.name());
        }

        Logging.getLogger().debug("Exporting playlist " + playlist);

        Path destination = destinationLocation(playlist);

        // Compute the list of paths that should be put into the playlist
        List<String> contentLines =
                playlist
                        .tracks()
                        .stream()
                        .map((track) -> convertTrack(track, destination))
                        .toList();

        // Merge them into a single string
        String content =
                contentLines
                        .stream()
                        .filter((s) -> !s.equals(""))
                        .reduce((s1, s2) -> s1 + "\n" + s2)
                        .orElse("");

        if (content.isEmpty())
        {
            Logging.getLogger().debug("Skipping playlist " + playlist + " with no valid tracks.");
            return;
        }

        // We can now actually write the file
        writePlaylist(destination, content);

        playlistsProcessed++;
        if (settings.getShowContinuousProgress())
        {
            progressPrinter.update(playlistsProcessed, "Generating " + playlist.name());
        }
    }

    /**
     * Check if a playlist should not be exported
     * because it is a folder and tasks.generatePlaylists.onlyActualPlaylists is set,
     * or because its name is specified in tasks.generatePlaylists.ignorePlaylists.
     *
     * @param playlist the play
     * @return true iff it should not be exported
     */
    private boolean isIgnored (Playlist playlist)
    {
        return
                (settings.getIgnoreDistinguishedPlaylists() && playlist.distinguishedKind() != null)
                        ||
                        (settings.getOnlyActualPlaylists() && playlist.isFolder())
                        ||
                        (playlist.name() != null && settings.getIgnorePlaylists().contains(playlist.name()))
                ;
    }

    /**
     * Compute the path to the file to which the playlist should be exported,
     * taking tasks.generatePlaylists.hierarchicalNames and tasks.generatePlaylists.organizeInFolders into account.
     *
     * @param playlist the playlist
     * @return the path
     */
    private Path destinationLocation (Playlist playlist)
    {
        String outputFolder = settings.getOutputFolder();

        Path result = Paths.get(outputFolder);

        // Compute the file location
        // If tasks.generatePlaylists.organizeInFolders is set, we have to create intermediary folders
        if (settings.getOrganizeInFolders() && playlist.ancestry() != null && playlist.ancestry().size() > 1)
        {
            // We need to skip the last element of ancestry() because that is the playlist itself
            int ancestors = playlist.ancestry().size();
            for (Playlist ancestor : playlist.ancestry().stream().limit(ancestors - 1).toList())
            {
                result = result.resolve(ancestor.name());
            }
        }

        // Compute the file name
        String fileName;
        if (settings.getHierarchicalNames() && playlist.ancestry() != null)
        {
            // If tasks.generatePlaylists.hierarchicalNames is set, the file name is composed of the ancestry
            fileName = playlist.ancestry().stream().map(Playlist::name).reduce((s1, s2) -> s1 + " - " + s2).orElse(playlist.name());
        }
        else
        {
            // Otherwise, the file name is simply the name of the playlist
            fileName = playlist.name();
        }

        // Get the extension from tasks.generatePlaylists.playlistExtension
        fileName += settings.getPlaylistExtension();

        return result.resolve(fileName);
    }

    /**
     * Convert a track into the path string that should be put into the .m3u file
     *
     * @param track                   the track
     * @param playlistDestinationPath the path to the .m3u file (which is needed for relative paths)
     * @return the string with the file path
     */
    private String convertTrack (Track track, Path playlistDestinationPath)
    {
        // Get the URI of the track and convert it
        String uriString = track.location();
        URI uri;
        try
        {
            uri = new URI(uriString);
        }
        catch (URISyntaxException e)
        {
            Logging.getLogger().warning("Error when converting track " + track + ": Bad URI. " + e + " (" + e.getMessage() + "); skipping this track.");
            return "";
        }

        // We can only deal with local files
        if (!uri.getAuthority().equals("localhost"))
        {
            Logging.getLogger().warning("Track " + track + " is at remote location " + uriString + "; skipping this track.");
            return "";
        }

        // Get of the authority (e.g. localhost)
        String pathString = uri.getPath();

        // Under Windows, the String may be of the shape /E:/... something
        // We need to get rid of the initial backslash
        if (pathString.contains(":") && pathString.charAt(0) == '/')
        {
            pathString = pathString.substring(1);
        }

        Path path = Paths.get(pathString);

        // If tasks.generatePlaylists.trackVerification is set, we should verify that the file actually exists.
        // This may be slow!
        if (settings.getTrackVerification() && !Files.exists(path))
        {
            Logging.getLogger().warning("File for track " + track + "  at location " + pathString + " does not exist; skipping this track.");
            return "";
        }

        // If tasks.generatePlaylists.useRelativePaths, we should compute a relative path
        if (settings.getUseRelativePaths() && playlistDestinationPath != null)
        {
            // Computing the relative path may fail if they don't share the same root
            try
            {
                path = playlistDestinationPath.getParent().relativize(path);
            }
            catch (Exception ignored)
            {
                // Just continue with the absolute path
            }
        }

        pathString = path.toString();

        // If tasks.generatePlaylists.warnSquareBrackets, is set, we should warn the user if the path contains [ or ]
        if (settings.getWarnSquareBrackets())
        {
            Matcher matcher = SQUARE_BRACKETS_PATTERN.matcher(pathString);
            if (matcher.find())
            {
                Logging.getLogger().warning("Path " + pathString + " for track " + track + " contains '[' or ']'.");
            }
        }

        // If tasks.generatePlaylists.slashAsSeparator, we should convert backslashes to slashes
        if (settings.getSlashAsSeparator())
        {
            // If the path is of the shape C:\Some\Folder, we should not apply the replacement to the first backslash C:\
            if (pathString.length() > 2 && pathString.charAt(1) == ':' && pathString.charAt(2) == '\\')
            {
                int firstBackslashIndex = pathString.indexOf('\\') + 1;
                String rootString = pathString.substring(0, firstBackslashIndex);
                String remainingString = pathString.substring(firstBackslashIndex);
                remainingString = remainingString.replace('\\', '/');
                pathString = rootString + remainingString;
            }
            else
            {
                pathString = pathString.replace('\\', '/');
            }
        }

        return pathString;
    }

    /**
     * Write an .m3u file
     *
     * @param destination the path to the destination file
     * @param content     the file content as a string, separated by newlines
     */
    private void writePlaylist (Path destination, String content)
    {
        Logging.getLogger().debug("Trying to write file " + destination);

        try
        {
            // Create parent dictionaries if needed
            Path parent = destination.getParent();
            Files.createDirectories(parent);

            // Write contents as UTF_8
            BufferedWriter bufferedWriter = Files.newBufferedWriter(destination, StandardCharsets.UTF_8);
            bufferedWriter.write(content);
            bufferedWriter.close();
        }
        catch (Exception e)
        {
            Logging.getLogger().warning("Writing file " + destination + " failed " + e + " (" + e.getMessage() + ").");
        }
    }
}
