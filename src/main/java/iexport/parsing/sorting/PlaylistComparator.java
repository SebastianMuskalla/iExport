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

import java.util.Comparator;
import java.util.List;

/**
 * A comparator for {@link Playlist}.
 * <p>
 * It prioritize non-null playlists, and then prioritizes playlists according to the lexicographic ordering
 * of their {@link Playlist#ancestry} using {@link LexicographicComparator}.
 * <p>
 * This means it will compare the ancestry of the playlists one by one and return a result
 * once it finds an entry that is not equal.
 * For comparing the entries of the ancestries,
 * it uses a comparator that prioritizes playlists with no children and otherwise compares the name.
 * <p>
 * If the ancestry of one playlist is a prefix of the ancestry of the other,
 * then the playlist with the shorter ancestry is prioritized.
 */
public class PlaylistComparator implements Comparator<Playlist>
{
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    /**
     * NULL_COMPARATOR ({@link BasicComparators#NULL_COMPARATOR()}) for type {@link Playlist}.
     */
    static private final Comparator<Playlist> NULL_PLAYLIST_COMPARATOR = BasicComparators.NULL_COMPARATOR();

    /**
     * NULL_COMPARATOR ({@link BasicComparators#NULL_COMPARATOR()}) for type {@link List<Playlist>}.
     */
    static private final Comparator<List<Playlist>> NULL_ANCESTRY_COMPARATOR = BasicComparators.NULL_COMPARATOR();

    /**
     * Comparator that prioritizes playlists that don't have children.
     */
    static private final Comparator<Playlist> CHILDREN_EXISTENCE_COMPARATOR =
            (Playlist o1, Playlist o2) -> {
                if (o1.hasChildren())
                {
                    if (!o2.hasChildren())
                    {
                        // First has has children, second one does not
                        return SECOND_HAS_PRIORITY;
                    }
                    else
                    {
                        // Both have children
                        return EQUAL;
                    }
                }
                else if (o2.hasChildren())
                {
                    // Second one has children, first one does not
                    return FIRST_HAS_PRIORITY;
                }
                else
                {
                    // None of them has children
                    return EQUAL;
                }
            };

    /**
     * Comparator that
     * <ol>
     *     <li> Prioritizes non-null playlists
     *     <li> Prioritizes playlists with no children
     *     <li> Prioritizes playlists with the smaller {@link Playlist#name} (using {@link String.CaseInsensitiveComparator}
     *     <li> Prioritizes playlists with the smaller {@link Playlist#playlistPersistentId}
     * </ol>
     */
    static private final Comparator<Playlist> BASIC_PLAYLIST_COMPARATOR =
            NULL_PLAYLIST_COMPARATOR
                    .thenComparing(CHILDREN_EXISTENCE_COMPARATOR)
                    .thenComparing(Playlist::name, BasicComparators.STRING_COMPARATOR)
                    .thenComparing(Playlist::playlistPersistentId, BasicComparators.STRING_COMPARATOR);

    /**
     * Lift {@link #BASIC_PLAYLIST_COMPARATOR} from {@code Playlist} to {@code List<Playlist>}  using {@link LexicographicComparator}.
     */
    static private final Comparator<List<Playlist>> ANCESTRY_COMPARATOR = new LexicographicComparator<>(BASIC_PLAYLIST_COMPARATOR);

    @Override
    public int compare (Playlist o1, Playlist o2)
    {
        return NULL_PLAYLIST_COMPARATOR
                .thenComparing(Playlist::ancestry, NULL_ANCESTRY_COMPARATOR)
                .thenComparing(Playlist::ancestry, ANCESTRY_COMPARATOR)
                .compare(o1, o2);
    }

}
