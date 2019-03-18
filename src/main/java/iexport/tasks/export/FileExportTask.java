package iexport.tasks.export;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.domain.Track;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;
import iexport.util.FolderDeleter;
import iexport.util.IntegerFormatter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileExportTask extends Task
{
    public static final String SHORTHAND = "fileexport";

    private final FileExportSettings fileExportSettings;

    private final File outputFolder;

    private int currentPlaylistNumber;

    private IntegerFormatter integerFormatter = new IntegerFormatter();

    public FileExportTask (Library library, TaskSettings taskSettings)
    {
        super(SHORTHAND, library, taskSettings);
        fileExportSettings = new FileExportSettings(taskSettings);
        outputFolder = new File(fileExportSettings.getOutputFolder());

        currentPlaylistNumber = fileExportSettings.getFirstPlaylistNumber();
    }

    @Override
    public void run ()
    {
        prepareFolder();

        exportAllPlaylists();
    }


    private void prepareFolder ()
    {
        if (outputFolder.exists())
        {
            FolderDeleter folderDeleter = new FolderDeleter();
            if (!folderDeleter.recursiveDelete(outputFolder))
            {
                throw new RuntimeException("Couldnt delete " + outputFolder);
            }
        }
        outputFolder.mkdirs();
    }


    private void exportAllPlaylists ()
    {
        for (Playlist p : getLibrary().getPlaylistsAtTopLevel())
        {
            export(p);
        }
    }

    private void export (Playlist p)
    {
        exportPlaylist(p, new ArrayList<>());
    }

    private void exportPlaylist (Playlist playlist, ArrayList<String> callStack)
    {
        // export the playlist unless it is ignored
        if (!fileExportSettings.isIgnored(playlist))
        {
            if (fileExportSettings.isExportedToRootFolder(playlist))
            {
                exportTracks(playlist, callStack, true);
            }
            else
            {
                exportTracks(playlist, callStack, false);
            }
        }

        // TODO should children of ignored playlists be ignored too?

        // if p has children
        if (!playlist.getChildren().isEmpty())
        {
            // create a (deep) copy of the call stack to avoid problems with aliasing
            ArrayList<String> newStack = new ArrayList<>(callStack);
            newStack.add(playlist.getName());
            for (Playlist sl : playlist.getChildren())
            {
                exportPlaylist(sl, newStack);
            }
        }
    }

    private void exportTracks (Playlist playlist, ArrayList<String> callStack, boolean exportToRootfolder)
    {
        List<Track> tracks = playlist.getTracks();

        StringBuilder content = new StringBuilder();

        StringBuilder playlistDestinationFolderString = new StringBuilder(outputFolder.toString());
        playlistDestinationFolderString.append(File.separator);

        if (!exportToRootfolder)
        {

            if (fileExportSettings.getConsecutivePlaylistNumbering())
            {
                playlistDestinationFolderString.append(integerFormatter.toStringOfSize(currentPlaylistNumber, integerFormatter.digits(getLibrary().getNumberOfPlayslists())));
                playlistDestinationFolderString.append(" ");
                currentPlaylistNumber++;
            }


            if (fileExportSettings.getPrependParents())
            {
                for (String s : callStack)
                {
                    playlistDestinationFolderString.append(s);
                    playlistDestinationFolderString.append(" - ");
                }
            }

            playlistDestinationFolderString.append(playlist.getName());
            playlistDestinationFolderString.append(File.separator);
        }

        File playlistDestinationFolderFile = new File(playlistDestinationFolderString.toString());
        if (!playlistDestinationFolderFile.mkdirs())
        {
            throw new RuntimeException("Failed to create folder " + playlistDestinationFolderFile.toString());
        }


        int trackCount = tracks.size();
        int trackNumber = 1;

        for (Track track : tracks)
        {
            String trackUriString = track.getLocation();
            URI trackUri = null;
            try
            {
                trackUri = new URI(trackUriString);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
            // discord the "authority" part of the uri
            File trackFile = new File(trackUri.getPath());

            if (!trackFile.exists())
            {
                throw new RuntimeException("File at location" + trackFile.toString() + " (taken from " + trackUriString + ") does not exist");
            }


            Path trackSourcePath = Paths.get(trackFile.toURI());

            String trackDestinationFileString = playlistDestinationFolderString.toString();
            trackDestinationFileString += integerFormatter.toStringOfSize(trackNumber, integerFormatter.digits(trackCount));
            trackDestinationFileString += " - ";
            trackDestinationFileString += trackSourcePath.getFileName();

            trackNumber++;

            Path trackDestinationPath = Paths.get(trackDestinationFileString);

            try
            {
                Files.copy(trackSourcePath, trackDestinationPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
    }

}
