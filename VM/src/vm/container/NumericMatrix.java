package vm.container;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    public static void printMatrix(Matrix matrix, PrintWriter writer)
    {
        if (!matrix.getClass().equals(NumericMatrix.class))
        {
            return; // can print only numeric matrix instances
        }

        int width = ((NumericMatrix) matrix).getRows(), height = ((NumericMatrix) matrix).getColumns();
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                writer.print(matrix.get(i, j) + "|");
            }
            writer.println("\n--------------------------");
        }
    }

    public static void printMatrix(Matrix matrix)
    {
        if (!matrix.getClass().equals(NumericMatrix.class))
        {
            return; // can print only numeric matrix instances
        }

        int rows = ((NumericMatrix) matrix).getRows(), cols = ((NumericMatrix) matrix).getColumns();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                System.out.print(matrix.get(i, j) + "|");
            }
            System.out.println("\n--------------------------");
        }
    }

    public static void printVector(Vector vector)
    {
        printMatrix(fromVector(vector));
    }

    public static Matrix zeroMatrix(int columns, int rows)
    {
        Matrix matrix = new NumericMatrix(columns, rows);
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                matrix.set(i, j, (double) 0);
            }
        }
        return (NumericMatrix) matrix;
    }

    /**
     * method returns quad matrix with 1 on diagonal
     *
     * @param size size of generated matrix
     * @return
     */
    public static Matrix singleMatrix(int size)
    {
        Matrix result = zeroMatrix(size, size);
        for (int i = 0; i < size; i++)
        {
            result.set(i, i, 1.0);
        }
        return result;
    }

    public static NumericMatrix randomMatrix(int width, int height)
    {
        Matrix matrix = new NumericMatrix(width, height);
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                matrix.set(i, j, rand.nextDouble());
            }
        }
        return (NumericMatrix) matrix;
    }

    /**
     * method writes NumericMatrix to file
     * numbers in one line are splited with | symbol
     * lines are splited with -- to end of line
     *
     * @param out
     * @param matrix
     * @throws IOException
     */
    public static void writeMatrix(FileWriter out, Matrix matrix) throws IOException
    {
        // TODO replace with StringBuffer in case of parallel server
        out.write(getMatrixAsString(matrix));
        out.flush();
        out.close();
    }

    public static String getMatrixAsString(Matrix matrix)
    {
        StringBuilder sb = new StringBuilder();
        int rows = ((NumericMatrix) matrix).getRows(), columns = ((NumericMatrix) matrix).getColumns();
        StringBuilder rowDelimiter = new StringBuilder("\n");
        for (int i = 0; i < rows; i++)
        {
            rowDelimiter.append("--");
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                sb.append(matrix.get(i, j));
                sb.append("|");
//                out.write(String.valueOf(matrix.get(i,j)));
//                out.write("|");
            }
            sb.append(rowDelimiter);
            sb.append("\n");
//            out.write(rowDelimiter.toString());
//            out.write("\n");
        }
        return sb.toString();
    }

    /**
     * method sums two matrix and returns result
     * it is assumed that both matrix have same dimensions
     *
     * @param m1 matrix 1
     * @param m2 matrix 1
     * @return sum of m1 and m2
     */
    public static Matrix sumMatrix(Matrix m1, Matrix m2)
    {
        if (!m1.getClass().equals(NumericMatrix.class))
        {
            throw new IllegalArgumentException("Not supported for type:" + m1.getClass().getName());
        }
        int width = ((NumericMatrix) m1).getRows(), height = ((NumericMatrix) m1).getColumns();
        Matrix matrix = new NumericMatrix(width, height);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                matrix.set(i, j, (double) m1.get(i, j) + (double) m2.get(i, j));
            }
        }

        return matrix;
    }

    /**
     * method performs subtraction m1 and m2
     * m1 - m2
     * result is NumericMatrix again
     *
     * @param m1
     * @param m2
     * @return
     */
    public static Matrix minusMatrix(Matrix m1, Matrix m2)
    {
        if (!m1.getClass().equals(NumericMatrix.class))
        {
            throw new IllegalArgumentException("Not supported for type:" + m1.getClass().getName());
        }

        int rows = ((NumericMatrix) m1).getRows(), columns = ((NumericMatrix) m1).getColumns();

        if (rows != ((NumericMatrix) m2).getRows() || columns != ((NumericMatrix) m2).getColumns())
        {
            throw new IllegalArgumentException("Cannot minus matrixes with different dimensions");
        }

        Matrix matrix = new NumericMatrix(rows, columns);

        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                matrix.set(i, j, (double) m1.get(i, j) - (double) m2.get(i, j));
            }
        }

        return matrix;
    }


    /**
     * TODO test
     * method multiplicates two matrix
     * if columns of first matrix is not equal to rows of second, method throws an exception
     * works only with NumericMatrix type
     * for other types an exception will be thrown
     *
     * @param m1 first matrix
     * @param m2 second matrix
     * @return multiplicated matrix
     */
    public static Matrix multiplicateMatrix(Matrix m1, Matrix m2)
    {
        if (!m1.getClass().equals(NumericMatrix.class))
        {
            throw new IllegalArgumentException("Not supported for type:" + m1.getClass().getName());
        }
        int columns = ((NumericMatrix) m1).getColumns(), rows = ((NumericMatrix) m2).getRows();

        if (rows != columns) throw new IllegalArgumentException("Bad dimensions of matrix");
        Matrix matrix = new NumericMatrix(rows, columns);
        for (int i = 0; i < ((NumericMatrix) m1).getRows(); i++)
        {
            for (int j = 0; j < ((NumericMatrix) m2).getColumns(); j++)
            {
                double cij = 0;
                for (int r = 0; r < ((NumericMatrix) m2).getRows(); r++)
                {
                    cij += (double) m1.get(i, r) * (double) m2.get(r, j);
                }
                matrix.set(i, j, cij);
            }
        }

        return matrix;
    }

    /**
     * method wraps matrix with single matrix. it means that it adds rows and columns
     * above and left
     * diagonal elements are 1
     *
     * @param m    matrix to wrap. this matrix should be quad
     * @param size size of new matrix. size of new matrix should be greater than old size
     * @return
     */
    public static Matrix wrapWithSingle(Matrix m, int size)
    {
        Matrix single = NumericMatrix.singleMatrix(size);
        int rows = ((NumericMatrix) m).getRows();
        if (rows > size)
        {
            throw new IllegalArgumentException("Size " + size + "should be greater than current size of matrix:" + rows);
        }
        if (rows == size)
        {
            return m; // do nothing if size equals m size
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                single.set(size - i - 1, size - j - 1,
                        m.get(i, j));
            }
        }
        return single;
    }

    /**
     * method converts a given vector to matrix of size 1,n, where n is size of vector
     * done for application in other methods.
     *
     * @param vector input vector
     * @return NumericMatrix with size 1,n and values of vector
     */
    public static Matrix<Double> fromVector(Vector vector)
    {
        Matrix<Double> m = zeroMatrix(1, vector.getSize());
        for (int i = 0; i < vector.getSize(); i++)
        {
//            System.out.println(i);
//            System.out.println(vector.get(i));
            m.set(0, i, vector.get(i));
        }
        return m;
    }

    public static Matrix<Double> multiplicateMatrixToVector(Matrix matrix, Vector vector)
    {
        Matrix<Double> m2 = fromVector(vector);
        return multiplicateMatrix(matrix, m2);
    }

    /**
     * method will read matrix from file
     * it is assumed that numbers are splited with | symbol
     * its not necesary to put | in end of line
     * it is assumed that lines are splited with - symbol
     * (if you will print - to end of line will be good)
     * PLS USE ONLY OOB METHODS
     *
     * @param in
     * @throws IOException
     */
    public static Matrix readMatrix(FileReader in) throws IOException
    {
        BufferedReader reader = new BufferedReader(in);
        String buffer = new String();
        LinkedList<String> inp = new LinkedList<>();
        int width, height;
        while ((buffer = reader.readLine()) != null)
        {
            if (!buffer.contains("--"))
            {
                inp.add(buffer);
            }
        }
        return getMatrixFromStringList(inp);
    }

    /**
     * method returns matrix from list of strings read from reader
     *
     * @param inp list of input strings
     * @return
     */
    public static Matrix getMatrixFromStringList(List<String> inp)
    {
        int columns, rows;
        if (inp.size() > 0)
        {
            columns = inp.get(0).split("\\|").length;
        } else
        {
            return new NumericMatrix(0, 0);
        }
        rows = inp.size();
//        System.out.println(rows);
//        System.out.println(rows);
        Matrix matrix = new NumericMatrix(rows, columns);
        int j = 0;
        for (String s : inp)
        {
            String[] nums = s.split("\\|");
            for (int i = 0; i < columns; i++)
            {
                if (s.contains("--")) continue;
                matrix.set(j, i, Double.parseDouble(nums[i]));
            }
            j++;
            if (j == rows) return matrix; // to avoid array out of bounds exception
        }
        return matrix;
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
                result[i] = Arrays.copyOf(matrix[i], matrix[i].length);
            }
        }
        return new NumericMatrix(result);
    }
}
