package iexport.domain;

public class ITunesLibrary
{

//    private final HashMap<String, Track> tracks = new HashMap<String, Track>();
//    private final HashMap<String, Playlist> all_playlists = new HashMap<>();
//    private TreeSet<Playlist> toplevel_playlists = new TreeSet<>(new PlaylistComparatorLeavesFirst()); // TODO
//
//    private int playlist_max_id = 1;
//    private boolean sublist_relation_build = false;
//
//    /**
//     * Creates a new instance of LibraryType
//     */
//    public ITunesLibrary ()
//    {
//    }
//
//    public void addTrack (Track track)
//    {
//        tracks.put(String.valueOf(track.getId()), track);
//    }
//
//    public Track getTrackById (int trackId)
//    {
//        if (!tracks.containsKey(String.valueOf(trackId)))
//        {
//            throw new RuntimeException("Can't find the track with id: " + trackId);
//        }
//        return tracks.get(String.valueOf(trackId));
//    }
//
//    public Track getTrackById (String trackId)
//    {
//        if (!tracks.containsKey(String.valueOf(trackId)))
//        {
//            throw new RuntimeException("Can't find the track with id: " + trackId);
//        }
//        return tracks.get(String.valueOf(trackId));
//    }
//
//    public void addPlaylist (Playlist playlist)
//    {
//        if (playlist.getPersistentId() == null)
//        {
//            throw new RuntimeException("Playlist with empty persistence ID: " + playlist);
//        }
//        all_playlists.put(playlist.getPersistentId(), playlist);
//        playlist.setSortId(playlist_max_id);
//        playlist_max_id++;
//    }
//
//    public Collection<Playlist> getPlaylists ()
//    {
//        return all_playlists.values();
//    }
//
//    public TreeSet<Playlist> getToplevelPlaylists ()
//    {
//        return toplevel_playlists;
//    }
//
//    public void buildSublistRelations ()
//    {
//        if (sublist_relation_build)
//        {
//            throw new RuntimeException("Sublist relation can only build once");
//        }
//
//        for (String pid : all_playlists.keySet())
//        {
//            Playlist playlist = all_playlists.get(pid);
//            String parent_pid = playlist.getParentPersistentId();
//
//            if (parent_pid != null) // playlist has a parent
//            {
//                Playlist parent = all_playlists.get(parent_pid);
//
//                if (parent == null)
//                {
//                    throw new RuntimeException("Playlist " + playlist + " has parent peristence id " + parent_pid + ", but no such playlist exists");
//                }
//
//                parent.addSublist(playlist);
//            }
//            else
//            {
//                toplevel_playlists.add(playlist);
//            }
//        }
//        sublist_relation_build = true;
//    }
//
//    public Playlist findPlayList (String playlistNameorID)
//    {
//        for (Playlist playlist : getPlaylists())
//        {
//            if (playlist.getName().equals(playlistNameorID))
//            {
//                return playlist;
//            }
//            if (playlist.getId().equals(playlistNameorID))
//            {
//                return playlist;
//            }
//        }
//        throw new RuntimeException("Cannot find playlist with name or id \"" + playlistNameorID + "\"");
//    }
//
//    public HashMap<String, Track> getTracks ()
//    {
//        return tracks;
//    }
//
//    public String structureToString ()
//    {
//        if (!sublist_relation_build)
//        {
//            throw new RuntimeException("Build sublist relation first");
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for (Playlist p : toplevel_playlists)
//        {
//            sb.append(p.structureToString());
//        }
//        return sb.toString();
//    }
}
