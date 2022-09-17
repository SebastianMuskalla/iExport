/*
 * Copyright 2014-2022 Sebastian Muskalla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package iexport.tasks.printing;

import iexport.itunes.Library;
import iexport.itunes.Playlist;
import iexport.itunes.Track;
import iexport.logging.LogLevel;
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;
import iexport.tasks.Task;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A task that will print tracks that are contained in multiple playlists.
 * <p>
 * We will only count actual playlists, not folders.
 * Additionally, we will include the master playlist, all distinguished playlists (e.g. "Music", "Downloaded").
 * The user may use the setting tasks.printMultiplyListedTracks.ignorePlaylists to specify additional playlist names
 * that should be ignored (i.e. they will not count towards the number of playlists a track is in).
 */
public class PrintMultiplyListedTracksTask extends Task
{
    /**
     * The settings used for this task.
     */
    PrintMultiplyListedTracksTaskSettings settings;

    @Override
    public String getTaskName ()
    {
        return "printMultiplyListedTracks";
    }

    @Override
    public String getDescription ()
    {
        return "prints tracks that are contained in multiple playlists";
    }

    @Override
    public void initialize (Library library, RawTaskSettings rawTaskSettings)
    {
        super.initialize(library, rawTaskSettings);

        // Convert the RawTaskSettings into settings for this type of task.
        settings = new PrintMultiplyListedTracksTaskSettings(rawTaskSettings);
    }

    @Override
    public void reportProblems ()
    {
        // Check that this task has been initialized.
        super.reportProblems();

        // Settings should now be non-null.
        if (settings == null)
        {
            throw new RuntimeException("Settings have not been initialized for Task " + getTaskName());
        }

        // Report if we are using default settings.
        if (settings.isDefault())
        {
            Logging.getLogger().warning("No settings for task " + getTaskName() + " have been specified in the .yaml file, using all default settings from now on");
        }
        else
        {
            // Report settings that are specified in the .yaml file, but not actually used by this task.
            for (String key : settings.unusedSettings())
            {
                Logging.getLogger().warning("Setting for key \"" + settings.getYamlPath(key) + "\""
                        + " specified in .yaml file, but it is not used by iExport");
            }
        }
    }

    @Override
    public void run ()
    {
        // It would be pretty silly to call this task but then hide the output.
        if (Logging.getLogger().getLogLevel().lessVerbose(LogLevel.NORMAL))
        {
            Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        }

        // Predicate that is true if a playlist should not count towards the number of playlists a track is in.
        final Predicate<Playlist> IGNORE_PLAYLISTS =
                (playlist ->
                        (
                                // Ignore the master playlist.
                                (playlist.isMaster())
                                        // Ignore distinguished playlists.
                                        || (playlist.distinguishedKind() != null)
                                        // Ignore playlists that are not actual playlists (instead of folders).
                                        || (playlist.isFolder())
                                        /// Ignore playlists whose name is specified in tasks.printMultiplyListedTracks.ignorePlaylists.
                                        || (playlist.name() != null && settings.getIgnorePlaylists().contains(playlist.name()))
                        )
                );

        // Predicate that is true if a track is in more than one playlist.
        final Predicate<Track> TRACK_PREDICATE =
                (track) -> track.inPlaylists().stream()
                        .filter(Predicate.not(IGNORE_PLAYLISTS))
                        .count() > 1;


        // Check if there are tracks that are in multiple non-ignored playlists
        if (library.tracks().stream().filter(TRACK_PREDICATE).findAny().isEmpty())
        {
            // Stream is empty
            Logging.getLogger().message("Every track is contained in at most one playlist.");
        }
        else
        {
            // There are such tracks
            Logging.getLogger().message("Tracks that are contained in multiple playlists");
            Logging.getLogger().message("-----------------------------------------------");
            Logging.getLogger().message("");

            // Consumer that prints the tracks and the playlists it is in
            final Consumer<Track> TRACK_CONSUMER =
                    (track)
                            ->
                    {
                        Logging.getLogger().message(track.toString());
                        track
                                .inPlaylists()
                                .stream()
                                .filter(Predicate.not(IGNORE_PLAYLISTS))
                                .map(Playlist::name)
                                .forEach((name) -> Logging.getLogger().message(1, name));
                    };

            library.tracks()
                    .stream()
                    .filter(TRACK_PREDICATE)
                    .forEach(TRACK_CONSUMER);
        }

    }

}
