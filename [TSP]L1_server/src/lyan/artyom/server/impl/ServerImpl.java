package lyan.artyom.server.impl;

import lyan.artyom.server.Server;
import vm.container.Matrix;
import vm.container.NumericMatrix;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * class contains logic for sending and recieving data from one connection
 * class doesn't implement handling of matrix
 * only io operations for only one connection
 * it is assumed that data is written and read only in given format.
 * otherwise an exception will be thrown
 * Created by artem on 13.09.15.
 */
public class ServerImpl implements Server
{
    /**
     * method read data from stream as sequence of integers
     * it is assumed that lines of matrix are delimited with * symbol
     * @param in input stream
     * @return Matrix, read from stream
     */
    @Override
    public Matrix getData(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String s=new String();
        List<String> strings = new LinkedList<>();
        while((s=reader.readLine()) != null){
            if (!s.contains("--") && s.length()!=0)
            strings.add(s);
        }
//        System.out.println("ServerImpl strings=" + strings);
//        System.out.println(strings.size());

        return NumericMatrix.getMatrixFromStringList(strings);
    }

    @Override
    public void sendData(OutputStream out, Matrix matrix) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(NumericMatrix.getMatrixAsString(matrix));
        writer.flush();
//        writer.close();
    }
}
