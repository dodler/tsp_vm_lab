package lyan.artyom;

import lyan.artyom.server.impl.ServerImpl;
import vm.container.Matrix;
import vm.container.NumericMatrix;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by artem on 13.09.15.
 */
public class AppServerL1 extends Thread
{
    private Socket socket;
    public AppServerL1(Socket socket){
        this.socket=socket;
    }

    private static final int port=12000;
    public static void main(String[] args) throws IOException
    {
        ServerSocket server = new ServerSocket(port);
        ArrayList<Socket> clients = new ArrayList<>();
        while(true)
        {
            new AppServerL1(server.accept()).run();

        }
    }

    @Override
    public void run(){
        ServerImpl handler=new ServerImpl();
        try
        {
            System.out.println("AppServer.run");
            InputStream in = socket.getInputStream();
            Matrix m1=handler.getData(in),
                    m2=handler.getData(in),
                    result= NumericMatrix.multiplicateMatrix(m1,m2);
            //Matrix result = NumericMatrix.randomMatrix(10,10);
            System.out.println("read data");
            handler.sendData(socket.getOutputStream(), result);
            System.out.println("sent data");
//            socket.close();
            System.out.println("socket closed");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
