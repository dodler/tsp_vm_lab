package tsp.model;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by artem on 06.09.15.
 */
public interface RemoteInterface extends Remote
{
    Image getImage() throws RemoteException;
    void handleImage() throws RemoteException;
    void setImage(Image image) throws RemoteException;
}
