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
 * This package contains classes for logging.
 * <p>
 * Currently, iExport only supports logging to STDOUT.
 * If a user wants to log to a file, they have to redirect the output themselves.
 * <p>
 * Internally, logging is done as follows: {@link iexport.logging.Logging} provides a static reference to a singleton logger
 * which can be called from all other classes.
 */
package iexport.logging;
