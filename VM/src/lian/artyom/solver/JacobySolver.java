package lian.artyom.solver;

import lian.artyom.Calculator;
import org.apache.commons.math3.linear.*;
import vm.container.Vector;

/**
 * Created by artem on 28.11.15.
 */
public class JacobySolver implements Solver
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        int size = a.getColumnDimension();
        RealMatrix B, E = MatrixUtils.createRealIdentityMatrix(size),
                D = MatrixUtils.createRealIdentityMatrix(size);

        for (int i = 0; i < size; i++)
        {
            D.setEntry(i, i, a.getEntry(i, i));
        }

        RealMatrix revD = new LUDecomposition(D).getSolver().getInverse();
        B = E.subtract(revD.multiply(a));
        double q = B.getNorm();
        return q < 1;
    }

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        int size = b.getDimension();

        RealMatrix B, E = MatrixUtils.createRealIdentityMatrix(size),
                D = MatrixUtils.createRealIdentityMatrix(size);

        for (int i = 0; i < size; i++)
        {
            D.setEntry(i, i, a.getEntry(i, i));
        }

        RealMatrix revD = new LUDecomposition(D).getSolver().getInverse();
        B = E.subtract(revD.multiply(a));
        double q = B.getNorm();

        RealVector prev = new ArrayRealVector(size),
                cur = Vector.zeroVector(size);

        int cnt = 0;
        double eps;
        do
        {
            cnt++;
            for (int i = 0; i < size; i++)
            {
                double sum = 0;
                for (int j = 0; j < size; j++)
                {
                    if (j == i) continue;
                    sum += a.getEntry(i, j) * prev.getEntry(j);
                }
                cur.setEntry(i, 1 / a.getEntry(i, i) * (b.getEntry(i) - sum));
            }
            eps = prev.subtract(cur).getMaxValue() / (1 - q);
            prev = new ArrayRealVector(cur.toArray());
        } while (eps < Calculator.EPSILON_THRESHOLD);

        return cur;
    }

    @Override
    public String getName()
    {
        return "Jacoby";
    }

}
