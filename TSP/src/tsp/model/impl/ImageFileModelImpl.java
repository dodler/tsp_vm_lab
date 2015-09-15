package tsp.model.impl;

import tsp.model.Model;


import javax.imageio.ImageIO;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteOrder;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * class that writes and reads image from file
 * Created by artem on 05.09.15.
 */
public class ImageFileModelImpl implements Model
{
    BufferedImage bufferedImage;

    @Override
    public Object getData()
    {
        return bufferedImage;
    }

    @Override
    public void setData(Object data)
    {
        bufferedImage = (BufferedImage) data;
    }

    public void setData(Image image)
    {
        this.bufferedImage = (BufferedImage) image;
    }

    @Override
    public void saveTo(OutputStream output)
    {
        try
        {
            ImageIO.write(bufferedImage, "png", output);
        } catch (IOException ioe)
        {
            // TODO (arli0415) add error handling
        }
    }

    @Override
    public void loadFrom(InputStream input)
    {
        try
        {
            bufferedImage = ImageIO.read(input);
        } catch (IOException ioe)
        {

        }
    }
}
