//package iexport.tasks.export;
//
//import model.itunes.ItunesLibrary;
//import model.itunes.Playlist;
//import model.itunes.Track;
//import model.settings.CommonSettings;
//import model.settings.TrackCopySettings;
//import old.Settings;
//import service.itunes.LibraryTagParser;
//import service.util.FileSystem;
//import service.util.Int;
//import service.util.LocationStringParser;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.TreeSet;
//
///**
// * @author Sebastian Muskalla
// */
//public class MP3Exporter
//{
//    private final CommonSettings cs;
//    private final TrackCopySettings ts;
//    private final boolean debug;
//    private ItunesLibrary lib;
//    private Collection<Playlist> playlists;
//    private int total_track_count;
//    private int playlist_count;
//    private int playlist_id = 0;
//    private File output;
//
//    public MP3Exporter (CommonSettings cs, TrackCopySettings ts)
//    {
//        this.cs = cs;
//        this.ts = ts;
//        debug = false;
//    }
//
//    public MP3Exporter (CommonSettings cs, TrackCopySettings ts, boolean debug)
//    {
//        this.cs = cs;
//        this.ts = ts;
//        this.debug = debug;
//    }
//
//    public void run ()
//    {
//        if (ts.start_with_2)
//        {
//            playlist_id++;
//        }
//
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
//        FileSystem.deleteFolderRecurisve(new File(ts.track_output_folder));
//        output = new File(ts.track_output_folder);
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
//        ArrayList<String> new_stack = new ArrayList<>(call_stack);
//        new_stack.add(p.getName());
//
//        if (!cs.isIgnored(p.getName()) && (!ts.ignore_folder_lists || p.getSublists().size() == 0))
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
//
//        String output_folder_name = ts.track_output_folder;
//
//        if (!ts.goesToRootfolder(p.getName())) // if this playlist goes NOT to the rootfolder
//        {
//            playlist_id++;
//            if (ts.number_folders)
//            {
//                output_folder_name += Int.toStringOfSize(playlist_id, Int.digits(playlist_count)) + " ";
//            }
//
//            if (ts.hierarchy_in_name)
//            {
//                for (String s : call_stack)
//                {
//                    output_folder_name += s + " - ";
//                }
//            }
//
//            // TODO: Does not work - need to do it on folder level
//            String new_name = cs.getNewName(p.getName());
//            if (new_name == null)
//            {
//                new_name = p.getName();
//            }
//            output_folder_name += new_name + File.separator;
//
//            System.out.println("Trying to create " + output_folder_name);
//            File output_folder = new File(output_folder_name);
//            if (!output_folder.mkdirs())
//            {
//                throw new RuntimeException("Could not create " + output_folder_name + "(or one of its parent directories)");
//            }
//        }
//
//        TreeSet<Track> tracks = p.getTracks();
//        int track_count = tracks.size();
//
//        int id = 0;
//        for (Track t : tracks)
//        {
//            id++;
//
//            File track = LocationStringParser.getFile(t.getFileLocation());
//
//            // TODO
//            String path = track.getAbsolutePath();
//            path = path.substring(Settings.PLAYLIST_OFFSET);
//
//            String output_track_name = output_folder_name;
//            output_track_name += Int.toStringOfSize(id, Int.digits(track_count));
//            output_track_name += " - ";
//            output_track_name += track.getName();
//
//            try
//            {
//
//                copy(track, output_track_name);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//                return;
//            }
//        }
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
//            throw new RuntimeException("Could not create Playlist at " + loc.getName());
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
//
//    private void copy (File source, String target)
//            throws IOException
//    {
//        if (debug)
//        {
//            return;
//        }
//
//        Path from = Paths.get(source.getAbsolutePath());
//        Path to = Paths.get(target);
//        Files.copy(from, to);
//    }
//}
