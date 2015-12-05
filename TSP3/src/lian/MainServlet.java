package lian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

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
        Parser parser;
        try
        {
            parser = new Parser();
            Task task = (Task) req.getSession().getAttribute("task");
            if (task == null)
            {
                throw new Exception("Task is missing in session.");
            }
            parser.parse(task.num1, task.num2);
        } catch (Exception e)
        {
            String msg = e.getMessage();
            msg.replace(" ", "");
            resp.sendRedirect("error.jsp?errorMessage=" + msg);
            return;
        }
        Task task = new Task();
//        task.setArgument1(String.valueOf(parser.getNumber1()));
//        task.setArgument2(String.valueOf(parser.getNumber2()));
//        task.setResult(String.valueOf(new Denominator().calc(parser.getNumber1(), parser.getNumber2())));
        task.num1 = String.valueOf(parser.getNumber1());
        task.num2 = String.valueOf(parser.getNumber2());
        task.res = String.valueOf(new Denominator().calc(parser.getNumber1(), parser.getNumber2()));

        req.getSession().setAttribute("task", task);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {


    }
}
