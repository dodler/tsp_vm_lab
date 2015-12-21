package lian.artyom.regularization.impl;

import lian.RarefiedMatrix;
import lian.artyom.Calculator;
import lian.artyom.regularization.Regulizer;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by artem on 10.12.15.
 */
public class TikhonovRegulizer implements Regulizer
{
    @Override
    public boolean isApplicable(RealMatrix a)
    {
        return true;
    }

    @Override
    public RealMatrix regulize(RealMatrix a, double... params)
    {
        if (params.length == 0) return a;
        double alpha = params[0];
        int size = a.getColumnDimension();
//        RealMatrix result = new Array2DRowRealMatrix(size, size);
        RealMatrix result = a.transpose().multiply(a);
        RealMatrix E = MatrixUtils.createRealIdentityMatrix(size).scalarMultiply(alpha);
        return result.add(E);

//        for (int i = 0; i < size; i++)
//        {
//            for (int j = 0; j < size; j++)
//            {
//                double ind = i == j ? 1 : 0;
//                result.setEntry(i, j, a.getEntry(i, j) * a.getEntry(j, i) + alpha * ind);
//            }
//        }
//        return result;
    }
}
