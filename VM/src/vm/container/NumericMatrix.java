package vm.container;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;
import vm.container.util.NumericMatrixUtils;

/**
 * matrix with fixed rows and columns
 * assumed that internal data is integer type
 * Created by artem on 12.09.15.
 */
public class NumericMatrix implements Matrix
{
    private static final Logger logger = Logger.getLogger(NumericMatrix.class);

    private double[][] matrix;
    private int rows, columns;

    public NumericMatrix(int rows, int columns)
    {
        matrix = new double[rows][columns];// because rows is
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * constructor with values
     *
     * @param values
     */
    public NumericMatrix(double[][] values)
    {
        this.matrix = values;
        this.columns = values.length;
        this.rows = values[0].length;
    }


    public Matrix getReversedMatrix(Matrix L, Matrix U)
    {

        Matrix revA = NumericMatrixUtils.zeroMatrix(getRows() + 1, getColumns() + 1);
        double SUM;
        for (int k = getRows(); k >= 1; k--)
        {
            for (int i = k; i >= 1; i--)
            {
                SUM = 0;
                if (i == k)
                {
                    for (int l = i + 1; l <= getRows(); l++) SUM = SUM + (Double) U.get(i, l) * (Double) revA.get(l, i);
                    revA.set(i, i, (1 - SUM) / (Double) U.get(i, i));
                } else
                {
                    for (int l = i + 1; l <= getRows(); l++) SUM = SUM + (Double) U.get(i, l) * (Double) revA.get(l, k);
                    revA.set(i, k, -SUM / (Double) U.get(i, i));
                }
            }
            for (int j = k - 1; j >= 1; j--)
            {
                SUM = 0;
                for (int l = j + 1; l <= getRows(); l++) SUM = SUM + (Double) L.get(l, j) * (Double) revA.get(k, l);
                revA.set(k, j, -SUM);
            }
        }
        return revA;
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return this.columns;
    }

    @Override
    public Object get(int i, int j)
    {
        if (i >= 0 && i < rows && j >= 0 && j < columns)
        {
            return matrix[i][j];
        } else
        {
            throw new IndexOutOfBoundsException("Index [" + i + ":" + j + "] is out of bounds: [" + rows + ":" + columns + "].");
        }
    }

    public Vector getCol(int i)
    {
        double[] values = new double[matrix.length];
        for (int j = 0; j < matrix.length; j++)
        {
            values[j] = matrix[j][i];
        }
        return new Vector(values);
    }

    public Vector getRow(int i)
    {
        double[] values = new double[matrix[i].length];
        for (int j = 0; j < matrix[i].length; j++)
        {
            values[j] = matrix[i][j];
        }
        return new Vector(values);
    }

    double zeroPoint = Math.pow(10, -3);

    /**
     * method takes only 4 digits
     * and cuts to zero all values less than 0.001
     *
     * @param i
     * @param j
     * @param value
     */
    @Override
    public void set(int i, int j, Object value)
    {
        double val = 0;
        try
        {
            val = (double) value;
        } catch (ClassCastException cce)
        {
            throw new IllegalArgumentException("Argument value has incompatible type.");
        }
        if (i >= 0 && i < rows && j >= 0 && j < columns)
        {
            matrix[i][j] = val;
//        }
        } else
        {
            throw new IndexOutOfBoundsException("Index [" + i + ":" + j + "] is out of bounds: [" + rows + ":" + columns + "].");
        }
    }

    /**
     * method multiplicates each member of matrix by current number
     * result is saved inside object
     *
     * @param number
     */
    public void multiplicateByNumber(double number)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                matrix[i][j] *= number;
            }
        }
    }

    /**
     * method returns transponed copy of current matrix
     *
     * @return
     */
    public Matrix transpone()
    {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                result[i][j] = matrix[j][i];
            }
        }
        return new NumericMatrix(result);
    }

    public RealMatrix getRealMatrix(){
        return new Array2DRowRealMatrix(this.matrix);
    }
}
