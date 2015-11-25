//package lian;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        ProblemCPolygonSquare.solve();
    }

    public static class ProblemCPolygonSquare
    {
        private static final boolean TEST = false;

        public static void solve() throws IOException
        {
            Scanner in;
            int n;
            Point[] points;
            if (TEST)
            {
                n = 3;
                points = new Point[n];
                points[0] = new Point(0, 0);
                points[1] = new Point(0, 1);
                points[2] = new Point(1, 1);
//                points[3] = new Point(2,4);
//                points[4] = new Point(4, 5);
//                points[5] = new Point(10, 0);
            } else
            {
                in = new Scanner(new FileReader(new File("area.in")));
                n = in.nextInt();
                points = new Point[n];
                for (int i = 0; i < n; i++)
                {
                    int x = in.nextInt(), y = in.nextInt();
                    points[i] = new Point(x, y);
                }
            }

            double square = 0;

            for (int i = 1; i < n - 1; i++)
            {
                square += ProblemDTriangleSquare.triangleSquare(
                        points[0].x,
                        points[0].y,
                        points[i].x,
                        points[i].y,
                        points[i + 1].x,
                        points[i + 1].y
                );
            }


            FileWriter out = new FileWriter(new File("area.out"));
            out.write(Double.toString(square/2));
            out.flush();
            out.close();
            System.out.println(square/2);
        }
    }

    public static class ProblemDTriangleSquare
    {
        private static double norm(long x, long y)
        {
            return Math.sqrt(x * x + y * y);
        }

        private static int pseudoScalar(int a1, int b1, int a2, int b2)
        {
            return a1 * b2 - a2 * b1;
        }

        public static int triangleSquare(int x1,
                                            int y1,
                                            int x2,
                                            int y2,
                                            int x3,
                                            int y3)
        {
            int x1_ = x2 - x1, y1_ = y2 - y1;

            int x2_ = x3 - x2, y2_ = y3 - y2;

            double square;

            double a = norm(x1_, y1_), b = norm(x2_, y2_);
            double angle = pseudoScalar(x1_, y1_, x2_, y2_) / (a * b);
            square = a * b * angle;

            return Math.abs(pseudoScalar(x1_, y1_, x2_, y2_));
        }

        public static void solve() throws IOException
        {
            Scanner in = new Scanner(new FileReader(new File("area1.in")));
            int x1 = in.nextInt(), y1 = in.nextInt(),
                    x2 = in.nextInt(), y2 = in.nextInt(),
                    x3 = in.nextInt(), y3 = in.nextInt();

            x1 = x2 - x1;
            y1 = y2 - y1;

            x2 = x3 - x2;
            y2 = y3 - y2;

            double square;

            double a = norm(x1, y1), b = norm(x2, y2);
            double angle = pseudoScalar(x1, y1, x2, y2) / (a * b);
            square = a * b * angle;


            FileWriter out = new FileWriter(new File("area1.out"));
            out.write(Double.toString(Math.abs(square / 2)));
            out.flush();
            out.close();
            System.out.println((Double.toString(square / 2)));
        }
    }

    public static class ProblemAAngle
    {
        public static void solve() throws IOException
        {
            Scanner in = new Scanner(new FileReader(new File("angle1.in")));
            int x = in.nextInt(), y = in.nextInt();
//            int x = 1, y = -1;
            double phi;

            if (x == 0 || y == 0)
                phi = Math.abs(Math.atan2(y, x));

            else phi = Math.atan((double) y / (double) x);

            if (x < 0 && y > 0)
            {
                phi += Math.PI;
            }
            if (x < 0 && y < 0)
            {
                phi += Math.PI;
            }
            if (y < 0)
            {
                if (x == 0)
                    phi += Math.PI;
                else if (x > 0)
                    phi += Math.PI * 2;
            }

            FileWriter out = new FileWriter(new File("angle1.out"));
            out.write(Double.toString(phi));
            out.flush();
            out.close();
            System.out.println((Double.toString(phi)));
        }
    }

    public static class ProblemB
    {
        public static void solve()
        {
            Scanner scanner = new Scanner(System.in);

            Point vector1, vector2, point1, point2, point3;
            point1 = new Point(scanner.nextInt(), scanner.nextInt());
            point2 = new Point(scanner.nextInt(), scanner.nextInt());
            point3 = new Point(scanner.nextInt(), scanner.nextInt());

            vector1 = point2.subtract(point1);
            vector2 = point3.subtract(point2);

            long scalar = vector1.pseudoScalar(vector2);
//            double scalar = ((point3.x - point1.x) / (point2.x - point1.x)) -
//                    ((point3.y - point1.y) / (point2.y - point1.y));

            if (scalar > 0)
            {
                System.out.println("LEFT");
                return;
            }
            if (scalar < 0)
            {
                System.out.println("RIGHT");
                return;
            } else
            {
                System.out.println("TOWARDS");
                return;
            }
        }
    }

    public static class Point
    {
        public int x, y;

        public String toString()
        {
            return "{" + x + ":" + y + "}";
        }

        public long pseudoScalar(Point vector)
        {
            return x * vector.y - vector.x * y;
        }

        public long scalarMultiplication(Point vector)
        {
            return x * vector.x + y * vector.y;
        }

        public Point subtract(Point p)
        {
            return new Point(x - p.x, y - p.y);
        }

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public static class ProblemA
    {
        public static void solve()
        {
            int n = 2, k = 1;
//            int n = 2, k = 1;
            Scanner scanner = new Scanner(System.in);
            n = scanner.nextInt();
            k = scanner.nextInt();
            double len = 0;
            Point[] points = new Point[n];
//            points[0]=new Point(0,0);
//            points[1]=new Point(10,0);
            for (int i = 0; i < n; i++)
            {
                int x, y;
                x = scanner.nextInt();
                y = scanner.nextInt();
                points[i] = new Point(x, y);
            }

            for (int i = 0; i < n - 1; i++)
            {
                len += (
                        Math.pow(
                                Math.pow(points[i + 1].x - points[i].x, 2) + Math.pow(points[i + 1].y - points[i].y, 2),
                                0.5)
                );
            }
            len *= k;
            System.out.println(len / 50.0);
        }
    }

    public static class Problem1920
    {
        public static void solve()
        {
            int n = 3, l = 6, delta = l - n;
            if (l / 2 != 0) System.out.println("not suitable");

            int lastPosX = 1, lastPosY = 1;
            for (int i = 1; i <= n; i++)
            {
                System.out.println(i + " " + lastPosY);
                lastPosX = i;
            }
            for (int j = 0; j <= delta; j++)
            {
                System.out.println(lastPosX + " " + j);
                lastPosY = j;
            }
        }
    }

    public static class BottleA
    {
        static void solve()
        {
            Scanner sc = new Scanner(System.in);
//            int a = sc.nextInt(), b = sc.nextInt(), c = sc.nextInt(), aC = 0, bC = 0;
            int a = 1, b = 1, c = 9, aC = 0, bC = 0;
            if (gcd(a, b) != 1)
            {
                System.out.println("-1");
                return;
            } else
            {
                System.out.println("0");
            }
            int current = 0;
            while (current != c)
            {
                if (current + a <= c)
                {
                    aC++;
                    current += a;
                } else
                {
                    bC++;
                    current -= b;
                }
            }
            if (bC != 0) bC *= (-1);
            System.out.println(aC + " " + bC);


        }

        private static int gcd(int a, int b)
        {
            if (b == 0)
                return a;
            else
                return gcd(b, a % b);
        }
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

    private static class Common
    {
        private static void invoke() throws IOException
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
    }
}
