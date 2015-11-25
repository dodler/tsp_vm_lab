package lian;

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
        number1 = Double.valueOf(str1);
        number2 = Double.valueOf(str2);
    }
}
