package iexport.util;

import java.io.File;

public class FolderDeleter
{

    public boolean recursiveDelete (File file)
    {
        if (file == null || !file.exists())
        {
            return false;
        }

        // if the file is a directory, we need to delete its content first
        if (file.isDirectory())
        {
            for (File f : file.listFiles())
            {
                if (!recursiveDelete(f))
                {
                    return false;
                }
            }
        }

        // if it is not a directory or an empty directory, we can delete it
        return file.delete();
    }

}
