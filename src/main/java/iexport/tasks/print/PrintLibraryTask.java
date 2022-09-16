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
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;

import java.util.List;
import java.util.function.Predicate;


public class PrintLibraryTask implements Task
{
    @Override
    public String getTaskName ()
    {
        return "printLibrary";
    }

    @Override
    public String getDescription ()
    {
        return "prints the whole library";
    }

    @Override
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        Logging.getLogger().message(library.toString());

        for (Playlist playlist : library.playlists())
        {
            Logging.getLogger().message(ancestryToString(playlist) + "    " + playlist);
        }

//        for (Playlist p : library.playlistsAtTopLevel())
//        {
//            printPlaylist(p);
//        }

        Logging.getLogger().message("");
        Logging.getLogger().message("---------------------------------");
        Logging.getLogger().message("");

        Logging.getLogger().message("Tracks that are in no playlist");

        List<Track> tracksWithoutPlaylist = library.tracks().stream().filter(t -> t.inPlaylists().isEmpty()).toList();

        tracksWithoutPlaylist.forEach(s -> Logging.getLogger().message(s.toString()));

        Logging.getLogger().message("");
        Logging.getLogger().message("---------------------------------");
        Logging.getLogger().message("");

        Logging.getLogger().message("Tracks that are in multiple playlists");

        Predicate<Playlist> playlistPredicate = p -> p.distinguishedKind() == null && (p.master() == null || !p.master()) && p.children().isEmpty() && !p.name().equals("NEUES");
        List<Track> tracksInMultiplePlayslists = library.tracks().stream().filter
                (
                        t -> t.inPlaylists().stream().filter(playlistPredicate).count() > 1
                ).toList();

        tracksInMultiplePlayslists.forEach(
                t -> {
                    Logging.getLogger().message(t.toString());
                    Logging.getLogger().message(" ( ");
                    t.inPlaylists().stream().filter(playlistPredicate).forEach(p -> Logging.getLogger().message(p + " , "));
                    Logging.getLogger().message(" ) ");
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
        Logging.getLogger().message(indent + p);
        for (Playlist subplaylist : p.children())
        {
            printPlaylist(subplaylist, indentLevel + 1);
        }
    }
}
