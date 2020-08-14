package iexport.domain.builder.sorting;

import iexport.domain.Playlist;
import iexport.domain.Track;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Sebastian Muskalla
 */
public class TrackComparator implements Comparator<Track>
{
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    static private final NullIntegerComparator nullIntegerComparator = new NullIntegerComparator();
    static private final NullStringComparator nullStringComparator = new NullStringComparator();

    static private final Comparator<Track> albumArtistOrArtistComparator =
            (o1,o2) ->
            {
                String o1s = o1.getAlbumArtist();
                String o2s = o2.getAlbumArtist();
                if (o1s == null || o1s.equals(""))
                {
                    o1s = o1.getArtist();
                }
                if (o2s == null || o2s.equals(""))
                {
                    o2s = o2.getArtist();
                }
                return nullStringComparator.compare(o1s, o2s);
            };

    static private final Comparator<Track> yearComparator = (o1,o2) -> nullIntegerComparator.compare(o1.getYear(), o2.getYear());

    static private final Comparator<Track> albumComparator = (o1,o2) -> nullStringComparator.compare(o1.getAlbum(), o2.getAlbum());

    static private final Comparator<Track> discNumberComparator = (o1,o2) -> nullIntegerComparator.compare(o1.getDiscNumber(), o2.getDiscNumber());

    static private final Comparator<Track> trackNumberComparator = (o1,o2) -> nullIntegerComparator.compare(o1.getTrackNumber(), o2.getTrackNumber());

    static private final Comparator<Track> artistComparator = (o1,o2) -> nullStringComparator.compare(o1.getArtist(), o2.getArtist());

    static private final Comparator<Track> nameComparator = (o1,o2) -> nullStringComparator.compare(o1.getName(), o2.getName());

    static private final Comparator<Track> persistentIdComparator = (o1,o2) -> nullStringComparator.compare(o1.getPersistentId(), o2.getPersistentId());

    public <T> int compare (T o1, T o2, List<? extends Comparator<T>> comparators)
    {
        for (var comparator : comparators)
        {
            int result = comparator.compare(o1,o2);
            if (result != EQUAL)
            {
                return result;
            }
        }
        return EQUAL;
    }

    @Override
    public int compare (Track o1, Track o2)
    {

        if (o1 == null || o2 == null)
        {
            throw new RuntimeException("I dont want to compare null!");
        }


        List<Comparator<Track>> comparators =  Arrays.asList(
            albumArtistOrArtistComparator,
            yearComparator,
            albumComparator,
            discNumberComparator,
            trackNumberComparator,
            artistComparator,
            nameComparator,
            persistentIdComparator
        );

        return compare(o1,o2,comparators);

    }

    @Override
    public boolean equals (Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return obj.getClass().equals(this.getClass());
    }
}
