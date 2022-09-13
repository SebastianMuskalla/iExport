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

/**
 * This package contains the handlers for the keys.
 * <p>
 * These handlers take a builder object (e.g. {@link iexport.parsing.builders.TrackBuilder})
 * and a value that was parsed from the .xml file.
 * They then check that the parsed value has the correct type.
 * If this is the case, they will set the corresponding field of the {@link iexport.parsing.builders.TrackBuilder}.
 */
package iexport.parsing.keys;
