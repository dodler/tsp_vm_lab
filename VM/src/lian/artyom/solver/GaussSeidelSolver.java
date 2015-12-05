package lian.artyom.solver;

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
        RealMatrix A2, E = MatrixUtils.createRealIdentityMatrix(size),
                D = MatrixUtils.createRealIdentityMatrix(size);

        for (int i = 0; i < size; i++)
        {
            D.setEntry(i, i, a.getEntry(i, i));
        }
        LUDecomposition decomposition = new LUDecomposition(a);
        RealMatrix L = decomposition.getL();
        L = L.add(D);
        L = new LUDecomposition(L).getSolver().getInverse();
        L = L.scalarMultiply(-1);
        L.multiply(decomposition.getU());

        double q = L.getNorm();
        return q < 1;
    }
    private int operationsCount = 0;

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        operationsCount = 0;
        LUDecomposition decomposition = new LUDecomposition(a);
        int cnt = 0, size = b.getDimension();
        RealMatrix l = decomposition.getL(), u = decomposition.getU();
        RealVector cur = new ArrayRealVector(size), prev = Vector.zeroVector(size);

        RealMatrix D = MatrixUtils.createRealIdentityMatrix(size),
                t = new LUDecomposition(l.add(D)).getSolver().getInverse();
        u = u.scalarMultiply(-1);


        for (int i = 0; i < size; i++)
        {
            D.setEntry(i, i, a.getEntry(i, i));
        }

        double eps;
        do
        {
            cur = u.preMultiply(prev);
            cur = cur.add(b);
            cur = t.preMultiply(cur);

            eps = cur.subtract(prev).getNorm();
            prev = new ArrayRealVector(cur.toArray());
            operationsCount++;
        } while (eps < Calculator.EPSILON_THRESHOLD);

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
}
