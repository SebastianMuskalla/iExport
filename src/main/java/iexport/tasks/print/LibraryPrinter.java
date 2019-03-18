package iexport.tasks.print;

import iexport.domain.Library;
import iexport.domain.Playlist;

import java.io.PrintStream;

/**
 * @author Sebastian Muskalla
 */
public class LibraryPrinter
{
    private final Library library;

    private final PrintStream out = System.out;

    public LibraryPrinter (Library library)
    {
        this.library = library;
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
