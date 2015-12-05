package lian.artyom.solver;

import lian.RarefiedMatrix;
import lian.artyom.AppVM1;
import lian.artyom.Calculator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static lian.artyom.Calculator.LU;

/**
 * Created by artem on 02.12.15.
 */
public class LUSolver implements Solver
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        return true; // TODO check it
    }

    private int operationsCount = 0;
    private int fillValue = 0;

    public int getOperationsCount()
    {
        return operationsCount;
    }

    public int getFillValue()
    {
        return fillValue;
    }

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        Calculator.MatrixContainer container = LU(a, true);
        int cnt = 0;
        RealMatrix l = container.getL(), u = container.getU();

        fillValue = ((RarefiedMatrix) l).noneZeroCount() + ((RarefiedMatrix) u).noneZeroCount() -
                ((RarefiedMatrix) a).noneZeroCount() - a.getColumnDimension();

        int size = l.getColumnDimension();
        RealVector result = new ArrayRealVector(size);
        double[] y = new double[size];
        y[0] = b.getEntry(0);
        for (int i = 1; i < y.length; i++)
        {
            double sum = 0;
            for (int j = 0; j < i; j++)
            {
                sum += l.getEntry(i, j) * y[j];
                cnt++;
            }
            y[i] = b.getEntry(i) - sum;
        }

        for (int i = result.getDimension() - 1; i > -1; i--)
        {
            double sum = 0;
            for (int j = i + 1; j < result.getDimension(); j++)
            {
                sum += u.getEntry(i, j) * result.getEntry(j);
                cnt++;
            }
            result.setEntry(i, (y[i] - sum) / u.getEntry(i, i));
        }
        return result;
    }
}
