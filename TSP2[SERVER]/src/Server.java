import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.rmi.RemoteException;
import java.awt.*;

/**
 * Created by artem on 20.11.15.
 */
public class Server implements RemoteInterface
{
    private static final Logger log = Logger.getLogger(Server.class);

    @Override
    public Object execute(Object argument) throws RemoteException
    {
        try
        {
            BufferedImage image = toBufferedImage(((ImageIcon) argument).getImage());
            log.debug("image recieved");
            return filter(image);
        } catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    private int size = 9; //size of the square filter

    //sort the array, and return the median
    private int median(int[] a)
    {
        int temp;
        int asize = a.length;
        //sort the array in increasing order
        for (int i = 0; i < asize; i++)
            for (int j = i + 1; j < asize; j++)
                if (a[i] > a[j])
                {
                    temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
        //if it's odd
        if (asize % 2 == 1)
            return a[asize / 2];
        else
            return ((a[asize / 2] + a[asize / 2 - 1]) / 2);
    }

    private int[] getArray(BufferedImage image, int x, int y)
    {
        int[] n; //store the pixel values of position(x, y) and its neighbors
        int h = image.getHeight();
        int w = image.getWidth();
        int xmin, xmax, ymin, ymax; //the limits of the part of the image on which the filter operate on
        xmin = x - size / 2;
        xmax = x + size / 2;
        ymin = y - size / 2;
        ymax = y + size / 2;

        //special edge cases
        if (xmin < 0)
            xmin = 0;
        if (xmax > (w - 1))
            xmax = w - 1;
        if (ymin < 0)
            ymin = 0;
        if (ymax > (h - 1))
            ymax = h - 1;
        //the actual number of pixels to be considered
        int nsize = (xmax - xmin + 1) * (ymax - ymin + 1);
        n = new int[nsize];
        int k = 0;
        for (int i = xmin; i <= xmax; i++)
            for (int j = ymin; j <= ymax; j++)
            {
                n[k] = image.getRGB(i, j); //get pixel value
                k++;
            }
        return n;
    }

    public ImageIcon filter(BufferedImage srcImage)
    {
        log.debug("filter started");
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();
        BufferedImage dstImage = srcImage.getSubimage(0, 0, width, height);

        int[] a; //the array that gets the pixel value at (x, y) and its neightbors

        for (int k = 0; k < height; k++)
        {
            for (int j = 0; j < width; j++)
            {
                a = getArray(srcImage, j, k);
                int[] red, green, blue;
                red = new int[a.length];
                green = new int[a.length];
                blue = new int[a.length];
                //get the red,green,blue value from the pixel
                for (int i = 0; i < a.length; i++)
                {
//                    red[i] = Pixel.getRed( a[i] );
                    red[i] = (a[i] >> 16) & 0x000000FF;
//                    green[i] = Pixel.getGreen( a[i] );
                    green[i] = (a[i] >> 8) & 0x000000FF;
//                    blue[i] = Pixel.getBlue( a[i] );
                    blue[i] = (a[i]) & 0x000000FF;
                }
                //find the median for each color
                int R = median(red);
                int G = median(green);
                int B = median(blue);
                //set the new pixel value using the median just found
//                int spixel = Pixel.createRGB(R, G, B);
                int spixel = (new Color(R, G, B)).getRGB();
                dstImage.setRGB(j, k, spixel);
            }
        }
        log.debug("filter finished");
        return new ImageIcon(dstImage);
    }
}
