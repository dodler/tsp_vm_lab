package lian.artyom.solver;

import lian.RarefiedMatrix;
import lian.artyom.Calculator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by artem on 05.12.15.
 */
public class HoleskySolver implements Solver
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        for (int i = 0; i < a.getColumnDimension(); i++)
        {
            RealMatrix t = a.getSubMatrix(0, 0, i, i);
            if (new LUDecomposition(a).getDeterminant() < 0)
            {
                return false;
            }
        }
        return true;
    }


    public static RealMatrix cholesky(RealMatrix a)
    {
        int size = a.getRowDimension();
        RealMatrix L = new RarefiedMatrix(size);

        for (int i = 0; i < size; i++)
        {
            double temp;
            for (int j = 0; j < i; j++)
            {
                temp = 0;
                for (int k = 0; k < j; k++)
                {
                    temp += L.getEntry(i, k) * L.getEntry(j, k);
                }
                L.setEntry(i, j, (a.getEntry(i, j) - temp) / L.getEntry(j, j));
            }

            //Находим значение диагонального элемента
            temp = a.getEntry(i, i);
            for (int k = 0; k < i; k++)
            {
                temp -= L.getEntry(i, k) * L.getEntry(i, k);
            }
            L.setEntry(i, i, Math.sqrt(temp));
        }

        return L;
    }

    private int fillValue = 0;

    public int getFillValue()
    {
        return fillValue;
    }

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        int size = b.getDimension();
        RealMatrix L = cholesky(a);



        RealVector y = new ArrayRealVector(size);

        for (int i = 0; i < size; i++)
        {
            double sum = 0;
            for (int k = 0; k < i; k++)
            {
                sum += L.getEntry(i, k) * y.getEntry(k);
            }
            y.setEntry(i, (b.getEntry(i) - sum) / L.getEntry(i, i));
        }

        RealMatrix Lt = L.transpose();

        fillValue = ((RarefiedMatrix) L).noneZeroCount() + ((RarefiedMatrix) Lt).noneZeroCount() -
                ((RarefiedMatrix) a).noneZeroCount() - a.getColumnDimension();

        RealVector x = new ArrayRealVector(size);

        for (int i = 0; i < size; i++)
        {
            double sum = 0;
            for (int k = 0; k < i; k++)
            {
                sum += Lt.getEntry(size - i - 1, size - k - 1) * x.getEntry(size - k - 1);
            }
            x.setEntry(size - i - 1, (y.getEntry(size - i - 1) - sum) / Lt.getEntry(size - i - 1, size - i - 1));
        }

//        return new LUSolver().solve(Lt, y);
        return x;
    }
}
