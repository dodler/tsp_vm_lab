package lian.artyom;

import lian.artyom.server.impl.ServerImpl;
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

//        File file = new File("test.txt");
//        File file = new File(args[0]);
//        File file2 = new File(args[1]);
        File file = new File("tset.txt");
        File file2 = new File("test2.txt");
        if (!file.exists())
        {
            file.createNewFile();
        }
        if (!file2.exists())
        {
            file2.createNewFile();
        }
        NumericMatrix.writeMatrix(new FileWriter(file), NumericMatrix.randomMatrix(3,3));
        NumericMatrix.writeMatrix(new FileWriter(file2), NumericMatrix.randomMatrix(3,3));

        FileReader in = new FileReader(file), in2=new FileReader(file2);
        Matrix m2 = NumericMatrix.readMatrix(in), m3=NumericMatrix.readMatrix(in2);
        printMatrix(m2);
        printMatrix(m3);

//        printMatrix(NumericMatrix.multiplicateMatrix(matrix, matrix));

        Socket socket = new Socket("localhost", 12000);
        ServerImpl handler = new ServerImpl();
        OutputStream out = socket.getOutputStream();
        handler.sendData(out, m2);
        handler.sendData(out, m3);
        Matrix result = handler.getData(socket.getInputStream());
        System.out.println("result");
        printMatrix(result);
//        handler.sendData(socket.getOutputStream(), matrix);

//        Matrix m3 = handler.getData(socket.getInputStream());
//        printMatrix(m3);
    }

    public static void printMatrix(Matrix matrix)
    {
        if (!matrix.getClass().equals(NumericMatrix.class)){
            return; // can print only numeric matrix instances
        }

        int width = ((NumericMatrix) matrix).getRows(), height = ((NumericMatrix) matrix).getColumns();
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
