package iexport.tasks.print;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;

import java.io.PrintStream;


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
    public String getShorthand() {
        return SHORTHAND;
    }


    public void run ()
    {
        out.println(library.toString());
        for (Playlist p : library.getPlaylistsAtTopLevel())
        {
            printPlaylist(p);
        }
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
        for (Playlist subplaylist : p.getChildren())
        {
            printPlaylist(subplaylist, indentLevel + 1);
        }
    }
}
