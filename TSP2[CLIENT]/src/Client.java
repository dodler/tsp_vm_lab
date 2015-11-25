import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by artem on 20.11.15.
 */
public class Client
{

    private static final String IMG_PATH="/media/artem/385BE95714C3BE20/IdeaProjects/Custom/TSP2[CLIENT]/img/kenji-yamamura-photographer-5014.jpg";

    private static final String STUB_NAME = "Server";

    private static void showImage(Image image){
        JFrame frame = new JFrame();

        JLabel lblimage = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(lblimage, BorderLayout.CENTER);
        frame.setSize(300, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException, NotBoundException
    {
        System.out.println("start");

        BufferedImage image = ImageIO.read(new File(IMG_PATH));
        showImage(image);


        Registry registry = LocateRegistry.getRegistry(2104);
        System.out.println("registry recieved");
        RemoteInterface stub = (RemoteInterface) registry.lookup(STUB_NAME);
        System.out.println("stub found");
        ImageIcon result = (ImageIcon) stub.execute(new ImageIcon(image));
        showImage(result.getImage());

    }
}
