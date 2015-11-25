import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by artem on 20.11.15.
 */
public interface RemoteInterface extends Remote
{
    void execute() throws RemoteException;
}
