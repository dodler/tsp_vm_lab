package lian;

import lian.artyom.Calculator;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;

import java.util.*;

/**
 * Created by artem on 25.11.15.
 */
public class RarefiedMatrix implements RealMatrix
{

    public static double ZERO_THRESHOLD = Math.pow(10, -15);

    private Map<Integer, Double> matrix; // becase matrix is rarefied
    // i use that set

    int size;

    public RarefiedMatrix(int size)
    {
        this.size = size;
        matrix = new LinkedHashMap<>(size / 2);
    }

    private int hash(int row, int column)
    {
        int hash = 1;
        hash += row * size;
        hash += column;
        return hash;
    }

    /**
     * method puts some data to matrix
     * in value is already in it, it will be overwritten
     * if value is close to zero, nothing will be written
     *
     * @param row
     * @param column
     */
    @Override
    public void setEntry(int row, int column, double value)
    {
        if (Math.abs(value) < ZERO_THRESHOLD) return;
        if (Double.isNaN(value)) return;
        if (Double.isInfinite(value)) return;
        matrix.put(hash(row, column), value);
    }

    public boolean isZeroAt(int row, int column)
    {
        return Math.abs(getEntry(row, column)) < ZERO_THRESHOLD;
    }

    public String getPortrait()
    {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (isZeroAt(i, j))
                {
                    result.append("- ");
                } else
                {
                    result.append("+ ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int noneZeroCount()
    {
        return matrix.size();
    }

    @Override
    public void addToEntry(int i, int i1, double v) throws OutOfRangeException
    {

    }

    @Override
    public void multiplyEntry(int i, int i1, double v) throws OutOfRangeException
    {

    }

    @Override
    public RealMatrix transpose()
    {
        RealMatrix result = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                result.setEntry(j, i, getEntry(i, j));
            }
        }
        return result;
    }

    @Override
    public double getTrace() throws NonSquareMatrixException
    {
        return 0;
    }

    @Override
    public double[] operate(double[] doubles) throws DimensionMismatchException
    {
        return new double[0];
    }

    @Override
    public RealVector operate(RealVector realVector) throws DimensionMismatchException
    {
        return null;
    }

    @Override
    public double[] preMultiply(double[] doubles) throws DimensionMismatchException
    {
        return new double[0];
    }

    @Override
    public RealVector preMultiply(RealVector realVector) throws DimensionMismatchException
    {
        return null;
    }

    @Override
    public double walkInRowOrder(RealMatrixChangingVisitor realMatrixChangingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInRowOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInRowOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public double walkInRowOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public double walkInColumnOrder(RealMatrixChangingVisitor realMatrixChangingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInColumnOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInColumnOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public double walkInColumnOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixChangingVisitor realMatrixChangingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor)
    {
        return 0;
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        return 0;
    }

    @Override
    public RealMatrix createMatrix(int i, int i1) throws NotStrictlyPositiveException
    {
        return null;
    }

    @Override
    public RealMatrix copy()
    {
        return null;
    }

    @Override
    public RealMatrix add(RealMatrix realMatrix) throws MatrixDimensionMismatchException
    {
        return null;
    }

    @Override
    public RealMatrix subtract(RealMatrix realMatrix) throws MatrixDimensionMismatchException
    {
        return null;
    }

    @Override
    public RealMatrix scalarAdd(double v)
    {
        return null;
    }

    @Override
    public RealMatrix scalarMultiply(double v)
    {
        return null;
    }

    @Override
    public RealMatrix multiply(RealMatrix realMatrix) throws DimensionMismatchException
    {
        RealMatrix result = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                result.setEntry(i, j, 0);
                for (int k = 0; k < size; k++)
                {
                    result.setEntry(i, j, result.getEntry(i, j) + getEntry(i, k) * realMatrix.getEntry(k, j));
                }

            }
        return result;
    }

    @Override
    public RealMatrix preMultiply(RealMatrix realMatrix) throws DimensionMismatchException
    {
        return null;
    }

    @Override
    public RealMatrix power(int i) throws NotPositiveException, NonSquareMatrixException
    {
        return null;
    }

    private double[][] data;

    @Override
    public double[][] getData()
    {
        data = new double[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                data[i][j] = getEntry(i, j);
            }
        }
        return data;
    }

    @Override
    public double getNorm()
    {
        RealVector eigen = Calculator.eigennumberQR(this);
        return eigen.getMaxValue()/eigen.getMinValue();
    }

    @Override
    public double getFrobeniusNorm()
    {
        return 0;
    }

    @Override
    public RealMatrix getSubMatrix(int i, int i1, int i2, int i3) throws OutOfRangeException, NumberIsTooSmallException
    {
        RealMatrix result = new RarefiedMatrix(i2 - i);
        for (int j = i; j < i2; j++)
        {
            for (int k = i1; k < i3; k++)
            {
                result.setEntry(j, k, getEntry(j, k));
            }
        }
        return result;
    }

    @Override
    public RealMatrix getSubMatrix(int[] ints, int[] ints1) throws NullArgumentException, NoDataException, OutOfRangeException
    {
        return null;
    }

    @Override
    public void copySubMatrix(int i, int i1, int i2, int i3, double[][] doubles) throws OutOfRangeException, NumberIsTooSmallException, MatrixDimensionMismatchException
    {

    }

    @Override
    public void copySubMatrix(int[] ints, int[] ints1, double[][] doubles) throws OutOfRangeException, NullArgumentException, NoDataException, MatrixDimensionMismatchException
    {

    }

    @Override
    public void setSubMatrix(double[][] doubles, int i, int i1) throws NoDataException, OutOfRangeException, DimensionMismatchException, NullArgumentException
    {

    }

    @Override
    public RealMatrix getRowMatrix(int i) throws OutOfRangeException
    {
        return null;
    }

    @Override
    public void setRowMatrix(int i, RealMatrix realMatrix) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    @Override
    public RealMatrix getColumnMatrix(int i) throws OutOfRangeException
    {
        return null;
    }

    @Override
    public void setColumnMatrix(int i, RealMatrix realMatrix) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    @Override
    public RealVector getRowVector(int i) throws OutOfRangeException
    {
        return null;
    }

    @Override
    public void setRowVector(int i, RealVector realVector) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    @Override
    public RealVector getColumnVector(int i) throws OutOfRangeException
    {
        return null;
    }

    @Override
    public void setColumnVector(int i, RealVector realVector) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    @Override
    public double[] getRow(int i) throws OutOfRangeException
    {
        return new double[0];
    }

    @Override
    public void setRow(int i, double[] doubles) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    @Override
    public double[] getColumn(int i) throws OutOfRangeException
    {
        return getData()[0];
    }

    @Override
    public void setColumn(int i, double[] doubles) throws OutOfRangeException, MatrixDimensionMismatchException
    {

    }

    // TODO out of bounds exception
    @Override
    public double getEntry(int row, int column)
    {
        int hash = hash(row, column);
        if (matrix.containsKey(hash))
        {
            return matrix.get(hash(row, column));
        } else
        {
            return 0;
        }
    }

    StringBuilder toString;

    @Override
    public String toString()
    {
        if (toString == null)
        {
            toString = new StringBuilder();
            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    toString.append(getEntry(i, j));
                    if (j < size - 1)
                        toString.append(";");
                }
                toString.append("\n");
            }
            return toString.toString();
        } else
        {
            return toString.toString();
        }
    }

    public RealMatrix toArray2DRowRealMatrix()
    {
        double[][] data = new double[size][size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                data[i][j] = getEntry(i, j);
            }
        }
        return new Array2DRowRealMatrix(data);
    }

    @Override
    public boolean isSquare()
    {
        return true;
    }

    @Override
    public int getRowDimension()
    {
        return size;
    }

    @Override
    public int getColumnDimension()
    {
        return size;
    }
}
