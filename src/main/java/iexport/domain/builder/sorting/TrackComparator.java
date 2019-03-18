package iexport.domain.builder.sorting;

import iexport.domain.Playlist;
import iexport.domain.Track;

import java.util.Comparator;
import java.util.List;

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

    private boolean isAlbumTrack (Track o)
    {
        if (inPlaylist == null)
        {
            return false;
        }

        if (o.getAlbumArtist() == null && o.getAlbum() == null)
        {
            return false;
        }
        List<Track> tracks = inPlaylist.getTracks();
        int count = 0;
        for (Track track : tracks)
        {
            String album = track.getAlbum();
            if (album != null && album.equals(o.getAlbum()))
            {
                count++;
            }
        }
        return count >= MIN_ALBUM_TRACK_COUNT;
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
