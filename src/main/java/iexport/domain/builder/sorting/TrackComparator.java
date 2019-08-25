package iexport.domain.builder.sorting;

import iexport.domain.Playlist;
import iexport.domain.Track;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * @author Sebastian Muskalla
 */
public class TrackComparator implements Comparator<Track>
{
    static public final int MIN_ALBUM_TRACK_COUNT = 5; // TODO

    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;


    static private final NullIntegerComparator nullIntegerComparator = new NullIntegerComparator();
    static private final NullStringComparator nullStringComparator = new NullStringComparator();

    private final Playlist inPlaylist;

    public TrackComparator ()
    {
        this.inPlaylist = null;
    }

    public TrackComparator (Playlist inPlaylist)
    {
        this.inPlaylist = inPlaylist;
    }

    @Override
    public int compare (Track o1, Track o2)
    {

        if (o1 == null || o2 == null)
        {
            throw new RuntimeException("I dont want to compare null!");
        }

        if (isAlbumTrack(o1))
        {
            if (isAlbumTrack(o2))
            {
                return compareAlbumTracks(o1, o2);
            }
            else
            {
                return FIRST_HAS_PRIORITY;
            }
        }
        else
        {
            if (isAlbumTrack(o2))
            {
                return SECOND_HAS_PRIORITY;
            }
            else
            {
                return compareNonAlbumTracks(o1, o2);
            }
        }
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

    private boolean isAlbumTrack (Track track)
    {
        if (inPlaylist == null)
        {
            return false;
        }

        if (track.getAlbumArtist() == null || track.getAlbum() == null)
        {
            return false;
        }

        Predicate<Track> trackPredicate = t -> track.getAlbum().equals(t.getAlbum())
                && track.getAlbumArtist().equals(t.getAlbumArtist())
                && ((track.getDiscNumber() == null && t.getDiscNumber() == null) || (track.getDiscNumber() != null && track.getDiscNumber().equals(t.getDiscNumber())));

        long tracksFromSameAlbumCount = inPlaylist.getTracks().stream().filter(trackPredicate).count();


        return tracksFromSameAlbumCount >= MIN_ALBUM_TRACK_COUNT || (track.getTrackCount() != null && tracksFromSameAlbumCount >= track.getTrackCount());
    }

    private int compareAlbumTracks (Track o1, Track o2)
    {
        int res = nullStringComparator.compare(o1.getAlbumArtist(), o2.getAlbumArtist());
        if (res != EQUAL)
        {
            return res;
        }

        res = nullIntegerComparator.compare(o1.getYear(), o2.getYear());
        if (res != EQUAL)
        {
            return res;
        }

        res = nullStringComparator.compare(o1.getAlbum(), o2.getAlbum());
        if (res != EQUAL)
        {
            return res;
        }

        res = nullIntegerComparator.compare(o1.getDiscNumber(), o2.getDiscNumber());
        if (res != EQUAL)
        {
            return res;
        }

        res = nullIntegerComparator.compare(o1.getTrackNumber(), o2.getTrackNumber());
        if (res != EQUAL)
        {
            return res;
        }

        return nullStringComparator.compare(o1.getPersistentId(), o2.getPersistentId());
    }

    private int compareNonAlbumTracks (Track o1, Track o2)
    {
        int res = nullStringComparator.compare(o1.getArtist(), o2.getArtist());
        if (res != EQUAL)
        {
            return res;
        }

        res = nullStringComparator.compare(o1.getName(), o2.getName());
        if (res != EQUAL)
        {
            return res;
        }

        return nullStringComparator.compare(o1.getPersistentId(), o2.getPersistentId());
    }
}
