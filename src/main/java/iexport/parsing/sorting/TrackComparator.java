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

package iexport.parsing.sorting;

import iexport.itunes.Playlist;
import iexport.itunes.Track;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * A comparator for {@link Track} that works as follows:
 * <ol>
 *     <li> Prioritizes non-null tracks
 *     <li> Prioritizes artist (using {@link Track#sortAlbumArtist}, {@link Track#albumArtist}, {@link Track#sortArtist}, {@link Track#artist} in this order) using {@link String.CaseInsensitiveComparator}
 *     <li> Prioritize earlier {@link Track#year}
 *     <li> Prioritize album (using {@link Track#sortAlbum} or {@link Track#album}) using {@link String.CaseInsensitiveComparator}
 *     <li> Prioritize smaller {@link Track#discNumber}
 *     <li> Prioritize smaller {@link Track#trackNumber}
 *     <li> Prioritize name (using {@link Track#sortName} or {@link Track#name}) using {@link String.CaseInsensitiveComparator}
 *     <li> Prioritze {@link Track#persistentId}
 * </ol>
 */
public class TrackComparator implements Comparator<Track>
{

    /**
     * NULL_COMPARATOR (@link {@link BasicComparators#NULL_COMPARATOR()} for type {@link Playlist}.
     */
    static private final Comparator<Track> NULL_TRACK_COMPARATOR = BasicComparators.NULL_COMPARATOR();

    /**
     * Compares the artist of two tracks.
     * <p>
     * For each of the tracks it will get the first of the following values that is neither {@code null} nor empty:
     * {@link Track#sortAlbumArtist}, {@link Track#albumArtist}, {@link Track#sortArtist}, {@link Track#artist}
     * and then compare them using {@link String.CaseInsensitiveComparator}
     */
    static private final Comparator<Track> ARTIST_COMPARATOR = (Track t1, Track t2) -> {
        var artists1 = Arrays.asList(t1.sortAlbumArtist(), t1.albumArtist(), t1.sortArtist(), t1.artist());
        var artists2 = Arrays.asList(t2.sortAlbumArtist(), t2.albumArtist(), t2.sortArtist(), t2.artist());

        var artist1 = artists1.stream().filter(Objects::nonNull).filter((s) -> !s.equals("")).findFirst().orElse(null);
        var artist2 = artists2.stream().filter(Objects::nonNull).filter((s) -> !s.equals("")).findFirst().orElse(null);

        return BasicComparators.STRING_COMPARATOR.compare(artist1, artist2);
    };

    /**
     * Compares the albums of two tracks.
     * <p>
     * For each track, it will use the value {@link Track#sortAlbum} if it is not null and non-empty, otherwise {@link Track#album},
     * and then compare them using {@link String.CaseInsensitiveComparator}
     */
    static private final Comparator<Track> ALBUM_COMPARATOR = (Track t1, Track t2) -> {
        var album1 = t1.sortAlbum();
        if (album1 == null || album1.equals(""))
        {
            album1 = t1.album();
        }
        var album2 = t2.sortAlbum();
        if (album2 == null || album2.equals(""))
        {
            album2 = t2.album();
        }
        return BasicComparators.STRING_COMPARATOR.compare(album1, album2);
    };

    /**
     * Compares the names (titles of two tracks.
     * <p>
     * For each track, it will use the value {@link Track#sortName} if it is not null and non-empty, otherwise {@link Track#name},
     * and then compare them using {@link String.CaseInsensitiveComparator}
     */
    static private final Comparator<Track> NAME_COMPARATOR = (Track t1, Track t2) -> {
        var name1 = t1.sortName();
        if (name1 == null || name1.equals(""))
        {
            name1 = t1.name();
        }
        var name2 = t2.sortName();
        if (name2 == null || name2.equals(""))
        {
            name2 = t2.name();
        }
        return BasicComparators.STRING_COMPARATOR.compare(name1, name2);
    };

    /**
     * Compares the years of two tracks, prioritizing non-null and earlier years.
     */
    static private final Comparator<Track> YEAR_COMPARATOR = Comparator.comparing(Track::year, BasicComparators.INTEGER_COMPARATOR);

    /**
     * Compares the disc numbers of two tracks, prioritizing non-null and smaller numbers.
     */
    static private final Comparator<Track> DISC_NUMBER_COMPARATOR = Comparator.comparing(Track::discNumber, BasicComparators.INTEGER_COMPARATOR);

    /**
     * Compares the track numbers of two tracks, prioritizing non-null and smaller numbers.
     */
    static private final Comparator<Track> TRACK_NUMBER_COMPARATOR = Comparator.comparing(Track::trackNumber, BasicComparators.INTEGER_COMPARATOR);

    /**
     * Compares the persistent ids of two tracks, prioritizing non-null and smaller ids
     */
    static private final Comparator<Track> PERSISTENT_ID_COMPARATOR = Comparator.comparing(Track::persistentId, BasicComparators.STRING_COMPARATOR);


    @Override
    public int compare (Track o1, Track o2)
    {
        return NULL_TRACK_COMPARATOR
                .thenComparing(ARTIST_COMPARATOR)
                .thenComparing(YEAR_COMPARATOR)
                .thenComparing(ALBUM_COMPARATOR)
                .thenComparing(DISC_NUMBER_COMPARATOR)
                .thenComparing(TRACK_NUMBER_COMPARATOR)
                .thenComparing(NAME_COMPARATOR)
                .thenComparing(PERSISTENT_ID_COMPARATOR)
                .compare(o1, o2);
    }

}
