import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import tsp.client.impl.RemoteClientImpl;
import tsp.model.RemoteInterface;
import tsp.model.impl.RmiModelImpl;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by artem on 06.09.15.
 */
public class Client
{
    private static final Logger log = Logger.getLogger(Client.class);

    public static void main(String[] args)
    {
        DOMConfigurator.configure("/home/artem/IdeaProjects/Custom/config/log4j-config.xml");
        RemoteClientImpl remoteClient = new RemoteClientImpl();
        log.debug("Starting");
        try
        {
            Registry registry = LocateRegistry.getRegistry(null, 12917);
            log.debug("Created registry");
            RemoteInterface server = (RemoteInterface) registry.lookup("ClientInterface");

            RmiModelImpl model = (RmiModelImpl) UnicastRemoteObject.exportObject(remoteClient,0);
            log.debug("Model exported");
            model.setData(ImageIO.read(new FileInputStream(new File("/home/artem/IdeaProjects/Custom/TSP/pic/test.jpg"))));
            log.debug("Data sent");
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (NotBoundException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
