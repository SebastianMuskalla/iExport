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


public class PrintUnlistedTracksTask implements Task
{


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
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        // It would be pretty silly to call this task but then hide the output
        if (Logging.getLogger().getLogLevel().lessVerbose(LogLevel.NORMAL))
        {
            Logging.getLogger().setLogLevel(LogLevel.NORMAL);
        }
        
        PrintUnlistedTracksTaskSettings settings = new PrintUnlistedTracksTaskSettings(rawTaskSettings);

        final Predicate<Playlist> IGNORE_PLAYLISTS =
                (playlist ->
                        // Ignore the master playlist.
                        (playlist.master() != null && playlist.master())
                                // Ignore distinguished playlists.
                                || (playlist.distinguishedKind() != null)
                                // Ignore playlists whose name is specified in tasks.printUnlistedTracks.ignorePlaylists.
                                || (playlist.name() != null && settings.getSettingIgnorePlaylists().contains(playlist.name()))
                );

        final Predicate<Track> TRACKS_PREDICATE =
                (track ->
                        track
                                .inPlaylists()
                                .stream()
                                .noneMatch(Predicate.not(IGNORE_PLAYLISTS))
                );

        Logging.getLogger().message("Tracks that are not in any playlist");
        Logging.getLogger().message("-----------------------------------");

        library
                .tracks()
                .stream()
                .filter(TRACKS_PREDICATE)
                .map(Track::toString)
                .forEach(Logging.getLogger()::message);
    }

}
