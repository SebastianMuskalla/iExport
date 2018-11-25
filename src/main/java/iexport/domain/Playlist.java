//package iexport.domain;
//
//import service.sorting.PlaylistComparatorLeavesFirst;
//import service.sorting.TrackComparatorAlbumsFirst;
//
//import java.util.ArrayList;
//import java.util.TreeSet;
//
//public class Playlist
//{
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
//                // make a copy of the track because the tracknumber is diff for diff playlists
//                Track trackCopy = new Track(track);
//                trackCopy.setTrackId(getBufferedLong(trackNumber, bufferedTrackNumberSpaces));
//                tracks.add(trackCopy);
//                trackNumber++;
//            }
//        }
//    }
//}
