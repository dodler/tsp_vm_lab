package lian.artyom;

import org.apache.log4j.Logger;
import vm.container.Matrix;
import vm.container.NumericMatrix;
import vm.container.Vector;

import java.util.ArrayList;
import java.util.Iterator;

import static vm.container.NumericMatrix.*;

/**
 * class that incapsulates logic for lab work
 * Created by artem on 18.09.15.
 */
public abstract class Calculator
{
    private static Logger logger = Logger.getLogger(Calculator.class);

    public static class MatrixContainer
    {
        Matrix<Double> L, U;

        public Matrix<Double> getL()
        {
            return L;
        }

        public Matrix<Double> getU()
        {
            return U;
        }
    }

    public static Vector solveWithLU(MatrixContainer container, Vector b)
    {
//        NumericMatrix.printVector(b);
        Matrix<Double> l = container.L;
//        NumericMatrix.printMatrix(l);
        int size = ((NumericMatrix) l).getRows();
        if (b.getSize() != size)
        {
            throw new IllegalArgumentException("Size " + b.getSize() + " right side isn't equal to L matrix size;");
        }

        Vector y = new Vector(size); // temp vector for LU solve
        for (int i = 0; i < size; i++)
        {
//            System.out.println("i=" + i);
            double value = b.get(i);
//            System.out.println("index=" + (size-i-1));
            for (int j = 0; j < i; j++)
            {
//                System.out.println("j=" + j);
                value -= l.get(i, j) * b.get(j);
            }
//            System.out.println("value=" + value);
            y.set(value, i);
        } // it's direct substition here. this algo maybe should be extracted to separate method.
        Matrix<Double> u = container.U;
        Vector x = new Vector(size); // now time for reverse substitution
        for (int i = size - 1; i >= 0; i--)
        {
//            System.out.println("i=" + i);
            double value = y.get(i);
            for (int j = i; j < size - 1; j++)
            {
//                System.out.println("j=" + j);
                value -= u.get(i, j + 1)
                        * x.get(j + 1);
            }

            value /= u.get(i, i);
            x.set(value, i);
//            System.out.println(x.get(i));
        }

        return x;
    }

    public static int calcDeterminant(MatrixContainer container)
    {
        int size = ((NumericMatrix) container.L).getColumns(), result = 1;
        for (int i = 0; i < size; i++)
        {
            result *= container.L.get(i, i);
        }
        for (int i = 0; i < size; i++)
        {
            result *= container.U.get(i, i);
        }
        return result;
    }

    /**
     * methods calculated LU decomposition for specifued matrix
     * result is container, that holds two matrixes - L and U
     *
     * @param a matrix, for which LU is needed
     * @return LU decomposition
     */
    public static MatrixContainer LU(Matrix a)
    {
        MatrixContainer container = new MatrixContainer();

//        NumericMatrix.printMatrix(a);
//        System.out.println("|||||||||||||||||||||");
        int size = ((NumericMatrix) a).getRows();
        Matrix u = NumericMatrix.zeroMatrix(size, size), l = NumericMatrix.zeroMatrix(size, size);

        for (int j = 0; j < size; j++)
        {
            u.set(0, j, a.get(0, j));
        }
        for (int j = 1; j < size; j++)
        {
            l.set(j, 0, (double) a.get(j, 0) / (double) u.get(0, 0));
        }
        // got first row

        for (int i = 1; i < size; i++)
        {
            for (int j = i; j < size; j++)
            {
                double sum = 0;
                for (int k = 0; k < size - 1; k++)
                {
                    sum += (double) l.get(i, k) * (double) u.get(k, j);
                }
                u.set(i, j, (double) a.get(i, j) - sum);
            }
            for (int j = i + 1; j < size; j++)
            {
                double sum = 0;
                for (int k = 0; k < size - 1; k++)
                {
                    sum += (double) l.get(j, k) * (double) u.get(k, i);
                }
                l.set(j, i, ((double) a.get(j, i) - sum) / (double) u.get(i, i));
            }
        }
        for (int i = 0; i < size; i++)
        {
            l.set(i, i, 1.0);
        }
        container.L = l;
        container.U = u;
        return container;

//        NumericMatrix.printMatrix(u);
//        System.out.println("|||||||||||||||||||||");
//        NumericMatrix.printMatrix(l);
//        System.out.println("|||||||||||||||||||||");
//
//        NumericMatrix.printMatrix(NumericMatrix.multiplicateMatrix(l, u));


    }

    /**
     * method creats qr decomposition of matrix
     *
     * @param matrix
     * @return
     */
    public static MatrixContainer QR(Matrix matrix)
    {
        ArrayList<Matrix> hMatrix = new ArrayList<>();

        MatrixContainer result = new MatrixContainer();
        int size = ((NumericMatrix) matrix).getRows();
        Matrix currentA = matrix;
        for (int k = 0; k < size - 1; k++)
        {
            Vector p = Vector.zeroVector(size - k);
            for (int i = 0; i < p.getSize(); i++)
            {
                p.set((double) currentA.get(i + k, k), i);
            }
            int delta;
            if (p.get(0) >= 0)
            {
                delta = -1;
            } else
            {
                delta = 1;
            }

            Vector e = Vector.singleVector(p.getSize());
            e.multiplicateByNumber(p.norm(Vector.OCTA_NORM));
            e.multiplicateByNumber(delta);
            p.plus(e);

            Matrix m1 = p.multiplicateColumnByRow(p);
            double ptp = 2 / p.multiplicateRowByColumn(p);
            ((NumericMatrix) m1).multiplicateByNumber(ptp);

            hMatrix.add(wrapWithSingle(minusMatrix(singleMatrix(p.getSize()), m1), ((NumericMatrix) matrix).getRows())); // found h matrix
            Matrix multTemp = singleMatrix(((NumericMatrix) matrix).getRows());

            for (int i = hMatrix.size(); i > 0; i--)
            {
                multTemp = multiplicateMatrix(multTemp, hMatrix.get(i-1));
            }
            currentA = multiplicateMatrix(multTemp, matrix);
        }
        result.L=currentA;
        Matrix multTemp = singleMatrix(((NumericMatrix) matrix).getRows());
        for (int i = hMatrix.size(); i > 0; i--)
        {
            multTemp = multiplicateMatrix(multTemp, hMatrix.get(i-1));
        }
        result.U = ((NumericMatrix)multTemp).transpone();

        return result;
    }

    public Vector seidelSolve(Matrix<Double> a, Vector b)
    {
        Vector x = new Vector(b.getSize());

        return x;
    }

    public static boolean converge(Vector prev, Vector current)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
