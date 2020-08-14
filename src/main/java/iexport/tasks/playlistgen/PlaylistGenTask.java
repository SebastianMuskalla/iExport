package iexport.tasks.playlistgen;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.domain.Track;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;
import iexport.util.FolderDeleter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlaylistGenTask extends Task
{
    public static final String SHORTHAND = "playlistgen";

    private final PlaylistGenSettings playlistGenSettings;

    private final Path outputFolder;

    public PlaylistGenTask (Library library, TaskSettings taskSettings)
    {
        super(library, taskSettings);
        playlistGenSettings = new PlaylistGenSettings(taskSettings);
        outputFolder = Paths.get(playlistGenSettings.getOutputFolder());
    }

    @Override
    public String getShorthand() {
        return SHORTHAND;
    }

    @Override
    public void run ()
    {
        prepareFolder();

        exportAllPlaylists();
    }


    private void prepareFolder ()
    {
        try
        {
            if (Files.exists(outputFolder))
            {
                FolderDeleter folderDeleter = new FolderDeleter();

                folderDeleter.recursiveDelete(outputFolder);

            }
            Files.createDirectories(outputFolder);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
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
        Logger.log(LogLevel.INFO, "Exporting " + playlist);

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
        playlistDestinationFilenameString.append(playlistGenSettings.getPlaylistExtension());

        String playlistDestinationFileString = playlistDestinationFolderString.toString() + File.separator + playlistDestinationFilenameString.toString();
        Path playlistDestinationFile = Paths.get(playlistDestinationFileString);

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

            if (!trackUri.getAuthority().equals("localhost"))
            {
                throw new RuntimeException("Remote file");
            }


            //Path trackFile = Paths.get("F:\\Audio\\Music\\M\\Michael Jackson - Billie Jean.mp3");
            String string1 = trackUri.getPath();
            if (string1.contains(":") && string1.charAt(0) == '/')
            {
                string1 = string1.substring(1);
            }

//            System.out.println(trackUriString);
//            System.out.println(trackUri);
//            System.out.println(string1);

            Path trackFile = Paths.get(string1);

            if (!Files.exists(trackFile))
            {
                throw new RuntimeException("File at location" + trackFile.toString() + " (taken from " + trackUriString + ") does not exist");
            }

            // For example, on UNIX, if this path is "/a/b" and the given path is "/a/b/c/d" then the resulting relative path would be "c/d".
            Path playlistDestinationPath = Paths.get(playlistDestinationFolderString.toString());
            Path relativePath = playlistDestinationPath.relativize(trackFile);

            // check for brackets [ ] because VLC wants them to be escaped, but PowerAmp doesn't want to be the escaped
            // -> we warn
            String relativePathString = relativePath.toString();
            String patternString = "\\[|\\]";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(relativePathString);
            if (matcher.find())
            {
                Logger.log(LogLevel.WARNING, "Invalid unescapable character in \" + relativePathString");
            }

            content.append(relativePathString);
            content.append('\n');
        }

        writePlaylist(playlistDestinationFile, content.toString());
    }

    private void writePlaylist (Path destination, String content)
    {
        try
        {
            // create parent if needed
            Path parent = destination.getParent();

            // create parent directories
            Files.createDirectories(parent);

            //  Files.createFile(destination);

            // write stuff
            BufferedWriter bufferedWriter = Files.newBufferedWriter(destination, StandardCharsets.UTF_8);
            bufferedWriter.write(content);
            bufferedWriter.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
