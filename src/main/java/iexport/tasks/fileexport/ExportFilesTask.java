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

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.parsing.sorting.TrackComparator;
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;
import iexport.utils.FolderDeleter;
import iexport.utils.IntegerFormatter;
import iexport.utils.ProgressPrinter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * An advanced task that exports iTunes playlists as folders.
 * <p>
 * For each playlist in iTunes, this task will create a folder inside tasks.exportFiles.outputFolder
 * and then copy all files for tracks inside that playlist into that folder.
 */
public class ExportFilesTask extends Task
{
    /**
     * A list of tracks that should be exported into the root folder
     */
    private final List<Track> toRootFolder = new ArrayList<>();

    /**
     * Printer for a progress bar.
     */
    private ProgressPrinter progressPrinter;

    /**
     * The settings used for this task.
     */
    private ExportFilesTaskSettings settings;

    /**
     * The number of tracks that have been processed
     * <p>
     * Needed for displaying a progress bar.
     */
    private int tracksProcessed = 0;

    /**
     * The number of the next folder that should be exported.
     * <p>
     * Needed for folder numbering.
     */
    private int nextFolderNumber = 0;

    /**
     * The total number of folders that should be exported.
     * <p>
     * Needed for folder numbering.
     */
    private int totalFolderNumber = 0;

    @Override
    public String getTaskName ()
    {
        return "exportFiles";
    }

    @Override
    public String getDescription ()
    {
        return "export playlists as folders containing their tracks as files";
    }

    @Override
    public void initialize (Library library, RawTaskSettings rawTaskSettings)
    {
        super.initialize(library, rawTaskSettings);

        // Convert the RawTaskSettings into settings for this type of task.
        settings = new ExportFilesTaskSettings(rawTaskSettings);
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

        // Initialize some variables
        tracksProcessed = 0;
        nextFolderNumber = settings.getInitialNumber();
        totalFolderNumber = playlistsToProcess.size();

        int totalTrackNumber = (int) playlistsToProcess.stream().mapToLong(Playlist::getNumberOfTracks).sum();

        progressPrinter = new ProgressPrinter(totalTrackNumber);

        Logging.getLogger().message("Exporting " + totalFolderNumber + " playlists with "
                + totalTrackNumber + " tracks.");

        // Export each playlist
        playlistsToProcess.forEach(this::exportPlaylist);

        // Export the tracks that should go to the root folder

        // The tracks in the root folder might come from different playlists, we should sort them.
        toRootFolder.sort(new TrackComparator());

        // Now we can copy them
        copyTracks(toRootFolder, Paths.get(settings.getOutputFolder()));
    }

    /**
     * Prepare the output folder,
     * i.e. check whether it exists,
     * delete it if tasks.exportFiles.deleteFolder is set,
     * then recreate it.
     */
    private void prepareOutputFolder ()
    {
        // Get the location of the output folder.
        String outputFolderPathAsString = settings.getOutputFolder();
        Path outputFolderPath = Paths.get(outputFolderPathAsString);

        if (Files.exists(outputFolderPath))
        {
            // The folder already exists.
            if (!settings.getDeleteFolder())
            {
                throw new RuntimeException("The specified output folder " + outputFolderPathAsString + " already exists. Delete the folder or set tasks.exportFiles.deleteFolder to true.");
            }
            else
            {
                // Delete the folder
                Logging.getLogger().message("Folder " + outputFolderPathAsString + " exists and tasks.exportFiles.deleteFolder is set to true, deleting it.");

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

    private void exportPlaylist (Playlist playlist)
    {
        // Check if this playlist should go to the root folder.
        // If yes, we will process it later.
        if (settings.getToRootFolder().contains(playlist.name()))
        {
            toRootFolder.addAll(playlist.tracks());
            return;
        }

        Logging.getLogger().debug("Exporting playlist " + playlist);

        // Create the folder this playlist should be exported to
        Path destination = destinationFolder(playlist);

        try
        {
            Files.createDirectories(destination);
        }
        catch (IOException e)
        {
            throw new RuntimeException(" Creating the directory at " + destination + " failed", e);
        }

        copyTracks(playlist.tracks(), destination);
    }

    /**
     * Check if a playlist should not be exported
     * because it is a folder and tasks.exportFiles.onlyActualPlaylists is set,
     * because it is distinguished and tasks.exportFiles.ignoreDistinguishedPlaylists is set,
     * or because its name is specified in tasks.exportFiles.ignorePlaylists.
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
     * taking tasks.exportFiles.hierarchicalNames and tasks.exportFiles.organizeInFolders into account.
     *
     * @param playlist the playlist
     * @return the path
     */
    private Path destinationFolder (Playlist playlist)
    {
        String outputFolder = settings.getOutputFolder();

        String folderName = "";

        // If tasks.exportFiles.folderNumbering is set, we should start the folder name with a number
        if (settings.getFolderNumbering())
        {
            // If tasks.exportFiles.padFolderNumbers, we should pad the folder numbers to be of the same length
            String folderNumber;
            if (settings.getPadFolderNumbers())
            {
                folderNumber = IntegerFormatter.pad(nextFolderNumber, IntegerFormatter.digits(totalFolderNumber), '0');
            }
            else
            {
                folderNumber = Integer.toString(nextFolderNumber);
            }
            nextFolderNumber++;

            folderName += folderNumber;
            folderName += " - ";
        }

        if (settings.getHierarchicalNames() && playlist.ancestry() != null)
        {
            // If tasks.exportFiles.hierarchicalNames is set, the folder name is composed of the ancestry
            folderName += playlist.ancestry().stream().map(Playlist::name).reduce((s1, s2) -> s1 + " - " + s2).orElse(playlist.name());
        }
        else
        {
            // Otherwise, the folder name is simply the name of the playlist
            folderName = playlist.name();
        }

        // If tasks.exportFiles.normalize is set, we should normalize the name to only use ASCII
        if (settings.getNormalize())
        {
            folderName = Normalizer
                    .normalize(folderName, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
        }

        return Paths.get(outputFolder).resolve(folderName);
    }

    /**
     * Compute the path to the file to which a track should be exported.
     *
     * @param source                       the source path of the track
     * @param trackNumber                  the track number
     * @param totalTrackNumberInThisFolder the total number of tracks in that folder
     * @return the destination path as string
     */
    private String destinationFilename (Path source, int trackNumber, int totalTrackNumberInThisFolder)
    {
        String fileName = "";

        // If tasks.exportFiles.trackNumbering is set, we should start the file name with a number
        if (settings.getTrackNumbering())
        {
            // If tasks.exportFiles.padTrackNumbers is set, we should pad the track numbers to be of the same length
            String fileNumber;
            if (settings.getPadTrackNumbers())
            {
                fileNumber = IntegerFormatter.pad(trackNumber, IntegerFormatter.digits(totalTrackNumberInThisFolder), '0');
            }
            else
            {
                fileNumber = Integer.toString(trackNumber);
            }

            fileName += fileNumber;
            fileName += " - ";
        }

        // Then we can use the real filename on the dic.
        fileName += source.getFileName().toString();

        // If tasks.exportFiles.normalize is set, we should normalize the name to only use ASCII
        if (settings.getNormalize())
        {
            fileName = Normalizer
                    .normalize(fileName, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
        }

        return fileName;
    }

    /**
     * Copy the source files for a list of tracks into a destination folder,
     * renaming them appropriately.
     *
     * @param tracks      a list of tracks
     * @param destination the destination folder
     */
    private void copyTracks (List<Track> tracks, Path destination)
    {
        int trackNumber = 0;
        int totalTrackNumberInThisFolder = tracks.size();

        for (Track track : tracks)
        {
            trackNumber++;

            String uriString = track.location();
            URI uri;
            try
            {
                uri = new URI(uriString);
            }
            catch (URISyntaxException e)
            {
                Logging.getLogger().warning("Error when converting track " + track + ": Bad URI. " + e + " (" + e.getMessage() + "); skipping this track.");
                return;
            }

            // We can only deal with local files
            if (!uri.getAuthority().equals("localhost"))
            {
                Logging.getLogger().warning("Track " + track + " is at remote location " + uriString + "; skipping this track.");
                return;
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

            String fileName = destinationFilename(path, trackNumber, totalTrackNumberInThisFolder);

            String destinationString = destination.toString();
            destinationString += File.separator;
            destinationString += fileName;
            Path destinationPath = Paths.get(destinationString);

            try
            {
                Files.copy(path, destinationPath, REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }

            tracksProcessed++;
            if (settings.getShowContinuousProgress())
            {
                progressPrinter.update(tracksProcessed, "Exporting to " + destination.getFileName().toString());
            }
        }
    }
}
