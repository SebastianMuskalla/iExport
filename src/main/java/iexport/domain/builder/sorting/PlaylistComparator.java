package iexport.domain.builder.sorting;

import iexport.domain.Playlist;

import java.util.Comparator;

/**
 * @author Sebastian Muskalla
 */
public class PlaylistComparator implements Comparator<Playlist>
{
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    static private final NullStringComparator nullStringComparator = new NullStringComparator();
    static private final NullIntegerComparator ni = new NullIntegerComparator();

    @Override
    public int compare (Playlist o1, Playlist o2)
    {
        if (o1 == null || o2 == null)
        {
            throw new RuntimeException("I dont want to compare null!");
        }
        if (o1 == o2)
        {
            return EQUAL;
        }

        if (o1.getChildren().size() == 0 && o2.getChildren().size() > 0)
        {
            return FIRST_HAS_PRIORITY;
        }

        if (o2.getChildren().size() == 0 && o1.getChildren().size() > 0)
        {
            return SECOND_HAS_PRIORITY;
        }

        int res = nullStringComparator.compare(o1.getName(), o2.getName());
        if (res != EQUAL)
        {
            return res;
        }
        return nullStringComparator.compare(o1.getPlaylistPersistentId(), o2.getPlaylistPersistentId());
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
