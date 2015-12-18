package lian.artyom.regularization;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by artem on 10.12.15.
 */
public interface Regulizer
{
    public boolean isApplicable(RealMatrix a);
    public RealMatrix regulize(RealMatrix a, double ...params);
}
