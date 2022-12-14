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
import iexport.itunes.Track;
import iexport.logging.LogLevel;
import iexport.logging.Logging;
import iexport.tasks.Task;


/**
 * A task that prints the iTunes library, including
 * <ul>
 *     <li> general information,
 *     <li> the list of playlists in the library, and
 *     <li> the full list of tracks.
 * </ul>
 */
public class PrintLibraryTask extends Task
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
    public void run ()
    {
        // It would be pretty silly to call this task but then hide the output
        if (Logging.getLogger().getLogLevel().lessVerbose(LogLevel.NORMAL))
        {
            Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        }

        // Print information about the library
        Logging.getLogger().message("Library");
        Logging.getLogger().message("-------");
        Logging.getLogger().message("");
        Logging.getLogger().message(library.toString());
        Logging.getLogger().message("");

        // Print all playlists.
        Logging.getLogger().message("Playlists");
        Logging.getLogger().message("---------");
        Logging.getLogger().message("");

        for (Playlist playlist : library.playlists())
        {
            // Some formatting to represent the folder structure.
            String res = "";
            res += "|   ".repeat(playlist.depth());
            res += playlist;
            Logging.getLogger().message(res);
        }
        Logging.getLogger().message("");

        // Print all tracks.
        Logging.getLogger().message("Tracks");
        Logging.getLogger().message("------");
        Logging.getLogger().message("");

        for (Track track : library.tracks())
        {
            Logging.getLogger().message(track.toString());
        }

    }

}
