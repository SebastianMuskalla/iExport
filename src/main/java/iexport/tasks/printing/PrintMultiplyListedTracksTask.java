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


public class PrintMultiplyListedTracksTask implements Task
{
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
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        // It would be pretty silly to call this task but then hide the output
        if (Logging.getLogger().getLogLevel().lessVerbose(LogLevel.NORMAL))
        {
            Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        }
        
        PrintMultiplyListedTracksTaskSettings settings = new PrintMultiplyListedTracksTaskSettings(rawTaskSettings);

        Logging.getLogger().message("Tracks that are contained in multiple playlists");
        Logging.getLogger().message("-----------------------------------------------");

        final Predicate<Playlist> IGNORE_PLAYLISTS =
                (playlist ->
                        (
                                // Ignore the master playlist.
                                (playlist.master() != null && playlist.master())
                                        // Ignore distinguished playlists.
                                        || (playlist.distinguishedKind() != null)
                                        // Ignore playlists that are not actual playlists (instead of folders).
                                        || (playlist.children() != null && playlist.children().size() > 0)
                                        /// Ignore playlists whose name is specified in tasks.printMultiplyListedTracks.ignorePlaylists.
                                        || (playlist.name() != null && settings.getSettingIgnorePlaylists().contains(playlist.name()))
                        )
                );

        final Predicate<Track> TRACK_PREDICATE =
                (track) -> track.inPlaylists().stream()
                        .filter(Predicate.not(IGNORE_PLAYLISTS))
                        .count() > 1;


        final Consumer<Track> TRACK_CONSUMER =
                (track)
                        ->
                {
                    Logging.getLogger().message(track.toString());
                    track.inPlaylists().stream().filter(Predicate.not(IGNORE_PLAYLISTS)).map(Playlist::name).forEach((name) -> Logging.getLogger().message(1, name));
                };

        library
                .tracks()
                .stream()
                .filter(TRACK_PREDICATE)
                .forEach(TRACK_CONSUMER);

    }

}
