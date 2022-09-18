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

package iexport.utils;

/**
 * A static class providing numbers for formatting integers.
 * <p>
 * We do not use {@link java.text.DecimalFormat} because it does not support padding with leading zeroes - sad.
 */
public class IntegerFormatter
{
    /**
     * Pads the representation of a number to a desired number of digits
     * by prepending a specified character suitably often.
     * <p>
     * If the number already exceeds the specified number of digits, nothing will be done.
     *
     * @param number           the number to be formatted
     * @param characters       the desired number of digits
     * @param leadingCharacter the character used for padding
     * @return the formatted number
     */
    public static String pad (long number, int characters, char leadingCharacter)
    {
        String res = Long.toString(number);

        if (res.length() < characters)
        {
            res = (String.valueOf(leadingCharacter)).repeat(characters - res.length()) + res;
        }
        return res;
    }

    /**
     * @param number the number
     * @return the number of digits needed to display that number
     */
    public static int digits (long number)
    {
        return Long.toString(number).length();
    }

    /**
     * Do not instantiate this class.
     */
    private IntegerFormatter ()
    {
    }

}
