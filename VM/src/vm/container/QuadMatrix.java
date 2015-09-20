package vm.container;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * matrix has fixed dimensions
 * Created by artem on 04.09.15.
 */
public class QuadMatrix implements Matrix
{

    Object[][] matrix;
    int size;

    /**
     * returns null if index is out of bounds
     *
     * @param i row index
     * @param j column index
     * @return
     */
    @Override
    public Object get(int i, int j)
    {
        if (i > 0 && j > 0 && i < size && j < size)
        {
            return matrix[i][j];
        } else
        {
            return null;
        }
    }

    /**
     * method does nothing if index is out of bounds
     *
     * @param i row index
     * @param j column ndex
     */
    @Override
    public void set(int i, int j, Object value)
    {
        if (i > 0 && j > 0 && i < size && j < size)
        {
            matrix[i][j] = value;
        } else
        {
            return;
        }
    }

    public int getSize()
    {
        return size;
    }

    /**
     * methods that adds new rows and columns
     * wasn't tested
     *
     * @param newSize new size of matrix
     */
    public void resizeTo(int newSize)
    {
        Object[][] newMatrix = new Object[newSize][newSize];
        for (int i = 0; i < size; i++)
        {
            newMatrix[i] = Arrays.copyOf(matrix[i], newSize);
        }
        matrix=newMatrix.clone();
    }


    public void QuadMatrix(int size)
    {
        matrix = new Object[size][size];
        this.size = size;
    }

}
