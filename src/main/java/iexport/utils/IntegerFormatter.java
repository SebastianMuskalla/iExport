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

public class IntegerFormatter
{
    public String toStringOfSize (int i, int digits)
    {
        String res = Integer.toString(i);

        int len = res.length();

        while (len < digits)
        {
            res = Character.toString('0') + res;
            len++;
        }
        return res;
    }

    public int digits (int i)
    {
        if (i < 0)
        {
            throw new RuntimeException("Not implemented for negative numbers");
        }

        if (i == 0)
        {
            return 1;
        }

        int digits = 0;
        while (i != 0)
        {
            i = i / 10;
            digits++;
        }
        return digits;
    }

}
