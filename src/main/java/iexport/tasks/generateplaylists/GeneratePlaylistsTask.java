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

import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.tasks.Task;
import iexport.utils.FolderDeleter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;


public class GeneratePlaylistsTask extends Task
{

    private static final PrintStream OUT = new PrintStream(System.out, true, UTF_8);

    private static final String SQUARE_BRACKETS = "\\[|\\]";
    private static final Pattern SQUARE_BRACKETS_PATTERN = Pattern.compile(SQUARE_BRACKETS);

    private int playlistsProcessed = 0;
    private int playlistsToProcess = 0;

    private GeneratePlaylistsTaskSettings settings;

    @Override
    public String getTaskName ()
    {
        return "generatePlaylists";
    }

    @Override
    public String getDescription ()
    {
        return "export playlists as m3u";
    }

    @Override
    public void run ()
    {
        settings = new GeneratePlaylistsTaskSettings(rawTaskSettings);

        // Prepare the output folder
        prepareOutputFolder();

        playlistsToProcess = library.numberOfPlaylists();

        Logging.getLogger().message("Exporting " + playlistsToProcess + " playlists.");

        // Export each playlist
        library.playlists().forEach(this::exportPlaylist);

        reset();
    }

    private void reset ()
    {
        playlistsProcessed = 0;
        playlistsToProcess = 0;
        settings = null;
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
        String outputFolderPathAsString = settings.getSettingOutputFolder();
        Path outputFolderPath = Paths.get(outputFolderPathAsString);

        if (Files.exists(outputFolderPath))
        {
            // The folder already exists
            if (!settings.getSettingDeleteFolder())
            {
                throw new RuntimeException("The specified output folder " + outputFolderPathAsString + " already exists. Delete the folder or set tasks.generatePlaylists.deleteFolder to true.");
            }
            else
            {
                // Delete the folder
                Logging.getLogger().message("Folder " + outputFolderPathAsString + " exists and tasks.generatePlaylists.deleteFolder is set to true, deleting it.");

                FolderDeleter folderDeleter = new FolderDeleter();
                try
                {
                    folderDeleter.recursiveDelete(outputFolderPath);
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

    private void exportPlaylist (Playlist playlist)
    {
        if (settings.getSettingShowContinuousProgress())
        {
            printProgress(playlist);
        }

        // We first check if this playlist is ignored.
        if (isIgnored(playlist))
        {
            Logging.getLogger().debug("Ignoring playlist " + playlist);
            return;
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
        if (settings.getSettingShowContinuousProgress())
        {
            printProgress(playlist);
        }
    }

    private void printProgress (Playlist playlist)
    {
        double ratio = (double) playlistsProcessed / (double) playlistsToProcess;

        int percent = (int) Math.round(ratio * 100);

        String percentString = (percent > 99) ? Integer.toString(percent) :
                (percent > 9) ? " " + Integer.toString(percent) :
                        (percent > 0) ? "  " + Integer.toString(percent) : "  0";

        int totalSegments = 20;
        int filledSegments = (int) Math.round(ratio * totalSegments);

        String progress = "\r" + "[";
        for (int i = 0; i < filledSegments; i++)
        {
            progress += '\u2588';
        }
        for (int i = filledSegments; i < totalSegments; i++)
        {
            progress += '\u00B7';
        }
        progress += "] " + percentString + "% " + " Exporting " + playlist.name();

        // Make sure progress is exactly 80 characters long
        if (progress.length() > 80)
        {
            progress = progress.substring(0, 77);
            progress += "...";
        }
        else
        {
            progress += " ".repeat(80 - progress.length());
        }

        OUT.print(progress);

        if (percent == 100)
        {
            OUT.println("");
            OUT.println("Done!");
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
                (settings.getSettingOnlyActualPlaylists() && playlist.isFolder())
                        ||
                        (playlist.name() != null && settings.getSettingIgnorePlaylists().contains(playlist.name()))
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
        String outputFolder = settings.getSettingOutputFolder();

        Path result = Paths.get(outputFolder);

        // Compute the file location
        // If tasks.generatePlaylists.organizeInFolders is set, we have to create intermediary folders
        if (settings.getSettingOrganizeInFolders() && playlist.ancestry() != null && playlist.ancestry().size() > 1)
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
        if (settings.getSettingHierarchicalNames() && playlist.ancestry() != null)
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
        fileName += settings.getSettingPlaylistExtension();

        return result.resolve(fileName);
    }

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
        if (settings.getSettingTrackVerification() && !Files.exists(path))
        {
            Logging.getLogger().warning("File for track " + track + "  at location " + pathString + " does not exist; skipping this track.");
            return "";
        }

        // If tasks.generatePlaylists.useRelativePaths, we should compute a relative path
        if (settings.getSettingUseRelativePaths() && playlistDestinationPath != null)
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
        if (settings.getSettingWarnSquareBrackets())
        {
            Matcher matcher = SQUARE_BRACKETS_PATTERN.matcher(pathString);
            if (matcher.find())
            {
                Logging.getLogger().warning("Path " + pathString + " for track " + track + " contains '[' or ']'.");
            }
        }

        // If tasks.generatePlaylists.slashAsSeparator, we should convert backslashes to slashes
        if (settings.getSettingSlashAsSeparator())
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
