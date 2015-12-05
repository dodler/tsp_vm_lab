package lian.artyom;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
import lian.RarefiedMatrix;
import lian.artyom.solver.GaussSeidelSolver;
import lian.artyom.solver.HoleskySolver;
import lian.artyom.solver.JacobySolver;
import lian.artyom.solver.LUSolver;
import org.apache.commons.math3.linear.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import vm.container.Vector;
import vm.container.util.NumericMatrixUtils;

import java.io.*;

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
        FullTask2.execute();
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
            } catch (Exception e)
            {
                logger.debug(e);
                SIZE = 50;
                C = 25;
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
            logger.debug("is holesky applicable:");
            logger.debug(holeskySolver.isApplicable(a));
            logger.debug("holesky solve:");
            RealVector solutionHolesky = holeskySolver.solve(a, b);
            logger.debug(solutionHolesky);
            logger.debug("holesky error");
            logger.debug(Calculator.relativeError(x, solutionHolesky));
            logger.debug("holesky fill value");
            logger.debug(holeskySolver.getFillValue());

            out.append("Решением методом наименьших квадратов. Относительная погрешность:");
            out.append(Calculator.relativeError(x, solutionHolesky));
            out.append("\n");
            out.append("Величина заполнения:");
            out.append(holeskySolver.getFillValue());
            out.append("\n");

            GaussSeidelSolver solver = new GaussSeidelSolver();
            RealVector seidel = solver.solve(a, b);

            out.append("Решением методом Гаусса-Зейделя. Относительная погрешность:");
            out.append(Calculator.relativeError(x, seidel));
            out.append("\n");
            out.append("Величина заполнения:");
            out.append(0);
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
