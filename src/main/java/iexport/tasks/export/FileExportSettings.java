package iexport.tasks.export;

import iexport.domain.Playlist;
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
        if (playlist.getMaster() != null && playlist.getMaster())
        {
            return true;
        }
        if (playlist.getDistinguishedKind() != null)
        {
            return true;
        }

        if (getOnlyExportLeaves() && !playlist.getChildren().isEmpty())
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
        return playlist.getName().toLowerCase().equals("Neues".toLowerCase());
    }

    public Boolean getOnlyExportLeaves ()
    {
        return onlyExportLeaves;
    }

    public Boolean createPlaylistPerFolder()
    {
        return createPlaylistPerFolder;
    }
}
