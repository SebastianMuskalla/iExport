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

import java.util.function.Predicate;

/**
 * A task that will print tracks that are not contained in any playlist.
 * <p>
 * We ignore the master playlist,  all distinguished playlists (e.g. "Music", "Downloaded").
 * The user may use the setting tasks.printUnlistedTracks.ignorePlaylists to specify additional playlist names
 * that should be ignored (i.e. a track will count as unlisted even if it is in one of these playlists).
 */
public class PrintUnlistedTracksTask extends Task
{
    /**
     * The settings used for this task.
     */
    PrintUnlistedTracksTaskSettings settings;

    @Override
    public String getTaskName ()
    {
        return "printUnlistedTracks";
    }

    @Override
    public String getDescription ()
    {
        return "prints tracks that are not contained in any playlist";
    }

    @Override
    public void initialize (Library library, RawTaskSettings rawTaskSettings)
    {
        super.initialize(library, rawTaskSettings);

        // Convert the RawTaskSettings into settings for this type of task.
        settings = new PrintUnlistedTracksTaskSettings(rawTaskSettings);
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

        // Predicate that is true if a playlist should not count towards the playlists a track is in.
        final Predicate<Playlist> IGNORE_PLAYLISTS =
                (playlist ->
                        // Ignore the master playlist.
                        (playlist.isMaster())
                                // Ignore distinguished playlists.
                                || (playlist.distinguishedKind() != null)
                                // Ignore playlists whose name is specified in tasks.printUnlistedTracks.ignorePlaylists.
                                || (playlist.name() != null && settings.getIgnorePlaylists().contains(playlist.name()))
                );

        // Predicate that is true if a track is not in any non-ignored playlist.
        final Predicate<Track> TRACK_PREDICATE =
                (track ->
                        track
                                .inPlaylists()
                                .stream()
                                .noneMatch(Predicate.not(IGNORE_PLAYLISTS))
                );

        // Check if there are tracks that are in multiple non-ignored playlists
        if (library.tracks().stream().filter(TRACK_PREDICATE).findAny().isEmpty())
        {
            // Stream is empty
            Logging.getLogger().message("Every track is contained in at least one playlist.");
        }
        else
        {
            // There are such tracks
            Logging.getLogger().message("Tracks that are not contained in any playlist");
            Logging.getLogger().message("---------------------------------------------");
            Logging.getLogger().message("");

            // Print them
            library
                    .tracks()
                    .stream()
                    .filter(TRACK_PREDICATE)
                    .map(Track::toString)
                    .forEach(Logging.getLogger()::message);
        }

    }

}
