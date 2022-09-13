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

package iexport.tasks.print;

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Predicate;


public class LibraryPrinter extends Task
{
    public static final String SHORTHAND = "print";

    private final Library library;

    private final PrintStream out = System.out;

    public LibraryPrinter (Library library, TaskSettings taskSettings)
    {
        super(library, taskSettings);
        this.library = library;
    }

    @Override
    public String getShorthand ()
    {
        return SHORTHAND;
    }

    public void run ()
    {
        out.println(library.toString());

        for (Playlist playlist : library.playlists())
        {
            System.out.println(ancestryToString(playlist) + "    " + playlist);
        }

//        for (Playlist p : library.playlistsAtTopLevel())
//        {
//            printPlaylist(p);
//        }

//        out.println();
//        out.println("---------------------------------");
//        out.println();
//
//        out.println("Tracks that are in no playlist");

//        List<Track> tracksWithoutPlaylist = library.tracks().stream().filter(t -> t.inPlaylists().isEmpty()).collect(Collectors.toList());
//
//        tracksWithoutPlaylist.forEach(out::println);

        out.println();
        out.println("---------------------------------");
        out.println();

        out.println("Tracks that are in multiple playlists");

        Predicate<Playlist> playlistPredicate = p -> p.distinguishedKind() == null && (p.master() == null || !p.master()) && p.children().isEmpty() && !p.name().equals("NEUES");
        List<Track> tracksInMultiplePlayslists = library.tracks().stream().filter
                (
                        t -> t.inPlaylists().stream().filter(playlistPredicate).count() > 1
                ).toList();

        tracksInMultiplePlayslists.forEach(
                t -> {
                    out.print(t);
                    out.print(" ( ");
                    t.inPlaylists().stream().filter(playlistPredicate).forEach(p -> out.print(p + " , "));
                    out.println(" ) ");
                }
        );


    }

    private String ancestryToString (Playlist playlist)
    {
        return playlist.ancestry().stream().reduce("", (s, playlist1) -> s + "/" + playlist1.name(), String::concat);
    }

    private void printPlaylist (Playlist p)
    {
        printPlaylist(p, 0);
    }

    private void printPlaylist (Playlist p, int indentLevel)
    {
        assert indentLevel >= 0;
        String indent = "";
        for (int i = 0; i < indentLevel; i++)
        {
            indent += "    ";
        }
        out.println(indent + p);
        for (Playlist subplaylist : p.children())
        {
            printPlaylist(subplaylist, indentLevel + 1);
        }
    }
}
