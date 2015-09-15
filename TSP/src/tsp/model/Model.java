package tsp.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by artem on 04.09.15.
 */
public interface Model
{
    public Object getData();
    public void setData(Object data);
    public void saveTo(OutputStream output);
    public void loadFrom(InputStream input);
}
