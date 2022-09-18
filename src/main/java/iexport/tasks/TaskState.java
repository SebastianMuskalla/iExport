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

package iexport.tasks;

/**
 * Represents the state a {@link Task} is in.
 * <p>
 * When a task is created, it will be {@link #UNINITIALIZED}.
 * <p>
 * It should then be initialized by providing the settings etc.
 * It will change to {@link #READY}.
 * In this state, it can be queried e.g. for errors in the settings.
 * <p>
 * Finally, a task can be executed once, which will make it switch to state {@link #DONE}.
 */
public enum TaskState
{
    /**
     * The task has not been initialized and cannot be used.
     */
    UNINITIALIZED,

    /**
     * The task is ready to be executed.
     */
    READY,

    /**
     * The task has been executed once and cannot be executed again.
     */
    DONE
}
