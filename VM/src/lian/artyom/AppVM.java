package lian.artyom;

import org.apache.commons.math3.linear.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import vm.container.Vector;
import vm.container.util.NumericMatrixUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * entry point for lab work
 * Created by artem on 19.09.15.
 */
public class AppVM
{
    private static final Logger logger = Logger.getLogger(AppVM.class);


    public static StringBuilder message = new StringBuilder();


    public static class TaskLU
    {
        public static void executeLU()
        {
//            numericMatrix.setEntry(0,1, 25000); // this is for testing modified LU method

            RealVector luSolve = Calculator.solveWithLU(numericMatrix, vector);

            message.append("относительная погрешность метода LU:" + Calculator.relativeError(xVector, luSolve) + Math.pow(10, -15));
            message.append("|\n");
            message.append("--------------------------------------------------\n");

            RealVector luModifiedSolve = Calculator.LUModified2(numericMatrix, vector);

            message.append("|относительная погрешность метода:");
            message.append(Calculator.relativeError(xVector, luModifiedSolve));
            message.append("\n");
            message.append("--------------------------------------------------\n");

            RealVector seidelSolve = Calculator.seidelSolve2(numericMatrix, vector);

            message.append("|");
            message.append("относительная погрешность метода:" + Calculator.relativeError(xVector, seidelSolve));
            message.append("|\n");
        }
    }

    public static int WIDTH;
    public static String outPath;

    static RealMatrix numericMatrix;
    static RealVector xVector;
    static RealVector vector;

    public static final boolean TEST = false;
    public static boolean GOOD = false;
    public static boolean FULL = false;

    static
    {
        prepare();
    }

    private static void prepare()
    {
        if (GOOD)
        {
            message.append("Хорошо обусловленная матрица\n");
            WIDTH = 50;
            outPath = "out.txt";
            numericMatrix = NumericMatrixUtils.conditionedMatrix(WIDTH);
        } else
        {
            message.append("Плохо обусловленная матрица\n");
            WIDTH = 10;
            outPath = "out2.txt";
            numericMatrix = NumericMatrixUtils.hilbert(WIDTH);
        }
        xVector = Vector.risingVector(WIDTH);
        vector = NumericMatrixUtils.multiplicateMatrix(numericMatrix, xVector);
    }

    public static void main(String[] args) throws IOException
    {
        DOMConfigurator.configure("/media/artem/385BE95714C3BE20/IdeaProjects/Custom/config/log4j-config.xml");

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
            GOOD=!GOOD;
            prepare();
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


            logger.debug(numericMatrix);

            RealVector v1 = Calculator.solveWithLU(numericMatrix, vector), v2 = Calculator.LUModified2(numericMatrix, vector);

            logger.debug(v1);
            logger.debug(v1);

            logger.debug(Calculator.relativeError(xVector, v1));
            logger.debug(Calculator.relativeError(xVector, v2));

//            logger.debug(Calculator.eigennumberQR(numericMatrix));

            RealVector v3 = Calculator.seidelSolve2(numericMatrix, vector);
            logger.debug(v3);
            logger.debug(Calculator.relativeError(xVector, v3));
        }
    }

    private static class Task2
    {
        private static void invoke()
        {
            message.append("-----------------------\n");
            message.append("Определитель матрицы системы=" + Calculator.calcDeterminant(numericMatrix));
            message.append("\n");
            RealVector v1 = Calculator.eigennumberQR(numericMatrix);
            message.append("максимальное с.ч. системы A * transponize(A):" + v1.getMaxValue() + ":минимальное  с.ч. системы A * transponize(A):" + v1.getMinValue());
            message.append("\n");
            RealMatrix reversedA = Calculator.reverseA(numericMatrix);
            message.append("cond(A)=" + (reversedA.getNorm() * numericMatrix.getNorm()));
            message.append("\n");
        }
    }
}
