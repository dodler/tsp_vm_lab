//package lian;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by artem on 10.10.15.
 */
public class Main
{
    private static int[] vector;
    private static int size = 10;

    private static int left = 0;
    private static int right = 10;

    public static void main(String args[]) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(new File("cows.in")));
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("cows.out")));

        String nk = in.readLine();
        String[] nkUnparsed = nk.split(" ");
        //        int n = 5, k = 3;
        int n = Integer.parseInt(nkUnparsed[0]), k = Integer.parseInt(nkUnparsed[1]);
//        int[] inp = new int[]{1, 2, 3, 100, 1000};
        int[] inp = new int[n];
        String[] valuesUnparsed = in.readLine().split(" ");
        for (int i = 0; i < n; i++)
        {
            inp[i] = Integer.parseInt(valuesUnparsed[i]);
        }
        int[] dist = new int[n - 1];
        for (int i = 1; i < n; i++)
        {
            dist[i - 1] = inp[i] - inp[0];
        }
//        System.out.println(dist[n - k]);
        System.out.println(dist[n - k]);
        out.write(String.valueOf(dist[n - k]));
        out.flush();
        out.close();
    }

    public static void solveA() throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(new File("collect.in")));
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("collect.out")));

        int len1 = Integer.parseInt(in.readLine());
//        int len1 = 7;
        String butTypes = in.readLine();
//        String butTypes = "10 47 50 63 89 90 99";
        String[] butTypesUnParsed = butTypes.split(" ");
        Map types = new HashMap<String, Integer>(len1);
        for (int i = 0; i < len1; i++)
        {
            types.put(butTypesUnParsed[i], i);
        }
        int checkLen = Integer.parseInt(in.readLine());
//        int checkLen = 4;
        String typesToCheck = in.readLine();
//        String typesToCheck="84 33 10 82";
        String[] typesToCheckUnParsed = typesToCheck.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < checkLen; i++)
        {
            if (types.containsKey(typesToCheckUnParsed[i]))
            {
                sb.append("YES\n");
                continue;
            }
            sb.append("NO\n");
        }
        sb.delete(sb.length() - 1, sb.length());
//        System.out.println(sb.toString());
        out.write(sb.toString());
        out.flush();
        out.close();
    }

    /**
     * method finds min value of vector
     *
     * @param vector
     * @return max value of vector
     */
    public static int min(int[] vector)
    {
        int min = vector[0];
        for (int i = 0; i < vector.length; i++)
        {
            if (min > vector[i]) min = vector[i];
        }
        return min;
    }

    /**
     * method finds max value of vector
     *
     * @param vector
     * @return max value of vector
     */
    public static int max(int[] vector)
    {
        int max = vector[0];
        for (int i = 0; i < vector.length; i++)
        {
            if (max < vector[i]) max = vector[i];
        }
        return max;
    }

    public static int binarySearch(int left, int right, int[] vector, Checkable foo)
    {
        int res = Integer.MIN_VALUE;
        for (int l = min(vector), r = max(vector); left <= right; )
        {
            int m = (l + r) / 2;
            if (foo.check(m))
            {
                res = m;
                l = m + 1;
            } else
            {
                r = m - 1;
            }
        }
        return res;
    }

    public static interface Checkable
    {
        public boolean check(int n);
    }
}
