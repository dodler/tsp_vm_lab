package lian.artyom.solver;

import lian.artyom.solver.Solver;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by artem on 19.12.15.
 */
public class QRSolver implements Solver
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        return true;
    }

    @Override
    public RealVector solve(RealMatrix a, RealVector b)
    {
        return new QRDecomposition(a).getSolver().solve(b);
    }

    @Override
    public String getName()
    {
        return "QR";
    }
}
