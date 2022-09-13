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

package iexport.tasks.playlistgen;

import iexport.itunes.Playlist;
import iexport.tasks.common.TaskSettings;

public class PlaylistGenSettings
{
    private final String outputFolder = "E:\\Audio\\Playlists";

    private final Boolean organizeInFolders = true;

    private final Boolean prependParents = true;

    /**
     * using .m3u leads to problems with non-ASCII chars (french accents, japanese characters, ...) in filenames within the playlist
     */
    private final String playlistExtension = ".m3u8";

    public PlaylistGenSettings (TaskSettings taskSettings)
    {
    }


    public String getOutputFolder ()
    {
        return outputFolder;
    }

    public Boolean getOrganizeInFolders ()
    {
        return organizeInFolders;
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
        if (playlist.distinguishedKind() != null && playlist.distinguishedKind() == 65)
        {
            return true;
        }

        return false;
    }

    public String getPlaylistExtension ()
    {
        return playlistExtension;
    }
}
