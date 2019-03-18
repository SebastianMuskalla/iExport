package iexport.domain.builder;

import iexport.domain.Library;
import iexport.domain.Playlist;
import iexport.domain.Track;
import iexport.domain.builder.sorting.PlaylistComparator;
import iexport.domain.builder.sorting.TrackComparator;
import iexport.helper.logging.LogLevel;
import iexport.helper.logging.Logger;
import iexport.parsing.itunes.IExportParsingException;

import javax.annotation.CheckForNull;
import java.util.*;

public class LibraryBuilder
{

    private final Map<Integer, Track> tracksById = new HashMap<>();

    private final Map<String, PlaylistBuilder> playlistsBuildersByPersistentId = new HashMap<>();

    private final Map<String, Playlist> playlistsByPersistentId = new HashMap<>();

    private final List<Track> allTracks = new LinkedList<>();

    private final List<PlaylistBuilder> allPlaylistBuilders = new LinkedList<>();

    private final List<Playlist> allPlaylists = new LinkedList<>();

    private final List<Playlist> playlistsAtTopLevel = new LinkedList<>();

    private Library library;

    @CheckForNull
    private String applicationVersion;
    @CheckForNull
    private String persistentId;
    @CheckForNull
    private Date date;
    @CheckForNull
    private String musicFolder;
//    private final HashMap<String, Playlist> all_playlists = new HashMap<>();

//    private TreeSet<Playlist> toplevel_playlists = new TreeSet<>(new PlaylistComparatorLeavesFirst()); // TODO

    public void setApplicationVersion (String applicationVersion)
    {
        this.applicationVersion = applicationVersion;
    }

    public void setPersistentId (String persistentId)
    {
        this.persistentId = persistentId;
    }

    public void setDate (Date date)
    {
        this.date = date;
    }

    public void setMusicFolder (String musicFolder)
    {
        this.musicFolder = musicFolder;
    }

    public void addTrack (Track track)
    {
        Integer trackId = track.getTrackId();
        if (tracksById.get(trackId) != null)
        {
            Logger.log(LogLevel.WARNING, "Library already contains track with id " + trackId + ". Skipping new track.");
            Logger.log(LogLevel.WARNING, 1, "Old track:" + tracksById.get(trackId));
            Logger.log(LogLevel.WARNING, 1, "New track:" + track);
            return;
        }
        tracksById.put(track.getTrackId(), track);
        allTracks.add(track);
    }

    public void addPlaylist (PlaylistBuilder playlist)
    {
        // for some reason, the playlist could not be parsed - skip it
        if (playlist == null)
        {
            return;
        }

        String persistentId = playlist.getPlaylistPersistentId();

        if (persistentId == null)
        {
            Logger.log(LogLevel.WARNING, "Playlist " + playlist + " has no persistent id. Skipping it.");
            return;
        }

        if (playlistsBuildersByPersistentId.get(persistentId) != null)
        {
            Logger.log(LogLevel.WARNING, "Library already contains playlist with persistent id " + persistentId + ". Skipping new playlist.");
            Logger.log(LogLevel.WARNING, 1, "Old playlist:" + playlistsBuildersByPersistentId.get(persistentId));
            Logger.log(LogLevel.WARNING, 1, "New playlist:" + playlist);
            return;
        }

        playlistsBuildersByPersistentId.put(persistentId, playlist);
        allPlaylistBuilders.add(playlist);
    }

    public Library construct ()
            throws
            IExportParsingException
    {
        if (library != null)
        {
            return library;
        }


        constructPlaylists();

        library = new Library(allTracks, allPlaylists, playlistsAtTopLevel, applicationVersion, persistentId, date, musicFolder);

        sortLibrary();

        return library;
    }

    private void sortLibrary ()
    {
        library.getPlaylistsAtTopLevel().sort(new PlaylistComparator());
        library.getPlaylists().sort(new PlaylistComparator());
        library.getTracks().sort(new TrackComparator());

        for (Playlist playlist : playlistsAtTopLevel)
        {
            sortPlaylist(playlist);
        }
    }

    private void sortPlaylist (Playlist playlist)
    {
        playlist.getTracks().sort(new TrackComparator(playlist));
        playlist.getChildren().sort(new PlaylistComparator());

        for (Playlist childPlaylist : playlist.getChildren())
        {
            sortPlaylist(childPlaylist);
        }
    }


    private void constructPlaylists ()
            throws
            IExportParsingException
    {
        int iterationCount = 0;

        int numberOfPlaylists = allPlaylistBuilders.size();

        while (!allPlaylistBuilders.isEmpty())
        {
            iterationCount++;

            if (iterationCount > numberOfPlaylists * numberOfPlaylists)
            {
                Logger.log(LogLevel.ERROR, "Could not finish creating playlist dependencies. Unhandled entries:");

                // print the remaining builders:
                for (PlaylistBuilder remaining : allPlaylistBuilders)
                {
                    Logger.log(LogLevel.ERROR, 1, remaining.toString());
                }
                throw new IExportParsingException("Cannot resolve playlist dependencies.");
            }

            // take and remove the first list
            PlaylistBuilder playlistBuilder = allPlaylistBuilders.get(0);
            allPlaylistBuilders.remove(playlistBuilder);

            Playlist parent = null;

            // check if this list has a parent set
            String parentPersistentId = playlistBuilder.getParentPersistentId();
            if (parentPersistentId != null)
            {
                // try to find the parent that is already constructed
                parent = playlistsByPersistentId.get(parentPersistentId);
                if (parent == null)
                {
                    // parent hasn't been constructed
                    // check whether it exists at all...
                    if (playlistsBuildersByPersistentId.get(parentPersistentId) == null)
                    {
                        String errorMessage = "Parent with Persistent ID " + parentPersistentId + " of Playlist " + playlistBuilder + " does not exist.";
                        Logger.log(LogLevel.ERROR, errorMessage);
                        throw new IExportParsingException(errorMessage);
                    }
                    else
                    {
                        // simply requeue this playlist for later construction, and skip it for now
                        allPlaylistBuilders.add(playlistBuilder);
                        continue;
                    }

                }
            }

            // either this playlist has no parent, or we have found it successfully
            Integer depth = 0;

            if (parent != null)
            {
                depth = parent.getDepth() + 1;
            }

            // create playlist
            Playlist playlist = new Playlist(depth, parent, playlistBuilder.getPlaylistId(), playlistBuilder.getDistinguishedKind(), playlistBuilder.getName(), playlistBuilder.getMaster(), playlistBuilder.getVisible(), playlistBuilder.getAllItems(), playlistBuilder.getFolder(), playlistBuilder.getMusic(), playlistBuilder.getMovies(), playlistBuilder.getTvShows(), playlistBuilder.getAudibooks(), playlistBuilder.getPlaylistPersistentId(), playlistBuilder.getParentPersistentId());

            if (parent != null)
            {
                parent.addChild(playlist);
            }

            if (parent == null)
            {
                playlistsAtTopLevel.add(playlist);
            }

            // set tracks
            for (Integer trackId : playlistBuilder.getTracks())
            {
                Track track = tracksById.get(trackId);

                if (track == null)
                {
                    Logger.log(LogLevel.WARNING, "Playlis " + playlistBuilder + " should contain track with id " + trackId + ", but no such track exists. Skipping it");

                    continue;
                }

                playlist.addTrack(track);
                track.setContainedInPlaylist(playlist);
            }

            allPlaylists.add(playlist);
            playlistsByPersistentId.put(playlist.getPlaylistPersistentId(), playlist);

        } // while

        Logger.log(LogLevel.DEBUG, "Finished resolving all playlist dependencies within " + iterationCount + " iterations.");
    }
}
