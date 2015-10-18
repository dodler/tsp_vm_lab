package lian.artyom;

import org.apache.commons.math3.complex.Complex;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vm.container.Matrix;
import vm.container.NumericMatrix;
import vm.container.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        logger.debug("LU method started");
        int cnt = 0;

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
                cnt++;
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

                cnt++;
            }

            value /= u.get(i, i);
            x.set(value, i);
//            System.out.println(x.get(i));
        }
        logger.debug("number of iterations:");
        logger.debug(cnt);
        return x;
    }

    public static int calcDeterminant(MatrixContainer container)
    {
        logger.debug("LU determinant method started");
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
            double delta;
            if (p.get(0) >= 0)
            {
                delta = 1.0;
            } else
            {
                delta = -1.0;
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
                multTemp = multiplicateMatrix(multTemp, hMatrix.get(i - 1));
            }
            currentA = multiplicateMatrix(multTemp, matrix);
        }
        result.U = currentA;
//        Matrix multTemp = singleMatrix(((NumericMatrix) matrix).getRows());
        Matrix multTemp = null;
        for (int i = 0; i < hMatrix.size(); i++)
        {
            if (multTemp == null)
            {
                multTemp = hMatrix.get(0);
            } else
            {
                multTemp = multiplicateMatrix(multTemp, hMatrix.get(i));
            }

        }
//        result.L = ((NumericMatrix) multTemp).transpone();
        result.L = multTemp;

        return result;
    }

    /**
     * method finds own numbers of system with QR decomposition method
     *
     * @param mc container with Q and R matrix
     * @return solvation of system
     */
    public static Complex[] solveWithQR(MatrixContainer mc)
    {
        logger.debug("QR method started");
        int iterNum = 0;
        Matrix currentA = multiplicateMatrix(mc.L, mc.U),
                prevA = zeroMatrix(((NumericMatrix) currentA).getRows(), ((NumericMatrix) currentA).getColumns());
        MatrixContainer mcTemp = mc; // TODO add iteration finishing check
        while (!converge(
                ((NumericMatrix) prevA).getCol(0),
                ((NumericMatrix) currentA).getCol(0),
                0.001
        ))
        {
            mcTemp = QR(currentA);
            prevA = currentA;
            currentA = multiplicateMatrix(mcTemp.U, mcTemp.L);
            iterNum++;
        }

        // works only for 3 rowed matrix
        // fix it in future

        Complex[] result = new Complex[((NumericMatrix) currentA).getRows()];
        result[0] = new Complex((double) currentA.get(0, 0));
        Complex d = new Complex(Math.pow(-(double) currentA.get(1, 1) - (double) currentA.get(2, 2), 2)
                - 4 * (-(double) currentA.get(1, 2) * (double) currentA.get(2, 1) +
                (double) currentA.get(1, 1) * (double) currentA.get(2, 2)));
        d = d.sqrt();
        result[1] = new Complex(
                ((double) currentA.get(1, 1) + (double) currentA.get(2, 2) + d.getReal()),
                d.getImaginary()
        );
        result[1] = result[1].divide(2);
        result[2] = new Complex(
                ((double) currentA.get(1, 1) + (double) currentA.get(2, 2) - d.getReal()),
                d.getImaginary()
        );
        result[2] = result[2].divide(2);

        for (int i = 0; i < result.length; i++)
        {
            logger.debug(result[i]);
        }
        logger.debug("iterations:" + iterNum);

        return result;
    }


    public static Vector seidelSolve(Matrix<Double> a, Vector b)
    {
        logger.debug("seidel method started");
        int size = b.getSize();
        Vector x = Vector.zeroVector(size), p = Vector.zeroVector(size);
        double precision = 0.00001;
        int cnt = 0;
        do
        {

            for (int i = 0; i < size; i++)
            {
                p.set(x.get(i), i);
            }
            for (int i = 0; i < size; i++)
            {
                double temp = 0;
                for (int j = 0; j < i; j++)
                {
                    temp += (a.get(i, j) * x.get(j));
                    cnt++;
                }
                for (int j = i + 1; j < size; j++)
                {
                    temp += (a.get(i, j) * p.get(j));
                    cnt++;
                }
                x.set((b.get(i) - temp) / a.get(i, i), i);
            }
        } while (!converge(x, p, precision));
//        } while (cnt<10000);

        logger.debug("number of iterations:" + cnt);

        return x;
    }

    /**
     * condition of seidel method
     * currently it checks if norm of difference of 2 vectors is less than selected precision
     *
     * @param prev
     * @param current
     * @return
     */
    public static boolean converge(Vector prev, Vector current, double precision)
    {
        double norm = 0;
        for (int i = 0; i < prev.getSize(); i++)
        {
            norm += (prev.get(i) - current.get(i)) * (prev.get(i) - current.get(i));
        }

        return (Math.sqrt(norm) <= precision);
    }
}
