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

package iexport.tasks.export;

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;
import iexport.util.FolderDeleter;
import iexport.util.IntegerFormatter;

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


public class FileExportTask extends Task
{
    public static final String SHORTHAND = "fileexport";

    private final FileExportSettings fileExportSettings;

    private final Path outputFolder;

    private int currentPlaylistNumber;

    private IntegerFormatter integerFormatter = new IntegerFormatter();

    public FileExportTask (Library library, TaskSettings taskSettings)
    {
        super(library, taskSettings);
        fileExportSettings = new FileExportSettings(taskSettings);
        outputFolder = Paths.get(fileExportSettings.getOutputFolder());

        currentPlaylistNumber = fileExportSettings.getFirstPlaylistNumber();
    }


    @Override
    public String getShorthand ()
    {
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
        if (Files.exists(outputFolder))
        {
            FolderDeleter folderDeleter = new FolderDeleter();
            try
            {
                folderDeleter.recursiveDelete(outputFolder);
                Files.createDirectories(outputFolder);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void exportAllPlaylists ()
    {
        for (Playlist p : getLibrary().playlistsAtTopLevel())
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
        if (!playlist.children().isEmpty())
        {
            // create a (deep) copy of the call stack to avoid problems with aliasing
            ArrayList<String> newStack = new ArrayList<>(callStack);
            newStack.add(playlist.name());
            for (Playlist sl : playlist.children())
            {
                exportPlaylist(sl, newStack);
            }
        }
    }

    private void exportTracks (Playlist playlist, ArrayList<String> callStack, boolean exportToRootfolder)
    {
        List<Track> tracks = playlist.tracks();

        StringBuilder content = new StringBuilder();

        StringBuilder playlistDestinationFolderString = new StringBuilder(outputFolder.toString());
        playlistDestinationFolderString.append(File.separator);

        if (!exportToRootfolder)
        {

            if (fileExportSettings.getConsecutivePlaylistNumbering())
            {
                playlistDestinationFolderString.append(integerFormatter.toStringOfSize(currentPlaylistNumber, integerFormatter.digits(getLibrary().numberOfPlaylists())));
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

            playlistDestinationFolderString.append(playlist.name());
            playlistDestinationFolderString.append(File.separator);
        }

//        File playlistDestinationFolderFile = new File(playlistDestinationFolderString.toString());
////        if (!playlistDestinationFolderFile.mkdirs())
////        {
////            throw new RuntimeException("Failed to create folder " + playlistDestinationFolderFile.toString());
////        }


        int trackCount = tracks.size();
        int trackNumber = 1;

        StringBuilder playlistString = new StringBuilder();

        for (Track track : tracks)
        {
            String trackUriString = track.location();
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


            String filename = integerFormatter.toStringOfSize(trackNumber, integerFormatter.digits(trackCount));
            filename += " - ";
            filename += trackFile.getFileName();

            Path folder = Paths.get(playlistDestinationFolderString.toString());
            try
            {
                Files.createDirectories(folder);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }

            String trackDestinationFileString = playlistDestinationFolderString.toString() + filename;
            Path trackDestinationPath = Paths.get(trackDestinationFileString);

            playlistString.append(filename).append("\n");

            trackNumber++;

            try
            {
                Files.copy(trackFile, trackDestinationPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        if (fileExportSettings.createPlaylistPerFolder())
        {
            try
            {
                // create parent if needed
                // TODO: Should use .m3u8 here
                String destinationString = playlistDestinationFolderString.toString() + File.separator + playlist.name() + ".m3u";

                //  Files.createFile(destination);

                // write stuff
                BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(destinationString), StandardCharsets.UTF_8);
                bufferedWriter.write(playlistString.toString());
                bufferedWriter.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

}
