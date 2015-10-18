package lian.artyom;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import vm.container.Matrix;
import vm.container.NumericMatrix;
import vm.container.Vector;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * entry point for lab work
 * Created by artem on 19.09.15.
 */
public class AppVM
{
    private static final Logger logger = Logger.getLogger(AppVM.class);

    public static void main(String[] args) throws IOException
    {
        DOMConfigurator.configure("/media/artem/385BE95714C3BE20/IdeaProjects/Custom/config/log4j-config.xml");
//        final String step1_matr = "step1.txt";
//        Matrix numericMatrix = NumericMatrix.readMatrix(new FileReader(new File(step1_matr)));
        final String step2_bad = "step2_bad.txt";
        Matrix numericMatrix = NumericMatrix.readMatrix(new FileReader(new File(step2_bad)));
        Calculator.MatrixContainer container = Calculator.LU(numericMatrix);
        logger.debug("Matrx det=" + Calculator.calcDeterminant(container));
        Vector vector = new Vector(3);
        vector.set(1.0, 0);
        vector.set(23.0, 1);
        vector.set(2.0, 2);

        Vector luSolve = Calculator.solveWithLU(container, vector);
        Vector seidelSolve = Calculator.seidelSolve(numericMatrix, vector);

        Vector solve = new Vector(3);
        solve.set(804.0 / 145.0, 0);
        solve.set(227.0 / 29.0, 1);
        solve.set(-524.0 / 145.0, 2);

        logger.debug("matrix:");
        NumericMatrix.printMatrix(numericMatrix);
        logger.debug("vector:");
        NumericMatrix.printVector(vector);

        logger.debug("lu solve:");
        NumericMatrix.printVector(luSolve);
        logger.debug("seidel solve:");
        NumericMatrix.printVector(seidelSolve);
//        logger.debug("original solve:");
//        NumericMatrix.printVector(solve);

        logger.debug("relative errors:");
        logger.debug("lu:");
        for (int i = 0; i < solve.getSize(); i++)
        {
            logger.debug(
                    ((luSolve.get(i)) - solve.get(i))/luSolve.get(i)
            );
        }

        for (int i = 0; i < solve.getSize(); i++)
        {
            logger.debug(
                    ((seidelSolve.get(i)) - solve.get(i))/seidelSolve.get(i)
            );
        }


        logger.debug("QR");
        logger.debug("matrix:");
//        Matrix m = NumericMatrix.zeroMatrix(3,3);
//        m.set(0,0, 1.0);
//        m.set(0,1, 1.0);
//        m.set(1,0, 4.0);
//        m.set(1,1, 3.0);
//        m.set(2,0, 1.0);
//        m.set(2,1, 3.0);
//        m.set(0,2, 1.0);
//        m.set(1,2, 4.0);
//        m.set(2,2, 1.0);
//        NumericMatrix.writeMatrix(new FileWriter(new File("B.txt")), m);

        Matrix m = NumericMatrix.readMatrix(new FileReader(new File("B.txt")));
//        Matrix m = NumericMatrix.randomMatrix(3,3);
        NumericMatrix.printMatrix(m);
        Calculator.MatrixContainer container1 = Calculator.QR(m);
        logger.debug("Q=");
        NumericMatrix.printMatrix(container1.L);
        logger.debug("R=");
        NumericMatrix.printMatrix(container1.U);
        Calculator.solveWithQR(container1);
    }
}
