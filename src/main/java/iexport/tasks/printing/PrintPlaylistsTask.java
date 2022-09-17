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

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.logging.LogLevel;
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;


public class PrintPlaylistsTask implements Task
{
    @Override
    public String getTaskName ()
    {
        return "printPlaylists";
    }

    @Override
    public String getDescription ()
    {
        return "prints the playlists";
    }

    @Override
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        // It would be pretty silly to call this task but then hide the output
        Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        
        final int numberPlaylists = library.playlists().size();
        final int numberTopLevelPlaylists = library.playlistsAtTopLevel().size();
        final int numberFolders = (int) library.playlists().stream().filter((p) -> p.children() != null && p.children().size() > 0).count();
        final int actualPlaylists = numberPlaylists - numberFolders;


        Logging.getLogger().message("Library consists of " + numberPlaylists + " playlists");
        Logging.getLogger().message(1,
                "- " + numberTopLevelPlaylists + " top-level playlists");
        Logging.getLogger().message(1,
                "- " + numberFolders + " folders");
        Logging.getLogger().message(1,
                "- " + actualPlaylists + " actual playlists");

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
