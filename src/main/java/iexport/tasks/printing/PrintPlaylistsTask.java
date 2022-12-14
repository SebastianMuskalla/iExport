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

package iexport.tasks.printing;

import iexport.itunes.Playlist;
import iexport.logging.LogLevel;
import iexport.logging.Logging;
import iexport.tasks.Task;


/**
 * A task that prints all playlists (folders and actual playlists) in the iTunes library.
 */
public class PrintPlaylistsTask extends Task
{
    @Override
    public String getTaskName ()
    {
        return "printPlaylists";
    }

    @Override
    public String getDescription ()
    {
        return "prints folders & playlists";
    }

    @Override
    public void run ()
    {
        // It would be pretty silly to call this task but then hide the output.
        if (Logging.getLogger().getLogLevel().lessVerbose(LogLevel.NORMAL))
        {
            Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        }

        // Collect and show some basic information.

        final int numberPlaylists = library.playlists().size();
        final int numberTopLevelPlaylists = library.playlistsAtTopLevel().size();
        final int numberFolders = (int) library.playlists().stream().filter((p) -> p.children() != null && p.children().size() > 0).count();
        final int actualPlaylists = numberPlaylists - numberFolders;

        Logging.getLogger().message("Library consists of " + numberPlaylists + " playlists/folders");
        Logging.getLogger().message(1,
                "- " + numberTopLevelPlaylists + " top-level playlists/folders");
        Logging.getLogger().message(1,
                "- " + numberFolders + " folders");
        Logging.getLogger().message(1,
                "- " + actualPlaylists + " actual playlists");

        Logging.getLogger().message("");

        // Print all playlists.
        Logging.getLogger().message("Playlists");
        Logging.getLogger().message("---------");
        Logging.getLogger().message("");

        for (Playlist playlist : library.playlists())
        {
            String res = "";
            res += "|   ".repeat(playlist.depth());
            res += playlist;
            Logging.getLogger().message(res);
        }
    }

}
