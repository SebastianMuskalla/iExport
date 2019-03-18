package iexport.util;

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
