import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Created by artem on 20.11.15.
 */
public class AppSingleton extends Thread
{
    private static final Logger logger = Logger.getLogger(AppSingleton.class);

    private static AppSingleton instance;

    private Server server;

    private static final int PORT = 2104;

    private AppSingleton()
    {
        try
        {
//            System.setProperty("java.rmi.server.hostname", "192.168.1.2");
            System.setProperty("java.security.policy","file:///media/artem/385BE95714C3BE20/IdeaProjects/Custom/TSP2[SERVER]/security.policy");
            logger.debug(System.getProperty("java.security.policy"));
            if (System.getSecurityManager() == null)
                System.setSecurityManager(new SecurityManager());

            server = ServerFactory.getInstance().getServer();

            RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);
            logger.debug("exported object");
            Registry registry = LocateRegistry.createRegistry(PORT);
            logger.debug("got registry");
            registry.bind("Server", stub);

            logger.debug("Server initiated.");
        } catch (Exception e)
        {
            logger.debug("Failed to instantiate server. Exception:" + e);
        }
    }

    public static AppSingleton getInstance()
    {
        return instance;
    }

    @Override
    public void run()
    {

    }

    static
    {
        DOMConfigurator.configure("/media/artem/385BE95714C3BE20/IdeaProjects/Custom/config/log4j-config.xml");
    }

    public static void main(String[] args)
    {
        instance = new AppSingleton();
    }
}
