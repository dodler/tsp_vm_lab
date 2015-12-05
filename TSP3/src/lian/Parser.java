package lian;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artem on 22.11.15.
 */
public class Parser
{
    private double number1, number2;

    public double getNumber1()
    {
        return number1;
    }

    public double getNumber2()
    {
        return number2;
    }

    public void parse(String str1, String str2)
    {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m1 = p.matcher(str1), m2 = p.matcher(str2);

        if (!m1.matches() || !m2.matches())
        {
            throw new RuntimeException("Bad input for numbers:{" + str1 + ":" + str2 + "}");
        }

        number1 = Double.valueOf(str1);
        number2 = Double.valueOf(str2);
    }
}
