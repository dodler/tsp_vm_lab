package vm.container;

import java.util.Arrays;

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
    public static Vector singleVector(int size)
    {
        Vector result = new Vector(size);
        result.set(1, 0);
        for (int i = 1; i < size; i++)
        {
            result.set(0, i);
        }
        return result;
    }

    /**
     * method returns vector with zeros
     * @param size size of created vector
     * @return
     */
    public static Vector zeroVector(int size)
    {
        Vector result = singleVector(size);
        result.set(0, 0);
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

    public void set(double value, int i)
    {
        if (i < 0 || i > vector.length)
        {
            throw new IndexOutOfBoundsException("Index [" + i + "] is out of bounds.");
        }
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
    public Matrix multiplicateColumnByRow(Vector vector)
    {
        Matrix result = NumericMatrix.zeroMatrix(this.vector.length, this.vector.length);
        for (int i = 0; i < this.vector.length; i++)
        {
            for (int j = 0; j < this.vector.length; j++)
            {
                result.set(i, j, this.vector[i] * vector.get(j));
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
    public double multiplicateRowByColumn(Vector vector)
    {
        double result = 0;
        for (int i = 0; i < this.vector.length; i++)
        {
            result += this.vector[i] * vector.get(i);
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
