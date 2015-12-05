package lian.artyom.solver;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by artem on 28.11.15.
 */
public interface Solver
{
    public boolean isApplicable(RealMatrix a);
    public RealVector solve(RealMatrix a, RealVector b);
}
