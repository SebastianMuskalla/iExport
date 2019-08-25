package iexport.tasks.print;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.domain.Track;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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

        out.println();
        out.println("---------------------------------");
        out.println();

        out.println("Tracks that are in no playlist");

        List<Track> tracksWithoutPlaylist = library.getTracks().stream().filter(t -> t.getInPlaylists().isEmpty()).collect(Collectors.toList());

        tracksWithoutPlaylist.forEach(out::println);

        out.println();
        out.println("---------------------------------");
        out.println();

        out.println("Tracks that are in multiple playlists");

        Predicate<Playlist> playlistPredicate = p -> p.getDistinguishedKind() == null && (p.getMaster() == null || !p.getMaster()) && p.getChildren().isEmpty() && !p.getName().equals("NEUES");
        List<Track> tracksInMultiplePlayslists = library.getTracks().stream().filter
                (
                        t -> t.getInPlaylists().stream().filter(playlistPredicate).count() > 1
                ).collect(Collectors.toList());

        tracksInMultiplePlayslists.forEach(
                    t -> {
                        out.print(t);
                        out.print(" ( ");
                        t.getInPlaylists().stream().filter(playlistPredicate).forEach(p -> out.print(p + " , "));
                        out.println(" ) ");
                    }
        );


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
