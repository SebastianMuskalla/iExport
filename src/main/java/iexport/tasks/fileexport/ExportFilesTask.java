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

import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.parsing.sorting.TrackComparator;
import iexport.tasks.Task;
import iexport.utils.FolderDeleter;
import iexport.utils.IntegerFormatter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class ExportFilesTask extends Task
{

    private static final PrintStream OUT = new PrintStream(System.out, true, UTF_8);

    private final List<Track> toRootFolder = new ArrayList<>();
    private int tracksProcessed = 0;
    private int totalTrackNumber = 0;
    private int nextFolderNumber = 0;
    private int totalFolderNumber = 0;
    private ExportFilesTaskSettings settings;

    @Override
    public String getTaskName ()
    {
        return "exportFiles";
    }

    @Override
    public String getDescription ()
    {
        return "export files in playlist into folders";
    }

    @Override
    public void run ()
    {
        settings = new ExportFilesTaskSettings(rawTaskSettings);

        // Prepare the output folder
        prepareOutputFolder();

        List<Playlist> playlistsToProcess = library.playlists().stream().filter(Predicate.not(this::isIgnored)).toList();

        tracksProcessed = 0;
        totalTrackNumber = (int) playlistsToProcess.stream().mapToLong(Playlist::getNumberOfTracks).sum();
        nextFolderNumber = settings.getSettingInitialNumber();
        toRootFolder.clear();
        totalFolderNumber = playlistsToProcess.size();

        Logging.getLogger().message("Exporting " + totalFolderNumber + " playlists with "
                + totalTrackNumber + " tracks.");

        // Export each playlist
        playlistsToProcess.forEach(this::exportPlaylist);

        // Export the tracks that should go to the root folder

        // The tracks in the root folder might come from different playlists, we should sort them.
        toRootFolder.sort(new TrackComparator());

        // Now we can copy them
        copyTracks(toRootFolder, Paths.get(settings.getSettingOutputFolder()));

        reset();
    }

    private void reset ()
    {
        tracksProcessed = 0;
        totalTrackNumber = 0;
        nextFolderNumber = 0;
        totalFolderNumber = 0;
        toRootFolder.clear();
        settings = null;
    }

    /**
     * Prepare the output folder,
     * i.e. whether it exists,
     * delete it if tasks.exportFiles.deleteFolder is set,
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
                throw new RuntimeException("The specified output folder " + outputFolderPathAsString + " already exists. Delete the folder or set tasks.exportFiles.deleteFolder to true.");
            }
            else
            {
                // Delete the folder
                Logging.getLogger().message("Folder " + outputFolderPathAsString + " exists and tasks.exportFiles.deleteFolder is set to true, deleting it.");

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
        // Check if this playlist should go to the root folder.
        // If yes, we will process it later
        if (settings.getSettingsToRootFolder().contains(playlist.name()))
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
            // TODO
            throw new RuntimeException(e);
        }

        copyTracks(playlist.tracks(), destination);
    }

    private void printProgress (String playlist)
    {
        double ratio = (double) tracksProcessed / (double) totalTrackNumber;

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
        progress += "] " + percentString + "% " + " Exporting " + playlist;

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
     * because it is a folder and tasks.exportFiles.onlyActualPlaylists is set,
     * or because its name is specified in tasks.exportFiles.ignorePlaylists.
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
     * taking tasks.exportFiles.hierarchicalNames and tasks.exportFiles.organizeInFolders into account.
     *
     * @param playlist the playlist
     * @return the path
     */
    private Path destinationFolder (Playlist playlist)
    {
        String outputFolder = settings.getSettingOutputFolder();

        String folderName = "";

        // If tasks.exportFiles.folderNumbering is set, we should start the folder name with a number
        if (settings.getSettingFolderNumbering())
        {
            // If tasks.exportFiles.padFolderNumbers, we should pad the folder numbers to be of the same length
            String folderNumber;
            if (settings.getSettingPadFolderNumbers())
            {
                folderNumber = IntegerFormatter.toStringOfSize(nextFolderNumber, IntegerFormatter.digits(totalFolderNumber));
            }
            else
            {
                folderNumber = Integer.toString(nextFolderNumber);
            }
            nextFolderNumber++;

            folderName += folderNumber;
            folderName += " - ";
        }

        if (settings.getSettingHierarchicalNames() && playlist.ancestry() != null)
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
        if (settings.getSettingNormalize())
        {
            folderName = Normalizer
                    .normalize(folderName, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
        }

        return Paths.get(outputFolder).resolve(folderName);
    }

    /**
     * Compute the path to the file to which the playlist should be exported,
     * taking tasks.exportFiles.hierarchicalNames and tasks.exportFiles.organizeInFolders into account.
     *
     * @param playlist the playlist
     * @return the path
     */
    private String destinationFilename (Path source, int trackNumber, int totalTrackNumberInThisFolder)
    {
        String fileName = "";

        // If tasks.exportFiles.trackNumbering is set, we should start the file name with a number
        if (settings.getSettingTrackNumbering())
        {
            // If tasks.exportFiles.padTrackNumbers is set, we should pad the track numbers to be of the same length
            String fileNumber;
            if (settings.getSettingPadTrackNumbers())
            {
                fileNumber = IntegerFormatter.toStringOfSize(trackNumber, IntegerFormatter.digits(totalTrackNumberInThisFolder));
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
        if (settings.getSettingNormalize())
        {
            fileName = Normalizer
                    .normalize(fileName, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
        }

        return fileName;
    }

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
            if (settings.getSettingShowContinuousProgress())
            {
                printProgress(destination.getFileName().toString());
            }

        }
    }

}
