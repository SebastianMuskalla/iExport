//package iexport.tasks.print;
//
//import app.precompiled.Settings;
//import model.itunes.ItunesLibrary;
//import model.itunes.Playlist;
//import service.itunes.LibraryTagParser;
//import service.logging.Log;
//import service.logging.SoutLog;
//
//import java.io.File;
//
///**
// * @author Sebastian Muskalla
// */
//public class LibraryPrinter
//{
//    public void run ()
//    {
//        File f = new File(Settings.getCommonSettings().path_to_lib);
//
//        Log log = new SoutLog();
//        LibraryTagParser lib_parser = new LibraryTagParser(Settings.getCommonSettings(), log);
//
//        ItunesLibrary lib = lib_parser.parseLibrary(Settings.getCommonSettings().path_to_lib, false);
//
//        System.out.println(lib.getPlaylists().size() + " playlists");
//        System.out.println("--- List of playlists ---");
//        System.out.println(lib.structureToString());
//
//        for (Playlist p : lib.getPlaylists())
//        {
//            System.out.println(p.getName());
//        }
//    }
//}
