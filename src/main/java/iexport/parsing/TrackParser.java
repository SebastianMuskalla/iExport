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

package iexport.parsing;

import com.dd.plist.NSDictionary;
import iexport.itunes.Track;
import iexport.logging.Logging;
import iexport.parsing.builders.TrackBuilder;
import iexport.parsing.keys.TrackKeys;

/**
 * A class for converting a parsed {@link NSDictionary} from the .xml file into a {@link Track}.
 */
public class TrackParser
{
    /**
     * The dictionary containing the parsed value.
     */
    private final NSDictionary trackDictionary;

    /**
     * The mutable track builder that used to generate the track.
     */
    private TrackBuilder trackBuilder;

    /**
     * @param trackDictionary The dictionary of parsed key-value pairs for this track.
     */
    public TrackParser (NSDictionary trackDictionary)
    {
        this.trackDictionary = trackDictionary;
        trackBuilder = new TrackBuilder();
    }

    /**
     * Create a {@link Track}.
     * <p>
     * This method will use a mutable {@link TrackBuilder},
     * use the handlers from {@link TrackKeys} to set it fields,
     * and then create an immutable {@link Track} record.
     *
     * @return the track
     */
    Track parse ()
    {
        for (var keyValuePair : trackDictionary.entrySet())
        {
            String key = keyValuePair.getKey();
            Object value = keyValuePair.getValue().toJavaObject();

            var handler = TrackKeys.getHandlerFor(key);

            if (handler != null)
            {
                handler.accept(trackBuilder, value);
            }
            else
            {
                Logging.getLogger().debug(this.getClass().getSimpleName() + ": No handler for key \"" + key + "\" with value \"" + value.toString() + "\"");
            }
        }

        // we can now build the track
        Track track = trackBuilder.build();

        // reset the track Builder in case someone makes the mistake of using this track builder twice
        trackBuilder = new TrackBuilder();

        return track;
    }
}
