//package iexport.tasks.playlists;
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
//public class PlaylistGenerator
//{
//    private final CommonSettings cs;
//    private final PlaylistGenerationSettings gs;
//    private ItunesLibrary lib;
//    private Collection<Playlist> playlists;
//    private int total_track_count;
//    private int playlist_count;
//    private File output;
//
//    public PlaylistGenerator (CommonSettings cs, PlaylistGenerationSettings gs)
//    {
//        this.cs = cs;
//        this.gs = gs;
//    }
//
//    public void run ()
//    {
//        parseLib();
//
//        prepareFolder();
//
//        exportAllPlaylists();
//    }
//
//    private void parseLib ()
//    {
//        LibraryTagParser lib_parser = new LibraryTagParser(cs);
//
//        lib = lib_parser.parseLibrary(cs.path_to_lib, false);
//
//        playlists = lib.getPlaylists();
//        total_track_count = lib.getTracks().size();
//        playlist_count = playlists.size();
//    }
//
//    private void prepareFolder ()
//    {
//        FileSystem.deleteFolderRecurisve(new File(Settings.PLAYLIST_OUTPUT_FOLDER));
//        output = new File(Settings.PLAYLIST_OUTPUT_FOLDER);
//        output.mkdirs();
//    }
//
//    private void exportAllPlaylists ()
//    {
//        for (Playlist p : lib.getToplevelPlaylists())
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
//    private void exportPlaylist (Playlist p, ArrayList<String> call_stack)
//    {
//
//        ArrayList<String> new_stack = new ArrayList<>(call_stack);
//        new_stack.add(p.getName());
//
//        if (!cs.isIgnored(p.getName()))
//        {
//            exportTracks(p, call_stack);
//        }
//
//        for (Playlist sl : p.getSublists())
//        {
//            exportPlaylist(sl, new_stack);
//        }
//    }
//
//    private void exportTracks (Playlist p, ArrayList<String> call_stack)
//    {
//        TreeSet<Track> tracks = p.getTracks();
//        int track_count = tracks.size();
//
//        StringBuilder content = new StringBuilder();
//
//        for (Track t : tracks)
//        {
//            File track = LocationStringParser.getFile(t.getFileLocation());
//
//            // TODO
//            String path = track.getAbsolutePath();
//            path = path.substring(Settings.PLAYLIST_OFFSET);
//
//            String prefix = "";
//            prefix = ".." + File.separator + "Music" + File.separator;
//
//            if (gs.organize_in_folders)
//            {
//                for (String s : call_stack)
//                {
//                    prefix = ".." + File.separator + prefix;
//                }
//            }
//
//            path = prefix + path;
//
//            content.append(path);
//            content.append('\n');
//        }
//
//        writePlaylist(PathToFile.pathToFile(output, p.getName() + ".m3u", call_stack, gs.organize_in_folders, gs.hierarchy_in_name), content.toString());
//    }
//
//    private void writePlaylist (File loc, String content)
//    {
//
//        if (loc.getParentFile() != null)
//        {
//            loc.getParentFile().mkdirs();
//        }
//
//        try
//        {
//            loc.createNewFile();
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException("Could not create Playlist at " + loc.getName(), e);
//        }
//
//        PrintWriter file_writer;
//
//        try
//        {
//            file_writer = new PrintWriter(loc);
//        }
//        catch (FileNotFoundException e)
//        {
//            throw new RuntimeException("Could not write " + loc.getName());
//        }
//
//        file_writer.println(content);
//        file_writer.close();
//    }
//}
