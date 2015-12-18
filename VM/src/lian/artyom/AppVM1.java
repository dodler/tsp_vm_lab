package lian.artyom;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
import lian.RarefiedMatrix;
import lian.artyom.plotter.Plotter;
import lian.artyom.regularization.impl.TikhonovRegulizer;
import lian.artyom.solver.*;
import org.apache.commons.math3.linear.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import vm.container.Vector;
import vm.container.util.NumericMatrixUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * entry point for lab work
 * Created by artem on 19.09.15.
 */
public class AppVM1
{
    private static final Logger logger = Logger.getLogger(AppVM1.class);

    public static StringBuilder message = new StringBuilder();


    public static class TaskLU
    {
        public static void executeLU()
        {
//            numericMatrix.setEntry(0,1, 25000); // this is for testing modified LU method

            RealVector luSolve = Calculator.solveWithLU(FullTask1.numericMatrix, FullTask1.vector);

            message.append("относительная погрешность метода LU:" + Calculator.relativeError(FullTask1.xVector, luSolve) + Math.pow(10, -15));
            message.append("|\n");
            message.append("--------------------------------------------------\n");

            RealVector luModifiedSolve = Calculator.LUModified2(FullTask1.numericMatrix, FullTask1.vector);

            message.append("|относительная погрешность метода:");
            message.append(Calculator.relativeError(FullTask1.xVector, luModifiedSolve));
            message.append("\n");
            message.append("--------------------------------------------------\n");

            RealVector seidelSolve = Calculator.seidelSolve2(FullTask1.numericMatrix, FullTask1.vector);

            message.append("|");
            message.append("относительная погрешность метода:" + Calculator.relativeError(FullTask1.xVector, seidelSolve));
            message.append("|\n");
        }
    }

    public static final class Prepare1Task
    {
        public static void execute()
        {
            {
                if (FullTask1.GOOD)
                {
                    message.append("Хорошо обусловленная матрица\n");
                    FullTask1.WIDTH = 1500;
                    FullTask1.outPath = "out.txt";
                    FullTask1.numericMatrix = NumericMatrixUtils.conditionedMatrix(FullTask1.WIDTH);
                } else
                {
                    message.append("Плохо обусловленная матрица\n");
                    FullTask1.WIDTH = 10;
                    FullTask1.outPath = "out2.txt";
                    FullTask1.numericMatrix = NumericMatrixUtils.hilbert(FullTask1.WIDTH);
                }
                FullTask1.xVector = Vector.risingVector(FullTask1.WIDTH);
                FullTask1.vector = NumericMatrixUtils.multiplicateMatrix(FullTask1.numericMatrix, FullTask1.xVector);

            }
        }

    }

    public static final class FullTask1
    {

        public static int WIDTH;
        public static String outPath;

        public static RealMatrix numericMatrix;
        public static RealVector xVector;
        public static RealVector vector;

        public static final boolean TEST = true;
        public static boolean GOOD = true;
        public static boolean FULL = false;

        public static void execute() throws IOException
        {
            Prepare1Task.execute();

            if (!TEST)
            {
                logger.setLevel(Level.OFF);

                TaskLU.executeLU();
                Task2.invoke();
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outPath)));
                writer.write(message.toString());
                writer.flush();
                writer.close();

                if (!FULL) return;
                GOOD = !GOOD;
                Prepare1Task.execute();
                message.delete(0, message.length());

                TaskLU.executeLU();
                Task2.invoke();
                writer = new BufferedWriter(new FileWriter(new File(outPath)));
                writer.write(message.toString());
                writer.flush();

                writer.close();


            } else
            {
                TestTask.executeTest();
            }

            System.out.println(message.toString());
        }
    }

    public static void main(String[] args) throws IOException
    {
        DOMConfigurator.configure("/media/artem/385BE95714C3BE20/IdeaProjects/Custom/config/log4j-config.xml");
        Calculator.EPSILON_THRESHOLD = Math.pow(10, -20);
        FullTask3.K = 1;
        FullTask3.execute();

//        FullTask3.K = 2;
//        FullTask3.execute();
//
//        FullTask3.K = 3;
//        FullTask3.execute();
    }

    public static final class FullTask3
    {
        private static int N = 5, K = 3;
        private static double A = 0, B = 1, H = (B - A) / (N - 1);

        private static RealVector x, b, a;
        private static RealMatrix k;

        private static final void prepare()
        {
            k = new RarefiedMatrix(N);
            x = new ArrayRealVector(N);
            a = new ArrayRealVector(N);

            for (int i = 0; i < N; i++)
            {
                x.setEntry(i, A + i * H); // net
            }

            for (int i = 0; i < N; i++)
            {
                a.setEntry(i, Ai(i)); // weight coefficients
            }

            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    k.setEntry(i, j, Ai(j) * K(x.getEntry(i), x.getEntry(j)));
                }
            }

            b = new ArrayRealVector(N);
            for (int i = 0; i < N; i++)
            {
                b.setEntry(i, f(x.getEntry(i))); // right part
            }
        }

        private static final double Ai(int i)
        {
            if (i == 0 || i == N - 1)
            {
                return H / 2;
            } else
            {
                return H;
            }
        }

        private static final double alpha(int k)
        {
            return Math.pow(10, -k);
        }

        private static final double K(double s, double t)
        {
            return Math.pow(s + t, 1.0 / 3.0);
        }

        private static final double f(double t)
        {
            return t * t;
        }

        private static final double beta(RealMatrix a, int i)
        {
            switch (i)
            {
                case 1:
                    return 1;
                case 2:
                    return Math.sqrt(alpha(i));
                case 3:
                    double leastEigen = Calculator.eigennumberQR(a).getMinValue();
                    return Math.sqrt((Math.pow(leastEigen, 2) / 2) + alpha(i));
                default:
                    return 0;
            }
        }

        private static final RealMatrix extendedMatrix(RealMatrix A, double beta, double alpha)
        {
            int size = A.getColumnDimension();
            RealMatrix result = new RarefiedMatrix(size * 2), At = A.transpose();

            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    int ind = i == j ? 1 : 0;
                    result.setEntry(i, j, ind * beta);

                    result.setEntry(i + size, j, A.getEntry(i, j));
                    result.setEntry(i, j + size, At.getEntry(i, j));

                    result.setEntry(i + size, j + size, -ind * alpha * beta);
                }
            }

            return result;
        }

        private static final RealVector extendedVector(RealVector b)
        {
            int size = b.getDimension();
            RealVector result = new ArrayRealVector(size * 2);
            for (int i = 0; i < size; i++)
            {
                result.setEntry(i, b.getEntry(i));
                result.setEntry(i + size, 0);
            }
            return result;
        }

        private static RealVector cutX(RealVector xExt)
        {
            int size = xExt.getDimension();
            if (size != N * 2)
            {
                return xExt;
            }
            RealVector result = new ArrayRealVector(size / 2);
            for (int i = 0; i < size / 2; i++)
            {
                result.setEntry(i, xExt.getEntry(size / 2 + i));
            }
            return result;
        }

        private static RealVector solve, extSolve;

        private static final void solve()
        {
            RealMatrix regularizedK = new TikhonovRegulizer().regulize(k, alpha(K));
            b = regularizedK.transpose().preMultiply(b);

            RealMatrix extendedK = extendedMatrix(regularizedK, beta(regularizedK, K), alpha(K));
            RealVector extendedB = extendedVector(b);

            Solver holesky = new HoleskySolver();
            extSolve = cutX(new QRDecomposition(extendedK).getSolver().solve(extendedB));

            solve = holesky.isApplicable(regularizedK) ? cutX(holesky.solve(regularizedK, b)) : new ArrayRealVector(N);

            logger.debug("extended for QR" + extSolve);
            logger.debug("holesky" + solve);
        }

        private static final void alpha()
        {
            final Plotter plotter = new Plotter("Решения системы с параметрами");
            final RealVector axis = new ArrayRealVector(N);

            for (int i = 0; i < axis.getDimension(); i++)
            {
                axis.setEntry(i, i);
            }

            K = 1;
            prepare();
            solve();
            plotter.addRealVector(axis, solve, "Решение методом QR, alpha1");

            K = 2;
            prepare();
            solve();
            plotter.addRealVector(axis, solve, "Решение методом QR, alpha2");

            K = 3;
            prepare();
            solve();
            plotter.addRealVector(axis, solve, "Решение методом QR, alpha3");

            plotter.plot();
        }

        private static final void beta()
        {
            final Plotter plotter = new Plotter("Решения системы с параметрами");
            final RealVector axis = new ArrayRealVector(N);

            for (int i = 0; i < axis.getDimension(); i++)
            {
                axis.setEntry(i, i);
            }

            K = 1;
            prepare();
            solve();
            plotter.addRealVector(axis, extSolve, "Решение методом QR, beta1");

            K = 2;
            prepare();
            solve();
            plotter.addRealVector(axis, extSolve, "Решение методом QR, beta2");

            K = 3;
            prepare();
            solve();
            plotter.addRealVector(axis, extSolve, "Решение методом QR, beta3");

            plotter.plot();
        }

        public static final void execute()
        {
            alpha();
            beta();
        }
    }

    public static final class FullTask2
    {
        private static int SIZE = 5, C = 2;

        static RealVector x, b;
        static RealMatrix a;

        public static void execute()
        {
            FileWriter writer = null;
            StringBuilder out = new StringBuilder();
            try
            {
                File file = new File("prod_out/out2.txt");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                writer = new FileWriter(file);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(new File("prod_out/l2_set.txt")));
                SIZE = Integer.parseInt(reader.readLine());
                C = Integer.parseInt(reader.readLine());
                Calculator.EPSILON_THRESHOLD = Double.parseDouble(reader.readLine());
            } catch (Exception e)
            {
                logger.debug(e);
                SIZE = 10;
                C = 5;

                System.out.println("Введите размер матрицы");
                Scanner c = new Scanner(System.in);
                SIZE = c.nextInt();

                System.out.println("Введите параметр с");
                C = c.nextInt();

                System.out.println("Введите требуемую точность:");
                Calculator.EPSILON_THRESHOLD = c.nextDouble();

            }

            a = Calculator.enMatrix(SIZE, C);
            x = Vector.risingVector(SIZE);
            b = NumericMatrixUtils.multiplicateMatrix(a, x);

            out.append("Лабораторная работа 2. Разреженная матрица. Размер :");
            out.append(SIZE);
            out.append("x");
            out.append(SIZE);
            out.append(", параметр С=");
            out.append(C);
            out.append("\n");

            out.append("Портрет матрицы:\n");
            out.append(((RarefiedMatrix) a).getPortrait());
            out.append("\n");

            out.append("Хорошо обусловленная?");
            out.append(Calculator.isGoodConditioned(a));
            out.append("\n");
            out.append("Число обусловленности: ");
            out.append(a.getNorm());
            out.append("\n");

            HoleskySolver holeskySolver = new HoleskySolver();
//            logger.debug("is holesky applicable:");
//            logger.debug(holeskySolver.isApplicable(a));
            logger.debug(Calculator.isSymmetric(a));
            logger.debug(a.getNorm());
            logger.debug("holesky solve:");
            RealVector solutionHolesky = holeskySolver.solve(a, b);
            logger.debug(solutionHolesky);
            logger.debug("holesky error");
            logger.debug(Calculator.relativeError(x, solutionHolesky));
            logger.debug("holesky fill value");
            logger.debug(holeskySolver.getFillValue());

            out.append("Решением методом разложения Холецкого. Относительная погрешность:");
            out.append(Calculator.relativeError(x, solutionHolesky));
            out.append("\n");
            out.append("Величина заполнения:");
            out.append(holeskySolver.getFillValue());
            out.append("\n");

            GaussSeidelSolver solver = new GaussSeidelSolver();
            logger.debug(solver.isApplicable(a));
            RealVector seidel = solver.solve(a, b);
//            RealVector seidel = Calculator.seidelSolve(a, b);

            out.append("Решением методом Гаусса-Зейделя. Относительная погрешность:");
            out.append(Calculator.relativeError(x, seidel));
            out.append("\n");
            out.append("Величина заполнения:");
            out.append(0);
            out.append("\n");
            out.append("Точность:" + Calculator.EPSILON_THRESHOLD);
            out.append("\n");

            logger.debug(Calculator.isGoodConditioned(a));

            if (writer != null)
            {
                try
                {
                    writer.write(out.toString());
                    writer.flush();
                    writer.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class TestTask
    {
        public static void executeTest()
        {

//            numericMatrix = new Array2DRowRealMatrix(3,3);
//            numericMatrix.setEntry(0,0, 150);
//            numericMatrix.setEntry(0,1, 1);
//            numericMatrix.setEntry(0,2, 2);
//            numericMatrix.setEntry(1,0, 3);
//            numericMatrix.setEntry(1,1, 200);
//            numericMatrix.setEntry(1,2, 3);
//            numericMatrix.setEntry(2,0, 1);
//            numericMatrix.setEntry(2,1, 2);
//            numericMatrix.setEntry(2,2, 110);


            logger.debug("start");
            RealVector v1 = Calculator.solveWithLU(FullTask1.numericMatrix, FullTask1.vector), v2 = Calculator.LUModified2(FullTask1.numericMatrix, FullTask1.vector);
        }
    }

    private static class Task2
    {
        private static void invoke()
        {
            message.append("-----------------------\n");
            message.append("Определитель матрицы системы=" + Calculator.calcDeterminant(FullTask1.numericMatrix));
            message.append("\n");
            RealVector v1 = Calculator.eigennumberQR(FullTask1.numericMatrix);
            message.append("максимальное с.ч. системы A * transponize(A):" + v1.getMaxValue() + ":минимальное  с.ч. системы A * transponize(A):" + v1.getMinValue());
            message.append("\n");
            RealMatrix reversedA = Calculator.reverseA(FullTask1.numericMatrix);
            message.append("cond(A)=" + (reversedA.getNorm() * FullTask1.numericMatrix.getNorm()));
            message.append("\n");
        }
    }
}
