package tsp.client.impl;

import tsp.model.impl.ClientInterface;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

/**
 * Created by artem on 06.09.15.
 */
public class RemoteClientImpl implements ClientInterface
{
    @Override
    public Image getImage() throws RemoteException
    {
        return null;
    }

    @Override
    public void setImage(Image image) throws RemoteException
    {

    }
}
