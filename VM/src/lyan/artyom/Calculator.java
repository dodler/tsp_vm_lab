package lyan.artyom;

import vm.container.Matrix;
import vm.container.NumericMatrix;
import vm.container.Vector;

/**
 * class that incapsulates logic for lab work
 * Created by artem on 18.09.15.
 */
public abstract class Calculator
{
    public static class LUContainer
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

    public static void solveWithLU(LUContainer container)
    {

    }

    public static int calcDeterminant(LUContainer container)
    {
        int size = ((NumericMatrix) container.L).getHeight(), result=0;
        for (int i = 0; i < size; i++)
        {
            result *= container.L.get(i,i);
        }
        for (int i = 0; i < size; i++)
        {
            result *= container.U.get(i,i);
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
    public static LUContainer LU(Matrix a)
    {
        LUContainer container = new LUContainer();

//        NumericMatrix.printMatrix(a);
//        System.out.println("|||||||||||||||||||||");
        int size = ((NumericMatrix) a).getWidth();
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
}
