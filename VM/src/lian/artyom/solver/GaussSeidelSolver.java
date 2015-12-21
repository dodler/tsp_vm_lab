package lian.artyom.solver;

import lian.RarefiedMatrix;
import lian.artyom.AppVM1;
import lian.artyom.Calculator;
import org.apache.commons.math3.linear.*;
import vm.container.Vector;
import vm.container.util.NumericMatrixUtils;

/**
 * Created by artem on 28.11.15.
 */
public class GaussSeidelSolver implements Solver
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        int size = a.getColumnDimension();

        RealMatrix L, D, R;

        L = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < i; j++)
            {
                L.setEntry(i, j, a.getEntry(i, j));
            }
        }

        D = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
        {
            D.setEntry(i, i, a.getEntry(i, i));
        }

        R = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < i; j++)
            {
                R.setEntry(j, i, a.getEntry(j, i));
            }
        }

        L = L.add(D);
        L = Calculator.reverseA(L);
        L = L.scalarMultiply(-1);
        L = L.multiply(R);

        this.q = L.getNorm();
        return q < 1;
    }

    private double q = 0;

    private int operationsCount = 0;

    private static double cij(RealMatrix a, int i, int j)
    {
        return i == j ? 0 : (-a.getEntry(i, j) / a.getEntry(i, i));
    }

    private static double di(RealMatrix a, RealVector b, int i)
    {
        return b.getEntry(i) / a.getEntry(i, i);
    }

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        operationsCount = 0;
        int size = b.getDimension();

        RealVector cur = new ArrayRealVector(size), prev = new ArrayRealVector(size);

        for (int i = 0; i < size; i++)
        {
            cur.setEntry(i, 0.0);
            prev.setEntry(i, 0.0);
        }

        double eps;
        do
        {
            for (int i = 0; i < size; i++)
            {
                double sum = 0;
                for (int j = 0; j < i; j++)
                {
                    sum += cij(a, i, j) * cur.getEntry(j);
                }
                for (int j = i; j < size; j++)
                {
                    sum += cij(a, i, j) * cur.getEntry(j);
                }
                sum += di(a, b, i);
                cur.setEntry(i, sum);
            }

            eps = prev.subtract(cur).getNorm();

            prev = new ArrayRealVector(cur.toArray());
            operationsCount++;
        } while (eps > (1 - q) / q);

        return cur;
    }

    private RealVector extractVector(RealMatrix m)
    {
        return new ArrayRealVector(m.getColumn(0));
    }

    private RealMatrix wrapVector(RealVector v)
    {
        double[][] data = new double[v.getDimension()][1];
        for (int i = 0; i < v.getDimension(); i++)
        {
            data[i][0] = v.getEntry(i);
        }
        return new Array2DRowRealMatrix(data);
    }

    @Override
    public String getName()
    {
        return "Gauss-Seidel";
    }
}


