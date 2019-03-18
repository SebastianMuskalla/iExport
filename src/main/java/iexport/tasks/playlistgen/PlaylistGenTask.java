package iexport.tasks.playlistgen;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.domain.Track;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;
import iexport.util.FolderDeleter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//package iexport.tasks.playlistgen;
//
//import model.itunes.ItunesLibrary;
//import model.itunes.Playlist;
//import model.itunes.Track;
//import model.settings.CommonSettings;
//import model.settings.PlaylistGenerationSettings;
//import old.Settings;
//import service.itunes.LibraryTagParser;
//import service.util.FileSystem;
//import service.util.LocationStringParser;
//import service.util.PathToFile;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.TreeSet;
//
///**
// * @author Sebastian Muskalla
// */


public class PlaylistGenTask extends Task
{
    public static final String SHORTHAND = "playlistgen";

    private final PlaylistGenSettings playlistGenSettings;

    private final File outputFolder;

    public PlaylistGenTask (Library library, TaskSettings taskSettings)
    {
        super(SHORTHAND, library, taskSettings);
        playlistGenSettings = new PlaylistGenSettings(taskSettings);
        outputFolder = new File(playlistGenSettings.getOutputFolder());
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
        if (!playlistGenSettings.isIgnored(playlist))
        {
            exportTracks(playlist, callStack);
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

    private void exportTracks (Playlist playlist, ArrayList<String> callStack)
    {
        List<Track> tracks = playlist.getTracks();

        StringBuilder content = new StringBuilder();

        StringBuilder playlistDestinationFolderString = new StringBuilder(outputFolder.toString());

        if (playlistGenSettings.getOrganizeInFolders())
        {
            for (String s : callStack)
            {
                playlistDestinationFolderString.append(File.separator).append(s);
            }
        }

        StringBuilder playlistDestinationFilenameString = new StringBuilder();
        if (playlistGenSettings.getPrependParents())
        {
            for (String s : callStack)
            {
                playlistDestinationFilenameString.append(s);
                playlistDestinationFilenameString.append(" - ");
            }
        }
        playlistDestinationFilenameString.append(playlist.getName());
        playlistDestinationFilenameString.append(".m3u");

        String playlistDestinationFileString = playlistDestinationFolderString.toString() + File.separator + playlistDestinationFilenameString.toString();


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


            // For example, on UNIX, if this path is "/a/b" and the given path is "/a/b/c/d" then the resulting relative path would be "c/d".
            Path playlistDestinationPath = Paths.get(playlistDestinationFolderString.toString());
            Path trackPath = Paths.get(trackFile.toURI());
            Path relativePath = playlistDestinationPath.relativize(trackPath);

            content.append(relativePath);
            content.append('\n');
        }

        writePlaylist(new File(playlistDestinationFileString), content.toString());
    }

    private void writePlaylist (File loc, String content)
    {
        // create parent if needed
        // TODO does this work if nesting > 1?
        if (loc.getParentFile() != null && !loc.getParentFile().exists())
        {
            if (!loc.getParentFile().mkdirs())
            {
                throw new RuntimeException("Failed to create parent file " + loc.getParentFile().toString());
            }
        }

        try
        {
            if (!loc.createNewFile())
            {
                throw new RuntimeException("Could not create Playlist at " + loc.getName());
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not create Playlist at " + loc.getName() + " due to IOException", e);
        }

        PrintWriter file_writer;

        try
        {
            file_writer = new PrintWriter(loc);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not write " + loc.getName());
        }

        file_writer.println(content);
        file_writer.close();
    }
}
