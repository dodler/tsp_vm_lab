package lian.artyom.solver;

import lian.RarefiedMatrix;
import lian.artyom.Calculator;
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
        RealMatrix L = cholesky(a);

        RealVector y = new LUSolver().solve(L, b);
        RealMatrix Lt = L.transpose();

        fillValue = ((RarefiedMatrix) L).noneZeroCount() + ((RarefiedMatrix) Lt).noneZeroCount() -
                ((RarefiedMatrix) a).noneZeroCount() - a.getColumnDimension();

//        System.out.println(a);
//        System.out.println(L);
//        System.out.println(L.multiply(Lt));

        return new LUSolver().solve(Lt, y);
    }
}
