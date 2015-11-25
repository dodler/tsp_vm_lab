package vm.container;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import vm.container.util.NumericMatrixUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * class that incapsulates logic of work with
 * vectors
 * Created by artem on 18.09.15.
 */
public class Vector
{

    private double[] vector;
    private int capacity;

    /**
     * method return vector with 1 in 0 position, and 0 in other positions
     *
     * @param size size of vector
     * @return
     */
    public static RealVector singleVector(int size)
    {
        RealVector result = new ArrayRealVector(size);
        result.setEntry(0, 1);
        for (int i = 1; i < size; i++)
        {
            result.setEntry(i, 0.0);
        }
        return result;
    }

    /**
     * method creates vector where values are filled by order - from little to large
     *
     * @param size
     * @return
     */
    public static RealVector risingVector(int size)
    {
        RealVector result = new ArrayRealVector(size);
        for (int i = 0; i < size; i++)
        {
            result.setEntry(i, i + 1.0);
        }
        return result;
    }

    /**
     * method returns randomly generated vector
     *
     * @param size
     * @return
     */
    public static RealVector randomVector(int size)
    {
        RealVector result = new ArrayRealVector(size);
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; i++)
        {
            result.setEntry(i, rand.nextDouble());
        }
        return result;
    }

    public Double max()
    {
        double result = Double.MIN_VALUE;
        for (int i = 0; i < this.vector.length; i++)
        {
            if (vector[i] > result)
            {
                result = vector[i];
            }
        }
        return result;
    }

    public Double min()
    {
        double result = Double.MAX_VALUE;
        for (int i = 0; i < this.vector.length; i++)
        {
            if (vector[i] < result)
            {
                result = vector[i];
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Vector:{");
        for (int i = 0; i < vector.length; i++)
        {
            sb.append(vector[i]);
            if (i != vector.length-1)
            sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * method returns vector with zeros
     *
     * @param size size of created vector
     * @return
     */
    public static RealVector zeroVector(int size)
    {
        RealVector result = singleVector(size);
        for (int i = 0; i < size; i++)
        {
            result.setEntry(i, 0.0);
        }
        return result;
    }

    public Vector(double[] vector)
    {
        this.vector = Arrays.copyOf(vector, vector.length);
        this.capacity = vector.length;
    }

    public Vector(int capacity)
    {
        if (capacity < 0) throw new IllegalArgumentException("Capacity should be >= 0");
        vector = new double[capacity];
        this.capacity = capacity;
    }

    public int getSize()
    {
        return vector.length;
    }

    public double get(int i)
    {
        if (i < 0 || i >= vector.length)
        {
            throw new IndexOutOfBoundsException("Index [" + i + "] is out of bounds.");
        }
        return vector[i];
    }

    public static final double THRESHOLD_ZERO_VALUE = Math.pow(10, -5);

    public void set(double value, int i)
    {
        if (i < 0 || i > vector.length)
        {
            throw new IndexOutOfBoundsException("Index [" + i + "] is out of bounds.");
        }
//        if (value < THRESHOLD_ZERO_VALUE || value == Double.NaN || value == -Double.NaN) value = 0.0;
        vector[i] = value;
    }

    public void insertAtEnd(double value)
    {
        insert(value, vector.length);
    }

    private void insert(double value, int index)
    {
        if (vector.length == capacity)
        {

        }
    }

    /**
     * not clearly implemented yet
     * TODO implement other types of norm
     * now return only CUBIC NORM
     *
     * @param type
     * @return
     */
    public double norm(byte type)
    {
        switch (type)
        {
            case OCTA_NORM:
                return octaNorm();
            case SPHERE_NORM:
                break;
            case CUBIC_NORM:
                return cubicNorm();
        }
        return -1;
    }

    public double norm(){
        return norm(OCTA_NORM);
    }

    public double octaNorm()
    {
        double sum = 0;
        for (int i = 0; i < vector.length; i++)
        {
            sum += vector[i] * vector[i];
        }
        return Math.sqrt(sum);
    }

    public void minus(Vector vector)
    {
        for (int i = 0; i < vector.vector.length; i++)
        {
            this.vector[i] -= vector.vector[i];
        }
    }

    /**
     * method multiplicates 2 vector
     * assumed that current vector is column and vector for multiplying is row
     *
     * @param vector
     * @return
     */
    public static RealMatrix multiplicateColumnByRow(RealVector source, RealVector vector)
    {
        int size = source.getDimension();
        RealMatrix result = new Array2DRowRealMatrix(size, size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                result.setEntry(i, j, source.getEntry(i) * vector.getEntry(j));
            }
        }
        return result;
    }

    /**
     * method performs multiplication of 2 vectors
     * current should be row and another one - column.
     * TODO (arli0415) add check of sizes for both methods
     *
     * @param vector - vector to mulplicate on
     */
    public static double multiplicateRowByColumn(RealVector source, RealVector vector)
    {
        int size = source.getDimension();
        double result = 0;
        for (int i = 0; i < size; i++)
        {
            result += source.getEntry(i) * vector.getEntry(i);
        }
        return result;
    }

    /**
     * method multiplies each elm of vector by current number
     * changes are saved in current vector
     *
     * @param number number to multiplicate
     */
    public void multiplicateByNumber(double number)
    {
        for (int i = 0; i < vector.length; i++)
        {
            vector[i] *= number;
        }
    }

    public void plus(Vector vector)
    {
        for (int i = 0; i < vector.vector.length; i++)
        {
            this.vector[i] += vector.vector[i];
        }
    }

    private double cubicNorm()
    {
        double result = 0.0;
        for (double t : vector)
        {
            if (t > result)
            {
                result = t;
            }
        }
        return result;
    }

    public static final byte OCTA_NORM = 1;
    public static final byte SPHERE_NORM = 2;
    public static final byte CUBIC_NORM = 3;
}
