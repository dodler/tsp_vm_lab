package lian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by artem on 22.11.15.
 */
@WebServlet("/mainServlet")
public class MainServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream()));
        writer.write("<!--test-->\n");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        Parser parser;
        if (req.getParameterMap().size() > 0)
        {
            try
            {
                parser = new Parser();
                parser.parse(req.getParameter("num1"), req.getParameter("num2"));
            } catch (Exception e)
            {
                String msg = e.getMessage();
                msg.replace(" ", "");
                resp.sendRedirect("error.jsp?errorMessage=" + msg);
                return;
            }
            resp.sendRedirect("result.jsp?result=" + new Denominator().calc(parser.getNumber1(), parser.getNumber2()));
        } else
        {
            resp.sendRedirect("error.jsp?errorMessage=Too+few+arguments");
        }
    }
}
