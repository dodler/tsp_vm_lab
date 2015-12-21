package vm.container.util;

import org.apache.commons.math3.linear.*;
import vm.container.Matrix;
import vm.container.NumericMatrix;
import vm.container.Vector;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by artem on 31.10.15.
 */
public class NumericMatrixUtils
{
    public static Matrix fromRealMatrix(RealMatrix m)
    {
        Matrix result = singleMatrix(m.getColumnDimension());
        for (int i = 0; i < m.getColumnDimension(); i++)
        {
            for (int j = 0; j < m.getColumnDimension(); j++)
            {
                result.set(i, j, m.getEntry(i, j));
            }
        }
        return result;
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
        printMatrix(verticalFromVector(vector));
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
                matrix.set(i, j, Math.random());
            }
        }
        return (NumericMatrix) matrix;
    }

    /**
     * generate good conditioned matrix with
     * diagonal dominance
     * only quad matrix are supported
     *
     * @param size
     * @return
     */
    public static RealMatrix conditionedMatrix(int size)
    {
        RealMatrix result = MatrixUtils.createRealIdentityMatrix(size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                result.setEntry(j, i, (i == j) ? 1000 * Math.random() + 1001 : 100 * Math.random());
            }
        }
        return result;
    }

    /**
     * method returns matrix with dimensions size*size filled
     *
     * @param size
     * @return
     */
    public static RealMatrix hilbert(int size)
    {
//        Matrix result = singleMatrix(size);
        RealMatrix result = MatrixUtils.createRealIdentityMatrix(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                result.setEntry(i, j, (1.0 / (i + j + 1.0)));
            }

        return result;
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
     * method multiplies matrix by row resulting in a row
     * no check of dimension is performed
     * get ready for npe in case of bad dimension
     *
     * @return multiplicated matrix
     */
    public static RealVector multiplicateMatrix(RealMatrix a, RealVector x)
    {
        int size = x.getDimension();

        RealVector result = new ArrayRealVector(size);

        for (int i = 0; i < size; i++)
        {
            double cij = 0;
            for (int r = 0; r < size; r++)
            {
                cij += a.getEntry(i, r) * x.getEntry(r);
            }
            result.setEntry(i, cij);

        }

        return result;
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
    public static RealMatrix wrapWithSingle(RealMatrix m, int size)
    {
//        RealMatrix single = singleMatrix(size);
        RealMatrix single = MatrixUtils.createRealIdentityMatrix(size);
        int rows = m.getColumnDimension();
        if (rows > size)
        {
            throw new IllegalArgumentException("Size " + size + "should be greater than current size of matrix:" + rows);
        }
        if (rows == size)
        {
            return m; // do nothing if size equals m size
        }
        size -= rows;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                single.setEntry(size + i, size + j,
                        m.getEntry(i, j));
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
    public static Matrix<Double> verticalFromVector(Vector vector)
    {
        Matrix<Double> m = zeroMatrix(1, vector.getSize());
        for (int i = 0; i < vector.getSize(); i++)
        {
            m.set(0, i, vector.get(i));
        }
        return m;
    }

    public static Matrix<Double> horizontalFromVector(Vector vector)
    {
        Matrix<Double> m = zeroMatrix(vector.getSize(), 1);
        for (int i = 0; i < vector.getSize(); i++)
        {
            m.set(i, 0, vector.get(i));
        }
        return m;
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

    public static RealMatrix invhilb4()
    {
        RealMatrix result = new Array2DRowRealMatrix(4, 4);
        result.setEntry(0, 0, 16);
        result.setEntry(1, 0, -120);
        result.setEntry(2, 0, 240);
        result.setEntry(3, 0, -140);
        result.setEntry(0, 1, -120);
        result.setEntry(1, 1, 1200);
        result.setEntry(2, 1, -2700);
        result.setEntry(3, 1, 1680);
        result.setEntry(0, 2, 240);
        result.setEntry(1, 2, -2700);
        result.setEntry(2, 2, 6480);
        result.setEntry(3, 2, -4200);
        result.setEntry(0, 3, 140);
        result.setEntry(1, 3, 1680);
        result.setEntry(2, 3, -4200);
        result.setEntry(3, 3, 2800);
        return result;
    }
}
