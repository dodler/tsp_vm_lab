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
        Matrix numericMatrix=NumericMatrix.randomMatrix(3,3);
        numericMatrix.set(0,0, 10.0);
        numericMatrix.set(0,1, -7.0);
        numericMatrix.set(0,2, 0.0);
        numericMatrix.set(1,0, -3.0);
        numericMatrix.set(1,1, 6.0);
        numericMatrix.set(1,2, 2.0);
        numericMatrix.set(2,0, 5.0);
        numericMatrix.set(2,1, -1.0);
        numericMatrix.set(2,2, 5.0);
        Calculator.MatrixContainer container = Calculator.LU(numericMatrix);
        System.out.println(Calculator.calcDeterminant(container));
        Vector vector = new Vector(3);
        vector.set(1.0,0);
        vector.set(23.0,1);
        vector.set(2.0,2);

        NumericMatrix.printMatrix(numericMatrix);
        NumericMatrix.printVector(vector);
        NumericMatrix.printVector(Calculator.solveWithLU(container, vector));

        Calculator.seidelSolve(numericMatrix,vector);


        logger.debug("QR");
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
        logger.debug("Work finished");
        logger.debug("Q=");
        NumericMatrix.printMatrix(container1.L);
        logger.debug("R=");
        NumericMatrix.printMatrix(container1.U);
        Calculator.solveWithQR(container1);
    }
}
