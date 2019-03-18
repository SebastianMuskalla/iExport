package iexport.domain.builder.sorting;

import java.util.Comparator;

/**
 * @author Sebastian Muskalla
 */
public class NullIntegerComparator implements Comparator<Integer>
{
    private static final int EQUAL = 0;
    private static final int FIRST_HAS_PRIORITY = -1;
    private static final int SECOND_HAS_PRIORITY = 1;

    @Override
    public int compare (Integer o1, Integer o2)
    {
        if (o1 == null)
        {
            if (o2 != null)
            {
                return SECOND_HAS_PRIORITY;
            }
            else
            {
                return EQUAL;
            }
        }
        if (o2 == null)
        {
            return FIRST_HAS_PRIORITY;
        }

        return Integer.compare(o1, o2);
    }

    @Override
    public boolean equals (Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return obj.getClass().equals(this.getClass());
    }
}
