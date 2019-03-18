package iexport.domain;


import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class Playlist
{
    @Nonnull
    private final List<Track> tracks;
    @Nonnull
    private final List<Playlist> children;
    @Nonnull
    private final Integer depth;
    @CheckForNull
    private final Playlist parent;
    @CheckForNull
    private final Integer playlistId;
    @CheckForNull
    private final Integer distinguishedKind;
    @CheckForNull
    private final String name;
    @CheckForNull
    private final Boolean master;
    @CheckForNull
    private final Boolean visible;
    @CheckForNull
    private final Boolean allItems;
    @CheckForNull
    private final Boolean folder;
    @CheckForNull
    private final Boolean music;
    @CheckForNull
    private final Boolean movies;
    @CheckForNull
    private final Boolean tvShows;
    @CheckForNull
    private final Boolean audibooks;
    @Nonnull
    private String playlistPersistentId;
    @CheckForNull
    private String parentPersistentId;


    public Playlist (@Nonnull Integer depth, @CheckForNull Playlist parent, @CheckForNull Integer playlistId, @CheckForNull Integer distinguishedKind, @CheckForNull String name, @CheckForNull Boolean master, @CheckForNull Boolean visible, @CheckForNull Boolean allItems, @CheckForNull Boolean folder, @CheckForNull Boolean music, @CheckForNull Boolean movies, @CheckForNull Boolean tvShows, @CheckForNull Boolean audibooks, @Nonnull String playlistPersistentId, @CheckForNull String parentPersistentId)
    {
        this.tracks = new LinkedList<>();
        this.children = new LinkedList<>();
        this.depth = depth;
        this.parent = parent;
        this.playlistId = playlistId;
        this.distinguishedKind = distinguishedKind;
        this.name = name;
        this.master = master;
        this.visible = visible;
        this.allItems = allItems;
        this.folder = folder;
        this.music = music;
        this.movies = movies;
        this.tvShows = tvShows;
        this.audibooks = audibooks;
        this.playlistPersistentId = playlistPersistentId;
        this.parentPersistentId = parentPersistentId;
    }

    @Nonnull
    public List<Playlist> getChildren ()
    {
        return children;
    }

    @Nonnull
    public List<Track> getTracks ()
    {
        return tracks;
    }

    @Nonnull
    public Integer getDepth ()
    {
        return depth;
    }

    @CheckForNull
    public Playlist getParent ()
    {
        return parent;
    }

    @CheckForNull
    public Integer getPlaylistId ()
    {
        return playlistId;
    }

    @CheckForNull
    public Integer getDistinguishedKind ()
    {
        return distinguishedKind;
    }

    @CheckForNull
    public String getName ()
    {
        return name;
    }

    @CheckForNull
    public Boolean getMaster ()
    {
        return master;
    }

    @CheckForNull
    public Boolean getVisible ()
    {
        return visible;
    }

    @CheckForNull
    public Boolean getAllItems ()
    {
        return allItems;
    }

    @CheckForNull
    public Boolean getFolder ()
    {
        return folder;
    }

    @CheckForNull
    public Boolean getMusic ()
    {
        return music;
    }

    @CheckForNull
    public Boolean getMovies ()
    {
        return movies;
    }

    @CheckForNull
    public Boolean getTvShows ()
    {
        return tvShows;
    }

    @CheckForNull
    public Boolean getAudibooks ()
    {
        return audibooks;
    }

    @Nonnull
    public String getPlaylistPersistentId ()
    {
        return playlistPersistentId;
    }

    @CheckForNull
    public String getParentPersistentId ()
    {
        return parentPersistentId;
    }

    public void addChild (Playlist playlist)
    {
        this.children.add(playlist);
    }

    public void addTrack (Track track)
    {
        tracks.add(track);
    }

    @Override
    public String toString ()
    {
        return name + " {" +
                "tracks=" + getNumberOfTracks() +
                ", depth=" + depth +
                ", distinguishedKind=" + distinguishedKind +
                ", master=" + master +
                ", visible=" + visible +
                ", allItems=" + allItems +
                ", folder=" + folder +
                ", music=" + music +
                ", movies=" + movies +
                ", tvShows=" + tvShows +
                ", audibooks=" + audibooks +
                '}';
    }

    public int getNumberOfTracks ()
    {
        return tracks.size();
    }
}


//
//    private static String getBufferedLong (long num, int numberOfSpaces)
//    {
//        String value = String.valueOf(num);
//        while (value.length() < numberOfSpaces)
//        {
//            value = "0" + value;
//        }
//        return value;
//    }
//
//    private final ArrayList<String> track_ids = new ArrayList<>();
//    private final TreeSet<Playlist> sublists = new TreeSet<>(new PlaylistComparatorLeavesFirst());   // TODO
//    private String name = null;
//    private String id = null;
//    private TreeSet<Track> tracks = new TreeSet<>(new TrackComparatorAlbumsFirst(this));
//    private ItunesLibrary lib = null;
//    private long total_time = 0;
//    private long total_size = 0;
//    private String persistent_id = null;
//    private String parent_persistent_id = null;
//    private int sort_id = -1;
//
//    /**
//     * Creates a new instance of PlayListType
//     */
//
//    public Playlist (ItunesLibrary library)
//    {
//        if (library == null)
//        {
//            throw new RuntimeException("argument 'library' cannot be null");
//        }
//
//        lib = library;
//    }
//
//    public void addSublist (Playlist sub)
//    {
//        sublists.add(sub);
//    }
//
//    public String getName ()
//    {
//        return name;
//    }
//
//    public void setName (String value)
//    {
//        name = value;
//    }
//
//    public String getId ()
//    {
//        return id;
//    }
//
//    public void setId (String value)
//    {
//        id = value;
//    }
//
//    public void setPersistentID (String val)
//    {
//        persistent_id = val;
//    }
//
//    public void setParentPersistentID (String val)
//    {
//        parent_persistent_id = val;
//    }
//
//    public TreeSet<Track> getTracks ()
//    {
//        populateTracksArray();
//        return tracks;
//    }
//
//    public ItunesLibrary getLib ()
//    {
//        return lib;
//    }
//
//    public ArrayList<String> getTrackIds ()
//    {
//        return track_ids;
//    }
//
//    public void addTrackId (int trackID)
//    {
//        track_ids.add(String.valueOf(trackID));
//    }
//
//    public long getTotalTime ()
//    {
//        populateTracksArray();
//        return total_time;
//    }
//
//    public long getTotalSize ()
//    {
//        populateTracksArray();
//        return total_size;
//    }
//
//    public String getPersistentId ()
//    {
//        return persistent_id;
//    }
//
//    public String getParentPersistentId ()
//    {
//        return parent_persistent_id;
//    }
//
//    public String structureToString ()
//    {
//        StringBuilder sb = new StringBuilder(name);
//        sb.append("\n");
//        for (Playlist p : sublists)
//        {
//            String sub = p.structureToString();
//            String[] lines = sub.split("[\r\n]+");
//            for (String line : lines)
//            {
//                sb.append("    ");
//                sb.append(line);
//                sb.append("\n");
//            }
//        }
//        return sb.toString();
//    }
//
//    public int getSortId ()
//    {
//        return sort_id
//                ;
//    }
//
//    public void setSortId (int sort_id)
//    {
//        this.sort_id = sort_id;
//    }
//
//    public TreeSet<Playlist> getSublists ()
//    {
//        return sublists;
//    }
//
//    private void populateTracksArray ()
//    {
//        if (tracks.size() == 0 && track_ids.size() != 0)
//        {
//            long trackNumber = 1;
//            int bufferedTrackNumberSpaces = String.valueOf(track_ids.size()).length();
//            // populate track array with tracks from library
//
//            for (String track_id : track_ids)
//            {
//                int trackId = Integer.parseInt(track_id);
//                Track track = lib.getTrackById(trackId);
//                total_time += track.getTotalTime();
//                total_time += track.getSize();
//                // make a copy of the track because the tracknumber is diff for diff playlistgen
//                Track trackCopy = new Track(track);
//                trackCopy.setTrackId(getBufferedLong(trackNumber, bufferedTrackNumberSpaces));
//                tracks.add(trackCopy);
//                trackNumber++;
//            }
//        }
//    }

