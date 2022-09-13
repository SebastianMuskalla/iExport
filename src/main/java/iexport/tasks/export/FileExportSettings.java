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

import iexport.itunes.Playlist;
import iexport.tasks.common.TaskSettings;

public class FileExportSettings
{
    private final String outputFolder = "E:\\USBMP3\\";

    private final Boolean prependParents = true;

    private final Boolean consecutivePlaylistNumbering = true;

    private final Integer firstPlaylistNumber = 2;

    private final Boolean onlyExportLeaves = true;

    private final Boolean createPlaylistPerFolder = false;

    public FileExportSettings (TaskSettings taskSettings)
    {
    }


    public String getOutputFolder ()
    {
        return outputFolder;
    }


    public Boolean getPrependParents ()
    {
        return prependParents;
    }

    // TODO should this method be here?
    public boolean isIgnored (Playlist playlist)
    {
        if (playlist.master() != null && playlist.master())
        {
            return true;
        }
        if (playlist.distinguishedKind() != null)
        {
            return true;
        }

        if (getOnlyExportLeaves() && !playlist.children().isEmpty())
        {
            return true;
        }

        return false;
    }

    public Boolean getConsecutivePlaylistNumbering ()
    {
        return consecutivePlaylistNumbering;
    }

    public Integer getFirstPlaylistNumber ()
    {
        return firstPlaylistNumber;
    }

    // TODO should this method be here?
    public boolean isExportedToRootFolder (Playlist playlist)
    {
        return playlist.name().toLowerCase().equals("Neues".toLowerCase());
    }

    public Boolean getOnlyExportLeaves ()
    {
        return onlyExportLeaves;
    }

    public Boolean createPlaylistPerFolder ()
    {
        return createPlaylistPerFolder;
    }
}
