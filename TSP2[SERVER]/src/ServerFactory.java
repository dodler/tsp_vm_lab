/**
 * Created by artem on 20.11.15.
 */
public class ServerFactory
{
    private static ServerFactory instance = new ServerFactory();

    private ServerFactory(){}

    public static ServerFactory getInstance(){return instance;}

    public Server getServer(){
        return new Server();
    }
}
