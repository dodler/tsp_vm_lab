package vm.container;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * matrix with fixed width and height
 * assumed that internal data is integer type
 * Created by artem on 12.09.15.
 */
public class NumericMatrix implements Matrix
{
    private int[][] matrix;
    private int width, height;

    public NumericMatrix(int width, int height)
    {
        matrix = new int[width][height];
        this.width = width;
        this.height = height;
    }

    public static NumericMatrix randomMatrix(int width, int height){
        Matrix matrix = new NumericMatrix(width, height);
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                matrix.set(i,j,rand.nextInt());
            }
        }
        return (NumericMatrix)matrix;
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
        int width = ((NumericMatrix) matrix).getWidth(), height = ((NumericMatrix) matrix).getWidth();
        StringBuilder rowDelimiter = new StringBuilder("\n");
        for (int i = 0; i < width; i++)
        {
            rowDelimiter.append("--");
        }

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
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
     * @param m1 matrix 1
     * @param m2 matrix 1
     * @return sum of m1 and m2
     */
    public static Matrix sumMatrix(Matrix m1, Matrix m2){
        if (!m1.getClass().equals(NumericMatrix.class)){
            throw new IllegalArgumentException("Not supported for type:" + m1.getClass().getName());
        }
        int width = ((NumericMatrix)m1).getWidth(),height=((NumericMatrix)m1).getHeight();
        Matrix matrix = new NumericMatrix(width, height);

        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                matrix.set(i,j, (int)m1.get(i,j) + (int)m2.get(i,j));
            }
        }

        return matrix;
    }

    /**
     * TODO test
     * method multiplicates two matrix
     * if height of first matrix is not equal to width of second, method throws an exception
     * works only with NumericMatrix type
     * for other types an exception will be thrown
     * @param m1 first matrix
     * @param m2 second matrix
     * @return multiplicated matrix
     */
    public static Matrix multiplicateMatrix(Matrix m1, Matrix m2){
        if (!m1.getClass().equals(NumericMatrix.class)){
            throw new IllegalArgumentException("Not supported for type:" + m1.getClass().getName());
        }
        int width = ((NumericMatrix)m1).getWidth(),height=((NumericMatrix)m1).getHeight();
        if (height != ((NumericMatrix)m2).getWidth()) throw new IllegalArgumentException("Bad dimensions of matrix");
        Matrix matrix = new NumericMatrix(width, height);

        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                int cij=0;
                for(int r = 0; r<height; r++){
                    cij += (int)m1.get(i,r) * (int)m2.get(r,j);
                }
                matrix.set(i,j, cij);
            }
        }

        return matrix;
    }

    /**
     * method will read matrix from file
     * it is assumed that numbers are splited with | symbol
     * its not necesary to put | in end of line
     * it is assumed that lines are splited with - symbol
     * (if you will print - to end of line will be good)
     * PLS USE ONLY OOB METHODS
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
//                System.out.println("inp = " + buffer);
                inp.add(buffer);
            }
//            else
//                System.out.println("buffer = " + buffer);
        }
        return getMatrixFromStringList(inp);
    }

    /**
     * method returns matrix from list of strings read from reader
     * @param inp list of input strings
     * @return
     */
    public static Matrix getMatrixFromStringList(List<String> inp)
    {
        System.out.println(inp);
        int width, height;
        if (inp.size() >0)
        {
            width = inp.get(0).split("\\|").length;
        }else{
            return new NumericMatrix(0,0);
        }
        height = inp.size();
        Matrix matrix = new NumericMatrix(width, height);
        int j = 0;
//        System.out.println(width);
//        System.out.println(height);
        for (String s : inp)
        {
//            System.out.println("s=" + s);
            String[] nums = s.split("\\|");
//            System.out.println(nums.length);
            for (int i = 0; i < width; i++)
            {
//                System.out.println("nums[i]="+nums[i]);
//                nums[i].replace("|", "");
                if (s.contains("--")) continue;
                matrix.set(j, i, Integer.parseInt(nums[i]));
            }
            j++;
            if (j == height) return matrix; // to avoid array out of bounds exception
        }
        return matrix;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return this.height;
    }

    @Override
    public Object get(int i, int j)
    {
        if (i >= 0 && i < width && j >= 0 && j < height)
        {
            return matrix[i][j];
        } else
        {
            throw new IndexOutOfBoundsException("Index [" + i + ":" + j + "] is out of bounds: [" + width + ":" + height + "].");
        }
    }

    @Override
    public void set(int i, int j, Object value)
    {
        int val=0;
        try
        {
            val = (int) value;
        } catch (ClassCastException cce)
        {
            throw new IllegalArgumentException("Argument value has incompatible type.");
        }
        if (i >= 0 && i < width && j >= 0 && j < height)
        {
            matrix[i][j] = val;
//        }
        } else
        {
            throw new IndexOutOfBoundsException("Index [" + i + ":" + j + "] is out of bounds: [" + width + ":" + height + "].");
        }
    }
}
