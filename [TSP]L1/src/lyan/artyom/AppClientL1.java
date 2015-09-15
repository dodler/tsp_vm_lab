package lyan.artyom;

import lyan.artyom.server.impl.ServerImpl;
import vm.container.Matrix;
import vm.container.NumericMatrix;

import java.io.*;
import java.net.Socket;

/**
 * Created by artem on 12.09.15.
 */
public class AppClientL1
{
    public static void main(String[] args) throws IOException
    {
        Matrix matrix = NumericMatrix.randomMatrix(10, 10);
        printMatrix(matrix);

        File file = new File("test.txt");
        if (!file.exists())
        {
            file.createNewFile();
        }
        NumericMatrix.writeMatrix(new FileWriter(file), matrix);

        FileReader in = new FileReader(file);
        Matrix m2 = NumericMatrix.readMatrix(in);
        printMatrix(m2);

        printMatrix(NumericMatrix.multiplicateMatrix(matrix, matrix));

        Socket socket = new Socket("localhost", 12000);
        ServerImpl handler = new ServerImpl();
        OutputStream out = socket.getOutputStream();
        handler.sendData(out, matrix);
//        handler.sendData(socket.getOutputStream(), matrix);

        Matrix m3 = handler.getData(socket.getInputStream());
        printMatrix(m3);
    }

    public static void printMatrix(Matrix matrix)
    {
        if (!matrix.getClass().equals(NumericMatrix.class)){
            return; // can print only numeric matrix instances
        }

        int width = ((NumericMatrix) matrix).getWidth(), height = ((NumericMatrix) matrix).getHeight();
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                System.out.print(matrix.get(i, j) + "|");
            }
            System.out.println("\n--------------------------");
        }
    }
}
