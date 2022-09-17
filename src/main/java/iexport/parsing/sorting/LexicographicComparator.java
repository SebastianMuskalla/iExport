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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This comparator lifts a comparator on a type T to a comparator on {@link List<T>} using the lexicographic ordering.
 * <p>
 * Assume we are given lists [a1, a2, ..., an] and [b1, b2, ..., bm] with n <= m.
 * <p>
 * We find the smallest index i <= n such that
 * a1 equals b1, a2 equals b2, ... , a(i-1) equals b(i-1) (using the comparator on T)
 * and ai does not equal bi.
 * If ai is smaller than bi, then the list of the a's is smaller than the list of b's.
 * <p>
 * If both lists coincide for all indices {1, ..., n}, then the shorter list is smaller.
 *
 * @param <T> the type of elements of the list
 */
public class LexicographicComparator<T> implements Comparator<List<T>>
{
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    /**
     * The comparator that will be used for the base class
     */
    private final Comparator<T> comparator;

    /**
     * Constructs a lexicographic comparator for {@code List<T>}.
     *
     * @param comparator the comparator for the base class {@code T}
     */
    public LexicographicComparator (Comparator<T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public int compare (List<T> o1, List<T> o2)
    {
        // Checks for pointer equality and handles the case that one or more of the arguments is null
        if (o1 == o2)
        {
            return EQUAL;
        }
        if (o1 == null)
        {
            return SECOND_HAS_PRIORITY;
        }
        if (o2 == null)
        {
            return FIRST_HAS_PRIORITY;
        }

        Iterator<T> iterator1 = o1.iterator();
        Iterator<T> iterator2 = o2.iterator();

        // Check the common domain of the two lists
        while (iterator1.hasNext() && iterator2.hasNext())
        {
            // Compare the two elements at the same position and return if one of them is smaller than the other
            int result = comparator.compare(iterator1.next(), iterator2.next());
            if (result != EQUAL)
            {
                return result;
            }
        }

        // The two lists coincide on their common domain
        // Prioritize the shorter one
        if (iterator2.hasNext())
        {
            // o1 is shorter
            return FIRST_HAS_PRIORITY;
        }
        if (iterator1.hasNext())
        {
            // o2 is shorter
            return SECOND_HAS_PRIORITY;
        }

        // the two lists have the same length and their entries are the same
        return EQUAL;
    }
}
