import java.rmi.RemoteException;

/**
 * Created by artem on 20.11.15.
 */
public class Server implements RemoteInterface
{
    @Override
    public void execute() throws RemoteException
    {
        System.out.println("WORKING!");
    }
}
