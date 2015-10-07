package lian.artyom.server;

import vm.container.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by artem on 13.09.15.
 */
public interface Server
{
    public Matrix getData(InputStream in) throws IOException;
    public void sendData(OutputStream out, Matrix matrix) throws IOException;
}
