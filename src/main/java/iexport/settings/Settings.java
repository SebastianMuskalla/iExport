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

package iexport.settings;

import java.util.HashMap;
import java.util.Map;

public abstract class Settings
{
    protected static final Map<String, Object> DEFAULT_SETTINGS = new HashMap<>();

    private static Object getDefaultValueFor (String setting)
    {
        return DEFAULT_SETTINGS.get(setting);
    }

    private final Map<String, Object> settings;

    /**
     * Default constructor that sets {@link #settings} to the empty map.
     * This means the default values will be used for all settings.
     */
    public Settings ()
    {
        settings = new HashMap<>();
    }

    /**
     * Constructor that initializes {@link #settings} with user-provided values.
     *
     * @param settings the user-provided values
     */
    public Settings (Map<String, Object> settings)
    {
        this.settings = settings;
    }

    private Object getValueFor (String setting)
    {
        Object value = settings.get(setting);
        if (value != null)
        {
            return value;
        }

        value = getDefaultValueFor(setting);

        if (value != null)
        {
            settings.put(setting, value);
            return value;
        }
        else
        {
            throw new RuntimeException(this.getClass() + ": Setting " + setting + " is not valid");
        }

    }

}
