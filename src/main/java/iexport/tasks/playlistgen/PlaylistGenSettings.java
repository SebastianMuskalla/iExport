package iexport.tasks.playlistgen;

import iexport.domain.Playlist;
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
        if (playlist.getMaster() != null && playlist.getMaster())
        {
            return true;
        }
        if (playlist.getDistinguishedKind() != null && playlist.getDistinguishedKind() == 65)
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
