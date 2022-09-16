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
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;


public class GeneratePlaylistsTask implements Task
{
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
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        GeneratePlaylistsTaskSettings settings = new GeneratePlaylistsTaskSettings(rawTaskSettings);
    }

}


//public class PlaylistGenTask extends Task
//{
//    public static final String SHORTHAND = "playlistgen";
//
//    private final PlaylistGenSettings playlistGenSettings;
//
//    private final Path outputFolder;
//
//    public PlaylistGenTask (Library library, TaskSettings taskSettings)
//    {
//        super(library, taskSettings);
//        playlistGenSettings = new PlaylistGenSettings(taskSettings);
//        outputFolder = Paths.get(playlistGenSettings.getOutputFolder());
//    }
//
//    @Override
//    public String getShorthand ()
//    {
//        return SHORTHAND;
//    }
//
//    @Override
//    public void run ()
//    {
//        prepareFolder();
//
//        exportAllPlaylists();
//    }
//
//
//    private void prepareFolder ()
//    {
//        try
//        {
//            if (Files.exists(outputFolder))
//            {
//                FolderDeleter folderDeleter = new FolderDeleter();
//
//                folderDeleter.recursiveDelete(outputFolder);
//
//            }
//            Files.createDirectories(outputFolder);
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private void exportAllPlaylists ()
//    {
//        for (Playlist p : getLibrary().playlistsAtTopLevel())
//        {
//            export(p);
//        }
//    }
//
//    private void export (Playlist p)
//    {
//        exportPlaylist(p, new ArrayList<>());
//    }
//
//    private void exportPlaylist (Playlist playlist, ArrayList<String> callStack)
//    {
//        Logging.getLogger().info("Exporting " + playlist);
//
//        // export the playlist unless it is ignored
//        if (!playlistGenSettings.isIgnored(playlist))
//        {
//            exportTracks(playlist, callStack);
//        }
//
//        // TODO should children of ignored playlists be ignored too?
//
//        // if p has children
//        if (!playlist.children().isEmpty())
//        {
//            // create a (deep) copy of the call stack to avoid problems with aliasing
//            ArrayList<String> newStack = new ArrayList<>(callStack);
//            newStack.add(playlist.name());
//            for (Playlist sl : playlist.children())
//            {
//                exportPlaylist(sl, newStack);
//            }
//        }
//    }
//
//    private void exportTracks (Playlist playlist, ArrayList<String> callStack)
//    {
//        List<Track> tracks = playlist.tracks();
//
//        StringBuilder content = new StringBuilder();
//
//        StringBuilder playlistDestinationFolderString = new StringBuilder(outputFolder.toString());
//
//        if (playlistGenSettings.getOrganizeInFolders())
//        {
//            for (String s : callStack)
//            {
//                playlistDestinationFolderString.append(File.separator).append(s);
//            }
//        }
//
//        StringBuilder playlistDestinationFilenameString = new StringBuilder();
//        if (playlistGenSettings.getPrependParents())
//        {
//            for (String s : callStack)
//            {
//                playlistDestinationFilenameString.append(s);
//                playlistDestinationFilenameString.append(" - ");
//            }
//        }
//        playlistDestinationFilenameString.append(playlist.name());
//        playlistDestinationFilenameString.append(playlistGenSettings.getPlaylistExtension());
//
//        String playlistDestinationFileString = playlistDestinationFolderString.toString() + File.separator + playlistDestinationFilenameString.toString();
//        Path playlistDestinationFile = Paths.get(playlistDestinationFileString);
//
//        for (Track track : tracks)
//        {
//            String trackUriString = track.location();
//            URI trackUri = null;
//            try
//            {
//                trackUri = new URI(trackUriString);
//            }
//            catch (URISyntaxException e)
//            {
//                throw new RuntimeException(e);
//            }
//
//            if (!trackUri.getAuthority().equals("localhost"))
//            {
//                throw new RuntimeException("Remote file");
//            }
//
//
//            //Path trackFile = Paths.get("F:\\Audio\\Music\\M\\Michael Jackson - Billie Jean.mp3");
//            String string1 = trackUri.getPath();
//            if (string1.contains(":") && string1.charAt(0) == '/')
//            {
//                string1 = string1.substring(1);
//            }
//
//
//            Path trackFile = Paths.get(string1);
//
//            if (!Files.exists(trackFile))
//            {
//                throw new RuntimeException("File at location" + trackFile.toString() + " (taken from " + trackUriString + ") does not exist");
//            }
//
//            // For example, on UNIX, if this path is "/a/b" and the given path is "/a/b/c/d" then the resulting relative path would be "c/d".
//            Path playlistDestinationPath = Paths.get(playlistDestinationFolderString.toString());
//            Path relativePath = playlistDestinationPath.relativize(trackFile);
//
//            // check for brackets [ ] because VLC wants them to be escaped, but PowerAmp doesn't want to be the escaped
//            // -> we warn
//            String relativePathString = relativePath.toString();
//            String patternString = "\\[|\\]";
//            Pattern pattern = Pattern.compile(patternString);
//            Matcher matcher = pattern.matcher(relativePathString);
//            if (matcher.find())
//            {
//                Logging.getLogger().message("Invalid unescapable character in" + relativePathString);
//            }
//
//            content.append(relativePathString);
//            content.append('\n');
//        }
//
//        writePlaylist(playlistDestinationFile, content.toString());
//    }
//
//    private void writePlaylist (Path destination, String content)
//    {
//        try
//        {
//            // create parent if needed
//            Path parent = destination.getParent();
//
//            // create parent directories
//            Files.createDirectories(parent);
//
//            //  Files.createFile(destination);
//
//            // write stuff
//            BufferedWriter bufferedWriter = Files.newBufferedWriter(destination, StandardCharsets.UTF_8);
//            bufferedWriter.write(content);
//            bufferedWriter.close();
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//}