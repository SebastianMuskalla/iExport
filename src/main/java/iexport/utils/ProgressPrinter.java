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

import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A printer for a progress bar that tracks a goal (of type long) that should be reached.
 * <p>
 * As long as no other messages are written to STDOUT, the progress bar will be updated in the same line.
 * <p>
 * The progress bar will be essentially of the shape
 * <p>
 * {@literal XXXX....  xy% (current/goal) message}
 * </p>
 * consisting of an ASCII progress bar,
 * the percentage of progress that has been reached,
 * the precise progress,
 * and a message.
 * <p>
 * The whole line is truncated to 80 characters.
 */
public class ProgressPrinter
{
    /**
     * A version of {@link System#out} that uses UTF8.
     */
    private static final PrintStream OUT = new PrintStream(System.out, true, UTF_8);

    /**
     * How many segments should the progress bar consist of?
     * <p>
     * One segment per 5%.
     */
    private final static int SEGMENTS = 20;

    /**
     * Character for empty segments of the bar.
     */
    private final static char EMPTY_CHAR = '\u00B7';

    /**
     * Character for filled segments of the bar.
     */
    private final static char FILLED_CHAR = '\u2588';

    /**
     * The line is truncated to this number of characters.
     */
    private static final int MAX_CHARACTERS = 80;

    /**
     * The goal of the progress bar
     */
    private final long goal;

    /**
     * How many digits are needed to display goal.
     */
    private final int goalDigits;

    /**
     * Has the goal been reached?
     */
    private boolean done = false;

    /**
     * The goal that should be reached.
     *
     * @param goal a number representing the goal
     */
    public ProgressPrinter (long goal)
    {
        this.goal = goal;
        goalDigits = Long.toString(goal).length();
    }

    /**
     * Update the status of the progress bar.
     *
     * @param newValue the new value
     * @param message  the message that should be printed
     */
    public void update (long newValue, String message)
    {
        // If the progress has already been completed, we accept no additional updates
        if (done)
        {
            return;
        }

        // The progress has just been completed
        if (newValue >= goal)
        {
            newValue = goal;
            done = true;
        }

        double ratio = (double) newValue / (double) goal;

        int filledSegments = (int) Math.round(ratio * SEGMENTS);

        // Compute the percentage as a three-character representation, padded with leading spaces.
        int percent = (int) Math.round(ratio * 100);
        String percentage = IntegerFormatter.pad(percent, 3, ' ');
        percentage += "%";

        // Compute a fraction representation of the progress.
        String fraction = "("
                + IntegerFormatter.pad(newValue, goalDigits, ' ')
                + "/"
                + goal
                + ")";

        // Compute the progress bar itself.
        String progress = String.valueOf(FILLED_CHAR).repeat(filledSegments)
                + String.valueOf(EMPTY_CHAR).repeat(SEGMENTS - filledSegments);

        // Construct the string.
        String display =
                "\r"  // Move the cursor to here.
                        + progress
                        + " " + percentage
                        + " " + fraction
                        + " " + message;

        // Make sure progress is exactly 80 characters long
        if (display.length() > MAX_CHARACTERS)
        {
            display = display.substring(0, MAX_CHARACTERS - 3);
            display += "...";
        }
        else
        {
            display += " ".repeat(MAX_CHARACTERS - display.length());
        }

        // Print it
        OUT.print(display);

        // If we are done, also append a newline a print "Done!"
        if (done)
        {
            OUT.println();
            OUT.println("Done!");
        }
    }


}
