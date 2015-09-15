package tsp.model.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import sun.rmi.runtime.Log;
import tsp.controller.command.CommandsContstants;
import tsp.model.Model;
import tsp.model.RemoteInterface;
import tsp.model.context.Context;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by artem on 05.09.15.
 */
public class RmiModelImpl extends UnicastRemoteObject implements Model, RemoteInterface
{
    private static final Logger logger = Logger.getLogger(RmiModelImpl.class);

    private BufferedImage bufferedImage;
    private Model fileModel;
    private static final String FILE = "server/temp";
    private File file;

    public RmiModelImpl(int port) throws RemoteException
    {
        super(port);
        logger.debug("super(port)");
        fileModel = new ImageFileModelImpl();
        logger.debug("fileModel created");
    }

    @Override
    public void saveTo(OutputStream output)
    {
        logger.debug("Method started");

        file = new File(FILE);
        if (file.exists())
        {
            file = new File(FILE + String.valueOf(System.currentTimeMillis() & 0b1111111).replace("0", ""));
        }
        try
        {
            file.createNewFile();
            fileModel.saveTo(new FileOutputStream(file));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void loadFrom(InputStream input)
    {
        logger.debug("Method started");
        fileModel.loadFrom(input);
        bufferedImage = (BufferedImage) fileModel.getData();
    }

    @Override
    public Object getData()
    {
        logger.debug("Method started");
        return bufferedImage;
    }

    @Override
    public void setData(Object data)
    {
        logger.debug("Method started");
        bufferedImage = (BufferedImage) data;
    }

    @Override
    public Image getImage() throws RemoteException
    {
        logger.debug("Method started");
        return bufferedImage;
    }

    @Override
    public void handleImage() throws RemoteException
    {
        logger.debug("Method started");
        Context context = new Context();
        context.setModel(this);
        context.setCommand(CommandsContstants.MEDIAN_FILTER);

    }

    @Override
    public void setImage(Image image) throws RemoteException
    {
        logger.debug("Method started");
        this.bufferedImage = (BufferedImage) bufferedImage;
    }
}
