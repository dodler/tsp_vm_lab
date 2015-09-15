package tsp;

import org.apache.log4j.Logger;
import tsp.controller.Controller;
import tsp.model.Model;
import tsp.model.RemoteInterface;
import tsp.model.impl.ClientInterface;
import tsp.model.impl.RmiModelImpl;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by artem on 04.09.15.
 */
public class AppSingleton
{
    private static final Logger log = Logger.getLogger(AppSingleton.class);

    private AppSingleton()
    {
        // maybe add config
    }

    private static final AppSingleton instance = new AppSingleton();

    public static AppSingleton getInstance()
    {
        return instance;
    }

    public void execute() throws RemoteException, AlreadyBoundException
    {
        new App().run();
    }

    static class App extends Thread
    {

        private static Model model;
        private static final Controller controller;

        private static int port = 12021;

        static
        {
            try
            {
                model = new RmiModelImpl(port);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
            controller = new Controller();
        }

        public App() throws RemoteException, AlreadyBoundException
        {
            try
            {
                System.setProperty("java.security.policy", "/home/artem/IdeaProjects/Custom/TSP/misc/security.policy");
                log.debug(System.getProperty("java.security.policy"));
                System.setSecurityManager(new RMISecurityManager());
                Registry registry = LocateRegistry.getRegistry(port);
                //ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject((Remote) model, 0);
                registry.bind("ClientInterface", (Remote) model);
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }

        @Override
        public void run()
        {
            while (true)
            {
//                System.out.println("500ms passed");
                try
                {
                    sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
