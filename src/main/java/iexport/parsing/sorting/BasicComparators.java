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
import java.util.Objects;

/**
 * A static class providing some basic comparators
 */
public class BasicComparators
{
    /**
     * Compares two Strings by prioritizing non-null ones and otherwise using {@link String.CaseInsensitiveComparator}.
     */
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    /**
     * Compares two integers by prioritizing non-null ones and otherwise using the natural order.
     */
    public static final Comparator<Integer> INTEGER_COMPARATOR =
            (o1, o2) -> {
                if (Objects.equals(o1, o2))
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
                return Integer.compare(o1, o2);
            };

    /**
     * Compares two Strings by prioritizing non-null ones and otherwise using {@link java.lang.String.CaseInsensitiveComparator}
     */
    public static final Comparator<String> STRING_COMPARATOR =
            (o1, o2) -> {
                if (Objects.equals(o1, o2))
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
                return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
            };


    /**
     * Constructs a comparator that checks pointer equality and whether one of the arguments is null.
     * If at least one of the arguments is non-null, it is prioritized.
     * Otherwise, the result is EQUAL.
     * <p>
     * Note that this has to be a method and not just a field because of the Java template system.
     *
     * @param <T> the type
     * @return the comparator
     */
    public static <T> Comparator<T> NULL_COMPARATOR ()
    {
        return
                (T o1, T o2) -> {
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
                    return EQUAL;
                };
    }
}
